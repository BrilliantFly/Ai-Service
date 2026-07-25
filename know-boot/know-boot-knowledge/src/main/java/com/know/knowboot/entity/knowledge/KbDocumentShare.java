package com.know.knowboot.entity.knowledge;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 文档分享实体
 */
@Data
@TableName("kb_document_share")
@ApiModel("文档分享")
public class KbDocumentShare implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("文档ID")
    private Long documentId;

    @ApiModelProperty("分享令牌")
    private String shareToken;

    @ApiModelProperty("过期时间(null=永久)")
    private Long expireTime;

    @ApiModelProperty("访问密码")
    private String password;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Long createTime;
}
