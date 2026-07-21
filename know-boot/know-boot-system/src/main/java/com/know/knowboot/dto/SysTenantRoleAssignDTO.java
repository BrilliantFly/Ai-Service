package com.know.knowboot.dto;

import lombok.Data;

import java.util.List;

/**
 * 租户角色分配DTO
 */
@Data
public class SysTenantRoleAssignDTO {
    private Long tenantId;
    private List<Long> roleIds;
}