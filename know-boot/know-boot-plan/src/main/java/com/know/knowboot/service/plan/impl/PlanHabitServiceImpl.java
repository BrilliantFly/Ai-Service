package com.know.knowboot.service.plan.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.plan.PlanHabit;
import com.know.knowboot.entity.plan.PlanHabitRecord;
import com.know.knowboot.mapper.plan.PlanHabitMapper;
import com.know.knowboot.mapper.plan.PlanHabitRecordMapper;
import com.know.knowboot.service.plan.IPlanHabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 习惯服务实现
 */
@Service
public class PlanHabitServiceImpl extends ServiceImpl<PlanHabitMapper, PlanHabit> implements IPlanHabitService {

    @Autowired
    private PlanHabitMapper planHabitMapper;

    @Autowired
    private PlanHabitRecordMapper planHabitRecordMapper;

    @Override
    public IPage<PlanHabit> page(PlanHabit query, Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<PlanHabit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PlanHabit::getUserId, userId)
                .eq(query.getStatus() != null, PlanHabit::getStatus, query.getStatus())
                .like(query.getName() != null, PlanHabit::getName, query.getName())
                .orderByDesc(PlanHabit::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<PlanHabit> listByUserId(Long userId) {
        return list(new LambdaQueryWrapper<PlanHabit>()
                .eq(PlanHabit::getUserId, userId)
                .orderByDesc(PlanHabit::getCreateTime));
    }

    @Override
    public Map<String, Object> getStats(Long userId) {
        List<PlanHabit> habits = listByUserId(userId);
        long totalCount = habits.size();
        long activeCount = habits.stream().filter(h -> h.getStatus() == 0).count();
        long completedCount = habits.stream().filter(h -> h.getStatus() == 1).count();

        List<Long> habitIds = habits.stream().map(PlanHabit::getId).collect(Collectors.toList());
        long totalCheckins = habitIds.isEmpty() ? 0 : planHabitRecordMapper.selectCount(
                new LambdaQueryWrapper<PlanHabitRecord>()
                        .eq(PlanHabitRecord::getUserId, userId)
                        .in(PlanHabitRecord::getHabitId, habitIds));

        // 今日打卡数
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long todayStart = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        long todayEnd = cal.getTimeInMillis();

        long todayCheckins = habitIds.isEmpty() ? 0 : planHabitRecordMapper.selectCount(
                new LambdaQueryWrapper<PlanHabitRecord>()
                        .eq(PlanHabitRecord::getUserId, userId)
                        .in(PlanHabitRecord::getHabitId, habitIds)
                        .ge(PlanHabitRecord::getRecordDate, todayStart)
                        .lt(PlanHabitRecord::getRecordDate, todayEnd));

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCount", totalCount);
        stats.put("activeCount", activeCount);
        stats.put("completedCount", completedCount);
        stats.put("totalCheckins", totalCheckins);
        stats.put("todayCheckins", todayCheckins);
        return stats;
    }

    @Override
    public PlanHabit getById(Long id) {
        return planHabitMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(PlanHabit habit, Long userId) {
        habit.setUserId(userId);
        habit.setCreateBy(userId);
        habit.setCreateTime(System.currentTimeMillis());
        if (habit.getStatus() == null) {
            habit.setStatus(0); // 进行中
        }
        if (habit.getCurrentDays() == null) {
            habit.setCurrentDays(0);
        }
        if (habit.getTotalDays() == null) {
            habit.setTotalDays(0);
        }
        if (habit.getTargetDays() == null) {
            habit.setTargetDays(30);
        }
        return save(habit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(PlanHabit habit) {
        habit.setUpdateTime(System.currentTimeMillis());
        return updateById(habit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean checkin(Long habitId, Long userId) {
        return checkin(habitId, userId, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean checkin(Long habitId, Long userId, Long recordDate) {
        PlanHabit habit = planHabitMapper.selectById(habitId);
        if (habit == null) {
            return false;
        }

        long now = System.currentTimeMillis();
        long dayStart = normalizeDayStart(recordDate != null ? recordDate : now);

        // 检查是否已打卡
        Long exists = planHabitRecordMapper.selectCount(
                new LambdaQueryWrapper<PlanHabitRecord>()
                        .eq(PlanHabitRecord::getHabitId, habitId)
                        .eq(PlanHabitRecord::getUserId, userId)
                        .eq(PlanHabitRecord::getRecordDate, dayStart));
        if (exists != null && exists > 0) {
            return false; // 已打卡，防止重复
        }

        // 创建打卡记录
        PlanHabitRecord record = new PlanHabitRecord();
        record.setHabitId(habitId);
        record.setRecordDate(dayStart);
        record.setUserId(userId);
        record.setCreateBy(userId);
        record.setCreateTime(now);
        planHabitRecordMapper.insert(record);

        // 更新习惯统计
        habit.setTotalDays(countHabitRecords(habitId));
        habit.setCurrentDays(calculateStreak(habitId, normalizeDayStart(now)));
        habit.setUpdateTime(now);

        // 检查是否达成目标
        if (habit.getTargetDays() != null && habit.getTotalDays() >= habit.getTargetDays()) {
            habit.setStatus(1); // 已完成
        }

        planHabitMapper.updateById(habit);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean uncheckin(Long habitId, Long userId, Long recordDate) {
        PlanHabit habit = planHabitMapper.selectById(habitId);
        if (habit == null) {
            return false;
        }

        long now = System.currentTimeMillis();
        long dayStart = normalizeDayStart(recordDate != null ? recordDate : now);
        PlanHabitRecord record = planHabitRecordMapper.selectOne(
                new LambdaQueryWrapper<PlanHabitRecord>()
                        .eq(PlanHabitRecord::getHabitId, habitId)
                        .eq(PlanHabitRecord::getUserId, userId)
                        .eq(PlanHabitRecord::getRecordDate, dayStart)
                        .last("LIMIT 1"));
        if (record == null) {
            return false;
        }

        planHabitRecordMapper.deleteById(record.getId());
        habit.setTotalDays(countHabitRecords(habitId));
        habit.setCurrentDays(calculateStreak(habitId, normalizeDayStart(now)));
        if (habit.getStatus() != null
                && habit.getStatus() == 1
                && habit.getTargetDays() != null
                && habit.getTotalDays() < habit.getTargetDays()) {
            habit.setStatus(0);
        }
        habit.setUpdateTime(now);
        planHabitMapper.updateById(habit);
        return true;
    }



    @Override
    public List<PlanHabitRecord> getRecords(Long habitId) {
        return planHabitRecordMapper.selectList(
                new LambdaQueryWrapper<PlanHabitRecord>()
                        .eq(PlanHabitRecord::getHabitId, habitId)
                        .orderByDesc(PlanHabitRecord::getRecordDate));
    }

    /**
     * 计算连续打卡天数
     */
    private int calculateStreak(Long habitId, long todayStart) {
        int streak = 0;
        long dayStart = todayStart;
        long oneDay = 24 * 60 * 60 * 1000L;

        while (true) {
            Long count = planHabitRecordMapper.selectCount(
                    new LambdaQueryWrapper<PlanHabitRecord>()
                            .eq(PlanHabitRecord::getHabitId, habitId)
                            .eq(PlanHabitRecord::getRecordDate, dayStart));
            if (count != null && count > 0) {
                streak++;
                dayStart -= oneDay; // 往前一天
            } else {
                break;
            }
        }

        return streak;
    }

    private long normalizeDayStart(long timestamp) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    private int countHabitRecords(Long habitId) {
        Long count = planHabitRecordMapper.selectCount(
                new LambdaQueryWrapper<PlanHabitRecord>()
                        .eq(PlanHabitRecord::getHabitId, habitId));
        return count == null ? 0 : count.intValue();
    }
}
