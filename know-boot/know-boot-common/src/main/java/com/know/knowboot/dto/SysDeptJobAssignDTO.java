package com.know.knowboot.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("部门岗位分配DTO")
public class SysDeptJobAssignDTO {

    @ApiModelProperty("部门ID")
    private Long deptId;

    @ApiModelProperty("岗位ID列表")
    private List<Long> jobIds;
}