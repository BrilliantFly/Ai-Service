package com.know.knowboot.entity.biz.customer;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户企业实体
 */
@Data
@TableName("biz_customer_company")
@ApiModel("客户企业实体")
public class BizCustomerCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("企业名称")
    private String name;

    @ApiModelProperty("所属行业分类")
    private String industry;

    @ApiModelProperty("企业规模(初创/小型/中型/大型/集团)")
    private String scale;

    @ApiModelProperty("主要业务")
    private String business;

    @ApiModelProperty("主要产品/服务")
    private String mainProducts;

    @ApiModelProperty("成立时间")
    private String establishedDate;

    @ApiModelProperty("注册资本")
    private String capital;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("市场表现(营收概况/增长态势)")
    private String marketPerformance;

    @ApiModelProperty("竞争优势(技术/渠道/品牌)")
    private String competitiveAdvantage;

    @ApiModelProperty("联系人姓名")
    private String contactName;

    @ApiModelProperty("联系人手机(AES加密)")
    private String contactPhone;

    @ApiModelProperty("联系人职务")
    private String contactPosition;

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
    private Integer deleted;

    @ApiModelProperty("删除时间")
    private Long deleteTime;
}