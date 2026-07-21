package com.know.knowboot;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.know.knowboot.config.MicroserviceConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {RedisRepositoriesAutoConfiguration.class, SecurityAutoConfiguration.class})
@EnableCaching
@EnableScheduling
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.know.knowboot")
@EnableTransactionManagement
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.know.*.mapper"})
@Import(MicroserviceConfiguration.class)
public class MicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceApplication.class, args);
    }
}
