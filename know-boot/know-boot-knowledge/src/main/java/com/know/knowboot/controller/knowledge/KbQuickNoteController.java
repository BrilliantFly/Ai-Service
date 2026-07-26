package com.know.knowboot.controller.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.knowledge.KbQuickNote;
import com.know.knowboot.service.knowledge.IKbQuickNoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 小记控制器
 */
@Api(tags = "知识库-小记管理")
@RestController
@RequestMapping("/api/knowledge/quick-note")
public class KbQuickNoteController {

    @Autowired
    private IKbQuickNoteService kbQuickNoteService;

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public AjaxResult<IPage<KbQuickNote>> page(
            KbQuickNote query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = 1L;
        return AjaxResult.success(kbQuickNoteService.page(query, userId, pageNum, pageSize));
    }

    @ApiOperation("获取详情")
    @GetMapping("/{id}")
    public AjaxResult<KbQuickNote> get(@PathVariable Long id) {
        return AjaxResult.success(kbQuickNoteService.getById(id));
    }

    @ApiOperation("新增")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody KbQuickNote entity) {
        Long userId = 1L;
        return AjaxResult.success(kbQuickNoteService.add(entity, userId));
    }

    @ApiOperation("修改")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody KbQuickNote entity) {
        return AjaxResult.success(kbQuickNoteService.update(entity));
    }

    @ApiOperation("切换归档状态")
    @PutMapping("/{id}/archive")
    public AjaxResult<Boolean> toggleArchive(@PathVariable Long id) {
        return AjaxResult.success(kbQuickNoteService.toggleArchive(id));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(kbQuickNoteService.delete(id));
    }
}
