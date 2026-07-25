package com.know.knowboot.service.knowledge.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.knowledge.KbTag;
import com.know.knowboot.mapper.knowledge.KbTagMapper;
import com.know.knowboot.service.knowledge.IKbTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 标签服务实现
 */
@Service
public class KbTagServiceImpl extends ServiceImpl<KbTagMapper, KbTag> implements IKbTagService {

    @Autowired
    private KbTagMapper kbTagMapper;

    @Override
    public IPage<KbTag> page(KbTag query, Long userId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<KbTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getName() != null, KbTag::getName, query.getName())
                .orderByAsc(KbTag::getSort)
                .orderByDesc(KbTag::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public KbTag getById(Long id) {
        return kbTagMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(KbTag entity, Long userId) {
        entity.setCreateTime(System.currentTimeMillis());
        return save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(KbTag entity) {
        entity.setUpdateTime(System.currentTimeMillis());
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }
}
