package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.tenant.SysRolePermission;
import com.know.knowboot.mapper.tenant.SysRolePermissionMapper;
import com.know.knowboot.service.ISysRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色-权限关联服务实现
 */
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    public IPage<SysRolePermission> page(SysRolePermission query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<SysRolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getRoleId() != null, SysRolePermission::getRoleId, query.getRoleId())
                .eq(query.getPermissionId() != null, SysRolePermission::getPermissionId, query.getPermissionId())
                .orderByDesc(SysRolePermission::getId);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<SysRolePermission> listAll() {
        return list(new LambdaQueryWrapper<SysRolePermission>()
                .orderByDesc(SysRolePermission::getId));
    }

    @Override
    public List<Long> listPermissionIdsByRoleId(Long roleId) {
        return sysRolePermissionMapper.selectPermissionIdsByRoleId(roleId);
    }

    @Override
    public boolean assignPermissions(Long roleId, List<Long> permissionIds) {
        if (roleId == null) {
            throw new RuntimeException("角色ID不能为空");
        }
        if (permissionIds == null || permissionIds.isEmpty()) {
            throw new RuntimeException("权限ID列表不能为空");
        }

        // 1. 删除该角色的所有权限关联
        sysRolePermissionMapper.deleteByRoleId(roleId);

        // 2. 批量新增权限关联
        List<SysRolePermission> rolePermissions = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            SysRolePermission rp = new SysRolePermission();
            rp.setRoleId(roleId);
            rp.setPermissionId(permissionId);
            rolePermissions.add(rp);
        }

        return saveBatch(rolePermissions);
    }

    @Override
    public boolean add(SysRolePermission rolePermission) {
        return save(rolePermission);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public boolean deleteByRoleId(Long roleId) {
        return sysRolePermissionMapper.deleteByRoleId(roleId) > 0;
    }
}