package com.know.knowboot.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.CameraRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 录像记录Mapper
 */
@Mapper
public interface CameraRecordMapper extends IBaseMapper<CameraRecord> {

    /**
     * 分页查询设备录像
     */
    IPage<CameraRecord> selectPageByDeviceId(Page<CameraRecord> page, @Param("deviceId") Long deviceId);

    /**
     * 查询正在录制的录像
     */
    @Select("SELECT * FROM camera_record WHERE device_id = #{deviceId} AND status = 0 ORDER BY start_time DESC LIMIT 1")
    CameraRecord selectRecordingByDeviceId(@Param("deviceId") Long deviceId);
}