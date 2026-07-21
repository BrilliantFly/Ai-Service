package com.know.knowboot.service.tenant;

import com.baomidou.mybatisplus.extension.service.IService;
import com.know.knowboot.entity.tenant.SysUserJob;

import java.util.List;

public interface ISysUserJobService extends IService<SysUserJob> {

    /**
     * 根据用户ID查询岗位ID列表
     */
    List<Long> selectJobIdsByUserId(Long userId);

    /**
     * 根据岗位ID查询用户ID列表
     */
    List<Long> selectUserIdsByJobId(Long jobId);

    /**
     * 分配用户岗位关系
     */
    boolean assignJobs(Long userId, List<Long> jobIds);

    /**
     * 根据用户ID删除关联
     */
    boolean deleteByUserId(Long userId);

    /**
     * 根据岗位ID删除关联
     */
    boolean deleteByJobId(Long jobId);
}