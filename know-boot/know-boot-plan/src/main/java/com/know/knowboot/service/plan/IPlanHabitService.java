package com.know.knowboot.service.plan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.plan.PlanHabit;
import com.know.knowboot.entity.plan.PlanHabitRecord;

import java.util.List;
import java.util.Map;

/**
 * 习惯服务接口
 */
public interface IPlanHabitService {

    /**
     * 分页查询
     */
    IPage<PlanHabit> page(PlanHabit query, Long userId, Integer pageNum, Integer pageSize);

    /**
     * 查询用户习惯列表
     */
    List<PlanHabit> listByUserId(Long userId);

    /**
     * 统计
     */
    Map<String, Object> getStats(Long userId);

    /**
     * 获取详情
     */
    PlanHabit getById(Long id);

    /**
     * 新增习惯
     */
    boolean add(PlanHabit habit, Long userId);

    /**
     * 修改习惯
     */
    boolean update(PlanHabit habit);

    /**
     * 删除习惯
     */
    boolean delete(Long id);

    /**
     * 打卡（默认当天）
     */
    boolean checkin(Long habitId, Long userId);

    /**
     * 打卡（指定日期，时间戳）
     */
    boolean checkin(Long habitId, Long userId, Long recordDate);

    /**
     * 取消打卡（指定日期，时间戳；为空则取消当天）
     */
    boolean uncheckin(Long habitId, Long userId, Long recordDate);

    /**
     * 查询打卡记录
     */
    List<PlanHabitRecord> getRecords(Long habitId);
}
