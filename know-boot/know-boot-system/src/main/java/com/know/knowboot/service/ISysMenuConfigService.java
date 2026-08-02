package com.know.knowboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.know.knowboot.entity.tenant.SysMenuConfig;

import java.util.List;

/**
 * 菜单配置 Service 接口
 */
public interface ISysMenuConfigService extends IService<SysMenuConfig> {

    /**
     * 分页查询菜单配置
     */
    IPage<SysMenuConfig> listConfig(SysMenuConfig query, Integer pageNum, Integer pageSize);

    /**
     * 获取菜单配置列表
     */
    List<SysMenuConfig> listConfig(SysMenuConfig query);

    /**
     * 根据菜单类型获取菜单列表
     */
    List<SysMenuConfig> listByMenuType(Integer menuType);

    /**
     * 根据用户ID和菜单类型获取菜单列表（带权限过滤）
     */
    List<SysMenuConfig> listByUserIdAndMenuType(Long userId, Integer menuType);

    /**
     * 获取TabBar菜单列表
     */
    List<SysMenuConfig> listTabBar();

    /**
     * 获取TabBar菜单列表（带权限）
     */
    List<SysMenuConfig> listTabBarByUserId(Long userId);

    /**
     * 获取首页菜单列表
     */
    List<SysMenuConfig> listHomeMenu();

    /**
     * 获取首页菜单列表（带权限）
     */
    List<SysMenuConfig> listHomeMenuByUserId(Long userId);

    /**
     * 获取用户可用菜单列表
     */
    List<SysMenuConfig> listByUserId(Long userId);

    /**
     * 获取菜单详情
     */
    SysMenuConfig getConfigById(Long id);

    /**
     * 新增菜单配置
     */
    boolean addConfig(SysMenuConfig config);

    /**
     * 修改菜单配置
     */
    boolean updateConfig(SysMenuConfig config);

    /**
     * 删除菜单配置
     */
    boolean deleteConfig(Long id);
}