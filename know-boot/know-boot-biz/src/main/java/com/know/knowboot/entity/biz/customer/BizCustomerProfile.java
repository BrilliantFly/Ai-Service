package com.know.knowboot.entity.biz.customer;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户深度画像实体
 */
@Data
@TableName("biz_customer_profile")
@ApiModel("客户深度画像实体")
public class BizCustomerProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("客户ID(biz_customer.id)")
    private Long customerId;

    @ApiModelProperty("动态信息(人性观察/心理学特征/读心术信号；制度对客户的影响)")
    private String dynamicInfo;

    @ApiModelProperty("需求层次(1生理 2安全 3社交 4尊重 5自我实现)")
    private Integer valueLevel;

    @ApiModelProperty("客户期望(对产品或服务的核心期待)")
    private String valueExpect;

    @ApiModelProperty("利益点(客户的利益诉求/关注维度)")
    private String valueInterest;

    @ApiModelProperty("应对策略(沟通定位/切入角度)")
    private String strategy;

    @ApiModelProperty("话术设计(开场/痛点/方案/成交话术)")
    private String talkScript;

    @ApiModelProperty("分析(综合判断/下一步动作)")
    private String analysis;

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