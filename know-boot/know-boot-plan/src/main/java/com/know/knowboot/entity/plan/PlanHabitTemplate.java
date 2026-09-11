package com.know.knowboot.entity.plan;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 习惯模板实体
 */
@Data
@TableName("plan_habit_template")
@ApiModel("习惯模板实体")
public class PlanHabitTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("习惯名称")
    private String name;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("频率类型(1:每天 2:每周 3:自定义)")
    private Integer frequencyType;

    @ApiModelProperty("频率规则")
    private String frequencyRule;

    @ApiModelProperty("提醒时间(HH:mm)")
    private String reminderTime;

    @ApiModelProperty("休息日(逗号分隔，0-6)")
    private String restDays;

    @ApiModelProperty("目标天数")
    private Integer targetDays;

    @ApiModelProperty("目标值")
    private Integer targetValue;

    @ApiModelProperty("目标单位")
    private String targetUnit;

    @ApiModelProperty("打卡方式(boolean/numeric)")
    private String trackingType;

    @ApiModelProperty("计划类型(对应plan_type.type_code)")
    private String planType;

    @ApiModelProperty("标签(逗号分隔)")
    private String tags;

    @ApiModelProperty("使用次数")
    private Integer useCount;

    @ApiModelProperty("可见性(0:私有 1:公开)")
    private Integer visibility;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新人")
    private Long updateBy;

    @ApiModelProperty("更新时间")
    private Long updateTime;

    @TableLogic(value = "0", delval = "1")
    @ApiModelProperty("删除标记")
    private Integer delFlag;

    @ApiModelProperty("删除时间")
    private Long deleteTime;
}
