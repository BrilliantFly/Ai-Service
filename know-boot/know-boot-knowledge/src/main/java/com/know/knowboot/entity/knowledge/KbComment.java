package com.know.knowboot.entity.knowledge;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 文档评论实体
 */
@Data
@TableName("kb_comment")
@ApiModel("文档评论")
public class KbComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("文档ID")
    private Long documentId;

    @ApiModelProperty("父评论ID(回复)")
    private Long parentId;

    @ApiModelProperty("评论内容")
    private String content;

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
