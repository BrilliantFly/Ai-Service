package com.know.knowboot.controller.plan;

import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.plan.PlanFocusSession;
import com.know.knowboot.service.plan.IPlanFocusSessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 番茄专注记录控制器
 */
@Api(tags = "番茄专注记录管理")
@RestController
@RequestMapping("/api/plan/focus")
public class PlanFocusSessionController {

    @Autowired
    private IPlanFocusSessionService planFocusSessionService;

    @ApiOperation("新增专注记录")
    @PostMapping("/session")
    public AjaxResult<Boolean> addSession(@RequestBody PlanFocusSession session) {
        Long userId = 1L;
        return AjaxResult.success(planFocusSessionService.addSession(session, userId));
    }

    @ApiOperation("查询今日专注记录")
    @GetMapping("/today")
    public AjaxResult<List<PlanFocusSession>> listToday() {
        Long userId = 1L;
        return AjaxResult.success(planFocusSessionService.listToday(userId));
    }

    @ApiOperation("查询今日专注统计")
    @GetMapping("/stats")
    public AjaxResult<Map<String, Object>> getTodayStats() {
        Long userId = 1L;
        return AjaxResult.success(planFocusSessionService.getTodayStats(userId));
    }
}
