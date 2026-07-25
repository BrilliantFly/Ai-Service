package com.know.knowboot.controller.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.knowledge.KbKnowledgeBase;
import com.know.knowboot.service.knowledge.IKbKnowledgeBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识库控制器
 */
@Api(tags = "知识库-知识库管理")
@RestController
@RequestMapping("/api/knowledge/base")
public class KbKnowledgeBaseController {

    @Autowired
    private IKbKnowledgeBaseService kbKnowledgeBaseService;

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public AjaxResult<IPage<KbKnowledgeBase>> page(
            KbKnowledgeBase query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = 1L;
        return AjaxResult.success(kbKnowledgeBaseService.page(query, userId, pageNum, pageSize));
    }

    @ApiOperation("获取详情")
    @GetMapping("/{id}")
    public AjaxResult<KbKnowledgeBase> get(@PathVariable Long id) {
        return AjaxResult.success(kbKnowledgeBaseService.getById(id));
    }

    @ApiOperation("新增")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody KbKnowledgeBase entity) {
        Long userId = 1L;
        return AjaxResult.success(kbKnowledgeBaseService.add(entity, userId));
    }

    @ApiOperation("修改")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody KbKnowledgeBase entity) {
        return AjaxResult.success(kbKnowledgeBaseService.update(entity));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(kbKnowledgeBaseService.delete(id));
    }

    @ApiOperation("获取用户的知识库列表")
    @GetMapping("/list")
    public AjaxResult<List<KbKnowledgeBase>> listByUser() {
        Long userId = 1L;
        return AjaxResult.success(kbKnowledgeBaseService.listByUser(userId));
    }
}
