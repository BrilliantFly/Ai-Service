package com.know.knowboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.know.knowboot.entity.tenant.SysDept;
import com.know.knowboot.entity.tenant.SysUser;

import java.util.List;

/**
 * 部门服务接口
 */
public interface ISysDeptService extends IService<SysDept> {

    /**
     * 分页查询部门
     */
    IPage<SysDept> page(SysDept query, Integer pageNum, Integer pageSize);

    /**
     * 获取所有部门（树形结构用）
     */
    List<SysDept> listAll();

    /**
     * 构建部门树形结构
     */
    List<SysDept> buildTree();

    /**
     * 根据父ID获取子部门
     */
    List<SysDept> listByParentId(Long parentId);

    /**
     * 新增部门
     */
    boolean add(SysDept dept);

    /**
     * 修改部门
     */
    boolean update(SysDept dept);

    /**
     * 删除部门
     */
    boolean delete(Long id);

    /**
     * 获取部门下的用户列表
     */
    List<SysUser> getDeptUsers(Long deptId);
}