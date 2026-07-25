package com.know.knowboot.entity.knowledge;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 图片上传实体
 */
@Data
@TableName("kb_image")
@ApiModel("图片上传")
public class KbImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("原始文件名")
    private String originalName;

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("文件大小(字节)")
    private Long fileSize;

    @ApiModelProperty("MIME类型")
    private String mimeType;

    @ApiModelProperty("上传人")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Long createTime;
}
