package com.know.knowboot.service.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.knowledge.KbKnowledgeBase;

import java.util.List;

/**
 * 知识库服务接口
 */
public interface IKbKnowledgeBaseService {

    /**
     * 分页查询
     */
    IPage<KbKnowledgeBase> page(KbKnowledgeBase query, Long userId, Integer pageNum, Integer pageSize);

    /**
     * 获取详情
     */
    KbKnowledgeBase getById(Long id);

    /**
     * 新增
     */
    boolean add(KbKnowledgeBase entity, Long userId);

    /**
     * 修改
     */
    boolean update(KbKnowledgeBase entity);

    /**
     * 删除
     */
    boolean delete(Long id);

    /**
     * 获取用户的知识库列表
     */
    List<KbKnowledgeBase> listByUser(Long userId);
}
