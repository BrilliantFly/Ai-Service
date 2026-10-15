package com.know.knowboot.entity.knowledge;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 知识库上传文件实体
 */
@Data
@TableName("kb_uploaded_file")
@ApiModel("知识库上传文件")
public class KbUploadedFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("原始文件名")
    private String originalName;

    @ApiModelProperty("存储文件名")
    private String fileName;

    @ApiModelProperty("文件存储相对路径")
    private String filePath;

    @ApiModelProperty("扩展名: html / htm / md / markdown")
    private String fileExt;

    @ApiModelProperty("文件原始文本内容")
    private String fileContent;

    @ApiModelProperty("文件大小(字节)")
    private Long fileSize;

    @ApiModelProperty("MIME类型")
    private String mimeType;

    @ApiModelProperty("归属知识库ID")
    private Long knowledgeBaseId;

    @ApiModelProperty("解析后对应的kb_document.id")
    private Long documentId;

    @ApiModelProperty("解析状态: 0待解析 1成功 2失败")
    private Integer parseStatus;

    @ApiModelProperty("解析失败原因")
    private String parseError;

    @ApiModelProperty("来源类型: upload/import/system")
    private String sourceType;

    @ApiModelProperty("上传端: vue/uniapp-h5/uniapp-app/uniapp-mp")
    private String uploadPlatform;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态: 0禁用 1正常")
    private Integer status;

    @ApiModelProperty("上传人ID")
    private Long createBy;

    @ApiModelProperty("上传时间(毫秒时间戳)")
    private Long createTime;

    @ApiModelProperty("更新时间(毫秒时间戳)")
    private Long updateTime;

    @TableLogic(value = "0", delval = "1")
    @ApiModelProperty("删除时间")
    private Long deleteTime = 0L;
}
