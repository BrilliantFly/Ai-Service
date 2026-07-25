package com.know.knowboot.mapper.knowledge;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.knowledge.KbComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评论Mapper
 */
@Mapper
public interface KbCommentMapper extends IBaseMapper<KbComment> {
}
