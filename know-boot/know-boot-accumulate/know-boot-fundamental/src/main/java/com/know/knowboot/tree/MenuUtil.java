package com.know.knowboot.tree;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2019/6/10.
 */
public class MenuUtil {

    /**
     * @方法名: parseSysMenuTree<br>
     * @描述: 组装菜单<br>
     * @param list 数据库里面获取到的全量菜单列表
     * @return
     */
    public static List<SysMenuVo> parseSysMenuTree(List<SysMenuVo> list){
        List<SysMenuVo> result = new ArrayList<SysMenuVo>();

        // 1、获取第一级节点
        for (SysMenuVo sysMenu : list) {
            if(null == sysMenu.getParentId()) {
                result.add(sysMenu);
            }
        }

        // 2、递归获取子节点
        for (SysMenuVo parent : result) {
            parent = recursiveTree(parent, list);
        }

        return result;
    }

    /**
     * 递归获取子节点
     * @param parent
     * @param list
     * @return
     */
    public static SysMenuVo recursiveTree(SysMenuVo parent, List<SysMenuVo> list) {
        for (SysMenuVo sysMenu : list) {
            if(parent.getId() == sysMenu.getParentId()) {
                sysMenu = recursiveTree(sysMenu, list);
                parent.getChildrenMenu().add(sysMenu);
            }
        }
        return parent;
    }



    /**
     * @方法名: parseSysMenuTree<br>
     * @描述: 组装菜单<br>
     * @param list 数据库里面获取到的全量菜单列表
     * @return
     */
    public static List<ZtreeNode> parseZtreeNodeTree(List<ZtreeNode> list){
        List<ZtreeNode> result = new ArrayList<ZtreeNode>();

        // 1、获取第一级节点
        for (ZtreeNode ztreeNode : list) {
            if(null == ztreeNode.getPid()) {
                result.add(ztreeNode);
            }
        }

        // 2、递归获取子节点
        for (ZtreeNode parent : result) {
            parent = recursiveTreeNode(parent, list);
        }

        return result;
    }

    /**
     * 递归获取子节点
     * @param parent
     * @param list
     * @return
     */
    public static ZtreeNode recursiveTreeNode(ZtreeNode parent, List<ZtreeNode> list) {
        for (ZtreeNode ztreeNode : list) {
            if(parent.getId() == ztreeNode.getPid()) {
                ztreeNode = recursiveTreeNode(ztreeNode, list);
                parent.getChildren().add(ztreeNode);
            }
        }
        return parent;
    }

}

class ZtreeNode {

    private String id;
    private Integer state;
    private String pid;
    private String icon;
    private String iconClose;
    private String iconOpen;
    private String name;
    private boolean open;
    private boolean isParent;

    private List<ZtreeNode> children = new ArrayList<ZtreeNode>();

    public List<ZtreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<ZtreeNode> children) {
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconClose() {
        return iconClose;
    }

    public void setIconClose(String iconClose) {
        this.iconClose = iconClose;
    }

    public String getIconOpen() {
        return iconOpen;
    }

    public void setIconOpen(String iconOpen) {
        this.iconOpen = iconOpen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }
}

class SysMenuVo extends SysMenu {

    private List<SysMenu> childrenMenu = new ArrayList<SysMenu>();

    public List<SysMenu> getChildrenMenu() {
        return childrenMenu;
    }

    public void setChildrenMenu(List<SysMenu> childrenMenu) {
        this.childrenMenu = childrenMenu;
    }
}


class SysMenu {
    private String id;

    private String menuName;

    private String parentId;

    private String url;

    private String menuType;

    private String visible;

    private String perms;

    private String icon;

    private String remark;

    private String createBy;

    private String createName;

    private Date createTime;

    private String updateBy;

    private String updateName;

    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType == null ? null : menuType.trim();
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible == null ? null : visible.trim();
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms == null ? null : perms.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName == null ? null : createName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName == null ? null : updateName.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}