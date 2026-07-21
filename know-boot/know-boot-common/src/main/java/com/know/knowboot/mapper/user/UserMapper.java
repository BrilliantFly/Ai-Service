package com.know.knowboot.mapper.user;


import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper extends IBaseMapper<User> {
}
