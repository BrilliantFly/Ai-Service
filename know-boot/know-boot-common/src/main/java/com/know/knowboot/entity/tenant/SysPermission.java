package com.know.knowboot.entity.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 权限表
 */
@Data
@TableName("sys_permission")
@ApiModel("权限实体")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("父权限ID (0:根)")
    private Long parentId;

    @ApiModelProperty("权限名称")
    private String name;

    @ApiModelProperty("权限编码 (system:user:add)")
    private String code;

    @ApiModelProperty("权限类型 (button/api/data)")
    private String permissionType;

    @ApiModelProperty("资源类型 (button/menu/api)")
    private String resourceType;

    @ApiModelProperty("路由/接口路径")
    private String path;

    @ApiModelProperty("前端组件路径")
    private String component;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态 (1:启用, 0:禁用)")
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

    /**
     * 子权限列表（用于树形结构）
     */
    @ApiModelProperty("子权限列表")
    private List<SysPermission> children;
}