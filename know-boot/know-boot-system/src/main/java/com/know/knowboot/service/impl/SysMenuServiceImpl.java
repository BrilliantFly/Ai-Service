package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.tenant.SysMenu;
import com.know.knowboot.entity.tenant.SysRoleMenu;
import com.know.knowboot.entity.tenant.SysUserRole;
import com.know.knowboot.mapper.tenant.SysMenuMapper;
import com.know.knowboot.mapper.tenant.SysRoleMenuMapper;
import com.know.knowboot.mapper.tenant.SysUserRoleMapper;
import com.know.knowboot.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public IPage<SysMenu> page(SysMenu query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getMenuName() != null, SysMenu::getMenuName, query.getMenuName())
                .eq(query.getMenuType() != null, SysMenu::getMenuType, query.getMenuType())
                .eq(query.getStatus() != null, SysMenu::getStatus, query.getStatus())
                .orderByAsc(SysMenu::getSort);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<SysMenu> listAll() {
        return list(new LambdaQueryWrapper<SysMenu>()
                .orderByAsc(SysMenu::getSort));
    }
    
    @Override
    public List<SysMenu> listTree() {
        return listAll();
    }

    @Override
    public List<SysMenu> listUserMenus(Long userId) {
        // 获取用户角色
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
        if (userRoles.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> roleIds = userRoles.stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());

        // 获取角色关联的菜单
        List<SysRoleMenu> roleMenus = sysRoleMenuMapper.selectList(
                new LambdaQueryWrapper<SysRoleMenu>().in(SysRoleMenu::getRoleId, roleIds));
        if (roleMenus.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> menuIds = roleMenus.stream()
                .map(SysRoleMenu::getMenuId)
                .collect(Collectors.toList());

        // 获取菜单列表（去重）
        return list(new LambdaQueryWrapper<SysMenu>()
                .in(SysMenu::getId, menuIds)
                .eq(SysMenu::getStatus, 1)
                .orderByAsc(SysMenu::getSort));
    }

    @Override
    public List<SysMenu> listByParentId(Long parentId) {
        return list(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, parentId)
                .eq(SysMenu::getStatus, 1)
                .orderByAsc(SysMenu::getSort));
    }

    @Override
    public boolean add(SysMenu menu) {
        return save(menu);
    }

    @Override
    public boolean update(SysMenu menu) {
        SysMenu existMenu = getById(menu.getId());
        if (existMenu == null) {
            throw new RuntimeException("菜单不存在");
        }
        return updateById(menu);
    }

    @Override
    public boolean delete(Long id) {
        // 检查是否有子菜单
        Long count = count(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id));
        if (count > 0) {
            throw new RuntimeException("请先删除子菜单");
        }
        return removeById(id);
    }
}