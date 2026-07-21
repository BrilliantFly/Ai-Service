package com.know.knowboot.entity.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单配置实体
 */
@Data
@TableName("sys_menu_config")
public class SysMenuConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单编码
     */
    private String menuCode;

    /**
     * 菜单类型 [1:tabBar, 2:首页, 3:侧边栏]
     */
    private Integer menuType;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 选中图标
     */
    private String selectedIcon;

    /**
     * 页面路径
     */
    private String path;

    /**
     * 外部URL
     */
    private String url;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否显示 [0:否, 1:是]
     */
    private Integer isShow;

    /**
     * 是否凸起按钮 [0:否, 1:是]
     */
    private Integer isBig;

    /**
     * 凸起图标
     */
    private String bigIcon;

    /**
     * 凸起类型
     */
    private String bigType;

    /**
     * 凸起菜单列表 (JSON格式)
     */
    private String bigList;

    /**
     * 渲染类型 [1:动态列表, 2:固定表单, 3:指定界面]
     */
    private Integer renderType;

    /**
     * 渲染配置 (JSON格式)
     */
    private String renderConfig;

    /**
     * 权限ID
     */
    private Long permissionId;

    /**
     * 权限编码
     */
    private String permissionCode;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标志 [0:否, 1:是]
     */
    @TableLogic
    private Integer delFlag;
}