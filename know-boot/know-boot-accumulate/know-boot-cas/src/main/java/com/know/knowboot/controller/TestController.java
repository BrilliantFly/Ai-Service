package com.know.knowboot.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class TestController {

    @SneakyThrows
    @GetMapping(value = "/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {

        Assertion assertion = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
        String loginName = null;
        if (assertion != null) {
            AttributePrincipal principal = assertion.getPrincipal();
            loginName = principal.getName();
            System.out.println("访问者:" + loginName);
        }

        //只是为了跳转到cas服务端的登录页面  登录成功后会跳回此页
        //可根据自身系统进行代码增强
        return "登录成功了";
    }

    @GetMapping(value = "/logout")
    public void logout() {
        //只是为了跳转到cas服务端的登出页面
        //可根据自身系统进行代码增强
    }

    @GetMapping(value = "/hello")
    public String hello() {
        return "hello word";
    }
}
