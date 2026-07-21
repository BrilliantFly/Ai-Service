package com.know.knowboot.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户租户分配DTO
 */
@Data
public class SysUserTenantAssignDTO {
    private Long userId;
    private List<Long> tenantIds;
}