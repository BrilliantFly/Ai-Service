package com.know.knowboot.annotation.log.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.METHOD;

//记录接口的操作日志
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface OperationLog {

    String value() default"";

}
