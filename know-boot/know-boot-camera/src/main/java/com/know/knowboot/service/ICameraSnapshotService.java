package com.know.knowboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.CameraSnapshot;

import java.util.List;

/**
 * 截图记录服务接口
 */
public interface ICameraSnapshotService {

    /**
     * 分页查询
     */
    IPage<CameraSnapshot> page(CameraSnapshot query, Long deviceId, Integer pageNum, Integer pageSize);

    /**
     * 获取详情
     */
    CameraSnapshot getById(Long id);

    /**
     * 获取最新截图
     */
    CameraSnapshot getLatest(Long deviceId);

    /**
     * 保存截图
     */
    CameraSnapshot saveSnapshot(Long deviceId, Long userId, String filePath, String thumbnail);

    /**
     * 删除截图
     */
    boolean delete(Long id);

    /**
     * 批量删除截图
     */
    boolean deleteBatch(List<Long> ids);
}