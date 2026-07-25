package com.know.knowboot.service.knowledge.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.knowledge.KbDocument;
import com.know.knowboot.entity.knowledge.KbDocumentVersion;
import com.know.knowboot.mapper.knowledge.KbDocumentMapper;
import com.know.knowboot.mapper.knowledge.KbDocumentVersionMapper;
import com.know.knowboot.service.knowledge.IKbDocumentVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 文档版本服务实现
 */
@Service
public class KbDocumentVersionServiceImpl extends ServiceImpl<KbDocumentVersionMapper, KbDocumentVersion> implements IKbDocumentVersionService {

    @Autowired
    private KbDocumentVersionMapper kbDocumentVersionMapper;

    @Autowired
    private KbDocumentMapper kbDocumentMapper;

    @Override
    public IPage<KbDocumentVersion> page(KbDocumentVersion query, Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<KbDocumentVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getDocumentId() != null, KbDocumentVersion::getDocumentId, query.getDocumentId())
                .orderByDesc(KbDocumentVersion::getVersion);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<KbDocumentVersion> listByDocumentId(Long documentId) {
        LambdaQueryWrapper<KbDocumentVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbDocumentVersion::getDocumentId, documentId)
                .orderByDesc(KbDocumentVersion::getVersion);
        return list(wrapper);
    }

    @Override
    public KbDocumentVersion getById(Long id) {
        return kbDocumentVersionMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createSnapshot(Long documentId, Long userId) {
        KbDocument doc = kbDocumentMapper.selectById(documentId);
        if (doc == null) return false;

        int currentVersion = doc.getVersion() == null ? 1 : doc.getVersion();

        KbDocumentVersion version = new KbDocumentVersion();
        version.setDocumentId(documentId);
        version.setVersion(currentVersion);
        version.setTitle(doc.getTitle());
        version.setContent(doc.getContent());
        version.setContentType(doc.getContentType());
        version.setCreateBy(userId);
        version.setCreateTime(System.currentTimeMillis());

        boolean saved = save(version);
        if (saved) {
            // 更新文档版本号
            doc.setVersion(currentVersion + 1);
            kbDocumentMapper.updateById(doc);
        }
        return saved;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean restore(Long versionId, Long userId) {
        KbDocumentVersion version = getById(versionId);
        if (version == null) return false;

        KbDocument doc = kbDocumentMapper.selectById(version.getDocumentId());
        if (doc == null) return false;

        // 先保存当前版本快照
        createSnapshot(doc.getId(), userId);

        // 恢复内容
        doc.setTitle(version.getTitle());
        doc.setContent(version.getContent());
        doc.setContentType(version.getContentType());
        doc.setUpdateTime(System.currentTimeMillis());
        return kbDocumentMapper.updateById(doc) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }
}
