package com.know.knowboot.entity.biz.industry;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 行业-企业关联实体
 */
@Data
@TableName("biz_industry_enterprise_rel")
@ApiModel("行业-企业关联实体")
public class BizIndustryEnterpriseRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("行业ID(biz_industry.id)")
    private Long industryId;

    @ApiModelProperty("企业ID(biz_industry_enterprise.id)")
    private Long enterpriseId;

    @ApiModelProperty("关联备注")
    private String remark;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("更新人")
    private Long updateBy;

    @ApiModelProperty("更新时间")
    private Long updateTime;

    @TableLogic(value = "0", delval = "1")
    @ApiModelProperty("删除标记")
    private Integer delFlag;

    @ApiModelProperty("删除时间")
    private Long deleteTime;
}