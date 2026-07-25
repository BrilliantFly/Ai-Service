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

import java.util.*;

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
        // 查询所有目录（扁平）
        List<KbDirectory> allDirs = list(new LambdaQueryWrapper<KbDirectory>()
                .eq(KbDirectory::getKnowledgeBaseId, knowledgeBaseId)
                .orderByAsc(KbDirectory::getSort)
                .orderByDesc(KbDirectory::getCreateTime));

        // 构建树形结构
        Map<Long, KbDirectory> map = new LinkedHashMap<>();
        List<KbDirectory> roots = new ArrayList<>();

        // 先放入 map
        for (KbDirectory dir : allDirs) {
            dir.setChildren(new ArrayList<>());
            map.put(dir.getId(), dir);
        }

        // 建立父子关系
        for (KbDirectory dir : allDirs) {
            Long pid = dir.getParentId();
            if (pid == null || pid == 0L) {
                roots.add(dir);
            } else {
                KbDirectory parent = map.get(pid);
                if (parent != null) {
                    parent.getChildren().add(dir);
                } else {
                    roots.add(dir);
                }
            }
        }

        return roots;
    }
}
