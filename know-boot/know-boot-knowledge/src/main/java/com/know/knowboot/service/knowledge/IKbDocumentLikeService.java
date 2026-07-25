package com.know.knowboot.service.knowledge;

import com.know.knowboot.entity.knowledge.KbDocumentLike;

/**
 * 文档点赞服务接口
 */
public interface IKbDocumentLikeService {

    /**
     * 切换点赞状态
     */
    boolean toggle(Long documentId, Long userId);

    /**
     * 检查是否已点赞
     */
    boolean isLiked(Long documentId, Long userId);

    /**
     * 获取点赞数
     */
    long countByDocumentId(Long documentId);
}
