package com.know.knowboot.service.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.knowledge.KbSearchHistory;

import java.util.List;
import java.util.Map;

/**
 * 搜索服务接口
 */
public interface IKbSearchService {

    /**
     * 全文搜索
     */
    Map<String, Object> search(String keyword, Long userId);

    /**
     * 保存搜索历史
     */
    boolean saveHistory(String keyword, Long userId);

    /**
     * 获取搜索历史
     */
    List<KbSearchHistory> getHistory(Long userId, Integer limit);

    /**
     * 清空搜索历史
     */
    boolean clearHistory(Long userId);
}
