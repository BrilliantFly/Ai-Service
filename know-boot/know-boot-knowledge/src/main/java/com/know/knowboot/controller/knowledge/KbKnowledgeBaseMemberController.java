package com.know.knowboot.controller.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.knowledge.KbKnowledgeBaseMember;
import com.know.knowboot.service.knowledge.IKbKnowledgeBaseMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 知识库成员控制器
 */
@Api(tags = "知识库-成员管理")
@RestController
@RequestMapping("/api/knowledge/base/member")
public class KbKnowledgeBaseMemberController {

    @Autowired
    private IKbKnowledgeBaseMemberService kbKnowledgeBaseMemberService;

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public AjaxResult<IPage<KbKnowledgeBaseMember>> page(
            KbKnowledgeBaseMember query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = 1L;
        return AjaxResult.success(kbKnowledgeBaseMemberService.page(query, userId, pageNum, pageSize));
    }

    @ApiOperation("获取详情")
    @GetMapping("/{id}")
    public AjaxResult<KbKnowledgeBaseMember> get(@PathVariable Long id) {
        return AjaxResult.success(kbKnowledgeBaseMemberService.getById(id));
    }

    @ApiOperation("新增")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody KbKnowledgeBaseMember entity) {
        Long userId = 1L;
        return AjaxResult.success(kbKnowledgeBaseMemberService.add(entity, userId));
    }

    @ApiOperation("修改")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody KbKnowledgeBaseMember entity) {
        return AjaxResult.success(kbKnowledgeBaseMemberService.update(entity));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(kbKnowledgeBaseMemberService.delete(id));
    }
}
