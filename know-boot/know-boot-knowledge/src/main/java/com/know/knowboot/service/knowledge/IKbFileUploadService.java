package com.know.knowboot.service.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.know.knowboot.entity.knowledge.KbUploadedFile;

import java.util.Map;

/**
 * 文件上传服务接口
 */
public interface IKbFileUploadService extends IService<KbUploadedFile> {

    /**
     * 批量上传HTML/MD/PDF文件
     */
    Map<String, Object> batchUpload(Object[] files, Long knowledgeBaseId, String uploadPlatform, Long userId);

    /**
     * 重新解析单个文件
     */
    boolean parse(Long fileId);

    /**
     * 分页查询
     */
    IPage<KbUploadedFile> page(KbUploadedFile query, Long userId, Integer pageNum, Integer pageSize);

    /**
     * 删除文件及关联文档
     */
    boolean removeWithDocument(Long id);

    /**
     * PDF 逐页渲染为图片，返回每页可公开访问的图片地址（供 App/小程序内嵌预览）。
     * 渲染结果按文件缓存于 uploadPath/pdf-preview/{fileId}/。
     */
    Map<String, Object> pdfPages(Long fileId);
}
