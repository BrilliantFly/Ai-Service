package com.know.knowboot.controller.plan;

import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.service.plan.IPlanHomeConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 首页配置控制器
 */
@Api(tags = "首页配置")
@RestController
@RequestMapping("/api/plan/home")
public class PlanHomeController {

    @Autowired
    private IPlanHomeConfigService planHomeConfigService;

    @ApiOperation("获取首页配置")
    @GetMapping("/config")
    public AjaxResult<Map<String, Map<String, Object>>> getConfig(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String roleId) {
        Long currentUserId = userId == null ? 1L : userId;
        return AjaxResult.success(planHomeConfigService.getConfig(currentUserId, roleId));
    }
}
