package com.know.knowboot.entity.biz.industry;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 行业企业实体
 */
@Data
@TableName("biz_industry_enterprise")
@ApiModel("行业企业实体")
public class BizIndustryEnterprise implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("企业/平台类型(如:生产商/经销商/平台/SaaS服务商)")
    private String enterpriseType;

    @ApiModelProperty("企业/平台名称")
    private String enterpriseName;

    @ApiModelProperty("成立时间")
    private String establishedDate;

    @ApiModelProperty("注册资本")
    private String registeredCapital;

    @ApiModelProperty("实缴资本")
    private String paidCapital;

    @ApiModelProperty("企业规模(人数)")
    private String scale;

    @ApiModelProperty("参保人数")
    private Integer insuredCount;

    @ApiModelProperty("是否上市(0:否 1:是)")
    private Integer isListed;

    @ApiModelProperty("主要业务")
    private String mainBusiness;

    @ApiModelProperty("核心技术")
    private String coreTechnology;

    @ApiModelProperty("产品/服务")
    private String products;

    @ApiModelProperty("市场表现(营收/市占率)")
    private String marketPerformance;

    @ApiModelProperty("主要竞争对手")
    private String competitors;

    @ApiModelProperty("竞争优势")
    private String advantage;

    @ApiModelProperty("竞争不足")
    private String disadvantage;

    @ApiModelProperty("上游（原材料）")
    private String upstreamChain;

    @ApiModelProperty("中游（产品制造商）")
    private String midstreamChain;

    @ApiModelProperty("下游渠道")
    private String downstreamChannel;

    @ApiModelProperty("下游营销方式")
    private String downstreamMarketing;

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

    @TableField(exist = false)
    @ApiModelProperty("关联产品ID列表")
    private List<Long> productIds;

    @TableField(exist = false)
    @ApiModelProperty("关联产品名称列表")
    private List<String> productNames;
}