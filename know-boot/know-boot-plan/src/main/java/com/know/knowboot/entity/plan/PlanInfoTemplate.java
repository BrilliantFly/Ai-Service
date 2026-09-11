package com.know.knowboot.entity.plan;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 计划信息模板实体
 */
@Data
@TableName("plan_info_template")
@ApiModel("计划信息模板实体")
public class PlanInfoTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("父模板ID")
    private Long parentId;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("计划类型(对应plan_type.type_code)")
    private String planType;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("所属象限ID")
    private Long quadrantId;

    @ApiModelProperty("标签(逗号分隔)")
    private String tags;

    @ApiModelProperty("默认优先级")
    private Integer defaultPriority;

    @ApiModelProperty("默认持续天数")
    private Integer defaultDurationDays;

    @ApiModelProperty("默认提醒时间")
    private String defaultRemindTime;

    @ApiModelProperty("默认习惯定义(JSON)")
    private String defaultHabits;

    @ApiModelProperty("默认习惯模板ID列表(JSON数组)")
    private String defaultHabitIds;

    @ApiModelProperty("默认事件定义(JSON)")
    private String defaultEvents;

    @ApiModelProperty("默认事件模板ID列表(JSON数组)")
    private String defaultEventIds;

    @ApiModelProperty("默认子计划定义(JSON)")
    private String defaultSubPlans;

    @ApiModelProperty("使用次数")
    private Integer useCount;

    @ApiModelProperty("评分")
    private BigDecimal rating;

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
