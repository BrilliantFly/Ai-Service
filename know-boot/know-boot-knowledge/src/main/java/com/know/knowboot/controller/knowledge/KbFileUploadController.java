package com.know.knowboot.controller.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.knowledge.KbUploadedFile;
import com.know.knowboot.service.knowledge.IKbFileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 文件上传控制器
 */
@Api(tags = "知识库-文件上传")
@RestController
@RequestMapping("/api/knowledge/file")
public class KbFileUploadController {

    @Autowired
    private IKbFileUploadService fileUploadService;

    @ApiOperation("批量上传HTML/MD文件")
    @PostMapping("/upload")
    public AjaxResult<Map<String, Object>> upload(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "knowledgeBaseId", required = false) Long knowledgeBaseId,
            @RequestParam(value = "uploadPlatform", required = false) String uploadPlatform) {
        if (files == null || files.length == 0) {
            return (AjaxResult<Map<String, Object>>) (AjaxResult<?>) AjaxResult.failed("未接收到文件");
        }
        Long userId = 1L;
        return AjaxResult.success(fileUploadService.batchUpload(files, knowledgeBaseId, uploadPlatform, userId));
    }

    @ApiOperation("重新解析文件")
    @PostMapping("/parse")
    public AjaxResult<Boolean> parse(@RequestBody Map<String, Long> body) {
        Long fileId = body.get("fileId");
        if (fileId == null) {
            return (AjaxResult<Boolean>) (AjaxResult<?>) AjaxResult.failed("fileId不能为空");
        }
        return AjaxResult.success(fileUploadService.parse(fileId));
    }

    @ApiOperation("分页查询上传文件")
    @GetMapping("/page")
    public AjaxResult<IPage<KbUploadedFile>> page(
            KbUploadedFile query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = 1L;
        return AjaxResult.success(fileUploadService.page(query, userId, pageNum, pageSize));
    }

    @ApiOperation("文件详情")
    @GetMapping("/{id}")
    public AjaxResult<KbUploadedFile> detail(@PathVariable Long id) {
        return AjaxResult.success(fileUploadService.getById(id));
    }

    @ApiOperation("删除文件(软删)")
    @DeleteMapping("/{id}")
    public AjaxResult<Object> delete(@PathVariable Long id) {
        fileUploadService.removeWithDocument(id);
        return AjaxResult.success();
    }
}
