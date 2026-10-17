package com.know.knowboot.controller.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.knowledge.KbUploadedFile;
import com.know.knowboot.service.knowledge.IKbFileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Value("${upload.path:./uploads}")
    private String uploadPath;

    @ApiOperation("批量上传HTML/MD/PDF文件")
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

    @ApiOperation("渲染/下载原始文件(供 PDF 等预览, 内联返回)")
    @GetMapping("/{id}/render")
    public ResponseEntity<Resource> render(@PathVariable Long id) throws java.io.IOException {
        KbUploadedFile file = fileUploadService.getById(id);
        if (file == null || file.getFilePath() == null) {
            return ResponseEntity.notFound().build();
        }
        String rel = file.getFilePath();
        // 防目录穿越
        if (rel.contains("..")) {
            return ResponseEntity.badRequest().build();
        }
        Path path = Paths.get(uploadPath, rel).normalize().toAbsolutePath();
        File f = path.toFile();
        if (!f.exists() || !f.isFile()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = (file.getMimeType() != null && !file.getMimeType().trim().isEmpty())
                ? file.getMimeType()
                : guessContentType(file.getFileExt());

        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentType(MediaType.parseMediaType(contentType));
        } catch (Exception e) {
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        }
        if (file.getOriginalName() != null) {
            headers.setContentDisposition(ContentDisposition.inline()
                    .filename(URLEncoder.encode(file.getOriginalName(), "UTF-8").replace("+", "%20"))
                    .build());
        }
        headers.setContentLength(f.length());
        return ResponseEntity.ok().headers(headers).body(new FileSystemResource(f));
    }

    @ApiOperation("PDF 分页渲染：返回每页预览图片地址（供 App/小程序内嵌预览）")
    @GetMapping("/{id}/pages")
    public AjaxResult<Map<String, Object>> pages(@PathVariable Long id) {
        return AjaxResult.success(fileUploadService.pdfPages(id));
    }

    private String guessContentType(String ext) {
        if (ext == null) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        switch (ext.toLowerCase()) {
            case "pdf":
                return "application/pdf";
            case "html":
            case "htm":
                return "text/html;charset=UTF-8";
            case "md":
            case "markdown":
                return "text/markdown;charset=UTF-8";
            default:
                return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }

    @ApiOperation("删除文件(软删)")
    @DeleteMapping("/{id}")
    public AjaxResult<Object> delete(@PathVariable Long id) {
        fileUploadService.removeWithDocument(id);
        return AjaxResult.success();
    }
}
