package com.know.knowboot.service.plan.impl;

import com.know.knowboot.entity.plan.PlanHabit;
import com.know.knowboot.entity.plan.PlanHabitRecord;
import com.know.knowboot.entity.plan.PlanScheduleEvent;
import com.know.knowboot.mapper.plan.PlanHabitMapper;
import com.know.knowboot.mapper.plan.PlanHabitRecordMapper;
import com.know.knowboot.service.plan.IPlanCalendarService;
import com.know.knowboot.service.plan.IPlanScheduleEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 日历服务实现
 */
@Service
public class PlanCalendarServiceImpl implements IPlanCalendarService {

    @Autowired
    private IPlanScheduleEventService planScheduleEventService;

    @Autowired
    private PlanHabitMapper planHabitMapper;

    @Autowired
    private PlanHabitRecordMapper planHabitRecordMapper;

    @Override
    public Map<String, Object> getMonthlyData(Long userId, Integer year, Integer month, Integer execStatus) {
        // 计算月份边界
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

        // 1. 查询月份内的日程事件
        List<PlanScheduleEvent> events = planScheduleEventService.listByDateRange(userId, monthStart, monthEnd, execStatus);

        // 2. 查询用户所有习惯
        List<PlanHabit> habits = planHabitMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<PlanHabit>()
                        .eq(PlanHabit::getUserId, userId)
                        .eq(execStatus != null, PlanHabit::getExecStatus, execStatus));

        // 3. 查询月份内的所有习惯打卡记录
        List<PlanHabitRecord> allRecords = planHabitRecordMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<PlanHabitRecord>()
                        .eq(PlanHabitRecord::getUserId, userId)
                        .ge(PlanHabitRecord::getRecordDate, monthStart)
                        .le(PlanHabitRecord::getRecordDate, monthEnd)
                        .orderByAsc(PlanHabitRecord::getRecordDate));

        // 4. 组装习惯打卡日期映射: { habitId: { habitName, checkinDays: [1,3,5...] } }
        Map<String, Object> habitCheckinMap = new HashMap<>();
        for (PlanHabit habit : habits) {
            Map<String, Object> habitInfo = new HashMap<>();
            habitInfo.put("habitName", habit.getName());
            habitInfo.put("habitId", habit.getId());
            habitInfo.put("description", habit.getDescription() != null ? habit.getDescription() : "");
            habitInfo.put("currentDays", habit.getCurrentDays() != null ? habit.getCurrentDays() : 0);
            habitInfo.put("totalDays", habit.getTotalDays() != null ? habit.getTotalDays() : 0);
            habitInfo.put("icon", habit.getIcon());
            habitInfo.put("color", habit.getColor());
            habitInfo.put("category", habit.getCategory());
            habitInfo.put("targetValue", habit.getTargetValue());
            habitInfo.put("targetUnit", habit.getTargetUnit());
            habitInfo.put("trackingType", habit.getTrackingType());
            habitInfo.put("note", habit.getNote());
            habitInfo.put("motto", habit.getMotto());
            habitInfo.put("timePeriod", habit.getTimePeriod());
            habitInfo.put("allowBackfill", habit.getAllowBackfill());
            habitInfo.put("endDate", habit.getEndDate());
            habitInfo.put("restDays", habit.getRestDays());
            habitInfo.put("secondReminder", habit.getSecondReminder());
            habitInfo.put("targetDays", habit.getTargetDays());
            habitInfo.put("frequencyType", habit.getFrequencyType());
            habitInfo.put("frequencyRule", habit.getFrequencyRule());
            habitInfo.put("startDate", habit.getStartDate());
            habitInfo.put("reminderTime", habit.getReminderTime());
            habitInfo.put("status", habit.getStatus());
            List<Integer> days = new ArrayList<>();
            for (PlanHabitRecord record : allRecords) {
                if (record.getHabitId().equals(habit.getId())) {
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(record.getRecordDate());
                    days.add(c.get(Calendar.DAY_OF_MONTH));
                }
            }
            habitInfo.put("checkinDays", days);
            habitCheckinMap.put(String.valueOf(habit.getId()), habitInfo);
        }

        // 5. 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("events", events);
        result.put("habits", habitCheckinMap);
        result.put("year", year);
        result.put("month", month);
        return result;
    }
}
