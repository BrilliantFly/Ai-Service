package com.know.knowboot.service.tenant;

import com.baomidou.mybatisplus.extension.service.IService;
import com.know.knowboot.entity.tenant.SysUserDept;

import java.util.List;

public interface ISysUserDeptService extends IService<SysUserDept> {

    /**
     * 根据用户ID查询部门ID列表
     */
    List<Long> selectDeptIdsByUserId(Long userId);

    /**
     * 根据部门ID查询用户ID列表
     */
    List<Long> selectUserIdsByDeptId(Long deptId);

    /**
     * 分配用户部门关系
     */
    boolean assignDepts(Long userId, List<Long> deptIds);

    /**
     * 根据用户ID删除关联
     */
    boolean deleteByUserId(Long userId);

    /**
     * 根据部门ID删除关联
     */
    boolean deleteByDeptId(Long deptId);
}