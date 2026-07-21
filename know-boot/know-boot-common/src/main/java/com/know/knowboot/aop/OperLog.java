package com.know.knowboot.aop;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * 标注在Controller方法上，自动记录操作日志
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperLog {

    /**
     * 操作模块
     */
    String value() default "";

    /**
     * 业务类型
     * 0: 其他, 1: 新增, 2: 修改, 3: 删除, 4: 查询
     */
    int businessType() default 0;

    /**
     * 操作描述
     */
    String operDesc() default "";
}