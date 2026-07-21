package com.know.knowboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.CameraSnapshot;
import com.know.knowboot.service.ICameraSnapshotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 截图记录控制器
 */
@Api(tags = "截图记录管理")
@RestController
@RequestMapping("/camera/snapshot")
public class CameraSnapshotController {

    @Autowired
    private ICameraSnapshotService cameraSnapshotService;

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public AjaxResult<IPage<CameraSnapshot>> page(
            CameraSnapshot query,
            @ApiParam("设备ID") @RequestParam(required = false) Long deviceId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(cameraSnapshotService.page(query, deviceId, pageNum, pageSize));
    }

    @ApiOperation("获取详情")
    @GetMapping("/{id}")
    public AjaxResult<CameraSnapshot> get(@PathVariable Long id) {
        return AjaxResult.success(cameraSnapshotService.getById(id));
    }

    @ApiOperation("获取最新截图")
    @GetMapping("/latest")
    public AjaxResult<CameraSnapshot> latest(@ApiParam("设备ID") @RequestParam Long deviceId) {
        return AjaxResult.success(cameraSnapshotService.getLatest(deviceId));
    }

    @ApiOperation("保存截图")
    @PostMapping
    public AjaxResult<CameraSnapshot> save(
            @ApiParam("设备ID") @RequestParam Long deviceId,
            @ApiParam("文件路径") @RequestParam String filePath,
            @ApiParam("缩略图路径") @RequestParam(required = false) String thumbnail) {
        Long userId = 1L;
        return AjaxResult.success(cameraSnapshotService.saveSnapshot(deviceId, userId, filePath, thumbnail));
    }

    @ApiOperation("删除截图")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(cameraSnapshotService.delete(id));
    }

    @ApiOperation("批量删除截图")
    @DeleteMapping("/batch")
    public AjaxResult<Boolean> deleteBatch(@RequestBody List<Long> ids) {
        return AjaxResult.success(cameraSnapshotService.deleteBatch(ids));
    }
}