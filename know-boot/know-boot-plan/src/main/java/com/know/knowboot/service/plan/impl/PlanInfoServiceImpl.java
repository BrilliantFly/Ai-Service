package com.know.knowboot.service.plan.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.plan.PlanInfo;
import com.know.knowboot.entity.plan.PlanScheduleEvent;
import com.know.knowboot.mapper.plan.PlanInfoMapper;
import com.know.knowboot.service.plan.IPlanInfoService;
import com.know.knowboot.service.plan.IPlanScheduleEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 计划信息服务实现
 */
@Service
public class PlanInfoServiceImpl extends ServiceImpl<PlanInfoMapper, PlanInfo> implements IPlanInfoService {

    @Autowired
    private PlanInfoMapper planInfoMapper;

    @Autowired
    private IPlanScheduleEventService scheduleEventService;

    @Override
    public IPage<PlanInfo> page(PlanInfo query, Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<PlanInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getPlanType() != null, PlanInfo::getPlanType, query.getPlanType())
                .eq(query.getQuadrantId() != null, PlanInfo::getQuadrantId, query.getQuadrantId())
                .eq(query.getStatus() != null, PlanInfo::getStatus, query.getStatus())
                .like(query.getPlanName() != null, PlanInfo::getPlanName, query.getPlanName())
                .orderByDesc(PlanInfo::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<PlanInfo> listByQuadrant(Long quadrantId) {
        return list(new LambdaQueryWrapper<PlanInfo>()
                .eq(PlanInfo::getQuadrantId, quadrantId)
                .orderByDesc(PlanInfo::getCreateTime));
    }

    @Override
    public PlanInfo getById(Long id) {
        return planInfoMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(PlanInfo plan, Long userId) {
        plan.setCreateBy(userId);
        plan.setCreateTime(System.currentTimeMillis());
        if (plan.getStatus() == null) {
            plan.setStatus(0); // 默认待开始
        }
        if (plan.getProgress() == null) {
            plan.setProgress(0);
        }
        return save(plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(PlanInfo plan) {
        plan.setUpdateTime(System.currentTimeMillis());
        return updateById(plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProgress(Long id, Integer progress) {
        PlanInfo plan = planInfoMapper.selectById(id);
        if (plan == null) {
            return false;
        }
        plan.setProgress(progress);
        if (progress >= 100) {
            plan.setStatus(2); // 已完成
            plan.setActualEndTime(System.currentTimeMillis());
        } else if (progress > 0 && plan.getStatus() == 0) {
            plan.setStatus(1); // 进行中
            plan.setActualStartTime(System.currentTimeMillis());
        }
        plan.setUpdateTime(System.currentTimeMillis());
        return updateById(plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long toSchedule(Long planId, Long startTime, Long endTime) {
        PlanInfo plan = planInfoMapper.selectById(planId);
        if (plan == null) {
            throw new RuntimeException("计划不存在");
        }

        PlanScheduleEvent event = new PlanScheduleEvent();
        event.setTitle(plan.getPlanName());
        event.setContent(plan.getTargetEffect());
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setPlanId(planId);
        event.setQuadrant(plan.getQuadrantId() != null ? plan.getQuadrantId().intValue() : 2);
        event.setEventType(1);
        event.setStatus(0);
        event.setUserId(plan.getCreateBy());
        event.setCreateTime(System.currentTimeMillis());

        scheduleEventService.add(event, plan.getCreateBy());
        return event.getId();
    }
}
