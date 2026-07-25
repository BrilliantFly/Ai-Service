package com.know.knowboot.controller.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.knowledge.KbTag;
import com.know.knowboot.service.knowledge.IKbTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 标签控制器
 */
@Api(tags = "知识库-标签管理")
@RestController
@RequestMapping("/api/knowledge/tag")
public class KbTagController {

    @Autowired
    private IKbTagService kbTagService;

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public AjaxResult<IPage<KbTag>> page(
            KbTag query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = 1L;
        return AjaxResult.success(kbTagService.page(query, userId, pageNum, pageSize));
    }

    @ApiOperation("获取详情")
    @GetMapping("/{id}")
    public AjaxResult<KbTag> get(@PathVariable Long id) {
        return AjaxResult.success(kbTagService.getById(id));
    }

    @ApiOperation("新增")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody KbTag entity) {
        Long userId = 1L;
        return AjaxResult.success(kbTagService.add(entity, userId));
    }

    @ApiOperation("修改")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody KbTag entity) {
        return AjaxResult.success(kbTagService.update(entity));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(kbTagService.delete(id));
    }
}
