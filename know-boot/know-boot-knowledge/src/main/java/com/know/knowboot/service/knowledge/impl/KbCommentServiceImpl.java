package com.know.knowboot.service.knowledge.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.knowledge.KbComment;
import com.know.knowboot.entity.knowledge.KbDocument;
import com.know.knowboot.mapper.knowledge.KbCommentMapper;
import com.know.knowboot.mapper.knowledge.KbDocumentMapper;
import com.know.knowboot.service.knowledge.IKbCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 评论服务实现
 */
@Service
public class KbCommentServiceImpl extends ServiceImpl<KbCommentMapper, KbComment> implements IKbCommentService {

    @Autowired
    private KbCommentMapper kbCommentMapper;

    @Autowired
    private KbDocumentMapper kbDocumentMapper;

    @Override
    public IPage<KbComment> page(KbComment query, Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<KbComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getDocumentId() != null, KbComment::getDocumentId, query.getDocumentId())
                .orderByAsc(KbComment::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<KbComment> listByDocumentId(Long documentId) {
        LambdaQueryWrapper<KbComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbComment::getDocumentId, documentId)
                .orderByAsc(KbComment::getCreateTime);
        return list(wrapper);
    }

    @Override
    public KbComment getById(Long id) {
        return kbCommentMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(KbComment entity, Long userId) {
        entity.setCreateBy(userId);
        entity.setCreateTime(System.currentTimeMillis());
        boolean saved = save(entity);
        if (saved) {
            // 更新文档评论数
            KbDocument doc = kbDocumentMapper.selectById(entity.getDocumentId());
            if (doc != null) {
                doc.setCommentCount((doc.getCommentCount() == null ? 0 : doc.getCommentCount()) + 1);
                kbDocumentMapper.updateById(doc);
            }
        }
        return saved;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(KbComment entity) {
        entity.setUpdateTime(System.currentTimeMillis());
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        KbComment comment = getById(id);
        if (comment != null) {
            boolean removed = removeById(id);
            if (removed) {
                // 更新文档评论数
                KbDocument doc = kbDocumentMapper.selectById(comment.getDocumentId());
                if (doc != null && doc.getCommentCount() != null && doc.getCommentCount() > 0) {
                    doc.setCommentCount(doc.getCommentCount() - 1);
                    kbDocumentMapper.updateById(doc);
                }
            }
            return removed;
        }
        return false;
    }
}
