package com.know.knowboot.service.knowledge.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.knowledge.KbDocument;
import com.know.knowboot.mapper.knowledge.KbDocumentMapper;
import com.know.knowboot.service.knowledge.IKbDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 文档服务实现
 */
@Service
public class KbDocumentServiceImpl extends ServiceImpl<KbDocumentMapper, KbDocument> implements IKbDocumentService {

    @Autowired
    private KbDocumentMapper kbDocumentMapper;

    @Override
    public IPage<KbDocument> page(KbDocument query, Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<KbDocument> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getKnowledgeBaseId() != null, KbDocument::getKnowledgeBaseId, query.getKnowledgeBaseId())
                .eq(query.getDirectoryId() != null, KbDocument::getDirectoryId, query.getDirectoryId())
                .eq(query.getStatus() != null, KbDocument::getStatus, query.getStatus())
                .like(query.getTitle() != null, KbDocument::getTitle, query.getTitle())
                .orderByAsc(KbDocument::getSort)
                .orderByDesc(KbDocument::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public KbDocument getById(Long id) {
        return kbDocumentMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(KbDocument entity, Long userId) {
        entity.setCreateBy(userId);
        entity.setCreateTime(System.currentTimeMillis());
        if (entity.getStatus() == null) {
            entity.setStatus(0);
        }
        if (entity.getViewCount() == null) {
            entity.setViewCount(0);
        }
        return save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(KbDocument entity) {
        entity.setUpdateTime(System.currentTimeMillis());
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public List<KbDocument> listByKnowledgeBase(Long knowledgeBaseId) {
        return list(new LambdaQueryWrapper<KbDocument>()
                .eq(KbDocument::getKnowledgeBaseId, knowledgeBaseId)
                .orderByAsc(KbDocument::getSort)
                .orderByDesc(KbDocument::getCreateTime));
    }

    @Override
    public IPage<KbDocument> recent(Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<KbDocument> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbDocument::getStatus, 1)
                .orderByDesc(KbDocument::getUpdateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }
}
