package com.know.knowboot.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.tenant.SysMenuConfig;
import com.know.knowboot.service.ISysMenuConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单配置控制器
 */
@Api(tags = "菜单配置")
@RestController
@RequestMapping("/api/system/menu/config")
public class SysMenuConfigController {

    @Autowired
    private ISysMenuConfigService sysMenuConfigService;

    /**
     * 分页查询菜单配置
     */
    @ApiOperation("分页查询菜单配置")
    @GetMapping("/page")
    public AjaxResult<IPage<SysMenuConfig>> page(
            SysMenuConfig query,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页条数") @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(sysMenuConfigService.listConfig(query, pageNum, pageSize));
    }

    /**
     * 获取菜单配置列表
     */
    @ApiOperation("获取菜单配置列表")
    @GetMapping("/list")
    public AjaxResult<List<SysMenuConfig>> list(SysMenuConfig query) {
        return AjaxResult.success(sysMenuConfigService.listConfig(query));
    }

    /**
     * 根据菜单类型获取菜单列表
     */
    @ApiOperation("根据菜单类型获取菜单列表")
    @GetMapping("/type/{menuType}")
    public AjaxResult<List<SysMenuConfig>> listByMenuType(
            @ApiParam("菜单类型 [1:tabBar, 2:首页, 3:侧边栏]") @PathVariable Integer menuType) {
        return AjaxResult.success(sysMenuConfigService.listByMenuType(menuType));
    }

    /**
     * 获取TabBar菜单列表
     */
    @ApiOperation("获取TabBar菜单列表")
    @GetMapping("/tabbar")
    public AjaxResult<List<SysMenuConfig>> tabbar() {
        return AjaxResult.success(sysMenuConfigService.listTabBar());
    }

    /**
     * 获取TabBar菜单列表（带权限）
     */
    @ApiOperation("获取TabBar菜单列表（带权限）")
    @GetMapping("/tabbar/user")
    public AjaxResult<List<SysMenuConfig>> tabbarByUser() {
        Long userId = Long.parseLong(StpUtil.getLoginId().toString());
        return AjaxResult.success(sysMenuConfigService.listTabBarByUserId(userId));
    }

    /**
     * 获取首页菜单列表
     */
    @ApiOperation("获取首页菜单列表")
    @GetMapping("/home")
    public AjaxResult<List<SysMenuConfig>> homeMenu() {
        return AjaxResult.success(sysMenuConfigService.listHomeMenu());
    }

    /**
     * 获取首页菜单列表（带权限）
     */
    @ApiOperation("获取首页菜单列表（带权限）")
    @GetMapping("/home/user")
    public AjaxResult<List<SysMenuConfig>> homeMenuByUser() {
        Long userId = Long.parseLong(StpUtil.getLoginId().toString());
        return AjaxResult.success(sysMenuConfigService.listHomeMenuByUserId(userId));
    }

    /**
     * 获取用户可用菜单列表
     */
    @ApiOperation("获取用户可用菜单列表")
    @GetMapping("/user")
    public AjaxResult<List<SysMenuConfig>> userMenu() {
        Long userId = Long.parseLong(StpUtil.getLoginId().toString());
        return AjaxResult.success(sysMenuConfigService.listByUserId(userId));
    }

    /**
     * 获取菜单详情
     */
    @ApiOperation("获取菜单配置详情")
    @GetMapping("/{id}")
    public AjaxResult<SysMenuConfig> get(
            @ApiParam("菜单ID") @PathVariable Long id) {
        return AjaxResult.success(sysMenuConfigService.getConfigById(id));
    }

    /**
     * 新增菜单配置
     */
    @ApiOperation("新增菜单配置")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody SysMenuConfig config) {
        return AjaxResult.success(sysMenuConfigService.addConfig(config));
    }

    /**
     * 修改菜单配置
     */
    @ApiOperation("修改菜单配置")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody SysMenuConfig config) {
        return AjaxResult.success(sysMenuConfigService.updateConfig(config));
    }

    /**
     * 删除菜单配置
     */
    @ApiOperation("删除菜单配置")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(
            @ApiParam("菜单ID") @PathVariable Long id) {
        return AjaxResult.success(sysMenuConfigService.deleteConfig(id));
    }
}