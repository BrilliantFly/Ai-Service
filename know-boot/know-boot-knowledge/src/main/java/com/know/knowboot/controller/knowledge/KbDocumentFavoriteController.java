package com.know.knowboot.controller.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.knowledge.KbDocumentFavorite;
import com.know.knowboot.service.knowledge.IKbDocumentFavoriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 文档收藏控制器
 */
@Api(tags = "知识库-文档收藏")
@RestController
@RequestMapping("/api/knowledge/favorite")
public class KbDocumentFavoriteController {

    @Autowired
    private IKbDocumentFavoriteService kbDocumentFavoriteService;

    @ApiOperation("切换收藏状态")
    @PostMapping("/toggle")
    public AjaxResult<Boolean> toggle(@RequestParam Long documentId) {
        Long userId = 1L;
        return AjaxResult.success(kbDocumentFavoriteService.toggle(documentId, userId));
    }

    @ApiOperation("检查是否已收藏")
    @GetMapping("/status")
    public AjaxResult<Map<String, Object>> status(@RequestParam Long documentId) {
        Long userId = 1L;
        Map<String, Object> result = new HashMap<>();
        result.put("favorited", kbDocumentFavoriteService.isFavorited(documentId, userId));
        return AjaxResult.success(result);
    }

    @ApiOperation("收藏列表")
    @GetMapping("/list")
    public AjaxResult<IPage<KbDocumentFavorite>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = 1L;
        return AjaxResult.success(kbDocumentFavoriteService.listByUserId(userId, pageNum, pageSize));
    }
}
