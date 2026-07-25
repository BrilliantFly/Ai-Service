package com.know.knowboot.service.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.knowledge.KbTag;

/**
 * 标签服务接口
 */
public interface IKbTagService {

    /**
     * 分页查询
     */
    IPage<KbTag> page(KbTag query, Long userId, Integer pageNum, Integer pageSize);

    /**
     * 获取详情
     */
    KbTag getById(Long id);

    /**
     * 新增
     */
    boolean add(KbTag entity, Long userId);

    /**
     * 修改
     */
    boolean update(KbTag entity);

    /**
     * 删除
     */
    boolean delete(Long id);
}
