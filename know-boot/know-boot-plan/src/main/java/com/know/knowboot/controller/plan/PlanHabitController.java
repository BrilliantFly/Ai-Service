package com.know.knowboot.controller.plan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.plan.PlanHabit;
import com.know.knowboot.entity.plan.PlanHabitRecord;
import com.know.knowboot.service.plan.IPlanHabitRecordService;
import com.know.knowboot.service.plan.IPlanHabitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 习惯控制器
 */
@Api(tags = "习惯打卡管理")
@RestController
@RequestMapping("/api/plan/habit")
public class PlanHabitController {

    @Autowired
    private IPlanHabitService planHabitService;

    @Autowired
    private IPlanHabitRecordService planHabitRecordService;

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public AjaxResult<IPage<PlanHabit>> page(
            PlanHabit query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = 1L;
        return AjaxResult.success(planHabitService.page(query, userId, pageNum, pageSize));
    }

    @ApiOperation("查询列表")
    @GetMapping("/list")
    public AjaxResult<List<PlanHabit>> list(PlanHabit query) {
        Long userId = 1L;
        IPage<PlanHabit> page = planHabitService.page(query, userId, 1, 1000);
        return AjaxResult.success(page.getRecords());
    }

    @ApiOperation("统计")
    @GetMapping("/stats")
    public AjaxResult<Map<String, Object>> getStats(
            @RequestParam(required = false) Integer execStatus) {
        Long userId = 1L;
        return AjaxResult.success(planHabitService.getStats(userId, execStatus));
    }

    @ApiOperation("获取详情")
    @GetMapping("/{id}")
    public AjaxResult<PlanHabit> get(@PathVariable Long id) {
        return AjaxResult.success(planHabitService.getById(id));
    }

    @ApiOperation("新增习惯")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody PlanHabit habit) {
        Long userId = 1L;
        return AjaxResult.success(planHabitService.add(habit, userId));
    }

    @ApiOperation("修改习惯")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody PlanHabit habit) {
        return AjaxResult.success(planHabitService.update(habit));
    }

    @ApiOperation("删除习惯")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(planHabitService.delete(id));
    }

    @ApiOperation("打卡")
    @PostMapping("/{id}/checkin")
    public AjaxResult<Boolean> checkin(
            @PathVariable Long id,
            @RequestParam(required = false) Long recordDate,
            @RequestBody(required = false) Map<String, Object> body) {
        Long userId = 1L;
        return AjaxResult.success(planHabitService.checkin(id, userId, resolveRecordDate(recordDate, body)));
    }

    @ApiOperation("取消打卡")
    @PostMapping("/{id}/uncheckin")
    public AjaxResult<Boolean> uncheckin(
            @PathVariable Long id,
            @RequestParam(required = false) Long recordDate,
            @RequestBody(required = false) Map<String, Object> body) {
        Long userId = 1L;
        return AjaxResult.success(planHabitService.uncheckin(id, userId, resolveRecordDate(recordDate, body)));
    }

    @ApiOperation("查询打卡记录")
    @GetMapping("/{id}/records")
    public AjaxResult<List<PlanHabitRecord>> getRecords(@PathVariable Long id) {
        return AjaxResult.success(planHabitService.getRecords(id));
    }

    @ApiOperation("切换执行状态")
    @PutMapping("/{id}/exec-status")
    public AjaxResult<Boolean> updateExecStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        Long userId = 1L;
        Integer execStatus = body.get("execStatus") instanceof Number
                ? ((Number) body.get("execStatus")).intValue() : null;
        return AjaxResult.success(planHabitService.updateExecStatus(id, execStatus, userId));
    }

    @ApiOperation("按月统计打卡")
    @GetMapping("/records/monthly")
    public AjaxResult<Map<String, Object>> getMonthlyStats(
            @RequestParam Long habitId,
            @RequestParam Integer year,
            @RequestParam Integer month) {
        return AjaxResult.success(planHabitRecordService.getMonthlyStats(habitId, year, month));
    }

    private Long resolveRecordDate(Long recordDate, Map<String, Object> body) {
        if (recordDate != null) {
            return recordDate;
        }
        if (body == null || body.get("recordDate") == null) {
            return null;
        }
        Object value = body.get("recordDate");
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(String.valueOf(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
