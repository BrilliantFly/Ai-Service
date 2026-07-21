package com.know.knowboot.entity.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_tenant")
@ApiModel("租户信息实体")
public class SysTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("租户名称")
    private String name;

    @ApiModelProperty("租户编码")
    private String code;

    @ApiModelProperty("Logo")
    private String logo;

    @ApiModelProperty("状态 (0:停用, 1:正常)")
    private Integer status;

    @ApiModelProperty("过期时间")
    private Date expireTime;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新人")
    private String updateBy;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @TableLogic
    @ApiModelProperty("删除标志 (0:正常, 1:已删除)")
    private Integer delFlag;
}
