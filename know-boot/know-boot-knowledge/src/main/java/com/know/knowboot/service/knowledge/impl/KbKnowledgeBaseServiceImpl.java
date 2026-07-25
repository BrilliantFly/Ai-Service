package com.know.knowboot.service.knowledge.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.knowledge.KbKnowledgeBase;
import com.know.knowboot.mapper.knowledge.KbKnowledgeBaseMapper;
import com.know.knowboot.service.knowledge.IKbKnowledgeBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 知识库服务实现
 */
@Service
public class KbKnowledgeBaseServiceImpl extends ServiceImpl<KbKnowledgeBaseMapper, KbKnowledgeBase> implements IKbKnowledgeBaseService {

    @Autowired
    private KbKnowledgeBaseMapper kbKnowledgeBaseMapper;

    @Override
    public IPage<KbKnowledgeBase> page(KbKnowledgeBase query, Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<KbKnowledgeBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getStatus() != null, KbKnowledgeBase::getStatus, query.getStatus())
                .like(query.getName() != null, KbKnowledgeBase::getName, query.getName())
                .orderByAsc(KbKnowledgeBase::getSort)
                .orderByDesc(KbKnowledgeBase::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public KbKnowledgeBase getById(Long id) {
        return kbKnowledgeBaseMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(KbKnowledgeBase entity, Long userId) {
        entity.setCreateBy(userId);
        entity.setCreateTime(System.currentTimeMillis());
        if (entity.getStatus() == null) {
            entity.setStatus(1);
        }
        if (entity.getDocCount() == null) {
            entity.setDocCount(0);
        }
        return save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(KbKnowledgeBase entity) {
        entity.setUpdateTime(System.currentTimeMillis());
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public List<KbKnowledgeBase> listByUser(Long userId) {
        return list(new LambdaQueryWrapper<KbKnowledgeBase>()
                .eq(KbKnowledgeBase::getCreateBy, userId)
                .orderByAsc(KbKnowledgeBase::getSort)
                .orderByDesc(KbKnowledgeBase::getCreateTime));
    }
}
