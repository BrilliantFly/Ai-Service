package com.know.knowboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.know.knowboot.entity.tenant.SysPermission;

import java.util.List;

/**
 * 权限 Service 接口
 */
public interface ISysPermissionService extends IService<SysPermission> {

    /**
     * 分页查询
     */
    IPage<SysPermission> page(SysPermission query, Integer pageNum, Integer pageSize);

    /**
     * 获取所有权限
     */
    List<SysPermission> listAll();

    /**
     * 获取树形权限
     */
    List<SysPermission> listTree();

    /**
     * 根据角色ID获取权限
     */
    List<SysPermission> listByRoleId(Long roleId);

    /**
     * 根据用户ID获取权限编码列表
     */
    List<String> listCodesByUserId(Long userId);

    /**
     * 新增权限
     */
    boolean add(SysPermission permission);

    /**
     * 修改权限
     */
    boolean update(SysPermission permission);

    /**
     * 删除权限
     */
    boolean delete(Long id);
}