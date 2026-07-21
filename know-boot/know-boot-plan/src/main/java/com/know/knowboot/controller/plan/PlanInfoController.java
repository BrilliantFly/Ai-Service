package com.know.knowboot.controller.plan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.plan.PlanInfo;
import com.know.knowboot.service.plan.IPlanInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 计划信息控制器
 */
@Api(tags = "计划管理")
@RestController
@RequestMapping("/api/plan/info")
public class PlanInfoController {

    @Autowired
    private IPlanInfoService planInfoService;

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public AjaxResult<IPage<PlanInfo>> page(
            PlanInfo query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = 1L;
        return AjaxResult.success(planInfoService.page(query, userId, pageNum, pageSize));
    }

    @ApiOperation("按象限查询")
    @GetMapping("/quadrant/{quadrantId}")
    public AjaxResult<List<PlanInfo>> listByQuadrant(@PathVariable Long quadrantId) {
        return AjaxResult.success(planInfoService.listByQuadrant(quadrantId));
    }

    @ApiOperation("获取详情")
    @GetMapping("/{id}")
    public AjaxResult<PlanInfo> get(@PathVariable Long id) {
        return AjaxResult.success(planInfoService.getById(id));
    }

    @ApiOperation("新增计划")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody PlanInfo plan) {
        Long userId = 1L;
        return AjaxResult.success(planInfoService.add(plan, userId));
    }

    @ApiOperation("修改计划")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody PlanInfo plan) {
        return AjaxResult.success(planInfoService.update(plan));
    }

    @ApiOperation("删除计划")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(planInfoService.delete(id));
    }

    @ApiOperation("更新进度")
    @PutMapping("/{id}/progress")
    public AjaxResult<Boolean> updateProgress(
            @PathVariable Long id,
            @RequestParam Integer progress) {
        return AjaxResult.success(planInfoService.updateProgress(id, progress));
    }

    @ApiOperation("计划转日程")
    @PostMapping("/{id}/to-schedule")
    public AjaxResult<Long> toSchedule(
            @PathVariable Long id,
            @RequestParam Long startTime,
            @RequestParam Long endTime) {
        return AjaxResult.success(planInfoService.toSchedule(id, startTime, endTime));
    }
}
