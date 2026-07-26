package com.know.knowboot.controller.knowledge;

import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.knowledge.KbDocumentShare;
import com.know.knowboot.service.knowledge.IKbDocumentShareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 文档分享控制器
 */
@Api(tags = "知识库-文档分享")
@RestController
@RequestMapping("/api/knowledge/share")
public class KbDocumentShareController {

    @Autowired
    private IKbDocumentShareService kbDocumentShareService;

    @ApiOperation("创建分享链接")
    @PostMapping
    public AjaxResult<KbDocumentShare> create(
            @RequestParam Long documentId,
            @RequestParam(required = false) Long expireDays,
            @RequestParam(required = false) String password) {
        Long userId = 1L;
        return AjaxResult.success(kbDocumentShareService.create(documentId, userId, expireDays, password));
    }

    @ApiOperation("通过令牌获取分享信息")
    @GetMapping("/info")
    @SuppressWarnings("unchecked")
    public AjaxResult<Map<String, Object>> getByToken(@RequestParam String token) {
        Map<String, Object> result = kbDocumentShareService.getByToken(token);
        if (result == null) {
            return (AjaxResult<Map<String, Object>>) (AjaxResult<?>) AjaxResult.failed("分享链接不存在或已过期");
        }
        return AjaxResult.success(result);
    }

    @ApiOperation("删除分享")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(kbDocumentShareService.delete(id));
    }
}
