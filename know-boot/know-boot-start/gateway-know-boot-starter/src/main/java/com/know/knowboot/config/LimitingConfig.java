package com.know.knowboot.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * Redis限流
 */
@Slf4j
@Configuration
public class LimitingConfig {

    /**
     * 地址限流
     * @return
     */
    @Bean
    @Primary
    public KeyResolver ipKeyResolver() {
        log.info("---------------------- 地址限流 ---------------------------");
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }


    /**
     * 用户限流
     * @return
     */
    @Bean
    KeyResolver userKeyResolver() {
        log.info("---------------------- 用户限流 ---------------------------");
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("userId"));
    }


    /**
     * 接口限流
     * @return
     */
    @Bean
    KeyResolver apiKeyResolver() {
        log.info("---------------------- 接口限流 ---------------------------");
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }


}
