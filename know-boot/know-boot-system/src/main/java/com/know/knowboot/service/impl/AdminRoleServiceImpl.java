package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.know.knowboot.entity.system.SystemRoleMenu;
import com.know.knowboot.mapper.system.SystemRoleMenuMapper;
import com.know.knowboot.service.IAdminRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员角色服务实现
 */
@Service
public class AdminRoleServiceImpl implements IAdminRoleService {

    @Resource
    private SystemRoleMenuMapper roleMenuMapper;

    @Override
    public List<Integer> getRoleIdAttr(Integer adminId) {
        // 简化实现 - 返回空列表，实际应从admin_role关联表查询
        return new java.util.ArrayList<>();
    }
}