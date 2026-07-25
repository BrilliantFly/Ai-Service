package com.know.knowboot.controller.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.knowledge.KbDocumentVersion;
import com.know.knowboot.service.knowledge.IKbDocumentVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文档版本控制器
 */
@Api(tags = "知识库-文档版本")
@RestController
@RequestMapping("/api/knowledge/version")
public class KbDocumentVersionController {

    @Autowired
    private IKbDocumentVersionService kbDocumentVersionService;

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public AjaxResult<IPage<KbDocumentVersion>> page(
            KbDocumentVersion query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = 1L;
        return AjaxResult.success(kbDocumentVersionService.page(query, userId, pageNum, pageSize));
    }

    @ApiOperation("获取文档版本列表")
    @GetMapping("/list")
    public AjaxResult<List<KbDocumentVersion>> listByDocumentId(@RequestParam Long documentId) {
        return AjaxResult.success(kbDocumentVersionService.listByDocumentId(documentId));
    }

    @ApiOperation("获取详情")
    @GetMapping("/{id}")
    public AjaxResult<KbDocumentVersion> get(@PathVariable Long id) {
        return AjaxResult.success(kbDocumentVersionService.getById(id));
    }

    @ApiOperation("创建版本快照")
    @PostMapping("/snapshot")
    public AjaxResult<Boolean> createSnapshot(@RequestParam Long documentId) {
        Long userId = 1L;
        return AjaxResult.success(kbDocumentVersionService.createSnapshot(documentId, userId));
    }

    @ApiOperation("恢复到指定版本")
    @PostMapping("/restore/{id}")
    public AjaxResult<Boolean> restore(@PathVariable Long id) {
        Long userId = 1L;
        return AjaxResult.success(kbDocumentVersionService.restore(id, userId));
    }

    @ApiOperation("删除版本")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(kbDocumentVersionService.delete(id));
    }
}
