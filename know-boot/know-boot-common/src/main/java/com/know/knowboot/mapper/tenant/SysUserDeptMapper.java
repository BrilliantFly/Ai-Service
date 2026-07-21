package com.know.knowboot.mapper.tenant;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.tenant.SysUserDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserDeptMapper extends IBaseMapper<SysUserDept> {

    /**
     * 根据用户ID查询部门ID列表
     */
    @Select("SELECT dept_id FROM sys_user_dept WHERE user_id = #{userId}")
    List<Long> selectDeptIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据部门ID查询用户ID列表
     */
    @Select("SELECT user_id FROM sys_user_dept WHERE dept_id = #{deptId}")
    List<Long> selectUserIdsByDeptId(@Param("deptId") Long deptId);
}
