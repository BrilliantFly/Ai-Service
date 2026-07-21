package com.know.knowboot.controller;

import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.dto.RoleDataScopeDTO;
import com.know.knowboot.entity.tenant.SysRoleDataScope;
import com.know.knowboot.enums.ErrorEnum;
import com.know.knowboot.service.ISysRoleDataScopeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "角色数据权限管理")
@RestController
@RequestMapping("/system/role/data-scope")
@RequiredArgsConstructor
public class SysRoleDataScopeController {

    private final ISysRoleDataScopeService dataScopeService;

    @ApiOperation("获取角色数据权限配置")
    @GetMapping("/{roleId}")
    public AjaxResult<List<SysRoleDataScope>> getByRoleId(@PathVariable Long roleId) {
        return AjaxResult.success(dataScopeService.getByRoleId(roleId));
    }

    @ApiOperation("获取角色数据权限类型")
    @GetMapping("/type/{roleId}")
    public AjaxResult<Integer> getDataScopeType(@PathVariable Long roleId) {
        Integer type = dataScopeService.getDataScopeTypeByRoleId(roleId);
        return new AjaxResult<>(ErrorEnum.SUCCESS.getCode(), ErrorEnum.SUCCESS.getMsg(), type, ErrorEnum.HIDE_MSG.getCode());
    }

    @ApiOperation("保存角色数据权限配置")
    @PostMapping
    public AjaxResult<Boolean> saveConfig(@RequestBody RoleDataScopeDTO dto) {
        return AjaxResult.success(dataScopeService.saveConfig(dto.getRoleId(), dto.getDataScopeType(), dto.getCustomDeptIds()));
    }

    @ApiOperation("删除角色数据权限配置")
    @DeleteMapping("/{roleId}")
    public AjaxResult<Boolean> deleteByRoleId(@PathVariable Long roleId) {
        return AjaxResult.success(dataScopeService.deleteByRoleId(roleId));
    }
}