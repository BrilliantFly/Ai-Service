package com.know.knowboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.auth.CameraAuthContext;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.CameraRecord;
import com.know.knowboot.service.ICameraRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 录像记录控制器
 */
@Api(tags = "录像记录管理")
@RestController
@RequestMapping("/api/camera/record")
public class CameraRecordController {

    @Autowired
    private ICameraRecordService cameraRecordService;

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public AjaxResult<IPage<CameraRecord>> page(
            CameraRecord query,
            @ApiParam("设备ID") @RequestParam(required = false) Long deviceId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(cameraRecordService.page(query, deviceId, pageNum, pageSize));
    }

    @ApiOperation("获取详情")
    @GetMapping("/{id}")
    public AjaxResult<CameraRecord> get(@PathVariable Long id) {
        return AjaxResult.success(cameraRecordService.getById(id));
    }

    @ApiOperation("开始录制")
    @PostMapping("/start")
    public AjaxResult<CameraRecord> start(
            @ApiParam("设备ID") @RequestParam Long deviceId,
            @ApiParam("录制类型") @RequestParam(required = false, defaultValue = "1") Integer recordType) {
        Long userId = CameraAuthContext.getUserId();
        return AjaxResult.success(cameraRecordService.startRecord(deviceId, userId, recordType));
    }

    @ApiOperation("停止录制")
    @PostMapping("/stop/{id}")
    public AjaxResult<Boolean> stop(@PathVariable Long id) {
        return AjaxResult.success(cameraRecordService.stopRecord(id));
    }

    @ApiOperation("删除录像")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(cameraRecordService.delete(id));
    }

    @ApiOperation("批量删除录像")
    @DeleteMapping("/batch")
    public AjaxResult<Boolean> deleteBatch(@RequestBody List<Long> ids) {
        return AjaxResult.success(cameraRecordService.deleteBatch(ids));
    }

    @ApiOperation("获取正在录制的录像")
    @GetMapping("/recording")
    public AjaxResult<CameraRecord> recording(@ApiParam("设备ID") @RequestParam Long deviceId) {
        return AjaxResult.success(cameraRecordService.getRecordingByDeviceId(deviceId));
    }
}