package com.know.knowboot.mapper.knowledge;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.knowledge.KbUploadedFile;
import org.apache.ibatis.annotations.Mapper;

/**
 * 上传文件Mapper
 */
@Mapper
public interface KbUploadedFileMapper extends IBaseMapper<KbUploadedFile> {
}
