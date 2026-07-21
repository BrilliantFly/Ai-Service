package com.know.knowboot.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户角色分配DTO
 */
@Data
public class SysUserRoleAssignDTO {
    private Long userId;
    private List<Long> roleIds;
}