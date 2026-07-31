package com.know.knowboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.tenant.SysRolePermission;
import com.know.knowboot.service.ISysRolePermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色-权限关联控制器
 */
@Api(tags = "角色权限管理")
@RestController
@RequestMapping("/api/system/rolePermission")
public class SysRolePermissionController {

    @Autowired
    private ISysRolePermissionService sysRolePermissionService;

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public AjaxResult<IPage<SysRolePermission>> page(
            SysRolePermission query,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页条数") @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(sysRolePermissionService.page(query, pageNum, pageSize));
    }

    @ApiOperation("获取列表")
    @GetMapping("/list")
    public AjaxResult<List<SysRolePermission>> list() {
        return AjaxResult.success(sysRolePermissionService.listAll());
    }

    @ApiOperation("根据角色获取权限ID列表")
    @GetMapping("/role/{roleId}")
    public AjaxResult<List<Long>> listPermissionIdsByRoleId(@ApiParam("角色ID") @PathVariable Long roleId) {
        return AjaxResult.success(sysRolePermissionService.listPermissionIdsByRoleId(roleId));
    }

    @ApiOperation("分配权限")
    @PostMapping("/assign")
    public AjaxResult<Boolean> assignPermissions(
            @ApiParam("角色ID") @RequestParam Long roleId,
            @ApiParam("权限ID列表") @RequestBody List<Long> permissionIds) {
        return AjaxResult.success(sysRolePermissionService.assignPermissions(roleId, permissionIds));
    }

    @ApiOperation("新增关联")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody SysRolePermission rolePermission) {
        return AjaxResult.success(sysRolePermissionService.add(rolePermission));
    }

    @ApiOperation("删除关联")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@ApiParam("ID") @PathVariable Long id) {
        return AjaxResult.success(sysRolePermissionService.delete(id));
    }

    @ApiOperation("根据角色删除关联")
    @DeleteMapping("/role/{roleId}")
    public AjaxResult<Boolean> deleteByRoleId(@ApiParam("角色ID") @PathVariable Long roleId) {
        return AjaxResult.success(sysRolePermissionService.deleteByRoleId(roleId));
    }
}