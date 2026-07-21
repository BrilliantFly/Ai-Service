package com.know.knowboot.entity.plan;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 打卡记录实体
 */
@Data
@TableName("plan_habit_record")
@ApiModel("打卡记录实体")
public class PlanHabitRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("习惯ID")
    private Long habitId;

    @ApiModelProperty("打卡日期(时间戳)")
    private Long recordDate;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("图片(多张逗号分隔)")
    private String images;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Long createTime;
}
