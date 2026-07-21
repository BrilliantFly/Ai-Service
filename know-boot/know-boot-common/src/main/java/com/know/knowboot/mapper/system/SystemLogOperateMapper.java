package com.know.knowboot.mapper.system;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.system.OperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统操作日志
 */
@Mapper
public interface SystemLogOperateMapper extends IBaseMapper<OperationLog> {
}
