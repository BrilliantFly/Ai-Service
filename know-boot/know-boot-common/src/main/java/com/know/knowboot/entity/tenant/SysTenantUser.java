package com.know.knowboot.entity.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户租户关联表
 */
@Data
@TableName("sys_tenant_user")
@ApiModel("用户租户关联实体")
public class SysTenantUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("是否租户管理员 (0:否, 1:是)")
    private Integer isAdmin;

    @ApiModelProperty("状态 (1:正常)")
    private Integer status;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    private Long createTime;
}