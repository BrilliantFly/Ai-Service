package com.know.knowboot.mapper;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员Mapper
 */
@Mapper
public interface AdminMapper extends IBaseMapper<Admin> {
}