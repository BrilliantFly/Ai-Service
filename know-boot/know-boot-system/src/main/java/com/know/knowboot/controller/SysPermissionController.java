package com.know.knowboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.tenant.SysPermission;
import com.know.knowboot.service.ISysPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理控制器
 */
@Api(tags = "权限管理")
@RestController
@RequestMapping("/system/permission")
public class SysPermissionController {

    @Autowired
    private ISysPermissionService sysPermissionService;

    @ApiOperation("分页查询权限")
    @GetMapping("/page")
    public AjaxResult<IPage<SysPermission>> page(
            SysPermission query,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页条数") @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(sysPermissionService.page(query, pageNum, pageSize));
    }

    @ApiOperation("获取权限列表")
    @GetMapping("/list")
    public AjaxResult<List<SysPermission>> list() {
        return AjaxResult.success(sysPermissionService.listAll());
    }

    @ApiOperation("获取权限树")
    @GetMapping("/tree")
    public AjaxResult<List<SysPermission>> tree() {
        return AjaxResult.success(sysPermissionService.listTree());
    }

    @ApiOperation("获取权限详情")
    @GetMapping("/{id}")
    public AjaxResult<SysPermission> get(@ApiParam("权限ID") @PathVariable Long id) {
        return AjaxResult.success(sysPermissionService.getById(id));
    }

    @ApiOperation("新增权限")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody SysPermission permission) {
        return AjaxResult.success(sysPermissionService.add(permission));
    }

    @ApiOperation("修改权限")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody SysPermission permission) {
        return AjaxResult.success(sysPermissionService.update(permission));
    }

    @ApiOperation("删除权限")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@ApiParam("权限ID") @PathVariable Long id) {
        return AjaxResult.success(sysPermissionService.delete(id));
    }

    @ApiOperation("根据角色获取权限")
    @GetMapping("/role/{roleId}")
    public AjaxResult<List<SysPermission>> listByRoleId(@ApiParam("角色ID") @PathVariable Long roleId) {
        return AjaxResult.success(sysPermissionService.listByRoleId(roleId));
    }

    @ApiOperation("根据用户获取权限编码")
    @GetMapping("/user/{userId}")
    public AjaxResult<List<String>> listCodesByUserId(@ApiParam("用户ID") @PathVariable Long userId) {
        return AjaxResult.success(sysPermissionService.listCodesByUserId(userId));
    }
}