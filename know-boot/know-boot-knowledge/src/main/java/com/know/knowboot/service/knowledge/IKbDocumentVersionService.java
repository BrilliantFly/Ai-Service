package com.know.knowboot.service.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.knowledge.KbDocumentVersion;

import java.util.List;

/**
 * 文档版本服务接口
 */
public interface IKbDocumentVersionService {

    /**
     * 分页查询
     */
    IPage<KbDocumentVersion> page(KbDocumentVersion query, Long userId, Integer pageNum, Integer pageSize);

    /**
     * 获取文档版本列表
     */
    List<KbDocumentVersion> listByDocumentId(Long documentId);

    /**
     * 获取详情
     */
    KbDocumentVersion getById(Long id);

    /**
     * 创建版本快照
     */
    boolean createSnapshot(Long documentId, Long userId);

    /**
     * 恢复到指定版本
     */
    boolean restore(Long versionId, Long userId);

    /**
     * 删除
     */
    boolean delete(Long id);
}
