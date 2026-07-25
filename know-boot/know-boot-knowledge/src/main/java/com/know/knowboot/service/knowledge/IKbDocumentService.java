package com.know.knowboot.service.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.knowledge.KbDocument;

import java.util.List;

/**
 * 文档服务接口
 */
public interface IKbDocumentService {

    /**
     * 分页查询
     */
    IPage<KbDocument> page(KbDocument query, Long userId, Integer pageNum, Integer pageSize);

    /**
     * 获取详情
     */
    KbDocument getById(Long id);

    /**
     * 新增
     */
    boolean add(KbDocument entity, Long userId);

    /**
     * 修改
     */
    boolean update(KbDocument entity);

    /**
     * 删除
     */
    boolean delete(Long id);

    /**
     * 获取知识库下的文档列表
     */
    List<KbDocument> listByKnowledgeBase(Long knowledgeBaseId);

    /**
     * 获取最近编辑的文档
     */
    IPage<KbDocument> recent(Long userId, Integer pageNum, Integer pageSize);
}
