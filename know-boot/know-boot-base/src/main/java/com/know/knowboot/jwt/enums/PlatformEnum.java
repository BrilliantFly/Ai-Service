package com.know.knowboot.jwt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  PlatformEnum {

    PC("pc", "PC端"),
    MOBILE("mobile", "移动端"),
    APPLET("applet", "小程序"),

    ;

    private final String code;
    private final String name;

}
