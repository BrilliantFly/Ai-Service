package com.know.knowboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("博主更新监控实体")
public class AiUpdateMonitor implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value="id", type= IdType.AUTO)
    @ApiModelProperty(value = "")
    private Integer id;

    @ApiModelProperty(value = "名称")
    private String monitorName;

    @ApiModelProperty(value = "Url")
    private String getUrl;

    @ApiModelProperty(value = "历史版本")
    private String oldVersion;

    @ApiModelProperty(value = "新版本")
    private String newVersion;

    @ApiModelProperty(value = "是否处理")
    private Integer handleFlag;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "修改时间")
    private Long updateTime;

    @ApiModelProperty(value = "删除时间")
    private Long deleteTime;

}