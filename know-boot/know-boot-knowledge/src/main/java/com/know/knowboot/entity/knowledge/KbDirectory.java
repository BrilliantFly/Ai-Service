package com.know.knowboot.entity.knowledge;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 目录实体
 */
@Data
@TableName("kb_directory")
@ApiModel("知识库目录")
public class KbDirectory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("知识库ID")
    private Long knowledgeBaseId;

    @ApiModelProperty("父目录ID(0为根目录)")
    private Long parentId;

    @ApiModelProperty("目录名称")
    private String name;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新时间")
    private Long updateTime;

    @TableLogic(value = "0", delval = "1")
    @ApiModelProperty("删除时间")
    private Long deleteTime = 0L;

    @TableField(exist = false)
    @ApiModelProperty("子目录列表")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private List<KbDirectory> children = new ArrayList<>();
}
