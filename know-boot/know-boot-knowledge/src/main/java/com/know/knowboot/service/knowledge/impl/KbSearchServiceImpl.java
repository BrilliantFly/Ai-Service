package com.know.knowboot.service.knowledge.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.knowledge.KbDocument;
import com.know.knowboot.entity.knowledge.KbQuickNote;
import com.know.knowboot.entity.knowledge.KbSearchHistory;
import com.know.knowboot.mapper.knowledge.KbDocumentMapper;
import com.know.knowboot.mapper.knowledge.KbQuickNoteMapper;
import com.know.knowboot.mapper.knowledge.KbSearchHistoryMapper;
import com.know.knowboot.service.knowledge.IKbSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 搜索服务实现
 */
@Service
public class KbSearchServiceImpl extends ServiceImpl<KbSearchHistoryMapper, KbSearchHistory> implements IKbSearchService {

    @Autowired
    private KbSearchHistoryMapper kbSearchHistoryMapper;

    @Autowired
    private KbDocumentMapper kbDocumentMapper;

    @Autowired
    private KbQuickNoteMapper kbQuickNoteMapper;

    @Override
    public Map<String, Object> search(String keyword, Long userId) {
        Map<String, Object> result = new HashMap<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            result.put("documents", Collections.emptyList());
            result.put("notes", Collections.emptyList());
            return result;
        }

        String likeKeyword = "%" + keyword.trim() + "%";

        // 搜索文档
        LambdaQueryWrapper<KbDocument> docWrapper = new LambdaQueryWrapper<>();
        docWrapper.and(w -> w.like(KbDocument::getTitle, likeKeyword)
                        .or()
                        .like(KbDocument::getContent, likeKeyword))
                .eq(KbDocument::getStatus, 1)
                .orderByDesc(KbDocument::getUpdateTime)
                .last("LIMIT 20");
        List<KbDocument> documents = kbDocumentMapper.selectList(docWrapper);

        // 搜索小记
        LambdaQueryWrapper<KbQuickNote> noteWrapper = new LambdaQueryWrapper<>();
        noteWrapper.like(KbQuickNote::getContent, likeKeyword)
                .orderByDesc(KbQuickNote::getCreateTime)
                .last("LIMIT 20");
        List<KbQuickNote> notes = kbQuickNoteMapper.selectList(noteWrapper);

        result.put("documents", documents);
        result.put("notes", notes);

        // 保存搜索历史
        if (userId != null && userId > 0) {
            saveHistory(keyword.trim(), userId);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveHistory(String keyword, Long userId) {
        // 先删除相同关键词的历史
        LambdaQueryWrapper<KbSearchHistory> delWrapper = new LambdaQueryWrapper<>();
        delWrapper.eq(KbSearchHistory::getUserId, userId)
                .eq(KbSearchHistory::getKeyword, keyword);
        remove(delWrapper);

        KbSearchHistory history = new KbSearchHistory();
        history.setUserId(userId);
        history.setKeyword(keyword);
        history.setCreateTime(System.currentTimeMillis());
        return save(history);
    }

    @Override
    public List<KbSearchHistory> getHistory(Long userId, Integer limit) {
        LambdaQueryWrapper<KbSearchHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbSearchHistory::getUserId, userId)
                .orderByDesc(KbSearchHistory::getCreateTime)
                .last("LIMIT " + (limit != null ? limit : 20));
        return list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean clearHistory(Long userId) {
        LambdaQueryWrapper<KbSearchHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbSearchHistory::getUserId, userId);
        return remove(wrapper);
    }
}
