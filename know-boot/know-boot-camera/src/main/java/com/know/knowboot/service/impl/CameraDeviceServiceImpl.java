package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.CameraDevice;
import com.know.knowboot.entity.CameraFavorite;
import com.know.knowboot.mapper.CameraDeviceMapper;
import com.know.knowboot.mapper.CameraFavoriteMapper;
import com.know.knowboot.service.ICameraDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
}