package com.know.knowboot.entity.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_tabbar")
@ApiModel("底部导航实体")
public class SysTabbar implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("链接")
    private String url;

    @ApiModelProperty("选中图标")
    private String selected;

    @ApiModelProperty("未选中图标")
    private String unselected;

    @ApiModelProperty("跳转配置JSON")
    private String link;

    @ApiModelProperty("是否显示")
    private Integer isShow;

    @ApiModelProperty("是否大按钮")
    private Integer isBig;

    @ApiModelProperty("大按钮图标")
    private String bigIcon;

    @ApiModelProperty("大按钮类型")
    private String bigType;

    @ApiModelProperty("大按钮位置")
    private Integer bigPosition;

    @ApiModelProperty("弹出菜单JSON")
    private String bigList;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新人")
    private String updateBy;

    @ApiModelProperty("更新时间")
    private Long updateTime;

    @TableLogic
    @ApiModelProperty("删除标志")
    private Integer delFlag;
}