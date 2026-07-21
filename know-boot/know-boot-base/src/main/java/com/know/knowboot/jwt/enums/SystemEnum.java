package com.know.knowboot.jwt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SystemEnum {

    KNOW("know", "系统名称"),

    ;

    private final String code;
    private final String name;

}
