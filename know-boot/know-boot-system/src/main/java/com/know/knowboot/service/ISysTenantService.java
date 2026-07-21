package com.know.knowboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.know.knowboot.entity.tenant.SysTenant;

import java.util.List;

/**
 * 租户 Service 接口
 */
public interface ISysTenantService extends IService<SysTenant> {

    /**
     * 分页查询租户
     */
    IPage<SysTenant> page(SysTenant query, Integer pageNum, Integer pageSize);

    /**
     * 获取所有启用的租户
     */
    List<SysTenant> listEnabled();

    /**
     * 根据用户ID获取可访问的租户列表
     */
    List<SysTenant> listByUserId(Long userId);

    /**
     * 获取用户当前租户
     */
    SysTenant getUserCurrentTenant(Long userId);

    /**
     * 获取租户详情
     */
    SysTenant getById(Long id);

    /**
     * 根据编码获取租户
     */
    SysTenant getByCode(String code);

    /**
     * 新增租户
     */
    boolean add(SysTenant tenant);

    /**
     * 修改租户
     */
    boolean update(SysTenant tenant);

    /**
     * 删除租户
     */
    boolean delete(Long id);

    /**
     * 设置用户当前租户
     */
    boolean setUserCurrentTenant(Long userId, Long tenantId);

    /**
     * 授权用户到租户
     */
    boolean assignUserToTenant(Long userId, Long tenantId, Boolean isAdmin);

    /**
     * 从租户移除用户
     */
    boolean removeUserFromTenant(Long userId, Long tenantId);
}