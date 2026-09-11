package com.know.knowboot.service.plan.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.plan.PlanHabitTemplate;
import com.know.knowboot.mapper.plan.PlanHabitTemplateMapper;
import com.know.knowboot.service.plan.IPlanHabitTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 习惯模板服务实现
 */
@Service
public class PlanHabitTemplateServiceImpl extends ServiceImpl<PlanHabitTemplateMapper, PlanHabitTemplate> implements IPlanHabitTemplateService {

    @Autowired
    private PlanHabitTemplateMapper planHabitTemplateMapper;

    @Override
    public IPage<PlanHabitTemplate> page(PlanHabitTemplate query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<PlanHabitTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getTemplateName() != null, PlanHabitTemplate::getTemplateName, query.getTemplateName())
                .like(query.getName() != null, PlanHabitTemplate::getName, query.getName())
                .eq(query.getPlanType() != null, PlanHabitTemplate::getPlanType, query.getPlanType())
                .ne(query.getVisibility() == null, PlanHabitTemplate::getVisibility, 0)
                .orderByDesc(PlanHabitTemplate::getSort)
                .orderByDesc(PlanHabitTemplate::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<PlanHabitTemplate> hotList(int limit) {
        return list(new LambdaQueryWrapper<PlanHabitTemplate>()
                .ne(PlanHabitTemplate::getVisibility, 0)
                .orderByDesc(PlanHabitTemplate::getUseCount)
                .last("LIMIT " + limit));
    }

    @Override
    public PlanHabitTemplate getDetail(Long id) {
        return planHabitTemplateMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(PlanHabitTemplate t, Long userId) {
        t.setCreateBy(userId);
        t.setCreateTime(System.currentTimeMillis());
        t.setDelFlag(0);
        t.setUseCount(0);
        t.setDeleteTime(0L);
        save(t);
        return t.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(PlanHabitTemplate t, Long userId) {
        t.setUpdateBy(userId);
        t.setUpdateTime(System.currentTimeMillis());
        return updateById(t);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        return removeById(id);
    }
}
