package com.know.knowboot.mapper.tenant;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.tenant.SysTenant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 租户 Mapper
 */
@Mapper
public interface SysTenantMapper extends IBaseMapper<SysTenant> {

    /**
     * 根据编码查询租户
     */
    @Select("SELECT * FROM sys_tenant WHERE code = #{code} AND (del_flag = 0 OR del_flag IS NULL) LIMIT 1")
    SysTenant selectByCode(@Param("code") String code);
    
    /**
     * 查询用户关联的租户ID列表
     */
    @Select("SELECT tenant_id FROM sys_tenant_user WHERE user_id = #{userId}")
    List<Long> selectTenantIdsByUserId(@Param("userId") Long userId);
}