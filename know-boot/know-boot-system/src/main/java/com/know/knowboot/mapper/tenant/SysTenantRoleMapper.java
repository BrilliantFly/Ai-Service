package com.know.knowboot.mapper.tenant;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.know.knowboot.entity.tenant.SysTenantRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 租户角色关联 Mapper
 */
@Mapper
public interface SysTenantRoleMapper extends BaseMapper<SysTenantRole> {

    /**
     * 根据租户ID查询关联的角色ID列表
     */
    default List<SysTenantRole> selectByTenantId(Long tenantId) {
        return selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysTenantRole>()
                .eq(SysTenantRole::getTenantId, tenantId));
    }

    /**
     * 根据租户ID删除关联
     */
    default int deleteByTenantId(Long tenantId) {
        return delete(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysTenantRole>()
                .eq(SysTenantRole::getTenantId, tenantId));
    }
}