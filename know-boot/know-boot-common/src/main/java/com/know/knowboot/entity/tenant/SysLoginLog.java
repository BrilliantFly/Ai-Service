package com.know.knowboot.entity.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录日志表
 */
@Data
@TableName("sys_login_log")
@ApiModel("登录日志实体")
public class SysLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("登录IP")
    private String ipaddr;

    @ApiModelProperty("登录位置")
    private String loginLocation;

    @ApiModelProperty("浏览器类型")
    private String browser;

    @ApiModelProperty("操作系统")
    private String os;

    @ApiModelProperty("状态 (0:失败, 1:成功)")
    private Integer status;

    @ApiModelProperty("提示消息")
    private String msg;

    @ApiModelProperty("登录时间")
    private Long loginTime;

    @ApiModelProperty("租户ID")
    private Long tenantId;
}