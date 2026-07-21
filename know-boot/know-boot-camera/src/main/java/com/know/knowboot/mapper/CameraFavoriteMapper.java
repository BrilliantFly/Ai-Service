package com.know.knowboot.mapper;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.CameraFavorite;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 设备收藏Mapper
 */
@Mapper
public interface CameraFavoriteMapper extends IBaseMapper<CameraFavorite> {

    /**
     * 检查是否已收藏
     */
    @Select("SELECT * FROM camera_favorite WHERE device_id = #{deviceId} AND user_id = #{userId}")
    CameraFavorite selectByDeviceAndUser(@Param("deviceId") Long deviceId, @Param("userId") Long userId);

    /**
     * 删除收藏
     */
    @Delete("DELETE FROM camera_favorite WHERE device_id = #{deviceId} AND user_id = #{userId}")
    int deleteByDeviceAndUser(@Param("deviceId") Long deviceId, @Param("userId") Long userId);
}