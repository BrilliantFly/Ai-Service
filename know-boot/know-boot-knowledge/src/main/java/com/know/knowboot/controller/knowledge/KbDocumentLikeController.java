package com.know.knowboot.controller.knowledge;

import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.service.knowledge.IKbDocumentLikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 文档点赞控制器
 */
@Api(tags = "知识库-文档点赞")
@RestController
@RequestMapping("/api/knowledge/like")
public class KbDocumentLikeController {

    @Autowired
    private IKbDocumentLikeService kbDocumentLikeService;

    @ApiOperation("切换点赞状态")
    @PostMapping("/toggle")
    public AjaxResult<Boolean> toggle(@RequestParam Long documentId) {
        Long userId = 1L;
        return AjaxResult.success(kbDocumentLikeService.toggle(documentId, userId));
    }

    @ApiOperation("检查是否已点赞")
    @GetMapping("/status")
    public AjaxResult<Map<String, Object>> status(@RequestParam Long documentId) {
        Long userId = 1L;
        Map<String, Object> result = new HashMap<>();
        result.put("liked", kbDocumentLikeService.isLiked(documentId, userId));
        result.put("count", kbDocumentLikeService.countByDocumentId(documentId));
        return AjaxResult.success(result);
    }
}
