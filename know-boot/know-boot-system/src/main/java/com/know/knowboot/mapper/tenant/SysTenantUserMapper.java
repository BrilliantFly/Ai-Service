package com.know.knowboot.mapper.tenant;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.know.knowboot.entity.tenant.SysTenantUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 租户用户关联 Mapper
 */
@Mapper
public interface SysTenantUserMapper extends BaseMapper<SysTenantUser> {
    
    /**
     * 根据用户ID查询关联的租户ID列表
     */
    default List<Long> selectTenantIdsByUserId(Long userId) {
        List<SysTenantUser> list = selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysTenantUser>()
                .eq(SysTenantUser::getUserId, userId));
        List<Long> tenantIds = new ArrayList<>();
        for (SysTenantUser tu : list) {
            tenantIds.add(tu.getTenantId());
        }
        return tenantIds;
    }
}