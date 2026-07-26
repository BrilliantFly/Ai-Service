package com.know.knowboot.service.knowledge.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.knowledge.KbQuickNote;
import com.know.knowboot.entity.knowledge.KbTag;
import com.know.knowboot.mapper.knowledge.KbQuickNoteMapper;
import com.know.knowboot.mapper.knowledge.KbTagMapper;
import com.know.knowboot.service.knowledge.IKbQuickNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 小记服务实现
 */
@Service
public class KbQuickNoteServiceImpl extends ServiceImpl<KbQuickNoteMapper, KbQuickNote> implements IKbQuickNoteService {

    @Autowired
    private KbQuickNoteMapper kbQuickNoteMapper;

    @Autowired
    private KbTagMapper kbTagMapper;

    @Override
    public IPage<KbQuickNote> page(KbQuickNote query, Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<KbQuickNote> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getCreateBy() != null, KbQuickNote::getCreateBy, query.getCreateBy())
                .like(query.getContent() != null, KbQuickNote::getContent, query.getContent())
                .orderByDesc(KbQuickNote::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public KbQuickNote getById(Long id) {
        return kbQuickNoteMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(KbQuickNote entity, Long userId) {
        entity.setCreateBy(userId);
        entity.setCreateTime(System.currentTimeMillis());
        boolean result = save(entity);
        if (result) {
            syncTagsToTagTable(entity.getTags());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(KbQuickNote entity) {
        entity.setUpdateTime(System.currentTimeMillis());
        boolean result = updateById(entity);
        if (result) {
            syncTagsToTagTable(entity.getTags());
        }
        return result;
    }

    /**
     * 将小记中的标签名称同步到kb_tag表，不存在则自动创建
     */
    private void syncTagsToTagTable(String tags) {
        if (tags == null || tags.trim().isEmpty()) return;
        String[] tagNames = tags.replace("，", ",").split(",");
        for (String name : tagNames) {
            name = name.trim();
            if (name.isEmpty()) continue;
            LambdaQueryWrapper<KbTag> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(KbTag::getName, name);
            KbTag existing = kbTagMapper.selectOne(wrapper);
            if (existing == null) {
                KbTag tag = new KbTag();
                tag.setName(name);
                tag.setColor("#25B864");
                tag.setSort(0);
                tag.setCreateTime(System.currentTimeMillis());
                tag.setDeleteTime(0L);
                kbTagMapper.insert(tag);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleArchive(Long id) {
        KbQuickNote entity = kbQuickNoteMapper.selectById(id);
        if (entity == null) return false;
        entity.setIsArchived(entity.getIsArchived() == 1 ? 0 : 1);
        entity.setUpdateTime(System.currentTimeMillis());
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }
}
