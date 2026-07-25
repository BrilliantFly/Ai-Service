package com.know.knowboot.service.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.knowledge.KbQuickNote;

/**
 * 小记服务接口
 */
public interface IKbQuickNoteService {

    /**
     * 分页查询
     */
    IPage<KbQuickNote> page(KbQuickNote query, Long userId, Integer pageNum, Integer pageSize);

    /**
     * 获取详情
     */
    KbQuickNote getById(Long id);

    /**
     * 新增
     */
    boolean add(KbQuickNote entity, Long userId);

    /**
     * 修改
     */
    boolean update(KbQuickNote entity);

    /**
     * 删除
     */
    boolean delete(Long id);
}
