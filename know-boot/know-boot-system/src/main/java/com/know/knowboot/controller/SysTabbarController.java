package com.know.knowboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.tenant.SysTabbar;
import com.know.knowboot.service.ISysTabbarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "底部导航管理")
@RestController
@RequestMapping("/api/system/tabbar")
public class SysTabbarController {

    @Autowired
    private ISysTabbarService sysTabbarService;

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public AjaxResult<IPage<SysTabbar>> page(
            SysTabbar query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(sysTabbarService.page(query, pageNum, pageSize));
    }

    @ApiOperation("获取所有")
    @GetMapping("/list")
    public AjaxResult<List<SysTabbar>> list() {
        return AjaxResult.success(sysTabbarService.listAll());
    }

    @ApiOperation("获取详情")
    @GetMapping("/{id}")
    public AjaxResult<SysTabbar> get(@PathVariable Long id) {
        return AjaxResult.success(sysTabbarService.getById(id));
    }

    @ApiOperation("新增")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody SysTabbar tabbar) {
        return AjaxResult.success(sysTabbarService.add(tabbar));
    }

    @ApiOperation("修改")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody SysTabbar tabbar) {
        return AjaxResult.success(sysTabbarService.update(tabbar));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(sysTabbarService.delete(id));
    }
}