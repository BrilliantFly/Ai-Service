package com.know.knowboot.config;//package com.know.knowboot.config;
//
//import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
//import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class HystrixConfig {
//
//    //用来拦截处理HystrixCommand注解
//    @Bean
//    public HystrixCommandAspect hystrixAspect() {
//        return new HystrixCommandAspect();
//    }
//
//    //用来像监控中心Dashboard发送stream信息
//    @Bean
//    public ServletRegistrationBean hystrixMetricsStreamServlet() {
//        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
//        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
//        registrationBean.setLoadOnStartup(1);  //系统启动时加载顺序
//        registrationBean.addUrlMappings("/actuator/hystrix.stream");
//        registrationBean.setName("HystrixMetricsStreamServlet");
//        return registrationBean;
//    }
//
//}
