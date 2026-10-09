package com.know.knowboot.controller;

import com.know.knowboot.aop.NotLogin;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.service.ISysAppVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * App版本控制器
 */
@Api(tags = "App版本")
@RestController
@RequestMapping("/api/system/app/version")
public class SysAppVersionController {

    @Autowired
    private ISysAppVersionService sysAppVersionService;

    /**
     * 检测App是否有可用的新版本（免登录，App启动时调用）
     */
    @ApiOperation("检测App最新版本")
    @NotLogin
    @GetMapping("/latest")
    public AjaxResult<Map<String, Object>> latest(
            @ApiParam("平台 [android:安卓, ios:苹果]") @RequestParam("platform") String platform,
            @ApiParam("当前版本号(内部比较用)") @RequestParam("versionCode") Integer versionCode) {
        return AjaxResult.success(sysAppVersionService.checkLatest(platform, versionCode));
    }
}
