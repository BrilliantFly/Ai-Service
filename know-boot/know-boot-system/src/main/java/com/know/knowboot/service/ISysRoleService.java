package com.know.knowboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.know.knowboot.entity.tenant.SysRole;

import java.util.List;

/**
 * 角色服务接口
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 分页查询角色
     */
    IPage<SysRole> page(SysRole query, Integer pageNum, Integer pageSize);

    /**
     * 获取所有正常状态的角色
     */
    List<SysRole> listNormal();

    /**
     * 新增角色
     */
    boolean add(SysRole role);

    /**
     * 修改角色
     */
    boolean update(SysRole role);

    /**
     * 删除角色
     */
    boolean delete(Long id);

    /**
     * 获取角色关联的菜单ID列表
     */
    List<Long> getMenuIds(Long roleId);

    /**
     * 分配菜单权限
     */
    boolean assignMenus(Long roleId, List<Long> menuIds);
}