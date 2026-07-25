package com.know.knowboot.controller.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.knowledge.KbDocument;
import com.know.knowboot.service.knowledge.IKbDocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文档控制器
 */
@Api(tags = "知识库-文档管理")
@RestController
@RequestMapping("/api/knowledge/document")
public class KbDocumentController {

    @Autowired
    private IKbDocumentService kbDocumentService;

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public AjaxResult<IPage<KbDocument>> page(
            KbDocument query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = 1L;
        return AjaxResult.success(kbDocumentService.page(query, userId, pageNum, pageSize));
    }

    @ApiOperation("获取详情")
    @GetMapping("/{id}")
    public AjaxResult<KbDocument> get(@PathVariable Long id) {
        return AjaxResult.success(kbDocumentService.getById(id));
    }

    @ApiOperation("新增")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody KbDocument entity) {
        Long userId = 1L;
        return AjaxResult.success(kbDocumentService.add(entity, userId));
    }

    @ApiOperation("修改")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody KbDocument entity) {
        return AjaxResult.success(kbDocumentService.update(entity));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(kbDocumentService.delete(id));
    }

    @ApiOperation("获取知识库下的文档列表")
    @GetMapping("/list")
    public AjaxResult<List<KbDocument>> listByKnowledgeBase(@RequestParam Long knowledgeBaseId) {
        return AjaxResult.success(kbDocumentService.listByKnowledgeBase(knowledgeBaseId));
    }
}
