package com.know.knowboot.dto.plan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 在已有计划中使用模板结果
 */
@Data
@ApiModel("在已有计划中使用模板结果")
public class UseInPlanResult {

    @ApiModelProperty("计划ID")
    private Long planId;

    @ApiModelProperty("创建习惯数")
    private int habitCount;

    @ApiModelProperty("创建事件数")
    private int eventCount;
}
