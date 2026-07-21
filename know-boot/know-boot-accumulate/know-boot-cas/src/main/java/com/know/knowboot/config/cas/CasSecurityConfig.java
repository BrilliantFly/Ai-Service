package com.know.knowboot.config.cas;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Configuration
public class CasSecurityConfig {

    @Autowired
    private CasProperties casProperties;

    @Autowired
    private AuthenticationUserDetailsService casUserDetailService;

    /**
     * cas客户端信息
     */
    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        //设置cas客户端登录完整的url
        serviceProperties.setService(casProperties.getAppServerUrl() + casProperties.getAppServerLoginUrl());
        serviceProperties.setAuthenticateAllArtifacts(true);
        // 是否关闭单点登录，默认为false，所以也可以不设置
        serviceProperties.setSendRenew(false);
        return serviceProperties;
    }

    /**
     * cas认证入口
     */
    @Bean
    @Primary
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
        //设置cas 服务端登录url
        casAuthenticationEntryPoint.setLoginUrl(casProperties.getCasServerLoginUrl());
        //设置cas 客户端信息
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
        return casAuthenticationEntryPoint;
    }

    /**
     * cas 票证验证器
     */
    @Bean
    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
        return new Cas20ServiceTicketValidator(casProperties.getCasServerUrl());
    }

    /**
     * 设置AuthenticationProvider
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(casAuthenticationProvider());
    }

    /**
     * cas认证Provider
     */
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        //设置cas 客户端信息
        casAuthenticationProvider.setServiceProperties(serviceProperties());
        //设置cas 票证验证器
        casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
        //设置UserDetailService 登录成功后 会进入loadUserDetails方法中
        casAuthenticationProvider.setAuthenticationUserDetailsService(casUserDetailService);
        //设置cas 秘钥
        casAuthenticationProvider.setKey(casProperties.getCasKey());
        return casAuthenticationProvider;
    }

    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    /**
     * 单点退出过滤器
     * 用于跳转到cas服务端
     */
    @Bean
    public LogoutFilter logoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(casProperties.getCasServerLogoutUrl(), new SecurityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl(casProperties.getCasServiceLogoutUrl());
        return logoutFilter;
    }

    /**
     * 单点注销过滤器
     * 用于接收cas服务端的注销请求
     */
    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setCasServerUrlPrefix(casProperties.getCasServerUrl());
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

}
