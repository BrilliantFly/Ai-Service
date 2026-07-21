package com.know.knowboot.service;

import com.know.knowboot.entity.tenant.SysRoleDataScope;

import java.util.List;

public interface ISysRoleDataScopeService {

    List<SysRoleDataScope> getByRoleId(Long roleId);

    Integer getDataScopeTypeByRoleId(Long roleId);

    List<Long> getVisibleDeptIds(Long roleId);

    boolean saveConfig(Long roleId, Integer dataScopeType, List<Long> customDeptIds);

    boolean deleteByRoleId(Long roleId);

    List<Long> getUserDeptIds(Long userId);

    List<Long> getUserDeptAndChildIds(Long userId);
}