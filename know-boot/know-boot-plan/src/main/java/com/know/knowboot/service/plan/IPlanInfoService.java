package com.know.knowboot.service.plan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.plan.PlanInfo;

import java.util.List;

/**
 * 计划信息服务接口
 */
public interface IPlanInfoService {

    /**
     * 分页查询
     */
    IPage<PlanInfo> page(PlanInfo query, Long userId, Integer pageNum, Integer pageSize);

    /**
     * 按象限查询
     */
    List<PlanInfo> listByQuadrant(Long quadrantId);

    /**
     * 获取详情
     */
    PlanInfo getById(Long id);

    /**
     * 新增计划
     */
    boolean add(PlanInfo plan, Long userId);

    /**
     * 修改计划
     */
    boolean update(PlanInfo plan);

    /**
     * 删除计划
     */
    boolean delete(Long id);

    /**
     * 更新进度
     */
    boolean updateProgress(Long id, Integer progress);

    /**
     * 计划转日程
     */
    Long toSchedule(Long planId, Long startTime, Long endTime);

    /**
     * 更新计划状态
     */
    boolean updateStatus(Long id, Integer status, Long userId);

    /**
     * 按模板ID查询计划列表
     */
    List<PlanInfo> listByTemplateId(Long templateId);
}
