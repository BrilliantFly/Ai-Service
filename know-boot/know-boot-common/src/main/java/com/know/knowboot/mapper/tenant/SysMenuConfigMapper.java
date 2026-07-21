package com.know.knowboot.mapper.tenant;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.tenant.SysMenuConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 菜单配置 Mapper
 */
@Mapper
public interface SysMenuConfigMapper extends IBaseMapper<SysMenuConfig> {

    /**
     * 根据菜单类型查询菜单列表
     */
    @Select("SELECT * FROM sys_menu_config WHERE menu_type = #{menuType} AND (del_flag = 0 OR del_flag IS NULL) ORDER BY sort ASC")
    List<SysMenuConfig> selectByMenuType(@Param("menuType") Integer menuType);

    /**
     * 查询系统级菜单列表（不进行权限过滤，供超级管理员使用）
     */
    @Select("SELECT * FROM sys_menu_config WHERE menu_type = #{menuType} AND is_show = 1 AND (del_flag = 0 OR del_flag IS NULL) ORDER BY sort ASC")
    List<SysMenuConfig> selectVisibleByMenuType(@Param("menuType") Integer menuType);
}