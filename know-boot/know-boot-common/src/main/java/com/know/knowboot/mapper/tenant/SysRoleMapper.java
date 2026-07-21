package com.know.knowboot.mapper.tenant;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.tenant.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRoleMapper extends IBaseMapper<SysRole> {

    /**
     * 根据用户名ID查询角色ID列表
     */
    @Select("SELECT role_id FROM sys_user_role WHERE user_id = #{userId}")
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 查询正常状态的角色列表
     */
    @Select("SELECT * FROM sys_role WHERE status = 1 AND (del_flag = 0 OR del_flag IS NULL) ORDER BY sort")
    List<SysRole> selectNormalRoles();
}