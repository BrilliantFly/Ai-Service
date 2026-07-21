package com.know.knowboot.annotation.authorize.interceptor;

import com.know.knowboot.annotation.authorize.annotation.RoleAuthorize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class RoleInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("自定义角色拦截器注解 -> preHandle");

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        //在方法上寻找注解
        RoleAuthorize permission = handlerMethod.getMethodAnnotation(RoleAuthorize.class);
        if (permission == null) {
            //方法不存在则在类上寻找注解则在类上寻找注解
            permission = handlerMethod.getBeanType().getAnnotation(RoleAuthorize.class);
        }

        //如果没有添加权限注解则直接跳过允许访问
        if (permission == null) {
            return true;
        }

        //获取注解中的值
        String[] validateRoles = permission.value();
        //校验是否含有对应的角色
        for (String role : validateRoles) {
            //从springsecurity的上下文获取用户角色是否存在当前的角色名称
            return true;
        }

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
