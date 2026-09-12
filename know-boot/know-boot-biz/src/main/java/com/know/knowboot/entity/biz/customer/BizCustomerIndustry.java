package com.know.knowboot.entity.biz.customer;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户行业关联实体
 */
@Data
@TableName("biz_customer_industry")
@ApiModel("客户行业关联实体")
public class BizCustomerIndustry implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("客户ID(biz_customer.id)")
    private Long customerId;

    @ApiModelProperty("行业ID(biz_industry.id，同模块 industry 子域)")
    private Long industryId;

    @ApiModelProperty("关系类型(1:主营行业 2:关联行业 3:潜在行业)")
    private Integer relationType;

    @ApiModelProperty("是否主营行业(0:否 1:是，每个客户至多一个)")
    private Integer isMain;

    @ApiModelProperty("关联备注(如\"客户在该行业的角色\")")
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

    @TableField(exist = false)
    @ApiModelProperty("行业名称")
    private String industryName;
}