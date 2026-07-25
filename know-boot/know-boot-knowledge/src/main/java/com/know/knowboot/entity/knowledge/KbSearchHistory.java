package com.know.knowboot.entity.knowledge;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 搜索历史实体
 */
@Data
@TableName("kb_search_history")
@ApiModel("搜索历史")
public class KbSearchHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("搜索关键词")
    private String keyword;

    @ApiModelProperty("创建时间")
    private Long createTime;
}
