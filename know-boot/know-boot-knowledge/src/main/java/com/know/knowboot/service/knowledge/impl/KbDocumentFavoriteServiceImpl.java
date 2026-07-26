package com.know.knowboot.service.knowledge.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.knowledge.KbDocumentFavorite;
import com.know.knowboot.mapper.knowledge.KbDocumentFavoriteMapper;
import com.know.knowboot.service.knowledge.IKbDocumentFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文档收藏服务实现
 */
@Service
public class KbDocumentFavoriteServiceImpl extends ServiceImpl<KbDocumentFavoriteMapper, KbDocumentFavorite> implements IKbDocumentFavoriteService {

    @Autowired
    private KbDocumentFavoriteMapper kbDocumentFavoriteMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggle(Long documentId, Long userId) {
        LambdaQueryWrapper<KbDocumentFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbDocumentFavorite::getDocumentId, documentId)
                .eq(KbDocumentFavorite::getUserId, userId);
        KbDocumentFavorite existing = getOne(wrapper);

        if (existing != null) {
            return removeById(existing.getId());
        } else {
            KbDocumentFavorite fav = new KbDocumentFavorite();
            fav.setDocumentId(documentId);
            fav.setUserId(userId);
            fav.setCreateTime(System.currentTimeMillis());
            return save(fav);
        }
    }

    @Override
    public boolean isFavorited(Long documentId, Long userId) {
        LambdaQueryWrapper<KbDocumentFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbDocumentFavorite::getDocumentId, documentId)
                .eq(KbDocumentFavorite::getUserId, userId);
        return count(wrapper) > 0;
    }

    @Override
    public IPage<KbDocumentFavorite> listByUserId(Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<KbDocumentFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbDocumentFavorite::getUserId, userId)
                .orderByDesc(KbDocumentFavorite::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }
}
