package com.know.knowboot.entity.knowledge;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 标签实体
 */
@Data
@TableName("kb_tag")
@ApiModel("知识库标签")
public class KbTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("标签名称")
    private String name;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新时间")
    private Long updateTime;

    @TableLogic(value = "0", delval = "1")
    @ApiModelProperty("删除时间")
    private Long deleteTime = 0L;
}
