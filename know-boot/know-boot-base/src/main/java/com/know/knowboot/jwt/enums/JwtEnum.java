package com.know.knowboot.jwt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum JwtEnum {

    TOKEN_KEY_USER("user", "用户信息"),
    CLAIM_KEY_USER("user", "用户信息"),
    CLAIM_KEY_USERID("userId", "用户信息"),

    ;

    private final String code;
    private final String name;

    /**
     * 通过code取枚举
     *
     * @param code 参数
     * @return 返回值
     */
    public static JwtEnum getBusinessTypeByCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (JwtEnum enums : JwtEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums;
            }
        }
        return null;
    }



}
