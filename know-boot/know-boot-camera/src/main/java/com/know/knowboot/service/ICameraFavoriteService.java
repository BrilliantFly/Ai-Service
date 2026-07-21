package com.know.knowboot.service;

import com.know.knowboot.entity.CameraFavorite;

import java.util.List;

/**
 * 设备收藏服务接口
 */
public interface ICameraFavoriteService {

    /**
     * 获取用户的收藏列表
     */
    List<CameraFavorite> listByUser(Long userId);

    /**
     * 检查是否已收藏
     */
    boolean isFavorited(Long deviceId, Long userId);

    /**
     * 添加收藏
     */
    CameraFavorite add(Long deviceId, Long userId);

    /**
     * 取消收藏
     */
    boolean remove(Long deviceId, Long userId);

    /**
     * 切换收藏状态
     */
    boolean toggle(Long deviceId, Long userId);
}
