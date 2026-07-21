package com.know.knowboot.service.impl.tenant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.tenant.SysUserJob;
import com.know.knowboot.mapper.tenant.SysUserJobMapper;
import com.know.knowboot.service.tenant.ISysUserJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserJobServiceImpl extends ServiceImpl<SysUserJobMapper, SysUserJob> implements ISysUserJobService {

    @Autowired
    private SysUserJobMapper sysUserJobMapper;

    @Override
    public List<Long> selectJobIdsByUserId(Long userId) {
        return sysUserJobMapper.selectJobIdsByUserId(userId);
    }

    @Override
    public List<Long> selectUserIdsByJobId(Long jobId) {
        return sysUserJobMapper.selectUserIdsByJobId(jobId);
    }

    @Override
    @Transactional
    public boolean assignJobs(Long userId, List<Long> jobIds) {
        // 先删除原有关联
        deleteByUserId(userId);
        // 批量插入新关联
        if (jobIds != null && !jobIds.isEmpty()) {
            List<SysUserJob> list = new ArrayList<>();
            for (Long jobId : jobIds) {
                SysUserJob uj = new SysUserJob();
                uj.setUserId(userId);
                uj.setJobId(jobId);
                list.add(uj);
            }
            return saveBatch(list);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean deleteByUserId(Long userId) {
        return remove(new LambdaQueryWrapper<SysUserJob>().eq(SysUserJob::getUserId, userId));
    }

    @Override
    @Transactional
    public boolean deleteByJobId(Long jobId) {
        return remove(new LambdaQueryWrapper<SysUserJob>().eq(SysUserJob::getJobId, jobId));
    }
}