package com.know.knowboot.entity.knowledge;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 文档版本历史实体
 */
@Data
@TableName("kb_document_version")
@ApiModel("文档版本历史")
public class KbDocumentVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("文档ID")
    private Long documentId;

    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("内容类型")
    private String contentType;

    @ApiModelProperty("变更说明")
    private String changeSummary;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Long createTime;
}
