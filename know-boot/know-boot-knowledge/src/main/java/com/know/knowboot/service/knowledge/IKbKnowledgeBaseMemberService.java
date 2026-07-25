package com.know.knowboot.service.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.knowledge.KbKnowledgeBaseMember;

/**
 * 知识库成员服务接口
 */
public interface IKbKnowledgeBaseMemberService {

    /**
     * 分页查询
     */
    IPage<KbKnowledgeBaseMember> page(KbKnowledgeBaseMember query, Long userId, Integer pageNum, Integer pageSize);

    /**
     * 获取详情
     */
    KbKnowledgeBaseMember getById(Long id);

    /**
     * 新增
     */
    boolean add(KbKnowledgeBaseMember entity, Long userId);

    /**
     * 修改
     */
    boolean update(KbKnowledgeBaseMember entity);

    /**
     * 删除
     */
    boolean delete(Long id);
}
