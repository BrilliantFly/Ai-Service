package com.know.knowboot.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.CameraDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 摄像头设备Mapper
 */
@Mapper
public interface CameraDeviceMapper extends IBaseMapper<CameraDevice> {

    /**
     * 分页查询用户设备
     */
    IPage<CameraDevice> selectPageByUserId(Page<CameraDevice> page, @Param("userId") Long userId);

    /**
     * 根据设备编号查询
     */
    CameraDevice selectByDeviceCode(@Param("deviceCode") String deviceCode);

    /**
     * 更新设备状态
     */
    @Update("UPDATE camera_device SET status = #{status} WHERE id = #{id} AND delete_time IS NULL")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}