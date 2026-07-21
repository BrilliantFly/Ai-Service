package com.know.knowboot.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("角色数据权限配置DTO")
public class RoleDataScopeDTO {

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("数据权限类型")
    private Integer dataScopeType;

    @ApiModelProperty("自定义部门ID列表")
    private List<Long> customDeptIds;
}