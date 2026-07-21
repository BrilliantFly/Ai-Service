package com.know.knowboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.tenant.SysLoginLog;
import com.know.knowboot.entity.tenant.SysOperLog;
import com.know.knowboot.service.ISysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 日志管理控制器
 */
@Api(tags = "日志管理")
@RestController
@RequestMapping("/system/log")
public class SysLogController {

    @Autowired
    private ISysLogService sysLogService;

    @ApiOperation("分页查询操作日志")
    @GetMapping("/oper/page")
    public AjaxResult<IPage<SysOperLog>> pageOperLog(
            SysOperLog query,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页条数") @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(sysLogService.pageOperLog(query, pageNum, pageSize));
    }

    @ApiOperation("获取操作日志列表")
    @GetMapping("/oper/list")
    public AjaxResult<List<SysOperLog>> listOperLog() {
        return AjaxResult.success(sysLogService.listOperLog());
    }

    @ApiOperation("删除操作日志")
    @DeleteMapping("/oper/{id}")
    public AjaxResult<Boolean> deleteOperLog(@ApiParam("ID") @PathVariable Long id) {
        return AjaxResult.success(sysLogService.deleteOperLog(id));
    }

    @ApiOperation("清空操作日志")
    @DeleteMapping("/oper/clear")
    public AjaxResult<Boolean> clearOperLog() {
        return AjaxResult.success(sysLogService.clearOperLog());
    }

    @ApiOperation("分页查询登录日志")
    @GetMapping("/login/page")
    public AjaxResult<IPage<SysLoginLog>> pageLoginLog(
            SysLoginLog query,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页条数") @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(sysLogService.pageLoginLog(query, pageNum, pageSize));
    }

    @ApiOperation("获取登录日志列表")
    @GetMapping("/login/list")
    public AjaxResult<List<SysLoginLog>> listLoginLog() {
        return AjaxResult.success(sysLogService.listLoginLog());
    }

    @ApiOperation("删除登录日志")
    @DeleteMapping("/login/{id}")
    public AjaxResult<Boolean> deleteLoginLog(@ApiParam("ID") @PathVariable Long id) {
        return AjaxResult.success(sysLogService.deleteLoginLog(id));
    }

    @ApiOperation("清空登录日志")
    @DeleteMapping("/login/clear")
    public AjaxResult<Boolean> clearLoginLog() {
        return AjaxResult.success(sysLogService.clearLoginLog());
    }
}