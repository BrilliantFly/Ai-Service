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
     * 批量上传HTML/MD文件
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
}
