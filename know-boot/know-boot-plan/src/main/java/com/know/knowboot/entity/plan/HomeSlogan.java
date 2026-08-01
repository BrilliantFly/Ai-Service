package com.know.knowboot.entity.plan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 首页标语实体（定时切换）
 */
@Data
@TableName("plan_home_slogan")
@ApiModel("首页标语实体")
public class HomeSlogan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("标语内容")
    private String content;

    @ApiModelProperty("表情/符号(如 ✨ 🎯)")
    private String emoji;

    @ApiModelProperty("开始时间(时间戳, null表示不限)")
    private Long startTime;

    @ApiModelProperty("结束时间(时间戳, null表示不限)")
    private Long endTime;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态(0:禁用 1:启用)")
    private Integer status;

    @ApiModelProperty("删除标志(0:正常 1:删除)")
    @TableLogic(value = "0", delval = "1")
    private Integer delFlag;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新人")
    private Long updateBy;

    @ApiModelProperty("更新时间")
    private Long updateTime;
}
