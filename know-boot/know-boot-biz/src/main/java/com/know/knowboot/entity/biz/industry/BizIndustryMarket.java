package com.know.knowboot.entity.biz.industry;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 行业市场实体
 */
@Data
@TableName("biz_industry_market")
@ApiModel("行业市场实体")
public class BizIndustryMarket implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("市场需求（客群规模/痛点）")
    private String demand;

    @ApiModelProperty("商机（可切入的机会点）")
    private String opportunity;

    @ApiModelProperty("价值主张（为客户创造什么价值）")
    private String valueProposition;

    @ApiModelProperty("客户细分")
    private String customerSegment;

    @ApiModelProperty("渠道通路")
    private String channel;

    @ApiModelProperty("客户关系（如何建立/维护）")
    private String customerRelation;

    @ApiModelProperty("收入来源")
    private String revenueSource;

    @ApiModelProperty("关键资源")
    private String keyResource;

    @ApiModelProperty("关键伙伴")
    private String keyPartner;

    @ApiModelProperty("关键活动")
    private String keyActivity;

    @ApiModelProperty("成本结构")
    private String costStructure;

    @ApiModelProperty("价值评价")
    private String valueEvaluation;

    @ApiModelProperty("价值分配（产业链利润分配）")
    private String valueDistribution;

    @ApiModelProperty("竞争手段（行业/产品/企业三层）")
    private String competitionMethod;

    @ApiModelProperty("推广引流（企业/产品两个维度）")
    private String promoChannel;

    @ApiModelProperty("动态信息（社会/文化/行业/市场变化；制度影响）")
    private String dynamicInfo;

    @ApiModelProperty("价值信息（市场价值/机会评估，即「价值新增」）")
    private String valueInfo;

    @ApiModelProperty("如何把握（切入策略/竞争打法）")
    private String strategy;

    @ApiModelProperty("毛利润（销售收入-销售成本）")
    private java.math.BigDecimal grossProfit;

    @ApiModelProperty("毛利率（%）")
    private java.math.BigDecimal grossMargin;

    @ApiModelProperty("净利润（总收入-总费用）")
    private java.math.BigDecimal netProfit;

    @ApiModelProperty("净利率（%）")
    private java.math.BigDecimal netMargin;

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

    @ApiModelProperty("删除人")
    private Long deleteBy;

    @TableField(exist = false)
    @ApiModelProperty("关联行业ID列表")
    private List<Long> industryIds;

    @TableField(exist = false)
    @ApiModelProperty("关联行业名称列表")
    private List<String> industryNames;
}