package com.know.knowboot.service;

import java.util.List;

/**
 * 管理员角色服务接口
 */
public interface IAdminRoleService {

    /**
     * 获取用户角色ID列表
     * @param adminId 管理员ID
     * @return 角色ID列表
     */
    List<Integer> getRoleIdAttr(Integer adminId);
}