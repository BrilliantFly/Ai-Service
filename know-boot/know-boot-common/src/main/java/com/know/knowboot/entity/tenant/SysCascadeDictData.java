package com.know.knowboot.entity.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 级联字典数据实体（树形，parentId=0 为顶级）
 */
@Data
@TableName("sys_cascade_dict_data")
@ApiModel("级联字典数据实体")
public class SysCascadeDictData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("所属级联字典类型ID")
    private Long dictTypeId;

    @ApiModelProperty("父级ID (0:顶级)")
    private Long parentId;

    @ApiModelProperty("显示名")
    private String name;

    @ApiModelProperty("编码")
    private String value;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态 (1:启用, 0:停用)")
    private Integer status;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新人")
    private String updateBy;

    @ApiModelProperty("更新时间")
    private Long updateTime;

    @TableLogic
    @ApiModelProperty("删除标志")
    private Integer delFlag;
}
