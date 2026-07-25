package com.know.knowboot.service.knowledge.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.knowledge.KbQuickNote;
import com.know.knowboot.mapper.knowledge.KbQuickNoteMapper;
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
        return save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(KbQuickNote entity) {
        entity.setUpdateTime(System.currentTimeMillis());
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }
}
