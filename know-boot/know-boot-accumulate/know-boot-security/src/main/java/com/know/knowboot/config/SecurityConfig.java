package com.know.knowboot.config;

import com.know.knowboot.filter.AuthenticationInfoFilter;
import com.know.knowboot.handler.CustomAccessDeniedHandler;
import com.know.knowboot.handler.CustomAuthenticationEntryPoint;
import com.know.knowboot.properties.IgnoredUrlsProperties;
import com.know.knowboot.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.annotation.Resource;

/**
 * spring Security 核心配置类 通用安全
 * <p>
 * prePostEnabled属性决定Spring Security在接口前注解是否可用@PreAuthorize,@PostAuthorize等注解,设置为true,会拦截加了这些注解的接口
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 忽略验权配置
     */
    @Autowired
    private IgnoredUrlsProperties ignoredUrlsProperties;

    /**
     * 跨域处理
     */
    @Autowired
    private CorsConfigurationSource corsConfigurationSource;


    @Resource
    private RedisService redisService;



    /**
     * http请求拦截配置
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
                .authorizeRequests();

        // 配置的url 不需要授权
        // 用于filter执行顺序的原因，BasicAuthenticationFilter优先执行,所以忽略的链接可能在filter中被拦截造成permitAll无效
        for (String url : ignoredUrlsProperties.getUrls()) {
            registry.antMatchers(url).permitAll();
        }

        // 任何其它请求，都需要身份认证
        registry
                .anyRequest().authenticated()
                //允许跨域
                .and().cors().configurationSource(corsConfigurationSource)
                //自定义权限拒绝处理类
                .and().exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint()).accessDeniedHandler(new CustomAccessDeniedHandler())
                //添加JWT认证过滤器(通过缓存存权限)
                .and().addFilter(new AuthenticationInfoFilter(authenticationManager(), redisService));

    }

    /**
     * 忽略拦截url或静态资源文件夹
     * web.ignoring():
     * 会直接过滤该url - 将不会经过Spring Security过滤器链
     * http.permitAll():
     * 不会绕开springsecurity验证，相当于是允许该路径通过
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/images/**");
    }

}
