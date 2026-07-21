package com.know.knowboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.CameraDevice;

import java.util.List;

/**
 * 摄像头设备服务接口
 */
public interface ICameraDeviceService {

    /**
     * 分页查询
     */
    IPage<CameraDevice> page(CameraDevice query, Long userId, Integer pageNum, Integer pageSize);

    /**
     * 获取所有设备
     */
    List<CameraDevice> listByUserId(Long userId);

    /**
     * 获取收藏设备
     */
    List<CameraDevice> listFavorites(Long userId);

    /**
     * 获取详情
     */
    CameraDevice getById(Long id);

    /**
     * 新增设备
     */
    boolean add(CameraDevice device, Long userId);

    /**
     * 修改设备
     */
    boolean update(CameraDevice device);

    /**
     * 删除设备
     */
    boolean delete(Long id);

    /**
     * 检查设备编号是否存在
     */
    boolean existsByDeviceCode(String deviceCode, Long excludeId);

    /**
     * 更新设备状态
     */
    boolean updateStatus(Long id, Integer status);
}