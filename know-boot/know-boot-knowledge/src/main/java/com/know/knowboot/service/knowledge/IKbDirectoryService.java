package com.know.knowboot.service.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.knowledge.KbDirectory;

import java.util.List;

/**
 * 目录服务接口
 */
public interface IKbDirectoryService {

    /**
     * 分页查询
     */
    IPage<KbDirectory> page(KbDirectory query, Long userId, Integer pageNum, Integer pageSize);

    /**
     * 获取详情
     */
    KbDirectory getById(Long id);

    /**
     * 新增
     */
    boolean add(KbDirectory entity, Long userId);

    /**
     * 修改
     */
    boolean update(KbDirectory entity);

    /**
     * 删除
     */
    boolean delete(Long id);

    /**
     * 获取知识库目录树
     */
    List<KbDirectory> treeByKnowledgeBase(Long knowledgeBaseId);
}
