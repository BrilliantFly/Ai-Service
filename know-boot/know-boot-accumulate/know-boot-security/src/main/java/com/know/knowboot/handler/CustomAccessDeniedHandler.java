package com.know.knowboot.handler;

import com.know.knowboot.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义 拒绝访问响应
 * 用来解决认证过的用户访问无权限资源时的异常
 * 比如：.antMatchers("/authority/test").hasAuthority("user:add")
 * controller注解没有权限，会抛出AccessDeniedException异常，执行AccessDeniedHandler
 */
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        ResponseUtil.output(response, ResponseUtil.resultMap(false, 401, "抱歉，您没有访问权限"));
    }

}
