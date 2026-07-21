package com.know.knowboot.mapper.tenant;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.tenant.SysUserJob;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserJobMapper extends IBaseMapper<SysUserJob> {

    /**
     * 根据用户ID查询岗位ID列表
     */
    @Select("SELECT job_id FROM sys_user_job WHERE user_id = #{userId}")
    List<Long> selectJobIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据岗位ID查询用户ID列表
     */
    @Select("SELECT user_id FROM sys_user_job WHERE job_id = #{jobId}")
    List<Long> selectUserIdsByJobId(@Param("jobId") Long jobId);
}
