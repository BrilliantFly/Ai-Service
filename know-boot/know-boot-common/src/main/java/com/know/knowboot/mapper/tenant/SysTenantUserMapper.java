package com.know.knowboot.mapper.tenant;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.tenant.SysTenantUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户租户关联 Mapper
 */
@Mapper
public interface SysTenantUserMapper extends IBaseMapper<SysTenantUser> {

    /**
     * 根据用户ID查询租户ID列表
     */
    @Select("SELECT tenant_id FROM sys_tenant_user WHERE user_id = #{userId} AND status = 1")
    List<Long> selectTenantIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID和租户ID查询
     */
    @Select("SELECT * FROM sys_tenant_user WHERE user_id = #{userId} AND tenant_id = #{tenantId} LIMIT 1")
    SysTenantUser selectByUserAndTenant(@Param("userId") Long userId, @Param("tenantId") Long tenantId);
}