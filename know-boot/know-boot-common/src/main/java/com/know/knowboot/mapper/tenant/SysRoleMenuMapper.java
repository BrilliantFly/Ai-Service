package com.know.knowboot.mapper.tenant;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.tenant.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRoleMenuMapper extends IBaseMapper<SysRoleMenu> {

    /**
     * 批量插入角色菜单关联
     */
    void batchInsert(@Param("list") List<SysRoleMenu> list);
}
