//package com.know.knowboot.jwt.aspect;
//
//import com.know.knowboot.jwt.filter.JJwtFilter;
//import com.know.knowboot.jwt.service.JJwtService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * filter配置的第二种方式
// *      这种方式filter拦截器 @Autowired会失效
// *      所以通过  registration.setFilter(new JJwtFilter(jJwtService))
// *      解决失效问题
// */
//@Configuration
//public class FilterConfig {
//
//    @Autowired
//    private JJwtService jJwtService;
//
//    @Bean
//    public FilterRegistrationBean registerAuthFilter() {
//
//        //通过FilterRegistrationBean实例设置优先级可以生效
//        //通过@WebFilter无效
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        //注册自定义过滤器
//        registration.setFilter(new JJwtFilter(jJwtService));
//        //过滤器名称
//        registration.setName("jJwtFilter");
//        //过滤所有路径
//        registration.addUrlPatterns("/*");
//        //优先级，值越小，Filter越靠前
//        registration.setOrder(2);
//        return registration;
//    }
//
//    //如果有多个Filter，再写一个public FilterRegistrationBean registerOtherFilter(){...}即可。
//
//}
