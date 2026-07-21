package com.know.knowboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.tenant.SysRole;
import com.know.knowboot.service.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/system/role")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    @ApiOperation("分页查询角色")
    @GetMapping("/page")
    public AjaxResult<IPage<SysRole>> page(
            SysRole query,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页条数") @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(sysRoleService.page(query, pageNum, pageSize));
    }

    @ApiOperation("获取角色列表")
    @GetMapping("/list")
    public AjaxResult<List<SysRole>> list() {
        return AjaxResult.success(sysRoleService.listNormal());
    }

    @ApiOperation("获取角色详情")
    @GetMapping("/{id}")
    public AjaxResult<SysRole> get(@ApiParam("角色ID") @PathVariable Long id) {
        return AjaxResult.success(sysRoleService.getById(id));
    }

    @ApiOperation("新增角色")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody SysRole role) {
        return AjaxResult.success(sysRoleService.add(role));
    }

    @ApiOperation("修改角色")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody SysRole role) {
        return AjaxResult.success(sysRoleService.update(role));
    }

    @ApiOperation("删除角色")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@ApiParam("角色ID") @PathVariable Long id) {
        return AjaxResult.success(sysRoleService.delete(id));
    }

    @ApiOperation("获取角色关联的菜单ID")
    @GetMapping("/{id}/menus")
    public AjaxResult<List<Long>> getMenuIds(@ApiParam("角色ID") @PathVariable Long id) {
        return AjaxResult.success(sysRoleService.getMenuIds(id));
    }

    @ApiOperation("分配菜单权限")
    @PutMapping("/{id}/menus")
    public AjaxResult<Boolean> assignMenus(
            @ApiParam("角色ID") @PathVariable Long id,
            @RequestBody List<Long> menuIds) {
        return AjaxResult.success(sysRoleService.assignMenus(id, menuIds));
    }
}