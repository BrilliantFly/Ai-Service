package com.know.knowboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.auth.CameraAuthContext;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.CameraDevice;
import com.know.knowboot.entity.CameraDiscoveredDevice;
import com.know.knowboot.service.ICameraDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 摄像头设备控制器
 */
@Api(tags = "摄像头设备管理")
@RestController
@RequestMapping("/api/camera/device")
public class CameraDeviceController {

    @Autowired
    private ICameraDeviceService cameraDeviceService;

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public AjaxResult<IPage<CameraDevice>> page(
            CameraDevice query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = CameraAuthContext.getUserId();
        return AjaxResult.success(cameraDeviceService.page(query, userId, pageNum, pageSize));
    }

    @ApiOperation("获取所有设备")
    @GetMapping("/list")
    public AjaxResult<List<CameraDevice>> list() {
        Long userId = CameraAuthContext.getUserId();
        return AjaxResult.success(cameraDeviceService.listByUserId(userId));
    }

    @ApiOperation("获取收藏设备")
    @GetMapping("/favorites")
    public AjaxResult<List<CameraDevice>> favorites() {
        Long userId = CameraAuthContext.getUserId();
        return AjaxResult.success(cameraDeviceService.listFavorites(userId));
    }

    @ApiOperation("局域网发现设备")
    @GetMapping("/discover")
    public AjaxResult<List<CameraDiscoveredDevice>> discover() {
        Long userId = CameraAuthContext.getUserId();
        return AjaxResult.success(cameraDeviceService.discover(userId));
    }

    @ApiOperation("获取详情")
    @GetMapping("/{id}")
    public AjaxResult<CameraDevice> get(@PathVariable Long id) {
        return AjaxResult.success(cameraDeviceService.getById(id));
    }

    @ApiOperation("新增设备")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody CameraDevice device) {
        Long userId = CameraAuthContext.getUserId();
        return AjaxResult.success(cameraDeviceService.add(device, userId));
    }

    @ApiOperation("修改设备")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody CameraDevice device) {
        return AjaxResult.success(cameraDeviceService.update(device));
    }

    @ApiOperation("删除设备")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(cameraDeviceService.delete(id));
    }

    @ApiOperation("检查设备编号是否存在")
    @GetMapping("/check")
    public AjaxResult<Boolean> check(
            @ApiParam("设备编号") @RequestParam String deviceCode,
            @ApiParam("排除ID") @RequestParam(required = false) Long excludeId) {
        return AjaxResult.success(cameraDeviceService.existsByDeviceCode(deviceCode, excludeId));
    }

    @ApiOperation("更新设备状态")
    @PutMapping("/status/{id}")
    public AjaxResult<Boolean> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        return AjaxResult.success(cameraDeviceService.updateStatus(id, status));
    }
}