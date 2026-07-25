package com.know.knowboot.controller.knowledge;

import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.service.knowledge.IKbSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 搜索控制器
 */
@Api(tags = "知识库-搜索")
@RestController
@RequestMapping("/api/knowledge/search")
public class KbSearchController {

    @Autowired
    private IKbSearchService kbSearchService;

    @ApiOperation("全文搜索")
    @GetMapping
    public AjaxResult<Map<String, Object>> search(@RequestParam String q) {
        Long userId = 1L;
        return AjaxResult.success(kbSearchService.search(q, userId));
    }

    @ApiOperation("获取搜索历史")
    @GetMapping("/history")
    public AjaxResult<?> history(@RequestParam(defaultValue = "20") Integer limit) {
        Long userId = 1L;
        return AjaxResult.success(kbSearchService.getHistory(userId, limit));
    }

    @ApiOperation("清空搜索历史")
    @DeleteMapping("/history")
    public AjaxResult<Boolean> clearHistory() {
        Long userId = 1L;
        return AjaxResult.success(kbSearchService.clearHistory(userId));
    }
}
