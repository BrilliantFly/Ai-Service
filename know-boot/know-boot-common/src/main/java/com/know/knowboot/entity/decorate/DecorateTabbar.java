package com.know.knowboot.entity.decorate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("底部导航实体")
@TableName("sys_tabbar")
public class DecorateTabbar implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value="id", type= IdType.AUTO)
    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("导航名称")
    private String name;

    @ApiModelProperty("未选图标")
    private String selected;

    @ApiModelProperty("已选图标")
    private String unselected;

    @ApiModelProperty("链接地址")
    private String link;

    @ApiModelProperty("是否显示")
    private Integer isShow;

    @ApiModelProperty("是否大按钮(0否1是)")
    private Integer isBig;

    @ApiModelProperty("大按钮图标")
    private String bigIcon;

    @ApiModelProperty("大按钮类型(jump跳转/popup弹出)")
    private String bigType;

    @ApiModelProperty("大按钮位置")
    private Integer bigPosition;

    @ApiModelProperty("弹出菜单配置JSON")
    private String bigList;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新时间")
    private Long updateTime;

}
