package com.know.knowboot.annotation.authorize.annotation;

import java.lang.annotation.*;

/**
 * 角色校验注解（springsecurity中的角色校验）
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RoleAuthorize {

    String[] value() default {};

}
