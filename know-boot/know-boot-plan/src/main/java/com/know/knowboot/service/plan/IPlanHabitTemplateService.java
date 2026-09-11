package com.know.knowboot.service.plan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.plan.PlanHabitTemplate;

import java.util.List;

/**
 * 习惯模板服务接口
 */
public interface IPlanHabitTemplateService {

    /**
     * 分页查询
     */
    IPage<PlanHabitTemplate> page(PlanHabitTemplate query, Integer pageNum, Integer pageSize);

    /**
     * 热门模板列表
     */
    List<PlanHabitTemplate> hotList(int limit);

    /**
     * 获取详情
     */
    PlanHabitTemplate getDetail(Long id);

    /**
     * 创建模板
     */
    Long create(PlanHabitTemplate t, Long userId);

    /**
     * 更新模板
     */
    Boolean update(PlanHabitTemplate t, Long userId);

    /**
     * 删除模板
     */
    Boolean delete(Long id);
}
