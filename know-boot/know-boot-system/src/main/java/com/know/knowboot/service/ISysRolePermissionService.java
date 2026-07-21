package com.know.knowboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.know.knowboot.entity.tenant.SysRolePermission;

import java.util.List;

/**
 * 角色-权限关联 Service 接口
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {

    /**
     * 分页查询
     */
    IPage<SysRolePermission> page(SysRolePermission query, Integer pageNum, Integer pageSize);

    /**
     * 获取所有关联
     */
    List<SysRolePermission> listAll();

    /**
     * 根据角色ID获取权限ID列表
     */
    List<Long> listPermissionIdsByRoleId(Long roleId);

    /**
     * 根据角色ID分配权限
     */
    boolean assignPermissions(Long roleId, List<Long> permissionIds);

    /**
     * 新增关联
     */
    boolean add(SysRolePermission rolePermission);

    /**
     * 删除关联
     */
    boolean delete(Long id);

    /**
     * 根据角色ID删除
     */
    boolean deleteByRoleId(Long roleId);
}