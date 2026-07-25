package com.know.knowboot.entity.knowledge;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 文档收藏实体
 */
@Data
@TableName("kb_document_favorite")
@ApiModel("文档收藏")
public class KbDocumentFavorite implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("文档ID")
    private Long documentId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("创建时间")
    private Long createTime;
}
