package com.know.knowboot.entity.knowledge;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 文档实体
 */
@Data
@TableName("kb_document")
@ApiModel("知识库文档")
public class KbDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("目录ID")
    private Long directoryId;

    @ApiModelProperty("知识库ID")
    private Long knowledgeBaseId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("内容类型: richtext/markdown")
    private String contentType;

    @ApiModelProperty("浏览次数")
    private Integer viewCount;

    @ApiModelProperty("点赞数")
    private Integer likeCount;

    @ApiModelProperty("收藏数")
    private Integer favoriteCount;

    @ApiModelProperty("评论数")
    private Integer commentCount;

    @ApiModelProperty("当前版本号")
    private Integer version;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态: 0草稿/1已发布")
    private Integer status;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新时间")
    private Long updateTime;

    @TableLogic(value = "0", delval = "1")
    @ApiModelProperty("删除时间")
    private Long deleteTime = 0L;
}
