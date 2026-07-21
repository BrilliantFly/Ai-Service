package com.know.knowboot.service.plan;

import com.know.knowboot.entity.plan.PlanHabitRecord;

import java.util.List;
import java.util.Map;

/**
 * 打卡记录服务接口
 */
public interface IPlanHabitRecordService {

    /**
     * 按习惯ID查询记录
     */
    List<PlanHabitRecord> listByHabitId(Long habitId);

    /**
     * 按用户和日期查询
     */
    List<PlanHabitRecord> listByUserIdAndDate(Long userId, Long date);

    /**
     * 按用户和时间范围查询打卡记录
     */
    List<PlanHabitRecord> listByUserIdAndDateRange(Long userId, Long startTime, Long endTime);

    /**
     * 按月统计
     */
    Map<String, Object> getMonthlyStats(Long habitId, Integer year, Integer month);
}
