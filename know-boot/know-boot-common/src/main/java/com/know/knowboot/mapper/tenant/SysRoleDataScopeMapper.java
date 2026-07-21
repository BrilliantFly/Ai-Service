package com.know.knowboot.mapper.tenant;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.know.knowboot.entity.tenant.SysRoleDataScope;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRoleDataScopeMapper extends BaseMapper<SysRoleDataScope> {

    @Select("SELECT * FROM sys_role_data_scope WHERE role_id = #{roleId}")
    List<SysRoleDataScope> selectByRoleId(@Param("roleId") Long roleId);
}