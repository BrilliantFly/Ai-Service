package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.tenant.SysJob;
import com.know.knowboot.mapper.tenant.SysJobMapper;
import com.know.knowboot.service.ISysJobService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 岗位服务实现
 */
@Service
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements ISysJobService {

    @Override
    public IPage<SysJob> page(SysJob query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<SysJob> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getJobName() != null, SysJob::getJobName, query.getJobName())
                .eq(query.getStatus() != null, SysJob::getStatus, query.getStatus())
                .orderByAsc(SysJob::getSort);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<SysJob> listAll() {
        return list(new LambdaQueryWrapper<SysJob>()
                .orderByAsc(SysJob::getSort));
    }

    @Override
    public boolean add(SysJob job) {
        return save(job);
    }

    @Override
    public boolean update(SysJob job) {
        SysJob existJob = getById(job.getId());
        if (existJob == null) {
            throw new RuntimeException("岗位不存在");
        }
        return updateById(job);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public SysJob getById(Long id) {
        return baseMapper.selectById(id);
    }
}