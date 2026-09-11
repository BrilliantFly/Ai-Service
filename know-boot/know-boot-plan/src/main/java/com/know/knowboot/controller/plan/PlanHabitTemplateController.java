package com.know.knowboot.controller.plan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.plan.PlanHabitTemplate;
import com.know.knowboot.service.plan.IPlanHabitTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 习惯模板控制器
 */
@Api(tags = "习惯模板管理")
@RestController
@RequestMapping("/api/plan/habit-template")
public class PlanHabitTemplateController {

    @Autowired
    private IPlanHabitTemplateService planHabitTemplateService;

    @ApiOperation("分页查询模板")
    @GetMapping("/list")
    public AjaxResult<IPage<PlanHabitTemplate>> list(
            PlanHabitTemplate query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(planHabitTemplateService.page(query, pageNum, pageSize));
    }

    @ApiOperation("热门模板列表")
    @GetMapping("/hot")
    public AjaxResult<List<PlanHabitTemplate>> hot(
            @RequestParam(defaultValue = "10") Integer limit) {
        return AjaxResult.success(planHabitTemplateService.hotList(limit));
    }

    @ApiOperation("获取模板详情")
    @GetMapping("/{id}")
    public AjaxResult<PlanHabitTemplate> getDetail(@PathVariable Long id) {
        return AjaxResult.success(planHabitTemplateService.getDetail(id));
    }

    @ApiOperation("创建模板")
    @PostMapping
    public AjaxResult<Long> create(@RequestBody PlanHabitTemplate template) {
        Long userId = 1L;
        return AjaxResult.success(planHabitTemplateService.create(template, userId));
    }

    @ApiOperation("更新模板")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody PlanHabitTemplate template) {
        Long userId = 1L;
        return AjaxResult.success(planHabitTemplateService.update(template, userId));
    }

    @ApiOperation("删除模板")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(planHabitTemplateService.delete(id));
    }
}
