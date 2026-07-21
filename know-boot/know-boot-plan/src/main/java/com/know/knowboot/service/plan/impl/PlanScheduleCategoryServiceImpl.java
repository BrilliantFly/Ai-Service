package com.know.knowboot.service.plan.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.plan.PlanScheduleCategory;
import com.know.knowboot.mapper.plan.PlanScheduleCategoryMapper;
import com.know.knowboot.service.plan.IPlanScheduleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 日程分类服务实现
 */
@Service
public class PlanScheduleCategoryServiceImpl extends ServiceImpl<PlanScheduleCategoryMapper, PlanScheduleCategory> implements IPlanScheduleCategoryService {

    @Autowired
    private PlanScheduleCategoryMapper planScheduleCategoryMapper;

    @Override
    public List<PlanScheduleCategory> list() {
        return list(new LambdaQueryWrapper<PlanScheduleCategory>()
                .orderByAsc(PlanScheduleCategory::getSort));
    }

    @Override
    public PlanScheduleCategory getById(Long id) {
        return planScheduleCategoryMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(PlanScheduleCategory category) {
        category.setCreateTime(System.currentTimeMillis());
        return save(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(PlanScheduleCategory category) {
        category.setUpdateTime(System.currentTimeMillis());
        return updateById(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }
}
