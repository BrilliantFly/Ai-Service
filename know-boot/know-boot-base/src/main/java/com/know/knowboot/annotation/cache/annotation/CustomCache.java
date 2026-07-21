package com.know.knowboot.annotation.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomCache {

    //缓存前缀
    String prefix() default "";
    //缓存key
    String key() default "";

}
