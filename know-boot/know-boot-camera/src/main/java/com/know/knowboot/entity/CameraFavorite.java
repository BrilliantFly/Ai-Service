package com.know.knowboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备收藏实体
 */
@Data
@TableName("camera_favorite")
@ApiModel("设备收藏实体")
public class CameraFavorite implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("设备ID")
    private Long deviceId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("创建时间")
    private Long createTime;
}