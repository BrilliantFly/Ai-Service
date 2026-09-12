package com.know.knowboot.service.plan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.dto.plan.UseInPlanRequest;
import com.know.knowboot.dto.plan.UseInPlanResult;
import com.know.knowboot.dto.plan.UseTemplateRequest;
import com.know.knowboot.dto.plan.UseTemplateResult;
import com.know.knowboot.entity.plan.PlanInfoTemplate;

import java.util.List;
import java.util.Map;

/**
 * 计划信息模板服务接口
 */
public interface IPlanInfoTemplateService {

    /**
     * 分页查询
     */
    IPage<PlanInfoTemplate> page(PlanInfoTemplate query, Integer pageNum, Integer pageSize);

    /**
     * 热门模板列表
     */
    List<PlanInfoTemplate> hotList(int limit);

    /**
     * 获取详情
     */
    PlanInfoTemplate getDetail(Long id);

    /**
     * 获取子模板列表
     */
    List<PlanInfoTemplate> getChildren(Long parentId);

    List<Map<String, Object>> getTree(Long rootId);

    /**
     * 创建模板
     */
    Long create(PlanInfoTemplate t, Long userId);

    /**
     * 更新模板
     */
    Boolean update(PlanInfoTemplate t, Long userId);

    /**
     * 删除模板(递归删除子模板)
     */
    Boolean delete(Long id);

    /**
     * 使用模板创建计划
     */
    UseTemplateResult useTemplate(Long templateId, Long userId, UseTemplateRequest request);

    /**
     * 在已有计划中使用模板(添加习惯/事件)
     */
    UseInPlanResult useInPlan(UseInPlanRequest request, Long userId);

    /**
     * 从已有计划生成模板
     */
    Long generateFromPlan(Long planId, String templateName, String description, Integer visibility, Long userId);
}
