package com.know.knowboot.entity.biz.industry;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 行业产品实体
 */
@Data
@TableName("biz_industry_product")
@ApiModel("行业产品实体")
public class BizIndustryProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("产品分类")
    private String category;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品概念（核心定义）")
    private String productConcept;

    @ApiModelProperty("消费者洞察（市场关联点）")
    private String consumerInsight;

    @ApiModelProperty("利益承诺（给客户的利益）")
    private String benefitPromise;

    @ApiModelProperty("支撑点（承诺的依据）")
    private String supportPoint;

    @ApiModelProperty("核心产品（核心价值层）")
    private String coreProduct;

    @ApiModelProperty("基础产品（基本效用层）")
    private String basicProduct;

    @ApiModelProperty("附加产品（服务/售后/增值层）")
    private String additionalProduct;

    @ApiModelProperty("潜在产品（未来延伸层）")
    private String potentialProduct;

    @ApiModelProperty("产品生命周期(导入期/成长期/成熟期/衰退期)")
    private String lifeCycle;

    @ApiModelProperty("上游（原材料）")
    private String upstreamChain;

    @ApiModelProperty("中游（产品制造商）")
    private String midstreamChain;

    @ApiModelProperty("下游渠道")
    private String downstreamChannel;

    @ApiModelProperty("下游营销方式")
    private String downstreamMarketing;

    @ApiModelProperty("动态信息（社会/文化/行业/市场变化；制度影响）")
    private String dynamicInfo;

    @ApiModelProperty("价值信息（产品价值/机会评估，即「价值新增」）")
    private String valueInfo;

    @ApiModelProperty("如何把握（切入策略/竞争打法）")
    private String strategy;

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