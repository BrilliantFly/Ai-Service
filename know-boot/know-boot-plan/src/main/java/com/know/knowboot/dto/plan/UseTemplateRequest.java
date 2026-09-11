package com.know.knowboot.dto.plan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * 使用模板请求
 */
@Data
@ApiModel("使用模板请求")
public class UseTemplateRequest {

    @ApiModelProperty("模板ID")
    private Long templateId;

    @ApiModelProperty("计划名称(为空则使用模板名称)")
    private String planName;

    @ApiModelProperty("计划开始日期(毫秒时间戳,为空则使用当前时间)")
    private Long startDate;

    @ApiModelProperty("执行状态(默认1)")
    private Integer execStatus;

    @ApiModelProperty("自定义配置(skipSubPlans/habitOverrides/eventOverrides)")
    private Map<String, Object> customizations;
}
