package com.know.knowboot.controller.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.knowledge.KbComment;
import com.know.knowboot.service.knowledge.IKbCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论控制器
 */
@Api(tags = "知识库-评论管理")
@RestController
@RequestMapping("/api/knowledge/comment")
public class KbCommentController {

    @Autowired
    private IKbCommentService kbCommentService;

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public AjaxResult<IPage<KbComment>> page(
            KbComment query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = 1L;
        return AjaxResult.success(kbCommentService.page(query, userId, pageNum, pageSize));
    }

    @ApiOperation("获取文档评论列表")
    @GetMapping("/list")
    public AjaxResult<List<KbComment>> listByDocumentId(@RequestParam Long documentId) {
        return AjaxResult.success(kbCommentService.listByDocumentId(documentId));
    }

    @ApiOperation("获取详情")
    @GetMapping("/{id}")
    public AjaxResult<KbComment> get(@PathVariable Long id) {
        return AjaxResult.success(kbCommentService.getById(id));
    }

    @ApiOperation("新增评论")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody KbComment entity) {
        Long userId = 1L;
        return AjaxResult.success(kbCommentService.add(entity, userId));
    }

    @ApiOperation("修改评论")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody KbComment entity) {
        return AjaxResult.success(kbCommentService.update(entity));
    }

    @ApiOperation("删除评论")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(kbCommentService.delete(id));
    }
}
