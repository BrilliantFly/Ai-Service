package com.know.knowboot.service.knowledge.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.knowledge.KbDocument;
import com.know.knowboot.entity.knowledge.KbDocumentLike;
import com.know.knowboot.mapper.knowledge.KbDocumentLikeMapper;
import com.know.knowboot.mapper.knowledge.KbDocumentMapper;
import com.know.knowboot.service.knowledge.IKbDocumentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文档点赞服务实现
 */
@Service
public class KbDocumentLikeServiceImpl extends ServiceImpl<KbDocumentLikeMapper, KbDocumentLike> implements IKbDocumentLikeService {

    @Autowired
    private KbDocumentLikeMapper kbDocumentLikeMapper;

    @Autowired
    private KbDocumentMapper kbDocumentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggle(Long documentId, Long userId) {
        LambdaQueryWrapper<KbDocumentLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbDocumentLike::getDocumentId, documentId)
                .eq(KbDocumentLike::getUserId, userId);
        KbDocumentLike existing = getOne(wrapper);

        KbDocument doc = kbDocumentMapper.selectById(documentId);
        if (doc == null) return false;

        if (existing != null) {
            // 取消点赞
            removeById(existing.getId);
            doc.setLikeCount(Math.max(0, (doc.getLikeCount() == null ? 0 : doc.getLikeCount()) - 1));
        } else {
            // 添加点赞
            KbDocumentLike like = new KbDocumentLike();
            like.setDocumentId(documentId);
            like.setUserId(userId);
            like.setCreateTime(System.currentTimeMillis());
            save(like);
            doc.setLikeCount((doc.getLikeCount() == null ? 0 : doc.getLikeCount()) + 1);
        }
        return kbDocumentMapper.updateById(doc) > 0;
    }

    @Override
    public boolean isLiked(Long documentId, Long userId) {
        LambdaQueryWrapper<KbDocumentLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbDocumentLike::getDocumentId, documentId)
                .eq(KbDocumentLike::getUserId, userId);
        return count(wrapper) > 0;
    }

    @Override
    public long countByDocumentId(Long documentId) {
        LambdaQueryWrapper<KbDocumentLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbDocumentLike::getDocumentId, documentId);
        return count(wrapper);
    }
}
