package com.know.knowboot.controller.plan;

import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.plan.PlanScheduleCategory;
import com.know.knowboot.service.plan.IPlanScheduleCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 日程分类控制器
 */
@Api(tags = "日程分类管理")
@RestController
@RequestMapping("/api/plan/category")
public class PlanScheduleCategoryController {

    @Autowired
    private IPlanScheduleCategoryService planScheduleCategoryService;

    @ApiOperation("查询分类列表")
    @GetMapping("/list")
    public AjaxResult<List<PlanScheduleCategory>> list() {
        return AjaxResult.success(planScheduleCategoryService.list());
    }

    @ApiOperation("获取分类详情")
    @GetMapping("/{id}")
    public AjaxResult<PlanScheduleCategory> get(@PathVariable Long id) {
        return AjaxResult.success(planScheduleCategoryService.getById(id));
    }

    @ApiOperation("新增分类")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody PlanScheduleCategory category) {
        return AjaxResult.success(planScheduleCategoryService.add(category));
    }

    @ApiOperation("修改分类")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody PlanScheduleCategory category) {
        return AjaxResult.success(planScheduleCategoryService.update(category));
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(planScheduleCategoryService.delete(id));
    }
}
