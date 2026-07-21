package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.CameraFavorite;
import com.know.knowboot.mapper.CameraFavoriteMapper;
import com.know.knowboot.service.ICameraFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 设备收藏服务实现
 */
@Service
public class CameraFavoriteServiceImpl extends ServiceImpl<CameraFavoriteMapper, CameraFavorite> implements ICameraFavoriteService {

    @Autowired
    private CameraFavoriteMapper cameraFavoriteMapper;

    @Override
    public List<CameraFavorite> listByUser(Long userId) {
        LambdaQueryWrapper<CameraFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CameraFavorite::getUserId, userId)
                .orderByAsc(CameraFavorite::getSort);
        return list(wrapper);
    }

    @Override
    public boolean isFavorited(Long deviceId, Long userId) {
        return cameraFavoriteMapper.selectByDeviceAndUser(deviceId, userId) != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CameraFavorite add(Long deviceId, Long userId) {
        CameraFavorite favorite = new CameraFavorite();
        favorite.setDeviceId(deviceId);
        favorite.setUserId(userId);
        favorite.setCreateTime(System.currentTimeMillis());
        save(favorite);
        return favorite;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean remove(Long deviceId, Long userId) {
        return cameraFavoriteMapper.deleteByDeviceAndUser(deviceId, userId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggle(Long deviceId, Long userId) {
        if (isFavorited(deviceId, userId)) {
            return remove(deviceId, userId);
        } else {
            add(deviceId, userId);
            return true;
        }
    }
}
