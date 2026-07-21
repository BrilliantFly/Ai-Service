package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.know.knowboot.entity.tenant.SysDept;
import com.know.knowboot.entity.tenant.SysRoleDataScope;
import com.know.knowboot.entity.tenant.SysUserDept;
import com.know.knowboot.mapper.tenant.SysDeptMapper;
import com.know.knowboot.mapper.tenant.SysRoleDataScopeMapper;
import com.know.knowboot.mapper.tenant.SysUserDeptMapper;
import com.know.knowboot.service.ISysRoleDataScopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysRoleDataScopeServiceImpl implements ISysRoleDataScopeService {

    private final SysRoleDataScopeMapper dataScopeMapper;
    private final SysUserDeptMapper userDeptMapper;
    private final SysDeptMapper deptMapper;

    @Override
    public List<SysRoleDataScope> getByRoleId(Long roleId) {
        return dataScopeMapper.selectByRoleId(roleId);
    }

    @Override
    public Integer getDataScopeTypeByRoleId(Long roleId) {
        List<SysRoleDataScope> list = getByRoleId(roleId);
        if (list == null || list.isEmpty()) {
            return 1; // 默认全部数据权限
        }
        return list.get(0).getDataScopeType() != null ? list.get(0).getDataScopeType() : 1;
    }

    @Override
    public List<Long> getVisibleDeptIds(Long roleId) {
        Integer dataScopeType = getDataScopeTypeByRoleId(roleId);
        List<SysRoleDataScope> configs = getByRoleId(roleId);

        switch (dataScopeType) {
            case 1: // 全部数据权限
                return null;
            case 2: // 本部门数据权限 - 需要从DataScopeContext获取当前用户
            case 3: // 本部门及子部门数据权限 - 需要从DataScopeContext获取当前用户
            case 4: // 仅本人数据权限
                return null;
            case 5: // 自定义数据权限
                List<Long> result = new ArrayList<>();
                if (configs != null) {
                    for (SysRoleDataScope config : configs) {
                        if (config.getDeptId() != null) {
                            result.add(config.getDeptId());
                        }
                    }
                }
                return result;
            default:
                return null;
        }
    }

    @Override
    @Transactional
    public boolean saveConfig(Long roleId, Integer dataScopeType, List<Long> customDeptIds) {
        LambdaQueryWrapper<SysRoleDataScope> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleDataScope::getRoleId, roleId);
        dataScopeMapper.delete(wrapper);

        if (dataScopeType == 5 && customDeptIds != null && !customDeptIds.isEmpty()) {
            for (Long deptId : customDeptIds) {
                SysRoleDataScope config = new SysRoleDataScope();
                config.setRoleId(roleId);
                config.setDeptId(deptId);
                config.setDataScopeType(dataScopeType);
                dataScopeMapper.insert(config);
            }
        } else {
            SysRoleDataScope config = new SysRoleDataScope();
            config.setRoleId(roleId);
            config.setDataScopeType(dataScopeType);
            dataScopeMapper.insert(config);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean deleteByRoleId(Long roleId) {
        LambdaQueryWrapper<SysRoleDataScope> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleDataScope::getRoleId, roleId);
        return dataScopeMapper.delete(wrapper) >= 0;
    }

    @Override
    public List<Long> getUserDeptIds(Long userId) {
        return userDeptMapper.selectDeptIdsByUserId(userId);
    }

    @Override
    public List<Long> getUserDeptAndChildIds(Long userId) {
        List<Long> deptIds = getUserDeptIds(userId);
        if (deptIds == null || deptIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> result = new ArrayList<>(deptIds);
        for (Long deptId : deptIds) {
            result.addAll(getChildDeptIds(deptId));
        }
        return result;
    }

    private List<Long> getChildDeptIds(Long parentId) {
        List<Long> result = new ArrayList<>();
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDept::getParentId, parentId);
        List<SysDept> childDepts = deptMapper.selectList(wrapper);
        if (childDepts != null) {
            for (SysDept dept : childDepts) {
                result.add(dept.getId());
                result.addAll(getChildDeptIds(dept.getId()));
            }
        }
        return result;
    }
}