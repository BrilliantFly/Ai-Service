package com.know.knowboot.dto;

import lombok.Data;

import java.util.List;

/**
 * 租户用户分配DTO
 */
@Data
public class SysTenantUserAssignDTO {
    private Long tenantId;
    private List<Long> userIds;
    private Boolean isAdmin;
}