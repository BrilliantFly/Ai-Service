package com.know.knowboot.entity.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 操作日志表
 */
@Data
@TableName("sys_oper_log")
@ApiModel("操作日志实体")
public class SysOperLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("操作模块")
    private String title;

    @ApiModelProperty("业务类型 (0:其他, 1:新增, 2:修改, 3:删除)")
    private Integer businessType;

    @ApiModelProperty("方法名")
    private String method;

    @ApiModelProperty("请求方式 (GET/POST/PUT/DELETE)")
    private String requestMethod;

    @ApiModelProperty("操作类型 (0:其他, 1:后台用户, 2:手机端用户)")
    private Integer operatorType;

    @ApiModelProperty("操作人")
    private String operName;

    @ApiModelProperty("操作IP")
    private String operIp;

    @ApiModelProperty("操作位置")
    private String operLocation;

    @ApiModelProperty("请求URL")
    private String operUrl;

    @ApiModelProperty("请求参数")
    private String operParam;

    @ApiModelProperty("返回结果")
    private String jsonResult;

    @ApiModelProperty("状态 (0:正常, 1:异常)")
    private Integer status;

    @ApiModelProperty("错误信息")
    private String errorMsg;

    @ApiModelProperty("操作时间")
    private Long operTime;

    @ApiModelProperty("租户ID")
    private Long tenantId;
}