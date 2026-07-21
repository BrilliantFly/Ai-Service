package com.know.knowboot.mapper.user;


import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.user.UserAuth;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户授权Mapper
 */
@Mapper
public interface UserAuthMapper extends IBaseMapper<UserAuth> {
}
