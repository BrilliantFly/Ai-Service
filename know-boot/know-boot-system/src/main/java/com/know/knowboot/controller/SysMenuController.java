package com.know.knowboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.tenant.SysMenu;
import com.know.knowboot.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理控制器
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/api/system/menu")
public class SysMenuController {

    @Autowired
    private ISysMenuService sysMenuService;

    @ApiOperation("分页查询菜单")
    @GetMapping("/page")
    public AjaxResult<IPage<SysMenu>> page(
            SysMenu query,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页条数") @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(sysMenuService.page(query, pageNum, pageSize));
    }

    @ApiOperation("获取所有菜单")
    @GetMapping("/list")
    public AjaxResult<List<SysMenu>> list() {
        return AjaxResult.success(sysMenuService.listAll());
    }
    
    @ApiOperation("获取菜单树")
    @GetMapping("/tree")
    public AjaxResult<List<SysMenu>> tree() {
        return AjaxResult.success(sysMenuService.listTree());
    }

    @ApiOperation("获取用户菜单树")
    @GetMapping("/user/{userId}")
    public AjaxResult<List<SysMenu>> userMenus(@ApiParam("用户ID") @PathVariable Long userId) {
        return AjaxResult.success(sysMenuService.listUserMenus(userId));
    }

    @ApiOperation("获取菜单详情")
    @GetMapping("/{id}")
    public AjaxResult<SysMenu> get(@ApiParam("菜单ID") @PathVariable Long id) {
        return AjaxResult.success(sysMenuService.getById(id));
    }

    @ApiOperation("新增菜单")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody SysMenu menu) {
        return AjaxResult.success(sysMenuService.add(menu));
    }

    @ApiOperation("修改菜单")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody SysMenu menu) {
        return AjaxResult.success(sysMenuService.update(menu));
    }

    @ApiOperation("删除菜单")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@ApiParam("菜单ID") @PathVariable Long id) {
        return AjaxResult.success(sysMenuService.delete(id));
    }
}