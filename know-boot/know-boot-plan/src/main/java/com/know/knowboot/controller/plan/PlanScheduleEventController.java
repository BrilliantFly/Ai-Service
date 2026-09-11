package com.know.knowboot.controller.plan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.plan.PlanScheduleEvent;
import com.know.knowboot.service.plan.IPlanScheduleEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 日程事件控制器
 */
@Api(tags = "日程事件管理")
@RestController
@RequestMapping("/api/plan/event")
public class PlanScheduleEventController {

    @Autowired
    private IPlanScheduleEventService planScheduleEventService;

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public AjaxResult<IPage<PlanScheduleEvent>> page(
            PlanScheduleEvent query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        // TODO: 从Token获取用户ID
        Long userId = 1L;
        return AjaxResult.success(planScheduleEventService.page(query, userId, pageNum, pageSize));
    }

    @ApiOperation("查询列表")
    @GetMapping("/list")
    public AjaxResult<List<PlanScheduleEvent>> list(PlanScheduleEvent query) {
        Long userId = 1L;
        IPage<PlanScheduleEvent> page = planScheduleEventService.page(query, userId, 1, 1000);
        return AjaxResult.success(page.getRecords());
    }

    @ApiOperation("按日期查询")
    @GetMapping("/date")
    public AjaxResult<List<PlanScheduleEvent>> listByDate(
            @RequestParam Long date,
            @RequestParam(required = false) Integer execStatus) {
        Long userId = 1L;
        return AjaxResult.success(planScheduleEventService.listByDate(userId, date, execStatus));
    }

    @ApiOperation("按周查询")
    @GetMapping("/week")
    public AjaxResult<List<PlanScheduleEvent>> listByWeek(
            @RequestParam Long weekStart, @RequestParam Long weekEnd,
            @RequestParam(required = false) Integer execStatus) {
        Long userId = 1L;
        return AjaxResult.success(planScheduleEventService.listByWeek(userId, weekStart, weekEnd, execStatus));
    }

    @ApiOperation("按月查询")
    @GetMapping("/month")
    public AjaxResult<List<PlanScheduleEvent>> listByMonth(
            @RequestParam Integer year, @RequestParam Integer month,
            @RequestParam(required = false) Integer execStatus) {
        Long userId = 1L;
        return AjaxResult.success(planScheduleEventService.listByMonth(userId, year, month, execStatus));
    }

    @ApiOperation("按象限查询")
    @GetMapping("/quadrant/{quadrant}")
    public AjaxResult<List<PlanScheduleEvent>> listByQuadrant(
            @PathVariable Integer quadrant,
            @RequestParam(required = false) Integer execStatus) {
        Long userId = 1L;
        return AjaxResult.success(planScheduleEventService.listByQuadrant(userId, quadrant, execStatus));
    }

    @ApiOperation("按计划ID查询")
    @GetMapping("/plan/{planId}")
    public AjaxResult<List<PlanScheduleEvent>> listByPlanId(@PathVariable Long planId) {
        return AjaxResult.success(planScheduleEventService.listByPlanId(planId));
    }

    @ApiOperation("今日统计")
    @GetMapping("/today")
    public AjaxResult<Map<String, Object>> getTodayStats(
            @RequestParam(required = false) Integer execStatus) {
        Long userId = 1L;
        return AjaxResult.success(planScheduleEventService.getTodayStats(userId, execStatus));
    }

    @ApiOperation("获取详情")
    @GetMapping("/{id}")
    public AjaxResult<PlanScheduleEvent> get(@PathVariable Long id) {
        return AjaxResult.success(planScheduleEventService.getById(id));
    }

    @ApiOperation("新增日程")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody PlanScheduleEvent event) {
        Long userId = 1L;
        return AjaxResult.success(planScheduleEventService.add(event, userId));
    }

    @ApiOperation("修改日程")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody PlanScheduleEvent event) {
        return AjaxResult.success(planScheduleEventService.update(event));
    }

    @ApiOperation("完成日程")
    @PutMapping("/{id}/complete")
    public AjaxResult<Boolean> complete(@PathVariable Long id) {
        return AjaxResult.success(planScheduleEventService.complete(id));
    }

    @ApiOperation("取消完成日程")
    @PutMapping("/{id}/uncomplete")
    public AjaxResult<Boolean> uncomplete(@PathVariable Long id) {
        return AjaxResult.success(planScheduleEventService.uncomplete(id));
    }

    @ApiOperation("删除日程")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(planScheduleEventService.delete(id));
    }

    @ApiOperation("切换执行状态")
    @PutMapping("/{id}/exec-status")
    public AjaxResult<Boolean> updateExecStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        Long userId = 1L;
        Integer execStatus = body.get("execStatus") instanceof Number
                ? ((Number) body.get("execStatus")).intValue() : null;
        return AjaxResult.success(planScheduleEventService.updateExecStatus(id, execStatus, userId));
    }
}
