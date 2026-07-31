package com.know.knowboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.dto.SysUserTenantAssignDTO;
import com.know.knowboot.dto.SysUserRoleAssignDTO;
import com.know.knowboot.dto.SysUserDeptAssignDTO;
import com.know.knowboot.dto.SysUserJobAssignDTO;
import com.know.knowboot.entity.tenant.SysTenantUser;
import com.know.knowboot.entity.tenant.SysUser;
import com.know.knowboot.entity.tenant.SysUserRole;
import com.know.knowboot.mapper.tenant.SysTenantUserMapper;
import com.know.knowboot.mapper.tenant.SysUserRoleMapper;
import com.know.knowboot.service.ISysUserService;
import com.know.knowboot.service.tenant.ISysUserDeptService;
import com.know.knowboot.service.tenant.ISysUserJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理控制器
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/system/user")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private SysTenantUserMapper sysTenantUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private ISysUserDeptService sysUserDeptService;

    @Autowired
    private ISysUserJobService sysUserJobService;

    @ApiOperation("分页查询用户")
    @GetMapping("/page")
    public AjaxResult<IPage<SysUser>> page(
            SysUser query,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页条数") @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(sysUserService.page(query, pageNum, pageSize));
    }

    @ApiOperation("获取用户列表")
    @GetMapping("/list")
    public AjaxResult<IPage<SysUser>> list(
            SysUser query,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页条数") @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(sysUserService.page(query, pageNum, pageSize));
    }

    @ApiOperation("获取用户详情")
    @GetMapping("/{id}")
    public AjaxResult<SysUser> get(@ApiParam("用户ID") @PathVariable Long id) {
        return AjaxResult.success(sysUserService.getById(id));
    }

    @ApiOperation("新增用户")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody SysUser user) {
        return AjaxResult.success(sysUserService.add(user));
    }

    @ApiOperation("修改用户")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody SysUser user) {
        return AjaxResult.success(sysUserService.update(user));
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@ApiParam("用户ID") @PathVariable Long id) {
        return AjaxResult.success(sysUserService.delete(id));
    }

    @ApiOperation("重置密码")
    @PutMapping("/{id}/resetPassword")
    public AjaxResult<Boolean> resetPassword(@ApiParam("用户ID") @PathVariable Long id) {
        return AjaxResult.success(sysUserService.resetPassword(id));
    }

    /**
     * 获取用户关联的租户列表
     */
    @ApiOperation("获取用户关联的租户列表")
    @GetMapping("/{userId}/tenants")
    public AjaxResult<List<Long>> getUserTenants(@ApiParam("用户ID") @PathVariable Long userId) {
        List<SysTenantUser> list = sysTenantUserMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysTenantUser>()
                        .eq(SysTenantUser::getUserId, userId));
        List<Long> tenantIds = new ArrayList<>();
        for (SysTenantUser tu : list) {
            tenantIds.add(tu.getTenantId());
        }
        return AjaxResult.success(tenantIds);
    }

    /**
     * 分配租户给用户
     */
    @ApiOperation("分配租户给用户")
    @PostMapping("/assignTenants")
    public AjaxResult<Boolean> assignTenants(@RequestBody SysUserTenantAssignDTO dto) {
        Long userId = dto.getUserId();
        List<Long> tenantIds = dto.getTenantIds();
        // 先删除原有关联
        sysTenantUserMapper.delete(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysTenantUser>()
                        .eq(SysTenantUser::getUserId, userId));
        // 再添加新关联
        if (tenantIds != null && !tenantIds.isEmpty()) {
            for (Long tenantId : tenantIds) {
                SysTenantUser tu = new SysTenantUser();
                tu.setTenantId(tenantId);
                tu.setUserId(userId);
                tu.setIsAdmin(false);
                sysTenantUserMapper.insert(tu);
            }
        }
        return AjaxResult.success(true);
    }
    
    /**
     * 获取用户关联的角色列表
     */
    @ApiOperation("获取用户关联的角色列表")
    @GetMapping("/{userId}/roles")
    public AjaxResult<List<Long>> getUserRoles(@ApiParam("用户ID") @PathVariable Long userId) {
        List<Long> roleIds = sysUserRoleMapper.selectRoleIdsByUserId(userId);
        return AjaxResult.success(roleIds);
    }
    
    /**
     * 分配角色给用户
     */
    @ApiOperation("分配角色给用户")
    @PostMapping("/assignRoles")
    public AjaxResult<Boolean> assignRoles(@RequestBody SysUserRoleAssignDTO dto) {
        Long userId = dto.getUserId();
        List<Long> roleIds = dto.getRoleIds();
        // 先删除原有关联
        int deleted = sysUserRoleMapper.delete(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId));
        System.out.println("删除了 " + deleted + " 条记录");
        // 再添加新关联 - 逐条插入
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                try {
                    sysUserRoleMapper.insert(ur);
                } catch (Exception e) {
                    // 忽略已存在的关联
                }
            }
        }
        return AjaxResult.success(true);
    }

    // ==================== 用户-部门-岗位管理 ====================

    /**
     * 获取用户关联的部门列表
     */
    @ApiOperation("获取用户关联的部门列表")
    @GetMapping("/{userId}/depts")
    public AjaxResult<List<Long>> getUserDepts(@ApiParam("用户ID") @PathVariable Long userId) {
        List<Long> deptIds = sysUserDeptService.selectDeptIdsByUserId(userId);
        return AjaxResult.success(deptIds);
    }

    /**
     * 分配部门给用户
     */
    @ApiOperation("分配部门给用户")
    @PostMapping("/assignDepts")
    public AjaxResult<Boolean> assignDepts(@RequestBody SysUserDeptAssignDTO dto) {
        sysUserDeptService.assignDepts(dto.getUserId(), dto.getDeptIds());
        return AjaxResult.success(true);
    }

    /**
     * 获取用户关联的岗位列表
     */
    @ApiOperation("获取用户关联的岗位列表")
    @GetMapping("/{userId}/jobs")
    public AjaxResult<List<Long>> getUserJobs(@ApiParam("用户ID") @PathVariable Long userId) {
        List<Long> jobIds = sysUserJobService.selectJobIdsByUserId(userId);
        return AjaxResult.success(jobIds);
    }

    /**
     * 分配岗位给用户
     */
    @ApiOperation("分配岗位给用户")
    @PostMapping("/assignJobs")
    public AjaxResult<Boolean> assignJobs(@RequestBody SysUserJobAssignDTO dto) {
        sysUserJobService.assignJobs(dto.getUserId(), dto.getJobIds());
        return AjaxResult.success(true);
    }
}