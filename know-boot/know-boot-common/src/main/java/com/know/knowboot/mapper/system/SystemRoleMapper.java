package com.know.knowboot.mapper.system;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.system.SystemRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统角色Mapper
 */
@Mapper
public interface SystemRoleMapper extends IBaseMapper<SystemRole> {


    @Select("SELECT * FROM system_role role INNER JOIN admin_role lar ON lar.role_id = role.id WHERE lar.admin_id = #{adminId} AND role.delete_time IS NULL")
    List<SystemRole> getByAdminId(@Param("adminId") Integer adminId);

}
