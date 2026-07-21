package com.know.knowboot.config;//package com.know.knowboot.config;
//
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.web.client.RestTemplate;
//
//@Configuration
//public class RibbonConfig {
//
//    @Bean
//    @LoadBalanced
//    RestTemplate loadBalanced() {
//        return new RestTemplate();
//    }
//
//    @Primary
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
//
//}
