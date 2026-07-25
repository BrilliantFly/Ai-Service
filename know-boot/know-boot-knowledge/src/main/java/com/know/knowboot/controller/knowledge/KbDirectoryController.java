package com.know.knowboot.controller.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.knowledge.KbDirectory;
import com.know.knowboot.service.knowledge.IKbDirectoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 目录控制器
 */
@Api(tags = "知识库-目录管理")
@RestController
@RequestMapping("/api/knowledge/directory")
public class KbDirectoryController {

    @Autowired
    private IKbDirectoryService kbDirectoryService;

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public AjaxResult<IPage<KbDirectory>> page(
            KbDirectory query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = 1L;
        return AjaxResult.success(kbDirectoryService.page(query, userId, pageNum, pageSize));
    }

    @ApiOperation("获取详情")
    @GetMapping("/{id}")
    public AjaxResult<KbDirectory> get(@PathVariable Long id) {
        return AjaxResult.success(kbDirectoryService.getById(id));
    }

    @ApiOperation("新增")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody KbDirectory entity) {
        Long userId = 1L;
        return AjaxResult.success(kbDirectoryService.add(entity, userId));
    }

    @ApiOperation("修改")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody KbDirectory entity) {
        return AjaxResult.success(kbDirectoryService.update(entity));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(kbDirectoryService.delete(id));
    }

    @ApiOperation("获取知识库目录树")
    @GetMapping("/tree")
    public AjaxResult<List<KbDirectory>> treeByKnowledgeBase(@RequestParam Long knowledgeBaseId) {
        return AjaxResult.success(kbDirectoryService.treeByKnowledgeBase(knowledgeBaseId));
    }
}
