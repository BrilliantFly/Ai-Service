package com.know.knowboot.controller.knowledge;

import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.knowledge.KbImage;
import com.know.knowboot.service.knowledge.IKbImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 图片控制器
 */
@Api(tags = "知识库-图片管理")
@RestController
@RequestMapping("/api/knowledge/image")
public class KbImageController {

    @Autowired
    private IKbImageService kbImageService;

    @Value("${upload.path:./uploads}")
    private String uploadPath;

    @ApiOperation("上传图片")
    @PostMapping("/upload")
    public AjaxResult<KbImage> upload(@RequestParam("file") MultipartFile file) {
        try {
            Long userId = 1L;
            KbImage image = kbImageService.upload(
                    file.getBytes(),
                    file.getOriginalFilename(),
                    file.getContentType(),
                    userId
            );
            return AjaxResult.success(image);
        } catch (IOException e) {
            return AjaxResult.error("上传失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取图片信息")
    @GetMapping("/{id}")
    public AjaxResult<KbImage> get(@PathVariable Long id) {
        return AjaxResult.success(kbImageService.getById(id));
    }

    @ApiOperation("获取图片访问路径")
    @GetMapping("/url/{id}")
    public AjaxResult<String> getUrl(@PathVariable Long id) {
        KbImage image = kbImageService.getById(id);
        if (image == null) {
            return AjaxResult.error("图片不存在");
        }
        return AjaxResult.success("/uploads/" + image.getFilePath());
    }
}
