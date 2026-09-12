package com.know.knowboot.entity.biz.industry;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 行业主体实体
 */
@Data
@TableName("biz_industry")
@ApiModel("行业主体实体")
public class BizIndustry implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("行业名称")
    private String industryName;

    @ApiModelProperty("行业定义（边界说明）")
    private String definition;

    @ApiModelProperty("核心技术（行业需要的技术/设计能力）")
    private String technology;

    @ApiModelProperty("行业编码（自定义，便于引用）")
    private String industryCode;

    @ApiModelProperty("标签(逗号分隔)")
    private String tags;

    @ApiModelProperty("上游产业链（原材料/供应商）")
    private String upstreamChain;

    @ApiModelProperty("中游产业链（产品制造商/集成商）")
    private String midstreamChain;

    @ApiModelProperty("下游销售渠道")
    private String downstreamChannel;

    @ApiModelProperty("下游营销方式")
    private String downstreamMarketing;

    @ApiModelProperty("行业发展概况")
    private String developmentOverview;

    @ApiModelProperty("市场规模（可含单位）")
    private String marketSize;

    @ApiModelProperty("增长潜力/增速")
    private String growthPotential;

    @ApiModelProperty("动态信息（社会/文化/行业/市场变化；制度影响）")
    private String dynamicInfo;

    @ApiModelProperty("价值信息（行业价值/机会评估）")
    private String valueInfo;

    @ApiModelProperty("行业资源（关键资源/人脉/资质）")
    private String industryResources;

    @ApiModelProperty("如何把握（布局策略/竞争打法）")
    private String strategy;

    @ApiModelProperty("毛利润（销售收入-销售成本）")
    private BigDecimal grossProfit;

    @ApiModelProperty("毛利率（%）")
    private BigDecimal grossMargin;

    @ApiModelProperty("净利润（总收入-总费用）")
    private BigDecimal netProfit;

    @ApiModelProperty("净利率（%）")
    private BigDecimal netMargin;

    @ApiModelProperty("可见性(0:私有 1:公开 2:系统预置)")
    private Integer visibility;

    @ApiModelProperty("排序(越小越前)")
    private Integer sort;

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
    @ApiModelProperty("关联市场信息")
    private BizIndustryMarket market;

    @TableField(exist = false)
    @ApiModelProperty("关联产品列表")
    private List<BizIndustryProduct> products;

    @TableField(exist = false)
    @ApiModelProperty("关联企业列表")
    private List<BizIndustryEnterprise> enterprises;

    @TableField(exist = false)
    @ApiModelProperty("关联客户数")
    private Long customerCount;
}