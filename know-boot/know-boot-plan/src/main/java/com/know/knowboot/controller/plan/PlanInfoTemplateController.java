package com.know.knowboot.controller.plan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.dto.plan.UseInPlanRequest;
import com.know.knowboot.dto.plan.UseInPlanResult;
import com.know.knowboot.dto.plan.UseTemplateRequest;
import com.know.knowboot.dto.plan.UseTemplateResult;
import com.know.knowboot.entity.plan.PlanInfoTemplate;
import com.know.knowboot.service.plan.IPlanInfoTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 计划信息模板控制器
 */
@Api(tags = "计划信息模板管理")
@RestController
@RequestMapping("/api/plan/template")
public class PlanInfoTemplateController {

    @Autowired
    private IPlanInfoTemplateService planInfoTemplateService;

    @ApiOperation("分页查询模板")
    @GetMapping("/list")
    public AjaxResult<IPage<PlanInfoTemplate>> list(
            PlanInfoTemplate query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(planInfoTemplateService.page(query, pageNum, pageSize));
    }

    @ApiOperation("热门模板列表")
    @GetMapping("/hot")
    public AjaxResult<List<PlanInfoTemplate>> hot(
            @RequestParam(defaultValue = "10") Integer limit) {
        return AjaxResult.success(planInfoTemplateService.hotList(limit));
    }

    @ApiOperation("获取模板详情")
    @GetMapping("/{id}")
    public AjaxResult<PlanInfoTemplate> getDetail(@PathVariable Long id) {
        return AjaxResult.success(planInfoTemplateService.getDetail(id));
    }

    @ApiOperation("获取子模板列表")
    @GetMapping("/{id}/children")
    public AjaxResult<List<PlanInfoTemplate>> getChildren(@PathVariable Long id) {
        return AjaxResult.success(planInfoTemplateService.getChildren(id));
    }

    @ApiOperation("创建模板")
    @PostMapping
    public AjaxResult<Long> create(@RequestBody PlanInfoTemplate template) {
        Long userId = 1L;
        return AjaxResult.success(planInfoTemplateService.create(template, userId));
    }

    @ApiOperation("更新模板")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody PlanInfoTemplate template) {
        Long userId = 1L;
        return AjaxResult.success(planInfoTemplateService.update(template, userId));
    }

    @ApiOperation("删除模板")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(planInfoTemplateService.delete(id));
    }

    @ApiOperation("使用模板创建计划")
    @PostMapping("/use")
    public AjaxResult<UseTemplateResult> useTemplate(@RequestBody UseTemplateRequest request) {
        Long userId = 1L;
        if (request == null || request.getTemplateId() == null) {
            throw new RuntimeException("templateId 不能为空");
        }
        Long templateId = request.getTemplateId();
        return AjaxResult.success(planInfoTemplateService.useTemplate(templateId, userId, request));
    }

    @ApiOperation("在已有计划中使用模板")
    @PostMapping("/use-in-plan")
    public AjaxResult<UseInPlanResult> useInPlan(@RequestBody UseInPlanRequest request) {
        Long userId = 1L;
        return AjaxResult.success(planInfoTemplateService.useInPlan(request, userId));
    }

    @ApiOperation("从已有计划生成模板")
    @PostMapping("/from-plan/{planId}")
    public AjaxResult<Long> generateFromPlan(
            @PathVariable Long planId,
            @RequestBody Map<String, Object> body) {
        Long userId = 1L;
        String templateName = body.get("templateName") != null ? String.valueOf(body.get("templateName")) : null;
        String description = body.get("description") != null ? String.valueOf(body.get("description")) : null;
        Integer visibility = body.get("visibility") != null ? Integer.parseInt(String.valueOf(body.get("visibility"))) : 1;
        return AjaxResult.success(planInfoTemplateService.generateFromPlan(planId, templateName, description, visibility, userId));
    }
}
