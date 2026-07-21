package com.know.knowboot.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.CameraSnapshot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 截图记录Mapper
 */
@Mapper
public interface CameraSnapshotMapper extends IBaseMapper<CameraSnapshot> {

    /**
     * 分页查询设备截图
     */
    @Select("SELECT * FROM camera_snapshot WHERE device_id = #{deviceId} ORDER BY capture_time DESC")
    IPage<CameraSnapshot> selectPageByDeviceId(Page<CameraSnapshot> page, @Param("deviceId") Long deviceId);

    /**
     * 查询最新截图
     */
    @Select("SELECT * FROM camera_snapshot WHERE device_id = #{deviceId} ORDER BY capture_time DESC LIMIT 1")
    CameraSnapshot selectLatestByDeviceId(@Param("deviceId") Long deviceId);
}