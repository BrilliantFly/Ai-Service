package com.know.knowboot.service.plan.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.plan.PlanScheduleEvent;
import com.know.knowboot.mapper.plan.PlanScheduleEventMapper;
import com.know.knowboot.service.plan.IPlanScheduleEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日程事件服务实现
 */
@Service
public class PlanScheduleEventServiceImpl extends ServiceImpl<PlanScheduleEventMapper, PlanScheduleEvent> implements IPlanScheduleEventService {

    @Autowired
    private PlanScheduleEventMapper planScheduleEventMapper;

    @Override
    public IPage<PlanScheduleEvent> page(PlanScheduleEvent query, Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<PlanScheduleEvent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PlanScheduleEvent::getUserId, userId)
                .eq(query.getQuadrant() != null, PlanScheduleEvent::getQuadrant, query.getQuadrant())
                .eq(query.getStatus() != null, PlanScheduleEvent::getStatus, query.getStatus())
                .eq(query.getCategoryId() != null, PlanScheduleEvent::getCategoryId, query.getCategoryId())
                .eq(query.getEventType() != null, PlanScheduleEvent::getEventType, query.getEventType())
                .like(query.getTitle() != null, PlanScheduleEvent::getTitle, query.getTitle())
                .orderByDesc(PlanScheduleEvent::getStartTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<PlanScheduleEvent> listByDateRange(Long userId, Long startTime, Long endTime) {
        // 先查询：在期望区间内(原始 startTime 命中)的，以及重复日程的母事件(startTime 在区间之前，但重复可能延伸到区间内)
        List<PlanScheduleEvent> events = list(new LambdaQueryWrapper<PlanScheduleEvent>()
                .eq(PlanScheduleEvent::getUserId, userId)
                .le(PlanScheduleEvent::getStartTime, endTime)
                .orderByAsc(PlanScheduleEvent::getStartTime));

        // 过滤出落在区间内的实例（对重复日程做展开）
        List<PlanScheduleEvent> result = new ArrayList<>();
        for (PlanScheduleEvent event : events) {
            boolean isRepeat = event.getIsRepeat() != null && event.getIsRepeat() == 1;
            if (isRepeat) {
                result.addAll(expandRepeatInRange(event, startTime, endTime));
            } else if (event.getStartTime() != null && event.getStartTime() >= startTime && event.getStartTime() <= endTime) {
                result.add(event);
            }
        }
        // 结果再按开始时间排序，保证日历展示有序
        result.sort(Comparator.comparing(PlanScheduleEvent::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));
        return result;
    }

    /**
     * 将重复日程按 repeatType 在 [start, end] 区间内展开为多个实例。
     * 动态实例共享母事件 id，startTime 替换为各发生日期，便于前端区分归属。
     */
    private List<PlanScheduleEvent> expandRepeatInRange(PlanScheduleEvent event, Long start, Long end) {
        List<PlanScheduleEvent> list = new ArrayList<>();
        Long originalStart = event.getStartTime();
        if (originalStart == null) {
            return list;
        }
        // 重复结束日期：若未设置则视为无限（由查询区间 end 兜底）
        long repeatEnd = event.getRepeatEndDate() != null ? event.getRepeatEndDate() : end;
        // 遍历次数上限，防止异常数据导致死循环
        int maxOccurrences = 1000;
        int type = event.getRepeatType() != null ? event.getRepeatType() : 1;

        long cursor = originalStart;
        Calendar cal = Calendar.getInstance();
        while (cursor <= repeatEnd && cursor <= end && list.size() < maxOccurrences) {
            if (cursor >= start) {
                PlanScheduleEvent copy = new PlanScheduleEvent();
                copyBaseFields(event, copy);
                copy.setStartTime(cursor);
                list.add(copy);
            }
            cal.setTimeInMillis(cursor);
            switch (type) {
                case 2: // 每周
                    cal.add(Calendar.DAY_OF_MONTH, 7);
                    break;
                case 3: // 每月
                    cal.add(Calendar.MONTH, 1);
                    break;
                case 4: // 每年
                    cal.add(Calendar.YEAR, 1);
                    break;
                default: // 每日
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    break;
            }
            long next = cal.getTimeInMillis();
            // 防止推进失败导致死循环
            if (next <= cursor) {
                break;
            }
            cursor = next;
        }
        return list;
    }

    /** 复制事件基础字段，便于动态展开实例复用展示字段 */
    private void copyBaseFields(PlanScheduleEvent src, PlanScheduleEvent dst) {
        dst.setId(src.getId());
        dst.setTitle(src.getTitle());
        dst.setContent(src.getContent());
        dst.setTags(src.getTags());
        dst.setSubtasks(src.getSubtasks());
        dst.setNote(src.getNote());
        dst.setProgress(src.getProgress());
        dst.setEventType(src.getEventType());
        dst.setQuadrant(src.getQuadrant());
        dst.setPriority(src.getPriority());
        dst.setCategoryId(src.getCategoryId());
        dst.setPlanId(src.getPlanId());
        dst.setEndTime(src.getEndTime());
        dst.setIsAllDay(src.getIsAllDay());
        dst.setIsRepeat(src.getIsRepeat());
        dst.setRepeatType(src.getRepeatType());
        dst.setRepeatRule(src.getRepeatRule());
        dst.setRepeatEndDate(src.getRepeatEndDate());
        dst.setRemindTime(src.getRemindTime());
        dst.setRemindMinutes(src.getRemindMinutes());
        dst.setLocation(src.getLocation());
        dst.setStatus(src.getStatus());
        dst.setCompletedTime(src.getCompletedTime());
        dst.setColor(src.getColor());
        dst.setUserId(src.getUserId());
        dst.setCreateBy(src.getCreateBy());
        dst.setCreateTime(src.getCreateTime());
    }

    @Override
    public List<PlanScheduleEvent> listByDate(Long userId, Long date) {
        // 某一天：从 00:00:00 到 23:59:59
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long dayStart = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        long dayEnd = cal.getTimeInMillis() - 1;

        return listByDateRange(userId, dayStart, dayEnd);
    }

    @Override
    public List<PlanScheduleEvent> listByWeek(Long userId, Long weekStart, Long weekEnd) {
        return listByDateRange(userId, weekStart, weekEnd);
    }

    @Override
    public List<PlanScheduleEvent> listByMonth(Long userId, Integer year, Integer month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long monthStart = cal.getTimeInMillis();

        cal.add(Calendar.MONTH, 1);
        long monthEnd = cal.getTimeInMillis() - 1;

        return listByDateRange(userId, monthStart, monthEnd);
    }

    @Override
    public List<PlanScheduleEvent> listByQuadrant(Long userId, Integer quadrant) {
        return list(new LambdaQueryWrapper<PlanScheduleEvent>()
                .eq(PlanScheduleEvent::getUserId, userId)
                .eq(PlanScheduleEvent::getQuadrant, quadrant)
                .orderByAsc(PlanScheduleEvent::getStartTime));
    }

    @Override
    public List<PlanScheduleEvent> listByPlanId(Long planId) {
        return list(new LambdaQueryWrapper<PlanScheduleEvent>()
                .eq(PlanScheduleEvent::getPlanId, planId)
                .orderByAsc(PlanScheduleEvent::getStartTime));
    }

    @Override
    public Map<String, Object> getTodayStats(Long userId) {
        // 计算今日时间范围
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long todayStart = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        long todayEnd = cal.getTimeInMillis() - 1;

        // 今日所有日程
        List<PlanScheduleEvent> todayEvents = listByDateRange(userId, todayStart, todayEnd);
        long totalCount = todayEvents.size();
        long completedCount = todayEvents.stream().filter(e -> e.getStatus() == 1).count();
        long todoCount = totalCount - completedCount;

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCount", totalCount);
        stats.put("completedCount", completedCount);
        stats.put("todoCount", todoCount);
        stats.put("date", todayStart);
        return stats;
    }

    @Override
    public PlanScheduleEvent getById(Long id) {
        return planScheduleEventMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(PlanScheduleEvent event, Long userId) {
        event.setUserId(userId);
        event.setCreateBy(userId);
        event.setCreateTime(System.currentTimeMillis());
        if (event.getStatus() == null) {
            event.setStatus(0);
        }
        if (event.getProgress() == null) {
            event.setProgress(0);
        }
        return save(event);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(PlanScheduleEvent event) {
        event.setUpdateTime(System.currentTimeMillis());
        return updateById(event);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean complete(Long id) {
        PlanScheduleEvent event = planScheduleEventMapper.selectById(id);
        if (event == null) {
            return false;
        }
        event.setStatus(1);
        event.setCompletedTime(System.currentTimeMillis());
        event.setProgress(100);
        return updateById(event);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean uncomplete(Long id) {
        PlanScheduleEvent event = planScheduleEventMapper.selectById(id);
        if (event == null) {
            return false;
        }
        event.setStatus(0);
        Integer progress = event.getProgress();
        if (progress == null || progress >= 100) {
            progress = 0;
        }
        return update(new LambdaUpdateWrapper<PlanScheduleEvent>()
                .eq(PlanScheduleEvent::getId, id)
                .set(PlanScheduleEvent::getStatus, 0)
                .set(PlanScheduleEvent::getCompletedTime, null)
                .set(PlanScheduleEvent::getProgress, progress)
                .set(PlanScheduleEvent::getUpdateTime, System.currentTimeMillis()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }
}
