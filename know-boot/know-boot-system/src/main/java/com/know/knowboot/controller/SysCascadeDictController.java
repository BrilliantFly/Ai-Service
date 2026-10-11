package com.know.knowboot.controller;

import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.tenant.SysCascadeDictData;
import com.know.knowboot.entity.tenant.SysCascadeDictType;
import com.know.knowboot.service.ISysCascadeDictService;
import com.know.knowboot.vo.SysCascadeDictNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 级联字典管理
 */
@Api(tags = "级联字典管理")
@RestController
@RequestMapping("/api/system/cascade-dict")
@RequiredArgsConstructor
public class SysCascadeDictController {

    private final ISysCascadeDictService cascadeDictService;

    // ========== 级联字典类型 ==========

    @ApiOperation("级联字典类型列表")
    @GetMapping("/type/list")
    public AjaxResult<List<SysCascadeDictType>> typeList(
            @ApiParam("类型名称") @RequestParam(required = false) String typeName,
            @ApiParam("类型编码") @RequestParam(required = false) String typeCode,
            @ApiParam("状态") @RequestParam(required = false) Integer status) {
        return AjaxResult.success(cascadeDictService.listTypes(typeName, typeCode, status));
    }

    @ApiOperation("级联字典类型详情")
    @GetMapping("/type/{id}")
    public AjaxResult<SysCascadeDictType> typeGet(@PathVariable Long id) {
        return AjaxResult.success(cascadeDictService.getTypeById(id));
    }

    @ApiOperation("新增级联字典类型")
    @PostMapping("/type")
    public AjaxResult<Boolean> typeAdd(@RequestBody SysCascadeDictType type) {
        return AjaxResult.success(cascadeDictService.addType(type));
    }

    @ApiOperation("修改级联字典类型")
    @PutMapping("/type")
    public AjaxResult<Boolean> typeUpdate(@RequestBody SysCascadeDictType type) {
        return AjaxResult.success(cascadeDictService.updateType(type));
    }

    @ApiOperation("删除级联字典类型")
    @DeleteMapping("/type/{id}")
    public AjaxResult<Boolean> typeDelete(@PathVariable Long id) {
        return AjaxResult.success(cascadeDictService.deleteType(id));
    }

    // ========== 级联字典数据 ==========

    @ApiOperation("级联字典树")
    @GetMapping("/data/tree")
    public AjaxResult<List<SysCascadeDictNode>> dataTree(
            @ApiParam("类型编码") @RequestParam(required = false) String typeCode,
            @ApiParam("类型ID") @RequestParam(required = false) Long typeId,
            @ApiParam("状态(不传=全部)") @RequestParam(required = false) Integer status) {
        return AjaxResult.success(cascadeDictService.tree(typeCode, typeId, status));
    }

    @ApiOperation("级联字典数据平铺列表")
    @GetMapping("/data/list")
    public AjaxResult<List<SysCascadeDictData>> dataList(@ApiParam("类型ID") @RequestParam Long typeId) {
        return AjaxResult.success(cascadeDictService.listData(typeId));
    }

    @ApiOperation("新增级联字典数据")
    @PostMapping("/data")
    public AjaxResult<Boolean> dataAdd(@RequestBody SysCascadeDictData data) {
        return AjaxResult.success(cascadeDictService.addData(data));
    }

    @ApiOperation("修改级联字典数据")
    @PutMapping("/data")
    public AjaxResult<Boolean> dataUpdate(@RequestBody SysCascadeDictData data) {
        return AjaxResult.success(cascadeDictService.updateData(data));
    }

    @ApiOperation("删除级联字典数据")
    @DeleteMapping("/data/{id}")
    public AjaxResult<Boolean> dataDelete(@PathVariable Long id) {
        return AjaxResult.success(cascadeDictService.deleteData(id));
    }
}
