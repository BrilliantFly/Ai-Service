package com.know.knowboot.service.plan.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.plan.PlanHabitRecord;
import com.know.knowboot.mapper.plan.PlanHabitRecordMapper;
import com.know.knowboot.service.plan.IPlanHabitRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 打卡记录服务实现
 */
@Service
public class PlanHabitRecordServiceImpl extends ServiceImpl<PlanHabitRecordMapper, PlanHabitRecord> implements IPlanHabitRecordService {

    @Autowired
    private PlanHabitRecordMapper planHabitRecordMapper;

    @Override
    public List<PlanHabitRecord> listByHabitId(Long habitId) {
        return list(new LambdaQueryWrapper<PlanHabitRecord>()
                .eq(PlanHabitRecord::getHabitId, habitId)
                .orderByDesc(PlanHabitRecord::getRecordDate));
    }

    @Override
    public List<PlanHabitRecord> listByUserIdAndDate(Long userId, Long date) {
        // 归一化到当天0点
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long dayStart = cal.getTimeInMillis();

        return list(new LambdaQueryWrapper<PlanHabitRecord>()
                .eq(PlanHabitRecord::getUserId, userId)
                .eq(PlanHabitRecord::getRecordDate, dayStart)
                .orderByDesc(PlanHabitRecord::getCreateTime));
    }

    @Override
    public List<PlanHabitRecord> listByUserIdAndDateRange(Long userId, Long startTime, Long endTime) {
        return list(new LambdaQueryWrapper<PlanHabitRecord>()
                .eq(PlanHabitRecord::getUserId, userId)
                .ge(PlanHabitRecord::getRecordDate, startTime)
                .le(PlanHabitRecord::getRecordDate, endTime)
                .orderByAsc(PlanHabitRecord::getRecordDate));
    }

    @Override
    public Map<String, Object> getMonthlyStats(Long habitId, Integer year, Integer month) {
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

        List<PlanHabitRecord> records = list(new LambdaQueryWrapper<PlanHabitRecord>()
                .eq(PlanHabitRecord::getHabitId, habitId)
                .ge(PlanHabitRecord::getRecordDate, monthStart)
                .le(PlanHabitRecord::getRecordDate, monthEnd)
                .orderByAsc(PlanHabitRecord::getRecordDate));

        // 组装打卡日期列表
        List<String> checkinDates = new ArrayList<>();
        for (PlanHabitRecord record : records) {
            cal.setTimeInMillis(record.getRecordDate());
            int day = cal.get(Calendar.DAY_OF_MONTH);
            checkinDates.add(String.valueOf(day));
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDays", records.size());
        stats.put("checkinDates", checkinDates);
        stats.put("year", year);
        stats.put("month", month);
        return stats;
    }
}
