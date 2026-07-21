package com.know.knowboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.tenant.SysJob;
import com.know.knowboot.service.ISysJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位管理控制器
 */
@Api(tags = "岗位管理")
@RestController
@RequestMapping("/system/job")
public class SysJobController {

    @Autowired
    private ISysJobService sysJobService;

    @ApiOperation("分页查询岗位")
    @GetMapping("/page")
    public AjaxResult<IPage<SysJob>> page(
            SysJob query,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页条数") @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(sysJobService.page(query, pageNum, pageSize));
    }

    @ApiOperation("获取所有岗位")
    @GetMapping("/list")
    public AjaxResult<List<SysJob>> list() {
        return AjaxResult.success(sysJobService.listAll());
    }

    @ApiOperation("获取岗位详情")
    @GetMapping("/{id}")
    public AjaxResult<SysJob> get(@ApiParam("岗位ID") @PathVariable Long id) {
        return AjaxResult.success(sysJobService.getById(id));
    }

    @ApiOperation("新增岗位")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody SysJob job) {
        return AjaxResult.success(sysJobService.add(job));
    }

    @ApiOperation("修改岗位")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody SysJob job) {
        return AjaxResult.success(sysJobService.update(job));
    }

    @ApiOperation("删除岗位")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@ApiParam("岗位ID") @PathVariable Long id) {
        return AjaxResult.success(sysJobService.delete(id));
    }
}