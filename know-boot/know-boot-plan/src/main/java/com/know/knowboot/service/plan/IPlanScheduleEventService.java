package com.know.knowboot.service.plan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.plan.PlanScheduleEvent;

import java.util.List;
import java.util.Map;

/**
 * 日程事件服务接口
 */
public interface IPlanScheduleEventService {

    /**
     * 分页查询
     */
    IPage<PlanScheduleEvent> page(PlanScheduleEvent query, Long userId, Integer pageNum, Integer pageSize);

    /**
     * 按时间范围查询
     */
    List<PlanScheduleEvent> listByDateRange(Long userId, Long startTime, Long endTime);

    /**
     * 按日期查询(某一天)
     */
    List<PlanScheduleEvent> listByDate(Long userId, Long date);

    /**
     * 按周查询
     */
    List<PlanScheduleEvent> listByWeek(Long userId, Long weekStart, Long weekEnd);

    /**
     * 按月查询
     */
    List<PlanScheduleEvent> listByMonth(Long userId, Integer year, Integer month);

    /**
     * 按象限查询
     */
    List<PlanScheduleEvent> listByQuadrant(Long userId, Integer quadrant);

    /**
     * 按计划ID查询
     */
    List<PlanScheduleEvent> listByPlanId(Long planId);

    /**
     * 今日统计
     */
    Map<String, Object> getTodayStats(Long userId);

    /**
     * 获取详情
     */
    PlanScheduleEvent getById(Long id);

    /**
     * 新增日程
     */
    boolean add(PlanScheduleEvent event, Long userId);

    /**
     * 修改日程
     */
    boolean update(PlanScheduleEvent event);

    /**
     * 完成日程
     */
    boolean complete(Long id);

    /**
     * 取消完成日程
     */
    boolean uncomplete(Long id);

    /**
     * 删除日程
     */
    boolean delete(Long id);
}
