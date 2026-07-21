package com.know.knowboot.mapper.smsLog;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.smsLog.SmsLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信记录
 */
@Mapper
public interface SmsLogMapper extends IBaseMapper<SmsLog> {
}
