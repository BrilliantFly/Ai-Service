package com.know.knowboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.system.SystemLogOperation;
import com.know.knowboot.mapper.system.SystemLogOperationMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志控制器
 */
@Api(tags = "操作日志")
@RestController
@RequestMapping("/system/log/operation")
public class SystemLogOperationController {

    @Autowired
    private SystemLogOperationMapper logOperationMapper;

    @ApiOperation("分页查询操作日志")
    @GetMapping("/page")
    public AjaxResult<IPage<SystemLogOperation>> page(
            SystemLogOperation query,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页条数") @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(logOperationMapper.selectPage(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize),
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SystemLogOperation>()
                        .like(query.getModule() != null, SystemLogOperation::getModule, query.getModule())
                        .like(query.getUsername() != null, SystemLogOperation::getUsername, query.getUsername())
                        .orderByDesc(SystemLogOperation::getOperateTime)));
    }

    @ApiOperation("获取日志详情")
    @GetMapping("/{id}")
    public AjaxResult<SystemLogOperation> get(@ApiParam("日志ID") @PathVariable Long id) {
        return AjaxResult.success(logOperationMapper.selectById(id));
    }
}