package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.CameraSnapshot;
import com.know.knowboot.mapper.CameraSnapshotMapper;
import com.know.knowboot.service.ICameraSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 截图记录服务实现
 */
@Service
public class CameraSnapshotServiceImpl extends ServiceImpl<CameraSnapshotMapper, CameraSnapshot> implements ICameraSnapshotService {

    @Autowired
    private CameraSnapshotMapper cameraSnapshotMapper;

    @Override
    public IPage<CameraSnapshot> page(CameraSnapshot query, Long deviceId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<CameraSnapshot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(deviceId != null, CameraSnapshot::getDeviceId, deviceId)
                .orderByDesc(CameraSnapshot::getCaptureTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public CameraSnapshot getById(Long id) {
        return cameraSnapshotMapper.selectById(id);
    }

    @Override
    public CameraSnapshot getLatest(Long deviceId) {
        return cameraSnapshotMapper.selectLatestByDeviceId(deviceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CameraSnapshot saveSnapshot(Long deviceId, Long userId, String filePath, String thumbnail) {
        CameraSnapshot snapshot = new CameraSnapshot();
        snapshot.setDeviceId(deviceId);
        snapshot.setCaptureTime(System.currentTimeMillis());
        snapshot.setFilePath(filePath);
        snapshot.setThumbnail(thumbnail);
        snapshot.setCreateBy(userId);
        save(snapshot);
        return snapshot;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(List<Long> ids) {
        return removeByIds(ids);
    }
}