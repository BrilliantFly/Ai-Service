package com.know.knowboot.service.plan;

import com.know.knowboot.entity.plan.PlanScheduleCategory;

import java.util.List;

/**
 * 日程分类服务接口
 */
public interface IPlanScheduleCategoryService {

    /**
     * 查询所有分类
     */
    List<PlanScheduleCategory> list();

    /**
     * 获取详情
     */
    PlanScheduleCategory getById(Long id);

    /**
     * 新增分类
     */
    boolean add(PlanScheduleCategory category);

    /**
     * 修改分类
     */
    boolean update(PlanScheduleCategory category);

    /**
     * 删除分类
     */
    boolean delete(Long id);
}
