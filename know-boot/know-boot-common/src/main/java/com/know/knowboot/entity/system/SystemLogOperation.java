package com.know.knowboot.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_log_operation")
@ApiModel("操作日志实体")
public class SystemLogOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("操作模块")
    private String module;

    @ApiModelProperty("业务类型")
    private Integer businessType;

    @ApiModelProperty("请求方法")
    private String method;

    @ApiModelProperty("请求方式")
    private String requestMethod;

    @ApiModelProperty("操作类型")
    private Integer operatorType;

    @ApiModelProperty("请求URL")
    private String requestUrl;

    @ApiModelProperty("请求参数")
    private String requestParam;

    @ApiModelProperty("响应结果")
    private String responseResult;

    @ApiModelProperty("操作状态")
    private Integer status;

    @ApiModelProperty("错误消息")
    private String errorMsg;

    @ApiModelProperty("操作时间")
    private Date operateTime;

    @ApiModelProperty("耗时(毫秒)")
    private Integer costTime;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    private Date createTime;
}