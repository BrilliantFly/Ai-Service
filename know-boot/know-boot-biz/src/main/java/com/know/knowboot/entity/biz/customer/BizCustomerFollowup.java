package com.know.knowboot.entity.biz.customer;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户跟进记录实体
 */
@Data
@TableName("biz_customer_followup")
@ApiModel("客户跟进记录实体")
public class BizCustomerFollowup implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("客户ID(biz_customer.id)")
    private Long customerId;

    @ApiModelProperty("跟进方式(电话/面谈/微信/邮件)")
    private String type;

    @ApiModelProperty("跟进内容")
    private String content;

    @ApiModelProperty("跟进结果")
    private String result;

    @ApiModelProperty("下次跟进时间(毫秒时间戳)")
    private Long nextTime;

    @ApiModelProperty("创建人ID")
    private Long createUser;

    @ApiModelProperty("创建人姓名")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private Long createTime;
}