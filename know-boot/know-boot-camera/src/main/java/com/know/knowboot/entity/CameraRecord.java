package com.know.knowboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 录像记录实体
 */
@Data
@TableName("camera_record")
@ApiModel("录像记录实体")
public class CameraRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("设备ID")
    private Long deviceId;

    @ApiModelProperty("录像类型: 1-手动, 2-定时, 3-移动侦测")
    private Integer recordType;

    @ApiModelProperty("录像开始时间")
    private Long startTime;

    @ApiModelProperty("录像结束时间")
    private Long endTime;

    @ApiModelProperty("时长(秒)")
    private Integer duration;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("文件大小(字节)")
    private Long fileSize;

    @ApiModelProperty("云存储地址")
    private String cloudUrl;

    @ApiModelProperty("状态: 0-录制中, 1-已完成, 2-已上传")
    private Integer status;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Long createTime;
}