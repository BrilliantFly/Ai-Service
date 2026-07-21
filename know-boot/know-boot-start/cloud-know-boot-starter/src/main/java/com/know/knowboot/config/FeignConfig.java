package com.know.knowboot.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    /**
     * NONE：默认的，不显示任何日志
     * BASIC：仅记录请求方法、URL、响应状态码及执行时间
     * HEADERS：出了BASIC中定义的信息之外，还有请求和响应的头信息
     * FULL：除了HEADERS中定义的信息之外，还有请求和响应的正文及元素
     */
    @Bean
    public Logger.Level feginLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * feign重试
     * @return
     */
    @Bean
    public Retryer feignRetryer() {
        //最大请求次数为5，初始间隔时间为100ms，下次间隔时间1.5倍递增，重试间最大间隔时间为1s，
        return new Retryer.Default();
    }

    //自定义重试次数
//    @Bean
//    public Retryer feignRetryer(){
//        Retryer retryer = new Retryer.Default(100, 1000, 4);
//        return retryer;
//    }

    /**
     * feign禁止重试
     * @return
     */
//    @Bean
//    public Retryer feignRetryer() {
//        return Retryer.NEVER_RETRY;
//    }


    /**
     * 解决feign远程调用丢失请求头问题
     * 在远程调用之前要构造请求，调用很多拦截器，如果没有拦截器，feign就使用它构造的请求，
     * 这个请求是没有请求头等信息的，因此，就导致了请求头丢失的问题出现。
     * @return
     */
//    @Bean("requestInterceptor")
//    public RequestInterceptor requestInterceptor(){
//        return new RequestInterceptor() {
//            @Override
//            public void apply(RequestTemplate requestTemplate) {
//                System.out.println("feign远程调用之前执行RequestInterceptor.apply");
//                // 1. 使用RequestContextHolder拿到访问的请求对象
//                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//                HttpServletRequest request = requestAttributes.getRequest();
//                // 2. 同步请求头数据
//                // 注意：一般都是同步cookie  所以这里可以直接将cookie设置进去即可，不必要全部设置
//                Enumeration<String> headerNames = request.getHeaderNames();
//                if(null != headerNames && headerNames.hasMoreElements()){
//                    while(headerNames.hasMoreElements()){
//                        String headName = headerNames.nextElement();
//                        requestTemplate.header(headName,request.getHeader(headName));
//                    }
//                }
//            }
//        };
//    }

}
