package com.know.knowboot.entity.plan;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 首页配置实体
 */
@Data
@TableName("plan_home_config")
@ApiModel("首页配置实体")
public class PlanHomeConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("配置类型(banner:轮播图 notice:通知 menu:滚动菜单 grid:九宫格)")
    private String configType;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("配置内容JSON")
    private String content;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("链接地址")
    private String link;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态(0:禁用 1:启用)")
    private Integer status;

    @ApiModelProperty("角色ID(null表示全局)")
    private String roleId;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新人")
    private Long updateBy;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
