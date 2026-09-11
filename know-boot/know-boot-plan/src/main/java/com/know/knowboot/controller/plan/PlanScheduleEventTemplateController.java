package com.know.knowboot.controller.plan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.plan.PlanScheduleEventTemplate;
import com.know.knowboot.service.plan.IPlanScheduleEventTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 日程事件模板控制器
 */
@Api(tags = "日程事件模板管理")
@RestController
@RequestMapping("/api/plan/event-template")
public class PlanScheduleEventTemplateController {

    @Autowired
    private IPlanScheduleEventTemplateService planScheduleEventTemplateService;

    @ApiOperation("分页查询模板")
    @GetMapping("/list")
    public AjaxResult<IPage<PlanScheduleEventTemplate>> list(
            PlanScheduleEventTemplate query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(planScheduleEventTemplateService.page(query, pageNum, pageSize));
    }

    @ApiOperation("热门模板列表")
    @GetMapping("/hot")
    public AjaxResult<List<PlanScheduleEventTemplate>> hot(
            @RequestParam(defaultValue = "10") Integer limit) {
        return AjaxResult.success(planScheduleEventTemplateService.hotList(limit));
    }

    @ApiOperation("获取模板详情")
    @GetMapping("/{id}")
    public AjaxResult<PlanScheduleEventTemplate> getDetail(@PathVariable Long id) {
        return AjaxResult.success(planScheduleEventTemplateService.getDetail(id));
    }

    @ApiOperation("创建模板")
    @PostMapping
    public AjaxResult<Long> create(@RequestBody PlanScheduleEventTemplate template) {
        Long userId = 1L;
        return AjaxResult.success(planScheduleEventTemplateService.create(template, userId));
    }

    @ApiOperation("更新模板")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody PlanScheduleEventTemplate template) {
        Long userId = 1L;
        return AjaxResult.success(planScheduleEventTemplateService.update(template, userId));
    }

    @ApiOperation("删除模板")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(planScheduleEventTemplateService.delete(id));
    }
}
