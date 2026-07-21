package com.know.knowboot.service.tenant;

import com.baomidou.mybatisplus.extension.service.IService;
import com.know.knowboot.entity.tenant.SysDeptJob;

import java.util.List;

public interface ISysDeptJobService extends IService<SysDeptJob> {

    /**
     * 根据部门ID查询岗位ID列表
     */
    List<Long> selectJobIdsByDeptId(Long deptId);

    /**
     * 根据岗位ID查询部门ID列表
     */
    List<Long> selectDeptIdsByJobId(Long jobId);

    /**
     * 分配部门岗位关系
     */
    boolean assignJobs(Long deptId, List<Long> jobIds);

    /**
     * 根据部门ID删除关联
     */
    boolean deleteByDeptId(Long deptId);

    /**
     * 根据岗位ID删除关联
     */
    boolean deleteByJobId(Long jobId);
}