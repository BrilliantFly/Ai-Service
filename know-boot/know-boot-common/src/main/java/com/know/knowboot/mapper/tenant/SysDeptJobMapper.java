package com.know.knowboot.mapper.tenant;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.tenant.SysDeptJob;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysDeptJobMapper extends IBaseMapper<SysDeptJob> {

    /**
     * 根据部门ID查询岗位ID列表
     */
    @Select("SELECT job_id FROM sys_dept_job WHERE dept_id = #{deptId}")
    List<Long> selectJobIdsByDeptId(@Param("deptId") Long deptId);

    /**
     * 根据岗位ID查询部门ID列表
     */
    @Select("SELECT dept_id FROM sys_dept_job WHERE job_id = #{jobId}")
    List<Long> selectDeptIdsByJobId(@Param("jobId") Long jobId);
}
