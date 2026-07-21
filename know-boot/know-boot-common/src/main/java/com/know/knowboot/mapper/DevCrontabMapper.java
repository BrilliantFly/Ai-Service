package com.know.knowboot.mapper;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.DevCrontab;
import org.apache.ibatis.annotations.Mapper;

/**
 * 计划任务Mapper
 */
@Mapper
public interface DevCrontabMapper extends IBaseMapper<DevCrontab> {
}
