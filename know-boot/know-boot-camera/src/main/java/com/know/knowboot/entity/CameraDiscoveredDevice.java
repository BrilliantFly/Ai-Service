package com.know.knowboot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 局域网发现的摄像头设备
 */
@Data
@ApiModel("局域网发现的摄像头设备")
public class CameraDiscoveredDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("局域网IP")
    private String ipAddress;

    @ApiModelProperty("开放端口")
    private Integer port;

    @ApiModelProperty("推测厂商")
    private String brand;

    @ApiModelProperty("是否在线")
    private Boolean online;

    @ApiModelProperty("是否已添加")
    private Boolean isAdded;

    @ApiModelProperty("已添加时的设备ID")
    private Long deviceId;

    @ApiModelProperty("已添加时的设备名称")
    private String deviceName;
}
