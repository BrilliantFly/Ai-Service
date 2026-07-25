package com.know.knowboot.service.knowledge.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.knowledge.KbDirectory;
import com.know.knowboot.mapper.knowledge.KbDirectoryMapper;
import com.know.knowboot.service.knowledge.IKbDirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 目录服务实现
 */
@Service
public class KbDirectoryServiceImpl extends ServiceImpl<KbDirectoryMapper, KbDirectory> implements IKbDirectoryService {

    @Autowired
    private KbDirectoryMapper kbDirectoryMapper;

    @Override
    public IPage<KbDirectory> page(KbDirectory query, Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<KbDirectory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getKnowledgeBaseId() != null, KbDirectory::getKnowledgeBaseId, query.getKnowledgeBaseId())
                .eq(query.getParentId() != null, KbDirectory::getParentId, query.getParentId())
                .like(query.getName() != null, KbDirectory::getName, query.getName())
                .orderByAsc(KbDirectory::getSort)
                .orderByDesc(KbDirectory::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public KbDirectory getById(Long id) {
        return kbDirectoryMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(KbDirectory entity, Long userId) {
        entity.setCreateTime(System.currentTimeMillis());
        return save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(KbDirectory entity) {
        entity.setUpdateTime(System.currentTimeMillis());
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public List<KbDirectory> treeByKnowledgeBase(Long knowledgeBaseId) {
        return list(new LambdaQueryWrapper<KbDirectory>()
                .eq(KbDirectory::getKnowledgeBaseId, knowledgeBaseId)
                .orderByAsc(KbDirectory::getSort)
                .orderByDesc(KbDirectory::getCreateTime));
    }
}
