package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.tenant.SysRole;
import com.know.knowboot.entity.tenant.SysRoleMenu;
import com.know.knowboot.mapper.tenant.SysRoleMapper;
import com.know.knowboot.mapper.tenant.SysRoleMenuMapper;
import com.know.knowboot.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public IPage<SysRole> page(SysRole query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getRoleName() != null, SysRole::getRoleName, query.getRoleName())
                .like(query.getRoleCode() != null, SysRole::getRoleCode, query.getRoleCode())
                .eq(query.getStatus() != null, SysRole::getStatus, query.getStatus())
                .orderByDesc(SysRole::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<SysRole> listNormal() {
        return list(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getStatus, 1)
                .orderByAsc(SysRole::getSort));
    }

    @Override
    public boolean add(SysRole role) {
        // 检查角色编码是否已存在
        Long count = count(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, role.getRoleCode()));
        if (count > 0) {
            throw new RuntimeException("角色编码已存在");
        }
        return save(role);
    }

    @Override
    public boolean update(SysRole role) {
        SysRole existRole = getById(role.getId());
        if (existRole == null) {
            throw new RuntimeException("角色不存在");
        }
        // 检查角色编码是否已存在（排除自身）
        Long count = count(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, role.getRoleCode())
                .ne(SysRole::getId, role.getId()));
        if (count > 0) {
            throw new RuntimeException("角色编码已存在");
        }
        return updateById(role);
    }

    @Override
    public boolean delete(Long id) {
        SysRole role = getById(id);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        // 不能删除超级管理员角色
        if ("super_admin".equals(role.getRoleCode())) {
            throw new RuntimeException("不能删除超级管理员角色");
        }
        return removeById(id);
    }

    @Override
    public List<Long> getMenuIds(Long roleId) {
        List<SysRoleMenu> roleMenus = sysRoleMenuMapper.selectList(
                new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        return roleMenus.stream()
                .map(SysRoleMenu::getMenuId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignMenus(Long roleId, List<Long> menuIds) {
        // 删除���有菜单关联
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        // 新增菜单关联 - 逐条插入
        if (menuIds != null && !menuIds.isEmpty()) {
            for (Long menuId : menuIds) {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                sysRoleMenuMapper.insert(roleMenu);
            }
        }
        return true;
    }
}