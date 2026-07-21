package com.know.knowboot.controller;

import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.CameraFavorite;
import com.know.knowboot.service.ICameraFavoriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备收藏控制器
 */
@Api(tags = "设备收藏管理")
@RestController
@RequestMapping("/camera/favorite")
public class CameraFavoriteController {

    @Autowired
    private ICameraFavoriteService cameraFavoriteService;

    @ApiOperation("获取收藏列表")
    @GetMapping("/list")
    public AjaxResult<List<CameraFavorite>> list(@ApiParam("用户ID") @RequestParam(defaultValue = "1") Long userId) {
        return AjaxResult.success(cameraFavoriteService.listByUser(userId));
    }

    @ApiOperation("检查是否已收藏")
    @GetMapping("/check")
    public AjaxResult<Boolean> check(
            @ApiParam("设备ID") @RequestParam Long deviceId,
            @ApiParam("用户ID") @RequestParam(defaultValue = "1") Long userId) {
        return AjaxResult.success(cameraFavoriteService.isFavorited(deviceId, userId));
    }

    @ApiOperation("添加收藏")
    @PostMapping
    public AjaxResult<CameraFavorite> add(
            @ApiParam("设备ID") @RequestParam Long deviceId,
            @ApiParam("用户ID") @RequestParam(defaultValue = "1") Long userId) {
        return AjaxResult.success(cameraFavoriteService.add(deviceId, userId));
    }

    @ApiOperation("取消收藏")
    @DeleteMapping
    public AjaxResult<Boolean> remove(
            @ApiParam("设备ID") @RequestParam Long deviceId,
            @ApiParam("用户ID") @RequestParam(defaultValue = "1") Long userId) {
        return AjaxResult.success(cameraFavoriteService.remove(deviceId, userId));
    }

    @ApiOperation("切换收藏状态")
    @PostMapping("/toggle")
    public AjaxResult<Boolean> toggle(
            @ApiParam("设备ID") @RequestParam Long deviceId,
            @ApiParam("用户ID") @RequestParam(defaultValue = "1") Long userId) {
        return AjaxResult.success(cameraFavoriteService.toggle(deviceId, userId));
    }
}
