package com.know.knowboot.service.plan.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.plan.PlanScheduleEventTemplate;
import com.know.knowboot.mapper.plan.PlanScheduleEventTemplateMapper;
import com.know.knowboot.service.plan.IPlanScheduleEventTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 日程事件模板服务实现
 */
@Service
public class PlanScheduleEventTemplateServiceImpl extends ServiceImpl<PlanScheduleEventTemplateMapper, PlanScheduleEventTemplate> implements IPlanScheduleEventTemplateService {

    @Autowired
    private PlanScheduleEventTemplateMapper planScheduleEventTemplateMapper;

    @Override
    public IPage<PlanScheduleEventTemplate> page(PlanScheduleEventTemplate query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<PlanScheduleEventTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getTemplateName() != null, PlanScheduleEventTemplate::getTemplateName, query.getTemplateName())
                .like(query.getTitle() != null, PlanScheduleEventTemplate::getTitle, query.getTitle())
                .eq(query.getPlanType() != null, PlanScheduleEventTemplate::getPlanType, query.getPlanType())
                .ne(query.getVisibility() == null, PlanScheduleEventTemplate::getVisibility, 0)
                .orderByDesc(PlanScheduleEventTemplate::getSort)
                .orderByDesc(PlanScheduleEventTemplate::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<PlanScheduleEventTemplate> hotList(int limit) {
        return list(new LambdaQueryWrapper<PlanScheduleEventTemplate>()
                .ne(PlanScheduleEventTemplate::getVisibility, 0)
                .orderByDesc(PlanScheduleEventTemplate::getUseCount)
                .last("LIMIT " + limit));
    }

    @Override
    public PlanScheduleEventTemplate getDetail(Long id) {
        return planScheduleEventTemplateMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(PlanScheduleEventTemplate t, Long userId) {
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
    public Boolean update(PlanScheduleEventTemplate t, Long userId) {
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
