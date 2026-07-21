package com.know.knowboot.applet.dto;

import lombok.Data;

@Data
public class WxMsgDTO {

    /**
     * 业务类型
     */
    Integer businessType;

    /**
     * 申请人
     */
    String applicant;

    /**
     * 申请原因或备注
     */
    String remark;

    /**
     * 用户
     */
    String userIds;

    /**
     * 角色
     */
    String roleId;

}
