package com.know.knowboot.mapper.system;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.system.SystemRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统菜单Mapper
 */
@Mapper
public interface SystemRoleMenuMapper extends IBaseMapper<SystemRoleMenu> {


    @Select("SELECT DISTINCT menuR.menu_id FROM system_role_menu menuR INNER JOIN system_role role ON menuR.role_id = role.id INNER JOIN admin_role lar ON lar.role_id = role.id WHERE lar.admin_id = #{adminId} AND role.delete_time IS NULL")
    List<Integer> getMenuIds(@Param("adminId") Integer adminId);



}
