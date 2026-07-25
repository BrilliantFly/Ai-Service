package com.know.knowboot.service.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.knowledge.KbDocumentFavorite;

import java.util.List;

/**
 * 文档收藏服务接口
 */
public interface IKbDocumentFavoriteService {

    /**
     * 切换收藏状态
     */
    boolean toggle(Long documentId, Long userId);

    /**
     * 检查是否已收藏
     */
    boolean isFavorited(Long documentId, Long userId);

    /**
     * 获取收藏列表
     */
    IPage<KbDocumentFavorite> listByUserId(Long userId, Integer pageNum, Integer pageSize);
}
