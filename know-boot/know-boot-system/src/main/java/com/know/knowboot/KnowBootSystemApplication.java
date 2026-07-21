package com.know.knowboot;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude= {RedisRepositoriesAutoConfiguration.class,SecurityAutoConfiguration.class,
        OAuth2ResourceServerAutoConfiguration.class},
        scanBasePackages = {"com.know"})
@ComponentScan(basePackages = {"com.know"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.know\\.knowboot\\.feign\\..*")
})
@EnableCaching
@EnableScheduling
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.know.knowboot")
@EnableTransactionManagement
@MapperScan(basePackages = {"com.know.*.mapper"})
public class KnowBootSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowBootSystemApplication.class, args);
    }

}
