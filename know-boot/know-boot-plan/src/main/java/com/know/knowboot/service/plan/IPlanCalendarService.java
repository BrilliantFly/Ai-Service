package com.know.knowboot.service.plan;

import java.util.Map;

/**
 * 日历服务接口
 */
public interface IPlanCalendarService {

    /**
     * 按月获取日历数据（日程事件 + 习惯打卡记录）
     */
    Map<String, Object> getMonthlyData(Long userId, Integer year, Integer month);
}
