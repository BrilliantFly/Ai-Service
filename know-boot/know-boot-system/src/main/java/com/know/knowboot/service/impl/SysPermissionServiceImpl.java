package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.tenant.SysPermission;
import com.know.knowboot.entity.tenant.SysRolePermission;
import com.know.knowboot.entity.tenant.SysRole;
import com.know.knowboot.entity.tenant.SysUserRole;
import com.know.knowboot.mapper.tenant.SysPermissionMapper;
import com.know.knowboot.mapper.tenant.SysRoleMapper;
import com.know.knowboot.mapper.tenant.SysRolePermissionMapper;
import com.know.knowboot.mapper.tenant.SysUserRoleMapper;
import com.know.knowboot.service.ISysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限服务实现
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public IPage<SysPermission> page(SysPermission query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getName() != null, SysPermission::getName, query.getName())
                .eq(query.getCode() != null, SysPermission::getCode, query.getCode())
                .eq(query.getPermissionType() != null, SysPermission::getPermissionType, query.getPermissionType())
                .eq(query.getStatus() != null, SysPermission::getStatus, query.getStatus())
                .orderByAsc(SysPermission::getSort);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<SysPermission> listAll() {
        return list(new LambdaQueryWrapper<SysPermission>()
                .orderByAsc(SysPermission::getSort));
    }

    @Override
    public List<SysPermission> listTree() {
        List<SysPermission> all = listAll();
        return buildTree(all);
    }

    @Override
    public List<SysPermission> listByRoleId(Long roleId) {
        return sysPermissionMapper.selectPermissionsByRoleId(roleId);
    }

    @Override
    public List<String> listCodesByUserId(Long userId) {
        // 1. 获取用户角色ID列表
        List<Long> roleIds = sysUserRoleMapper.selectRoleIdsByUserId(userId);
        if (roleIds == null || roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 获取角色对应的权限列表
        List<SysPermission> permissions = sysPermissionMapper.selectPermissionsByRoleIds(roleIds);

        // 3. 返回权限编码列表
        return permissions.stream()
                .map(SysPermission::getCode)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public boolean add(SysPermission permission) {
        // 检查权限编码唯一性
        long count = count(new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getCode, permission.getCode()));
        if (count > 0) {
            throw new RuntimeException("权限编码已存在");
        }
        return save(permission);
    }

    @Override
    public boolean update(SysPermission permission) {
        return updateById(permission);
    }

    @Override
    public boolean delete(Long id) {
        // 删除角色权限关联
        sysRolePermissionMapper.deleteByPermissionId(id);
        return removeById(id);
    }

    /**
     * 构建树形结构
     */
    private List<SysPermission> buildTree(List<SysPermission> permissions) {
        List<SysPermission> roots = permissions.stream()
                .filter(p -> p.getParentId() == null || p.getParentId() == 0)
                .collect(Collectors.toList());

        for (SysPermission root : roots) {
            root.setChildren(getChildren(root.getId(), permissions));
        }
        return roots;
    }

    private List<SysPermission> getChildren(Long parentId, List<SysPermission> all) {
        return all.stream()
                .filter(p -> parentId.equals(p.getParentId()))
                .peek(p -> p.setChildren(getChildren(p.getId(), all)))
                .collect(Collectors.toList());
    }
}