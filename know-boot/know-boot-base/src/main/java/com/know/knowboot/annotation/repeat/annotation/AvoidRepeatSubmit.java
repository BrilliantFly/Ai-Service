package com.know.knowboot.annotation.repeat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 申请提交之后几秒内需要防止用户重复提交
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AvoidRepeatSubmit {

    /**
     * 指定时间内不可重复提交，单位：s
     *
     * @return
     */
    long timeout() default 3;

}
