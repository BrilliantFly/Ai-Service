package com.know.knowboot.mapper.tenant;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.tenant.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysDeptMapper extends IBaseMapper<SysDept> {

    /**
     * 根据部门ID查询用户ID列表
     */
    @Select("SELECT user_id FROM sys_user_dept WHERE dept_id = #{deptId}")
    List<Long> selectUserIdsByDeptId(@Param("deptId") Long deptId);
}