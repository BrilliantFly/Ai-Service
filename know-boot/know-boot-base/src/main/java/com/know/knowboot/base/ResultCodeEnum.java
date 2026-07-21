package com.know.knowboot.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCodeEnum {

    REQUEST_SUCCESS(200, "请求成功"),
    PERMANENT_REDIRECT(301, "永久重定向"),
    TEMPORARY_REDIRECT(302, "临时重定向"),
    CLIENT_ERROR(400, "客户端错误"),
    NO_ACCESS(403, "禁止访问"),
    NOT_FOUND(404, "请求的内容未找到或已删除"),
    INTERNAL_SERVER_ERROR(500, "服务器错误"),
    BAD_GATEWAY(502, "网关无响应"),
    SERVICE_UNAVAILABLE(503, "服务器端临时错误"),
    GATEWAY_TIMEOUT(504, "网关超时"),

    ;

    private Integer value;
    private String msg;

}
