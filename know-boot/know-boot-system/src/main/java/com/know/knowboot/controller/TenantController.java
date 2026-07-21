package com.know.knowboot.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.context.TenantContextHolder;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.tenant.SysRole;
import com.know.knowboot.entity.tenant.SysTenant;
import com.know.knowboot.entity.tenant.SysTenantRole;
import com.know.knowboot.entity.tenant.SysTenantUser;
import com.know.knowboot.dto.SysTenantRoleAssignDTO;
import com.know.knowboot.dto.SysTenantUserAssignDTO;
import com.know.knowboot.mapper.tenant.SysTenantMapper;
import com.know.knowboot.mapper.tenant.SysTenantRoleMapper;
import com.know.knowboot.mapper.tenant.SysTenantUserMapper;
import com.know.knowboot.service.ISysTenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 租户管理控制器
 */
@Api(tags = "租户管理")
@RestController
@RequestMapping("/tenant")
public class TenantController {

    @Autowired
    private ISysTenantService sysTenantService;

    @Autowired
    private SysTenantMapper sysTenantMapper;

    @Autowired
    private SysTenantUserMapper sysTenantUserMapper;

    @Autowired
    private SysTenantRoleMapper sysTenantRoleMapper;

    /**
     * 分页查询租户
     */
    @ApiOperation("分页查询租户")
    @GetMapping("/page")
    public AjaxResult<IPage<SysTenant>> page(
            SysTenant query,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页条数") @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(sysTenantService.page(query, pageNum, pageSize));
    }

    /**
     * 获取租户列表（公开）
     */
    @ApiOperation("获取所有租户列表")
    @GetMapping("/list")
    public AjaxResult<List<SysTenant>> list() {
        return AjaxResult.success(sysTenantService.list());
    }

    /**
     * 获取用户可访问的租户列表
     */
    @ApiOperation("获取用户可访问的租户列表")
    @GetMapping("/user/list")
    public AjaxResult<List<SysTenant>> userTenantList() {
        Long userId = Long.parseLong(StpUtil.getLoginId().toString());
        return AjaxResult.success(sysTenantService.listByUserId(userId));
    }

    /**
     * 获取用户当前租户
     */
    @ApiOperation("获取用户当前租户")
    @GetMapping("/current")
    public AjaxResult<SysTenant> currentTenant() {
        Long userId = Long.parseLong(StpUtil.getLoginId().toString());
        return AjaxResult.success(sysTenantService.getUserCurrentTenant(userId));
    }

    /**
     * 切换租户
     */
    @ApiOperation("切换当前租户")
    @PostMapping("/switch")
    public AjaxResult<Boolean> switchTenant(@ApiParam("租户ID") @RequestParam Long tenantId) {
        Long userId = Long.parseLong(StpUtil.getLoginId().toString());
        return AjaxResult.success(sysTenantService.setUserCurrentTenant(userId, tenantId));
    }

    /**
     * 获取租户详情
     */
    @ApiOperation("获取租户详情")
    @GetMapping("/{id}")
    public AjaxResult<SysTenant> get(@ApiParam("租户ID") @PathVariable Long id) {
        return AjaxResult.success(sysTenantService.getById(id));
    }

    /**
     * 新增租户
     */
    @ApiOperation("新增租户")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody SysTenant tenant) {
        return AjaxResult.success(sysTenantService.add(tenant));
    }

    /**
     * 修改租户
     */
    @ApiOperation("修改租户")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody SysTenant tenant) {
        return AjaxResult.success(sysTenantService.update(tenant));
    }

    /**
     * 删除租户
     */
    @ApiOperation("删除租户")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@ApiParam("租户ID") @PathVariable Long id) {
        return AjaxResult.success(sysTenantService.delete(id));
    }

    /**
     * 授权用户到租户
     */
    @ApiOperation("授权用户到租户")
    @PostMapping("/assign")
    public AjaxResult<Boolean> assignUser(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("租户ID") @RequestParam Long tenantId,
            @ApiParam("是否租户管理员") @RequestParam(defaultValue = "false") Boolean isAdmin) {
        return AjaxResult.success(sysTenantService.assignUserToTenant(userId, tenantId, isAdmin));
    }

    /**
     * 从租户移除用户
     */
    @ApiOperation("从租户移除用户")
    @PostMapping("/remove")
    public AjaxResult<Boolean> removeUser(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        return AjaxResult.success(sysTenantService.removeUserFromTenant(userId, tenantId));
    }

    /**
     * 获取租户关联的角色
     */
    @ApiOperation("获取租户关联的角色")
    @GetMapping("/{tenantId}/roles")
    public AjaxResult<List<Long>> getTenantRoles(@ApiParam("租户ID") @PathVariable Long tenantId) {
        List<SysTenantRole> list = sysTenantRoleMapper.selectByTenantId(tenantId);
        List<Long> roleIds = new ArrayList<>();
        for (SysTenantRole tr : list) {
            roleIds.add(tr.getRoleId());
        }
        return AjaxResult.success(roleIds);
    }

    /**
     * 分配角色给租户
     */
    @ApiOperation("分配角色给租户")
    @PostMapping("/assignRoles")
    public AjaxResult<Boolean> assignRoles(@RequestBody SysTenantRoleAssignDTO dto) {
        Long tenantId = dto.getTenantId();
        List<Long> roleIds = dto.getRoleIds();
        // 先删除原有关联
        sysTenantRoleMapper.deleteByTenantId(tenantId);
        // 再添加新关联
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                SysTenantRole tr = new SysTenantRole();
                tr.setTenantId(tenantId);
                tr.setRoleId(roleId);
                sysTenantRoleMapper.insert(tr);
            }
        }
        return AjaxResult.success(true);
    }

    /**
     * 获取租户关联的用户列表
     */
    @ApiOperation("获取租户关联的用户列表")
    @GetMapping("/{tenantId}/users")
    public AjaxResult<List<Long>> getTenantUsers(@ApiParam("租户ID") @PathVariable Long tenantId) {
        List<SysTenantUser> list = sysTenantUserMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysTenantUser>()
                        .eq(SysTenantUser::getTenantId, tenantId));
        List<Long> userIds = new ArrayList<>();
        for (SysTenantUser tu : list) {
            userIds.add(tu.getUserId());
        }
        return AjaxResult.success(userIds);
    }

    /**
     * 分配用户到租户
     */
    @ApiOperation("分配用户到租户")
    @PostMapping("/assignUsers")
    public AjaxResult<Boolean> assignUsers(@RequestBody SysTenantUserAssignDTO dto) {
        Long tenantId = dto.getTenantId();
        List<Long> userIds = dto.getUserIds();
        Boolean isAdmin = dto.getIsAdmin() != null && dto.getIsAdmin();
        // 先删除原有关联
        sysTenantUserMapper.delete(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysTenantUser>()
                        .eq(SysTenantUser::getTenantId, tenantId));
        // 再添加新关联
        if (userIds != null && !userIds.isEmpty()) {
            for (Long userId : userIds) {
                SysTenantUser tu = new SysTenantUser();
                tu.setTenantId(tenantId);
                tu.setUserId(userId);
                tu.setIsAdmin(isAdmin);
                sysTenantUserMapper.insert(tu);
            }
        }
        return AjaxResult.success(true);
    }
}