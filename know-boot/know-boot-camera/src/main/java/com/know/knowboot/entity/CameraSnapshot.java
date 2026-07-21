package com.know.knowboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 截图记录实体
 */
@Data
@TableName("camera_snapshot")
@ApiModel("截图记录实体")
public class CameraSnapshot implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("设备ID")
    private Long deviceId;

    @ApiModelProperty("截图时间")
    private Long captureTime;

    @ApiModelProperty("本地路径")
    private String filePath;

    @ApiModelProperty("云存储地址")
    private String cloudUrl;

    @ApiModelProperty("缩略图路径")
    private String thumbnail;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Long createTime;
}