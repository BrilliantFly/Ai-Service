package com.know.knowboot.service.knowledge.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.knowledge.KbDocument;
import com.know.knowboot.entity.knowledge.KbDocumentShare;
import com.know.knowboot.mapper.knowledge.KbDocumentMapper;
import com.know.knowboot.mapper.knowledge.KbDocumentShareMapper;
import com.know.knowboot.service.knowledge.IKbDocumentShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文档分享服务实现
 */
@Service
public class KbDocumentShareServiceImpl extends ServiceImpl<KbDocumentShareMapper, KbDocumentShare> implements IKbDocumentShareService {

    @Autowired
    private KbDocumentShareMapper kbDocumentShareMapper;

    @Autowired
    private KbDocumentMapper kbDocumentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KbDocumentShare create(Long documentId, Long userId, Long expireDays, String password) {
        // 检查是否已有分享
        LambdaQueryWrapper<KbDocumentShare> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbDocumentShare::getDocumentId, documentId)
                .eq(KbDocumentShare::getCreateBy, userId);
        KbDocumentShare existing = getOne(wrapper);

        if (existing != null) {
            return existing;
        }

        KbDocumentShare share = new KbDocumentShare();
        share.setDocumentId(documentId);
        share.setShareToken(UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        share.setPassword(password);
        share.setCreateBy(userId);
        share.setCreateTime(System.currentTimeMillis());

        if (expireDays != null && expireDays > 0) {
            share.setExpireTime(System.currentTimeMillis() + expireDays * 24 * 60 * 60 * 1000);
        }

        save(share);
        return share;
    }

    @Override
    public Map<String, Object> getByToken(String token) {
        LambdaQueryWrapper<KbDocumentShare> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbDocumentShare::getShareToken, token);
        KbDocumentShare share = getOne(wrapper);

        if (share == null) return null;

        // 检查是否过期
        if (share.getExpireTime() != null && share.getExpireTime() < System.currentTimeMillis()) {
            return null;
        }

        KbDocument doc = kbDocumentMapper.selectById(share.getDocumentId());
        if (doc == null) return null;

        Map<String, Object> result = new HashMap<>();
        result.put("share", share);
        result.put("document", doc);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }
}
