package com.know.knowboot;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
//jetcache启用缓存的主开关
@EnableCreateCacheAnnotation
//jetcache开启方法注解缓存
@EnableMethodCache(basePackages = "com.know.knowboot")
public class KnowBootCasApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowBootCasApplication.class, args);
    }

}
