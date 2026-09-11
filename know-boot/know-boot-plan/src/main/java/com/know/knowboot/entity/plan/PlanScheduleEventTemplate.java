package com.know.knowboot.entity.plan;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 日程事件模板实体
 */
@Data
@TableName("plan_schedule_event_template")
@ApiModel("日程事件模板实体")
public class PlanScheduleEventTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("日程标题")
    private String title;

    @ApiModelProperty("日程类型(1:日程 2:待办 3:提醒)")
    private Integer eventType;

    @ApiModelProperty("四象限(1:重要紧急 2:重要不紧急 3:紧急不重要 4:不紧急不重要)")
    private Integer quadrant;

    @ApiModelProperty("优先级(1:低 2:中 3:高)")
    private Integer priority;

    @ApiModelProperty("是否重复(0:否 1:是)")
    private Integer isRepeat;

    @ApiModelProperty("重复类型(1:每日 2:每周 3:每月 4:每年)")
    private Integer repeatType;

    @ApiModelProperty("重复规则JSON")
    private String repeatRule;

    @ApiModelProperty("是否全天(0:否 1:是)")
    private Integer isAllDay;

    @ApiModelProperty("开始时间")
    private Long startTime;

    @ApiModelProperty("结束时间")
    private Long endTime;

    @ApiModelProperty("提前提醒分钟数")
    private Integer remindMinutes;

    @ApiModelProperty("地点")
    private String location;

    @ApiModelProperty("日程内容")
    private String content;

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
