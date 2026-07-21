package com.know.knowboot.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("用户部门分配DTO")
public class SysUserDeptAssignDTO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("部门ID列表")
    private List<Long> deptIds;
}