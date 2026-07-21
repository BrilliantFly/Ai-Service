package com.know.knowboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.CameraRecord;

import java.util.List;

/**
 * 录像记录服务接口
 */
public interface ICameraRecordService {

    /**
     * 分页查询
     */
    IPage<CameraRecord> page(CameraRecord query, Long deviceId, Integer pageNum, Integer pageSize);

    /**
     * 获取详情
     */
    CameraRecord getById(Long id);

    /**
     * 开始录制
     */
    CameraRecord startRecord(Long deviceId, Long userId, Integer recordType);

    /**
     * 停止录制
     */
    boolean stopRecord(Long id);

    /**
     * 删除录像
     */
    boolean delete(Long id);

    /**
     * 获取正在录制的录像
     */
    CameraRecord getRecordingByDeviceId(Long deviceId);

    /**
     * 批量删除录像
     */
    boolean deleteBatch(List<Long> ids);
}