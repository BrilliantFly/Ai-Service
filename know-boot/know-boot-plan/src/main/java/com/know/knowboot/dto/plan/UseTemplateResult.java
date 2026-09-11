package com.know.knowboot.dto.plan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 使用模板结果
 */
@Data
@ApiModel("使用模板结果")
public class UseTemplateResult {

    @ApiModelProperty("根计划ID")
    private Long planId;

    @ApiModelProperty("创建计划数")
    private int plansCreated;

    @ApiModelProperty("创建习惯数")
    private int habitsCreated;

    @ApiModelProperty("创建事件数")
    private int eventsCreated;
}
