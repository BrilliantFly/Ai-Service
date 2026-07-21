package com.know.knowboot.entity.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_role_data_scope")
@ApiModel("角色数据权限配置实体")
public class SysRoleDataScope implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("自定义部门ID")
    private Long deptId;

    /**
     * 数据范围类型
     * 1: 全部数据权限
     * 2: 本部门数据权限
     * 3: 本部门及子部门数据权限
     * 4: 仅本人数据权限
     * 5: 自定义数据权限
     */
    @ApiModelProperty("数据范围类型 (1:全部, 2:本部门, 3:本部门及子部门, 4:仅本人, 5:自定义)")
    private Integer dataScopeType;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新人")
    private String updateBy;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}