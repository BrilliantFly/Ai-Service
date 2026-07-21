package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.context.TenantContextHolder;
import com.know.knowboot.entity.tenant.SysTenant;
import com.know.knowboot.entity.tenant.SysTenantUser;
import com.know.knowboot.mapper.tenant.SysTenantMapper;
import com.know.knowboot.mapper.tenant.SysTenantUserMapper;
import com.know.knowboot.service.ISysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 租户服务实现
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

    @Autowired
    private SysTenantMapper sysTenantMapper;

    @Autowired
    private SysTenantUserMapper sysTenantUserMapper;

    @Override
    public IPage<SysTenant> page(SysTenant query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getName() != null, SysTenant::getName, query.getName())
                .like(query.getCode() != null, SysTenant::getCode, query.getCode())
                .eq(query.getStatus() != null, SysTenant::getStatus, query.getStatus())
                .orderByDesc(SysTenant::getId);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<SysTenant> listEnabled() {
        return list(new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getStatus, 1)
                .orderByAsc(SysTenant::getId));
    }

    @Override
    public List<SysTenant> listByUserId(Long userId) {
        // 获取用户关联的租户ID列表
        List<Long> tenantIds = sysTenantUserMapper.selectTenantIdsByUserId(userId);
        if (tenantIds == null || tenantIds.isEmpty()) {
            // 未关联租户，返回默认租户
            return list(new LambdaQueryWrapper<SysTenant>()
                    .eq(SysTenant::getCode, "DEFAULT"));
        }
        return list(new LambdaQueryWrapper<SysTenant>()
                .in(SysTenant::getId, tenantIds)
                .eq(SysTenant::getStatus, 1));
    }

    @Override
    public SysTenant getUserCurrentTenant(Long userId) {
        // 获取用户的默认租户
        List<SysTenantUser> userTenants = sysTenantUserMapper.selectList(
                new LambdaQueryWrapper<SysTenantUser>()
                        .eq(SysTenantUser::getUserId, userId)
                        .orderByAsc(SysTenantUser::getId)
                        .last("limit 1")
        );
        
        if (userTenants == null || userTenants.isEmpty()) {
            // 返回系统默认租户
            return getByCode("DEFAULT");
        }
        
        return getById(userTenants.get(0).getTenantId());
    }

    @Override
    public SysTenant getById(Long id) {
        return sysTenantMapper.selectById(id);
    }

    @Override
    public SysTenant getByCode(String code) {
        return sysTenantMapper.selectOne(
                new LambdaQueryWrapper<SysTenant>()
                        .eq(SysTenant::getCode, code)
        );
    }

    @Override
    public boolean add(SysTenant tenant) {
        // 检查编码唯一性
        long count = count(new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getCode, tenant.getCode()));
        if (count > 0) {
            throw new RuntimeException("租户编码已存在");
        }
        return save(tenant);
    }

    @Override
    public boolean update(SysTenant tenant) {
        return updateById(tenant);
    }

    @Override
    public boolean delete(Long id) {
        // 删除租户用户关联
        sysTenantUserMapper.delete(
                new LambdaQueryWrapper<SysTenantUser>()
                        .eq(SysTenantUser::getTenantId, id)
        );
        return removeById(id);
    }

    @Override
    public boolean setUserCurrentTenant(Long userId, Long tenantId) {
        // 验证用户是否属于该租户
        long count = sysTenantUserMapper.selectCount(
                new LambdaQueryWrapper<SysTenantUser>()
                        .eq(SysTenantUser::getUserId, userId)
                        .eq(SysTenantUser::getTenantId, tenantId)
        );
        if (count == 0) {
            throw new RuntimeException("用户不属于该租户");
        }
        
        // 获取租户信息并设置到上下文
        SysTenant tenant = getById(tenantId);
        if (tenant == null) {
            throw new RuntimeException("租户不存在");
        }
        
        // 获取是否是租户管理员
        SysTenantUser userTenant = sysTenantUserMapper.selectOne(
                new LambdaQueryWrapper<SysTenantUser>()
                        .eq(SysTenantUser::getUserId, userId)
                        .eq(SysTenantUser::getTenantId, tenantId)
        );
        
        // 设置到上下文
        TenantContextHolder.setTenant(
                tenantId, 
                tenant.getCode(), 
                userTenant != null && Boolean.TRUE.equals(userTenant.getIsAdmin())
        );
        
        return true;
    }

    @Override
    public boolean assignUserToTenant(Long userId, Long tenantId, Boolean isAdmin) {
        // 检查是否已关联
        long count = sysTenantUserMapper.selectCount(
                new LambdaQueryWrapper<SysTenantUser>()
                        .eq(SysTenantUser::getUserId, userId)
                        .eq(SysTenantUser::getTenantId, tenantId)
        );
        if (count > 0) {
            throw new RuntimeException("用户已绑定到该租户");
        }
        
        SysTenantUser userTenant = new SysTenantUser();
        userTenant.setUserId(userId);
        userTenant.setTenantId(tenantId);
        userTenant.setIsAdmin(Boolean.TRUE.equals(isAdmin));
        
        return sysTenantUserMapper.insert(userTenant) > 0;
    }

    @Override
    public boolean removeUserFromTenant(Long userId, Long tenantId) {
        return sysTenantUserMapper.delete(
                new LambdaQueryWrapper<SysTenantUser>()
                        .eq(SysTenantUser::getUserId, userId)
                        .eq(SysTenantUser::getTenantId, tenantId)
        ) > 0;
    }
}