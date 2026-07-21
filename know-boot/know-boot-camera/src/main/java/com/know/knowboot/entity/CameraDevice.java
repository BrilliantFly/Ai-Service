package com.know.knowboot.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 摄像头设备实体
 */
@Data
@TableName("camera_device")
@ApiModel("摄像头设备实体")
public class CameraDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备编号")
    private String deviceCode;

    @ApiModelProperty("设备型号")
    private String deviceModel;

    @ApiModelProperty("厂商")
    private String manufacturer;

    @ApiModelProperty("局域网IP")
    private String ipAddress;

    @ApiModelProperty("MAC地址")
    private String macAddress;

    @ApiModelProperty("端口")
    private Integer port;

    @ApiModelProperty("设备用户名")
    private String username;

    @ApiModelProperty("设备密码(加密存储)")
    private String password;

    @ApiModelProperty("视频流地址")
    private String streamUrl;

    @ApiModelProperty("快照地址")
    private String snapshotUrl;

    @ApiModelProperty("状态: 0-离线, 1-在线")
    private Integer status;

    @ApiModelProperty("安装位置")
    private String position;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("所属用户")
    private Long userId;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新人")
    private Long updateBy;

    @ApiModelProperty("更新时间")
    private Long updateTime;

    @TableLogic
    @ApiModelProperty("删除时间")
    private Long deleteTime;
}