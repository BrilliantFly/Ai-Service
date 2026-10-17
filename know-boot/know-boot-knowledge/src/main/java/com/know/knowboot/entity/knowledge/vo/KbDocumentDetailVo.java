package com.know.knowboot.entity.knowledge.vo;

import com.know.knowboot.entity.knowledge.KbDocument;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 知识库文档详情 VO
 *
 * <p>在文档实体基础上附加「关联上传文件」信息，供前端按内容类型选择预览入口：
 * <ul>
 *   <li>{@code hasUploadedFile=true} 且 {@code fileExt} ∈ (html/htm/md/markdown/pdf) → 走文件预览</li>
 *   <li>否则 → 走富文本/Markdown 文档详情</li>
 * </ul>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("知识库文档详情(含关联上传文件)")
public class KbDocumentDetailVo extends KbDocument {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("是否有关联上传文件")
    private Boolean hasUploadedFile;

    @ApiModelProperty("关联上传文件ID(kb_uploaded_file.id)")
    private Long uploadedFileId;

    @ApiModelProperty("上传文件扩展名: html/htm/md/markdown/pdf")
    private String fileExt;

    @ApiModelProperty("上传文件相对路径")
    private String filePath;

    @ApiModelProperty("上传文件原始名")
    private String originalName;

    @ApiModelProperty("上传文件大小(字节)")
    private Long fileSize;

    @ApiModelProperty("上传文件访问URL(相对): /uploads/{filePath}")
    private String fileUrl;
}
