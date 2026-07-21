package com.know.knowboot.entity.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@TableName("sys_dept")
@ApiModel("部门实体")
public class SysDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("父部门ID")
    private Long parentId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("部门编码")
    private String deptCode;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("负责人")
    private String leader;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("状态 (1:正常, 0:停用)")
    private Integer status;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新人")
    private String updateBy;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @TableLogic
    @JsonIgnore
    @ApiModelProperty("删除标志")
    private Integer delFlag;

    @TableField(exist = false)
    @ApiModelProperty("子部门列表（前端树形组件用）")
    private List<SysDept> children = new ArrayList<>();

    @TableField(exist = false)
    @ApiModelProperty("是否展开（前端用）")
    private boolean expanded = true;

    @TableField(exist = false)
    @ApiModelProperty("是否叶子节点（前端用）")
    private boolean isLeaf = true;
}