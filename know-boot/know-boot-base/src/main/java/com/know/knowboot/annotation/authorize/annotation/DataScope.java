package com.know.knowboot.annotation.authorize.annotation;

import java.lang.annotation.*;

/**
 * 数据权限注解
 * 用于标注需要数据权限控制的接口
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 数据权限字段名
     * 默认为dept_id，即按部门过滤
     */
    String deptField() default "dept_id";

    /**
     * 用户ID字段名
     * 用于"仅本人"权限过滤
     */
    String userField() default "user_id";

    /**
     * 是否启用数据权限
     * 默认true
     */
    boolean enabled() default true;
}