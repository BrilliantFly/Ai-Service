package com.know.knowboot.entity.knowledge;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 知识库实体
 */
@Data
@TableName("kb_knowledge_base")
@ApiModel("知识库")
public class KbKnowledgeBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("知识库名称")
    private String name;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("封面图URL")
    private String cover;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("可见性: 0私密/1公开")
    private Integer visibility;

    @ApiModelProperty("文档数")
    private Integer docCount;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态: 0禁用/1正常")
    private Integer status;

    @ApiModelProperty("创建人ID")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新时间")
    private Long updateTime;

    @TableLogic(value = "0", delval = "1")
    @ApiModelProperty("删除时间")
    private Long deleteTime = 0L;
}
