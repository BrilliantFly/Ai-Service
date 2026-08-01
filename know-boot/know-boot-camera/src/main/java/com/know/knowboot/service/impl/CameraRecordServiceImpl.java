package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.CameraRecord;
import com.know.knowboot.mapper.CameraRecordMapper;
import com.know.knowboot.service.ICameraRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 录像记录服务实现
 */
@Service
public class CameraRecordServiceImpl extends ServiceImpl<CameraRecordMapper, CameraRecord> implements ICameraRecordService {

    @Autowired
    private CameraRecordMapper cameraRecordMapper;

    @Override
    public IPage<CameraRecord> page(CameraRecord query, Long deviceId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<CameraRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(deviceId != null, CameraRecord::getDeviceId, deviceId)
                .eq(query.getRecordType() != null, CameraRecord::getRecordType, query.getRecordType())
                .eq(query.getStatus() != null, CameraRecord::getStatus, query.getStatus())
                .orderByDesc(CameraRecord::getStartTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public CameraRecord getById(Long id) {
        return cameraRecordMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CameraRecord startRecord(Long deviceId, Long userId, Integer recordType) {
        CameraRecord record = new CameraRecord();
        record.setDeviceId(deviceId);
        record.setRecordType(recordType != null ? recordType : 1); // 默认手动录制
        long now = System.currentTimeMillis();
        record.setStartTime(now);
        record.setStatus(0); // 录制中
        record.setCreateBy(userId);
        // 生成录像文件名与路径（实际录制由设备端/前端完成，这里约定统一命名规范）
        record.setFileName("record_" + deviceId + "_" + now + ".mp4");
        record.setFilePath("records/" + deviceId + "/" + now + ".mp4");
        save(record);
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean stopRecord(Long id) {
        CameraRecord record = cameraRecordMapper.selectById(id);
        if (record == null) {
            return false;
        }
        
        long endTime = System.currentTimeMillis();
        int duration = (int) ((endTime - record.getStartTime()) / 1000);
        
        record.setEndTime(endTime);
        record.setDuration(duration);
        record.setStatus(1); // 已完成
        
        return updateById(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public CameraRecord getRecordingByDeviceId(Long deviceId) {
        return cameraRecordMapper.selectRecordingByDeviceId(deviceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(List<Long> ids) {
        return removeByIds(ids);
    }
}