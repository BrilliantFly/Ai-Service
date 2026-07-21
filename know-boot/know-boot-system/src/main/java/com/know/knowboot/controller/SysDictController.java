package com.know.knowboot.controller;

import com.know.knowboot.aop.NotLogin;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.tenant.SysDict;
import com.know.knowboot.entity.tenant.SysDictType;
import com.know.knowboot.service.ISysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "字典管理")
@RestController
@RequestMapping("/api/system/dict")
@RequiredArgsConstructor
public class SysDictController {

    private final ISysDictService sysDictService;

    // ========== 字典类型 ==========

    @ApiOperation("字典类型列表")
    @NotLogin
    @GetMapping("/type/list")
    public AjaxResult<List<SysDictType>> typeList() {
        return AjaxResult.success(sysDictService.listAll());
    }

    @ApiOperation("字典类型详情")
    @GetMapping("/type/{id}")
    public AjaxResult<SysDictType> typeGet(@PathVariable Long id) {
        return AjaxResult.success(sysDictService.getTypeById(id));
    }

    @ApiOperation("新增字典类型")
    @PostMapping("/type")
    public AjaxResult<Boolean> typeAdd(@RequestBody SysDictType dictType) {
        return AjaxResult.success(sysDictService.addType(dictType));
    }

    @ApiOperation("修改字典类型")
    @PutMapping("/type")
    public AjaxResult<Boolean> typeUpdate(@RequestBody SysDictType dictType) {
        return AjaxResult.success(sysDictService.updateType(dictType));
    }

    @ApiOperation("删除字典类型")
    @DeleteMapping("/type/{id}")
    public AjaxResult<Boolean> typeDelete(@PathVariable Long id) {
        return AjaxResult.success(sysDictService.deleteType(id));
    }

    // ========== 字典数据 ==========

    @ApiOperation("获取字典数据列表")
    @NotLogin
    @GetMapping("/list")
    public AjaxResult<List<SysDict>> list(@ApiParam("字典类型ID") @RequestParam Long dictTypeId) {
        return AjaxResult.success(sysDictService.listByTypeId(dictTypeId));
    }

    @ApiOperation("获取字典数据详情")
    @GetMapping("/{id}")
    public AjaxResult<SysDict> get(@PathVariable Long id) {
        return AjaxResult.success(sysDictService.getById(id));
    }

    @ApiOperation("新增字典数据")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody SysDict dict) {
        return AjaxResult.success(sysDictService.add(dict));
    }

    @ApiOperation("修改字典数据")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody SysDict dict) {
        return AjaxResult.success(sysDictService.update(dict));
    }

    @ApiOperation("删除字典数据")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(sysDictService.delete(id));
    }
}