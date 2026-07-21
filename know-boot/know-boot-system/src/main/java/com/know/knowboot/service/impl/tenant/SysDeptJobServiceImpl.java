package com.know.knowboot.service.impl.tenant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.tenant.SysDeptJob;
import com.know.knowboot.mapper.tenant.SysDeptJobMapper;
import com.know.knowboot.service.tenant.ISysDeptJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysDeptJobServiceImpl extends ServiceImpl<SysDeptJobMapper, SysDeptJob> implements ISysDeptJobService {

    @Autowired
    private SysDeptJobMapper sysDeptJobMapper;

    @Override
    public List<Long> selectJobIdsByDeptId(Long deptId) {
        return sysDeptJobMapper.selectJobIdsByDeptId(deptId);
    }

    @Override
    public List<Long> selectDeptIdsByJobId(Long jobId) {
        return sysDeptJobMapper.selectDeptIdsByJobId(jobId);
    }

    @Override
    @Transactional
    public boolean assignJobs(Long deptId, List<Long> jobIds) {
        // 先删除原有关联
        deleteByDeptId(deptId);
        // 批量插入新关联
        if (jobIds != null && !jobIds.isEmpty()) {
            List<SysDeptJob> list = new ArrayList<>();
            for (Long jobId : jobIds) {
                SysDeptJob dj = new SysDeptJob();
                dj.setDeptId(deptId);
                dj.setJobId(jobId);
                list.add(dj);
            }
            return saveBatch(list);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean deleteByDeptId(Long deptId) {
        return remove(new LambdaQueryWrapper<SysDeptJob>().eq(SysDeptJob::getDeptId, deptId));
    }

    @Override
    @Transactional
    public boolean deleteByJobId(Long jobId) {
        return remove(new LambdaQueryWrapper<SysDeptJob>().eq(SysDeptJob::getJobId, jobId));
    }
}