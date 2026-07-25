package com.know.knowboot.service.knowledge;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.knowledge.KbComment;

import java.util.List;

/**
 * 评论服务接口
 */
public interface IKbCommentService {

    /**
     * 分页查询
     */
    IPage<KbComment> page(KbComment query, Long userId, Integer pageNum, Integer pageSize);

    /**
     * 获取文档评论列表
     */
    List<KbComment> listByDocumentId(Long documentId);

    /**
     * 获取详情
     */
    KbComment getById(Long id);

    /**
     * 新增
     */
    boolean add(KbComment entity, Long userId);

    /**
     * 修改
     */
    boolean update(KbComment entity);

    /**
     * 删除
     */
    boolean delete(Long id);
}
