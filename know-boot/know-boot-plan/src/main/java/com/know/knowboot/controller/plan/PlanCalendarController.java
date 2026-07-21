package com.know.knowboot.controller.plan;

import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.service.plan.IPlanCalendarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 日历视图控制器
 */
@Api(tags = "日历视图")
@RestController
@RequestMapping("/api/plan/calendar")
public class PlanCalendarController {

    @Autowired
    private IPlanCalendarService planCalendarService;

    @ApiOperation("按月获取日历数据")
    @GetMapping("/monthly")
    public AjaxResult<Map<String, Object>> getMonthlyData(
            @RequestParam Integer year,
            @RequestParam Integer month) {
        // TODO: 从Token获取用户ID
        Long userId = 1L;
        return AjaxResult.success(planCalendarService.getMonthlyData(userId, year, month));
    }
}
