package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.tenant.SysDept;
import com.know.knowboot.entity.tenant.SysUser;
import com.know.knowboot.mapper.tenant.SysDeptMapper;
import com.know.knowboot.mapper.tenant.SysUserMapper;
import com.know.knowboot.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门服务实现
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public IPage<SysDept> page(SysDept query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getDeptName() != null, SysDept::getDeptName, query.getDeptName())
                .eq(query.getStatus() != null, SysDept::getStatus, query.getStatus())
                .orderByAsc(SysDept::getSort);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<SysDept> listAll() {
        return list(new LambdaQueryWrapper<SysDept>()
                .orderByAsc(SysDept::getSort));
    }

    @Override
    public List<SysDept> buildTree() {
        List<SysDept> allDepts = listAll();
        return buildTreeRecursive(allDepts, 0L);
    }

    /**
     * 递归构建树形结构
     */
    private List<SysDept> buildTreeRecursive(List<SysDept> allDepts, Long parentId) {
        return allDepts.stream()
                .filter(dept -> (parentId == null && dept.getParentId() == null)
                        || (parentId != null && parentId.equals(dept.getParentId())))
                .peek(dept -> {
                    List<SysDept> children = buildTreeRecursive(allDepts, dept.getId());
                    if (children != null && !children.isEmpty()) {
                        dept.setChildren(children);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SysDept> listByParentId(Long parentId) {
        return list(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getParentId, parentId)
                .eq(SysDept::getStatus, 1)
                .orderByAsc(SysDept::getSort));
    }

    @Override
    public boolean add(SysDept dept) {
        return save(dept);
    }

    @Override
    public boolean update(SysDept dept) {
        SysDept existDept = getById(dept.getId());
        if (existDept == null) {
            throw new RuntimeException("部门不存在");
        }
        return updateById(dept);
    }

    @Override
    public boolean delete(Long id) {
        // 检查是否有子部门
        Long count = count(new LambdaQueryWrapper<SysDept>().eq(SysDept::getParentId, id));
        if (count > 0) {
            throw new RuntimeException("请先删除子部门");
        }
        return removeById(id);
    }

    @Override
    public List<SysUser> getDeptUsers(Long deptId) {
        // 直接查询该部门下的所有用户（通过关联表）
        List<Long> userIds = sysDeptMapper.selectUserIdsByDeptId(deptId);

        if (userIds == null || userIds.isEmpty()) {
            // 暂时返回所有用户作为演示
            return sysUserMapper.selectList(null);
        }

        // 查询用户信息
        return sysUserMapper.selectBatchIds(userIds);
    }
}