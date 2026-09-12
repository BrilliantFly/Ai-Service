package com.know.knowboot.entity.biz.customer;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 客户实体
 */
@Data
@TableName("biz_customer")
@ApiModel("客户实体")
public class BizCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("客户姓名")
    private String name;

    @ApiModelProperty("性别(0:未知 1:男 2:女)")
    private Integer gender;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("手机号(AES加密)")
    private String phone;

    @ApiModelProperty("邮箱(AES加密)")
    private String email;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("区域编码(行政区划，用于按区域统计)")
    private String regionCode;

    @ApiModelProperty("学历(小学/初中/高中/大专/本科/硕士/博士)")
    private String education;

    @ApiModelProperty("教育背景详情(学校/专业，用于认知层度分析)")
    private String educationRaw;

    @ApiModelProperty("职业")
    private String occupation;

    @ApiModelProperty("职务/职位")
    private String position;

    @ApiModelProperty("性格(外向/内向/理性/感性等)")
    private String personality;

    @ApiModelProperty("兴趣爱好")
    private String hobby;

    @ApiModelProperty("价值观(核心信念/关注点)")
    private String valuesText;

    @ApiModelProperty("衣食住行(消费习惯/生活品质信号)")
    private String lifestyle;

    @ApiModelProperty("婚姻状况(未婚/已婚/离异/保密)")
    private String maritalStatus;

    @ApiModelProperty("家庭情况(成员构成/子女情况等)")
    private String familySituation;

    @ApiModelProperty("客户类型(1:个人 2:企业)")
    private Integer customerType;

    @ApiModelProperty("所属企业ID(biz_customer_company.id)")
    private Long companyId;

    @ApiModelProperty("状态(1:潜在 2:意向 3:成交 4:流失)")
    private Integer status;

    @ApiModelProperty("来源(线上推广/转介绍/展会/陌拜)")
    private String source;

    @ApiModelProperty("需求层级(1-5)")
    private Integer demandLevel;

    @ApiModelProperty("价值评分(1-5)")
    private Integer valueScore;

    @ApiModelProperty("需求意愿(0-100)")
    private Integer demandWillingness;

    @ApiModelProperty("需求预算")
    private BigDecimal demandBudget;

    @ApiModelProperty("决策角色(使用者/把关者/决策者)")
    private String demandDecision;

    @ApiModelProperty("需求优先级(1-5)")
    private Integer demandPriority;

    @ApiModelProperty("需求标签(JSON数组)")
    private String demandTags;

    @ApiModelProperty("需求描述")
    private String demandDesc;

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

    @TableField(exist = false)
    @ApiModelProperty("关联客户画像")
    private BizCustomerProfile profile;

    @TableField(exist = false)
    @ApiModelProperty("关联客户企业")
    private BizCustomerCompany company;

    @TableField(exist = false)
    @ApiModelProperty("关联行业列表")
    private List<BizCustomerIndustry> industries;

    @TableField(exist = false)
    @ApiModelProperty("行业ID列表")
    private List<Long> industryIds;
}