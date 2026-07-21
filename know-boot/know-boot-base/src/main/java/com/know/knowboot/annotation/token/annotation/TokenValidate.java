package com.know.knowboot.annotation.token.annotation;

import java.lang.annotation.*;

/**
 * 权限校验注解（校验token）
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenValidate {

    //必填参数
    String[] token() default {};

}
