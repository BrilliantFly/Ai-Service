package com.know.knowboot.config.security;

import com.know.knowboot.config.cas.CasProperties;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CasProperties casProperties;

    @Autowired
    private ServiceProperties serviceProperties;

    @Autowired
    private LogoutFilter logoutFilter;

    @Autowired
    private SingleSignOutFilter singleSignOutFilter;

    @Autowired
    private CasAuthenticationEntryPoint casAuthenticationEntryPoint;

    /**
     * security安全策略
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 校验是否case单点登录
//        if (casProperties.getCaseEnable() == "true") {
//
//        }

        http
                // 过滤请求
                .authorizeRequests()
                // 对于登录login 验证码captchaImage 允许匿名访问
                //.antMatchers("/login", "/captchaImage").anonymous()
                .antMatchers(
                        HttpMethod.OPTIONS, "/**"
                ).permitAll()// 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        http.exceptionHandling().authenticationEntryPoint(casAuthenticationEntryPoint)
                .and().addFilter(casAuthenticationFilter())
                .addFilterBefore(logoutFilter, LogoutFilter.class)
                .addFilterBefore(singleSignOutFilter, CasAuthenticationFilter.class);

        // CSRF禁用，因为不使用session
        http.csrf().disable();
        // 不能添加下面内容，添加下面内容，接口无法调用
        // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

    }

    /**
     * cas认证过滤器
     * casAuthenticationFilter.setFilterProcessesUrl 必须要设置完整路径 不然会无限重定向
     */
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        casAuthenticationFilter.setFilterProcessesUrl(casProperties.getAppServerUrl() + casProperties.getAppServerLoginUrl());
        casAuthenticationFilter.setServiceProperties(serviceProperties);
        return casAuthenticationFilter;
    }
}
