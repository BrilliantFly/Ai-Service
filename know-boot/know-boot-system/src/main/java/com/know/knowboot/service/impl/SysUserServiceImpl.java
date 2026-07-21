package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.tenant.SysUser;
import com.know.knowboot.mapper.tenant.SysUserMapper;
import com.know.knowboot.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public IPage<SysUser> page(SysUser query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getUsername() != null, SysUser::getUsername, query.getUsername())
                .like(query.getRealname() != null, SysUser::getRealname, query.getRealname())
                .eq(query.getStatus() != null, SysUser::getStatus, query.getStatus())
                .orderByDesc(SysUser::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public SysUser getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
    }

    @Override
    public boolean add(SysUser user) {
        // 检查用户名是否已存在
        Long count = count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, user.getUsername()));
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }
        // 加密密码
        user.setPassword(passwordEncoder.encode("123456")); // 默认密码
        return save(user);
    }

    @Override
    public boolean update(SysUser user) {
        SysUser existUser = getById(user.getId());
        if (existUser == null) {
            throw new RuntimeException("用户不存在");
        }
        // 不更新密码字段
        user.setPassword(null);
        return updateById(user);
    }

    @Override
    public boolean delete(Long id) {
        SysUser user = getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 不能删除超级管理员
        if ("admin".equals(user.getUsername())) {
            throw new RuntimeException("不能删除超级管理员");
        }
        return removeById(id);
    }

    @Override
    public boolean resetPassword(Long id) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setPassword(passwordEncoder.encode("123456"));
        return updateById(user);
    }

    @Override
    public boolean updatePassword(Long id, String oldPassword, String newPassword) {
        SysUser user = getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        SysUser updateUser = new SysUser();
        updateUser.setId(id);
        updateUser.setPassword(passwordEncoder.encode(newPassword));
        return updateById(updateUser);
    }
}