package com.know.knowboot.entity.plan;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 计划信息实体
 */
@Data
@TableName("plan_info")
@ApiModel("计划信息实体")
public class PlanInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("计划名称")
    private String planName;

    @ApiModelProperty("计划分类(对应plan_type.type_code)")
    private String planType;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("目标效果")
    private String targetEffect;

    @ApiModelProperty("所属象限(默认重要不紧急)")
    private Long quadrantId;

    @ApiModelProperty("优先级 0-10")
    private Integer priority;

    @ApiModelProperty("状态: 0-待开始, 1-进行中, 2-已完成, 3-已取消")
    private Integer status;

    @ApiModelProperty("进度百分比 0-100")
    private Integer progress;

    @ApiModelProperty("计划开始时间")
    private Long planStartTime;

    @ApiModelProperty("计划结束时间")
    private Long planEndTime;

    @ApiModelProperty("实际开始时间")
    private Long actualStartTime;

    @ApiModelProperty("实际结束时间")
    private Long actualEndTime;

    @ApiModelProperty("负责人ID")
    private Long leaderId;

    @ApiModelProperty("参与人ID列表(逗号分隔)")
    private String participantIds;

    @ApiModelProperty("父计划ID(支持WBS拆解)")
    private Long parentId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新人")
    private Long updateBy;

    @ApiModelProperty("更新时间")
    private Long updateTime;

    @TableLogic(value = "0", delval = "1")
    @ApiModelProperty("删除时间")
    private Long deleteTime = 0L;
}
