package com.know.knowboot.service.knowledge;

import com.know.knowboot.entity.knowledge.KbDocumentShare;

import java.util.Map;

/**
 * 文档分享服务接口
 */
public interface IKbDocumentShareService {

    /**
     * 创建分享链接
     */
    KbDocumentShare create(Long documentId, Long userId, Long expireDays, String password);

    /**
     * 通过令牌获取分享信息
     */
    Map<String, Object> getByToken(String token);

    /**
     * 删除分享
     */
    boolean delete(Long id);
}
