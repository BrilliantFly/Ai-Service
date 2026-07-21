package com.know.knowboot.entity.plan;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 计划类型实体
 */
@Data
@TableName("plan_type")
@ApiModel("计划类型实体")
public class PlanType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("类型名称")
    private String typeName;

    @ApiModelProperty("类型编码")
    private String typeCode;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态(0:禁用 1:启用)")
    private Integer status;
}
