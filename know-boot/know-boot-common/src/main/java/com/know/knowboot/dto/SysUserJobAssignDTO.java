package com.know.knowboot.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("用户岗位分配DTO")
public class SysUserJobAssignDTO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("岗位ID列表")
    private List<Long> jobIds;
}