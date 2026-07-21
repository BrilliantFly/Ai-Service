package com.know.knowboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.know.knowboot.entity.tenant.SysMenu;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 分页查询菜单
     */
    IPage<SysMenu> page(SysMenu query, Integer pageNum, Integer pageSize);

    /**
     * 获取所有菜单（树形结构用）
     */
    List<SysMenu> listAll();
    
    /**
     * 获取菜单树
     */
    List<SysMenu> listTree();

    /**
     * 获取用户菜单树（用于前端渲染）
     */
    List<SysMenu> listUserMenus(Long userId);

    /**
     * 根据父ID获取子菜单
     */
    List<SysMenu> listByParentId(Long parentId);

    /**
     * 新增菜单
     */
    boolean add(SysMenu menu);

    /**
     * 修改菜单
     */
    boolean update(SysMenu menu);

    /**
     * 删除菜单
     */
    boolean delete(Long id);
}