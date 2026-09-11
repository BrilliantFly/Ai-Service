package com.know.knowboot.dto.plan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 在已有计划中使用模板请求
 */
@Data
@ApiModel("在已有计划中使用模板请求")
public class UseInPlanRequest {

    @ApiModelProperty("目标计划ID")
    private Long planId;

    @ApiModelProperty("模板ID")
    private Long templateId;

    @ApiModelProperty("执行状态(默认1)")
    private Integer execStatus;

    @ApiModelProperty("选中的习惯模板ID列表(为空则全部)")
    private List<Long> selectedHabitIds;

    @ApiModelProperty("选中的事件模板ID列表(为空则全部)")
    private List<Long> selectedEventIds;

    @ApiModelProperty("自定义配置")
    private Map<String, Object> customizations;
}
