package com.know.knowboot.service.plan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.plan.PlanScheduleEventTemplate;

import java.util.List;

/**
 * 日程事件模板服务接口
 */
public interface IPlanScheduleEventTemplateService {

    /**
     * 分页查询
     */
    IPage<PlanScheduleEventTemplate> page(PlanScheduleEventTemplate query, Integer pageNum, Integer pageSize);

    /**
     * 热门模板列表
     */
    List<PlanScheduleEventTemplate> hotList(int limit);

    /**
     * 获取详情
     */
    PlanScheduleEventTemplate getDetail(Long id);

    /**
     * 创建模板
     */
    Long create(PlanScheduleEventTemplate t, Long userId);

    /**
     * 更新模板
     */
    Boolean update(PlanScheduleEventTemplate t, Long userId);

    /**
     * 删除模板
     */
    Boolean delete(Long id);
}
