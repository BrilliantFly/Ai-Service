package com.know.knowboot.mapper.system;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.system.SystemLogOperation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志Mapper
 */
@Mapper
public interface SystemLogOperationMapper extends IBaseMapper<SystemLogOperation> {
}