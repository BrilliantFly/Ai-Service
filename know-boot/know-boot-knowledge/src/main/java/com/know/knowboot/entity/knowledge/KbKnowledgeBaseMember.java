package com.know.knowboot.entity.knowledge;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 知识库成员实体
 */
@Data
@TableName("kb_knowledge_base_member")
@ApiModel("知识库成员")
public class KbKnowledgeBaseMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("知识库ID")
    private Long knowledgeBaseId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("角色: admin/editor/viewer")
    private String role;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新时间")
    private Long updateTime;

    @TableLogic(value = "0", delval = "1")
    @ApiModelProperty("删除时间")
    private Long deleteTime = 0L;
}
