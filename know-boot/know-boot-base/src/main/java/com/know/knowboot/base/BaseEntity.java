package com.know.knowboot.base;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 删除状态（0，正常，1已删除）
     * 实体类中属性加上@TableLogic注解，表示该字段是逻辑删除字段
     * value = “未删除的值，默认值为0”
     * delval = “删除后的值，默认值为1”
     */
//    @TableLogic(value = "0",delval = "1")
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

    /***
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     * 自动填充
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     * 自动填充
     */
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
