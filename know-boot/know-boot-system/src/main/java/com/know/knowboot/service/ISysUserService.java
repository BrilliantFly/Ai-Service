package com.know.knowboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.know.knowboot.entity.tenant.SysUser;

/**
 * 用户服务接口
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 分页查询用户
     */
    IPage<SysUser> page(SysUser query, Integer pageNum, Integer pageSize);

    /**
     * 根据用户名查询用户
     */
    SysUser getByUsername(String username);

    /**
     * 新增用户
     */
    boolean add(SysUser user);

    /**
     * 修改用户
     */
    boolean update(SysUser user);

    /**
     * 删除用户
     */
    boolean delete(Long id);

    /**
     * 重置密码
     */
    boolean resetPassword(Long id);

    /**
     * 修改密码
     */
    boolean updatePassword(Long id, String oldPassword, String newPassword);
}