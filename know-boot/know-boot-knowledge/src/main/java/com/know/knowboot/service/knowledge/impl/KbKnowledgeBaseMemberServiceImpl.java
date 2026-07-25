package com.know.knowboot.service.knowledge.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.knowledge.KbKnowledgeBaseMember;
import com.know.knowboot.mapper.knowledge.KbKnowledgeBaseMemberMapper;
import com.know.knowboot.service.knowledge.IKbKnowledgeBaseMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 知识库成员服务实现
 */
@Service
public class KbKnowledgeBaseMemberServiceImpl extends ServiceImpl<KbKnowledgeBaseMemberMapper, KbKnowledgeBaseMember> implements IKbKnowledgeBaseMemberService {

    @Autowired
    private KbKnowledgeBaseMemberMapper kbKnowledgeBaseMemberMapper;

    @Override
    public IPage<KbKnowledgeBaseMember> page(KbKnowledgeBaseMember query, Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<KbKnowledgeBaseMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getKnowledgeBaseId() != null, KbKnowledgeBaseMember::getKnowledgeBaseId, query.getKnowledgeBaseId())
                .eq(query.getUserId() != null, KbKnowledgeBaseMember::getUserId, query.getUserId())
                .eq(query.getRole() != null, KbKnowledgeBaseMember::getRole, query.getRole())
                .orderByDesc(KbKnowledgeBaseMember::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public KbKnowledgeBaseMember getById(Long id) {
        return kbKnowledgeBaseMemberMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(KbKnowledgeBaseMember entity, Long userId) {
        entity.setCreateTime(System.currentTimeMillis());
        return save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(KbKnowledgeBaseMember entity) {
        entity.setUpdateTime(System.currentTimeMillis());
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }
}
