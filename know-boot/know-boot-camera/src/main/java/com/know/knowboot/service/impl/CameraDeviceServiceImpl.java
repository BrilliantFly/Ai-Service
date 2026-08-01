package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.CameraDevice;
import com.know.knowboot.entity.CameraDiscoveredDevice;
import com.know.knowboot.entity.CameraFavorite;
import com.know.knowboot.mapper.CameraDeviceMapper;
import com.know.knowboot.mapper.CameraFavoriteMapper;
import com.know.knowboot.service.ICameraDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 摄像头设备服务实现
 */
@Service
public class CameraDeviceServiceImpl extends ServiceImpl<CameraDeviceMapper, CameraDevice> implements ICameraDeviceService {

    @Autowired
    private CameraDeviceMapper cameraDeviceMapper;

    @Autowired
    private CameraFavoriteMapper cameraFavoriteMapper;

    @Override
    public IPage<CameraDevice> page(CameraDevice query, Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<CameraDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CameraDevice::getUserId, userId)
                .like(query.getDeviceName() != null, CameraDevice::getDeviceName, query.getDeviceName())
                .like(query.getIpAddress() != null, CameraDevice::getIpAddress, query.getIpAddress())
                .eq(query.getStatus() != null, CameraDevice::getStatus, query.getStatus())
                .orderByDesc(CameraDevice::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<CameraDevice> listByUserId(Long userId) {
        return list(new LambdaQueryWrapper<CameraDevice>()
                .eq(CameraDevice::getUserId, userId)
                .orderByDesc(CameraDevice::getCreateTime));
    }

    @Override
    public List<CameraDevice> listFavorites(Long userId) {
        // 获取用户收藏的设备ID列表
        List<CameraFavorite> favorites = cameraFavoriteMapper.selectList(
                new LambdaQueryWrapper<CameraFavorite>()
                        .eq(CameraFavorite::getUserId, userId)
                        .orderByAsc(CameraFavorite::getSort));
        
        if (favorites.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Long> deviceIds = favorites.stream()
                .map(CameraFavorite::getDeviceId)
                .collect(Collectors.toList());
        
        // 按收藏顺序返回设备
        return list(new LambdaQueryWrapper<CameraDevice>()
                .in(CameraDevice::getId, deviceIds)
                .orderByDesc(CameraDevice::getCreateTime));
    }

    @Override
    public CameraDevice getById(Long id) {
        return cameraDeviceMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(CameraDevice device, Long userId) {
        device.setUserId(userId);
        device.setStatus(0); // 默认离线
        return save(device);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(CameraDevice device) {
        return updateById(device);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public boolean existsByDeviceCode(String deviceCode, Long excludeId) {
        LambdaQueryWrapper<CameraDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CameraDevice::getDeviceCode, deviceCode);
        if (excludeId != null) {
            wrapper.ne(CameraDevice::getId, excludeId);
        }
        return count(wrapper) > 0;
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        return cameraDeviceMapper.updateStatus(id, status) > 0;
    }

    /**
     * 常见摄像头端口（按优先级排列）
     */
    private static final int[] CAMERA_PORTS = {8000, 554, 37777, 8080, 80};

    @Override
    public List<CameraDiscoveredDevice> discover(Long userId) {
        List<CameraDevice> myDevices = listByUserId(userId);

        // 已添加设备的 IP 集合（避免重复扫描，单独做真实在线探测）
        Set<String> existingIps = myDevices.stream()
                .map(CameraDevice::getIpAddress)
                .filter(StringUtils::hasText)
                .collect(Collectors.toSet());

        // 生成待扫描网段：已添加设备所在网段 + 常用家用网段
        Set<String> prefixes = new HashSet<>();
        for (String ip : existingIps) {
            int idx = ip.lastIndexOf('.');
            if (idx > 0) {
                prefixes.add(ip.substring(0, idx));
            }
        }
        prefixes.add("192.168.1");
        prefixes.add("192.168.0");
        prefixes.add("192.168.31");
        prefixes.add("10.0.0");

        List<String> candidateIps = new ArrayList<>();
        for (String prefix : prefixes) {
            for (int i = 1; i <= 254; i++) {
                String ip = prefix + "." + i;
                if (existingIps.contains(ip)) {
                    continue;
                }
                candidateIps.add(ip);
            }
        }

        // 并发探测：已添加设备（已知端口优先）+ 网段扫描
        ExecutorService pool = Executors.newFixedThreadPool(64);
        List<Callable<CameraDiscoveredDevice>> tasks = new ArrayList<>();
        for (CameraDevice device : myDevices) {
            if (!StringUtils.hasText(device.getIpAddress())) {
                continue;
            }
            tasks.add(() -> probe(device.getIpAddress(), device.getPort(), true, device.getId(), device.getDeviceName()));
        }
        for (String ip : candidateIps) {
            tasks.add(() -> probe(ip, null, false, null, null));
        }

        try {
            List<Future<CameraDiscoveredDevice>> futures = pool.invokeAll(tasks, 15, TimeUnit.SECONDS);
            List<CameraDiscoveredDevice> result = new ArrayList<>();
            for (Future<CameraDiscoveredDevice> future : futures) {
                try {
                    CameraDiscoveredDevice device = future.get();
                    if (device != null) {
                        result.add(device);
                    }
                } catch (Exception ignored) {
                    // 单个探测失败/超时不影响整体结果
                }
            }
            return result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ArrayList<>();
        } finally {
            pool.shutdownNow();
        }
    }

    /**
     * 探测单个 IP：依次尝试常见端口，任一端口开放即视为发现设备
     */
    private CameraDiscoveredDevice probe(String ip, Integer knownPort, boolean isAdded, Long deviceId, String deviceName) {
        int[] ports;
        if (knownPort != null && knownPort > 0) {
            // 已添加设备：已知端口优先探测
            List<Integer> list = new ArrayList<>();
            list.add(knownPort);
            for (int p : CAMERA_PORTS) {
                if (p != knownPort) {
                    list.add(p);
                }
            }
            ports = list.stream().mapToInt(Integer::intValue).toArray();
        } else {
            ports = CAMERA_PORTS;
        }

        for (int port : ports) {
            if (isReachable(ip, port, 250)) {
                CameraDiscoveredDevice device = new CameraDiscoveredDevice();
                device.setIpAddress(ip);
                device.setPort(port);
                device.setBrand(inferBrand(port));
                device.setOnline(true);
                device.setIsAdded(isAdded);
                device.setDeviceId(deviceId);
                device.setDeviceName(deviceName);
                return device;
            }
        }
        return null;
    }

    /**
     * TCP 连接探测，超时毫秒
     */
    private boolean isReachable(String ip, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, port), timeout);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据端口推测厂商
     */
    private String inferBrand(int port) {
        switch (port) {
            case 8000:
                return "Hikvision";
            case 37777:
                return "Dahua";
            case 554:
                return "RTSP";
            default:
                return "其他";
        }
    }
}