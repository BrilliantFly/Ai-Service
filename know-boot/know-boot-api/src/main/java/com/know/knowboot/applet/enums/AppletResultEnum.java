package com.know.knowboot.applet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppletResultEnum {

    SYSTEM_BUSY("-1", "系统繁忙，此时请开发者稍候再试"),
    REQUEST_SUCCESS("0", "请求成功"),
    CODE_INVALID("40029", "code 无效"),
    FREQUENCY_LIMIT("45011", "频率限制，每个用户每分钟100次"),
    HIGH_RISK_USER("40226", "高风险等级用户，小程序登录拦截"),


    ;

    private final String code;
    private final String name;



}
