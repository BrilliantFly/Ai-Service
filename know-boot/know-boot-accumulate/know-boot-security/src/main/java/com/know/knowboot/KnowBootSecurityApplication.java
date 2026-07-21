package com.know.knowboot;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

//禁用Security
//@SpringBootApplication(exclude= {SecurityAutoConfiguration.class})
//exclude= DataSourceAutoConfiguration.class解决Failed to configure a DataSource: 'url' attribute is not specified and no embedded
@SpringBootApplication(exclude= DataSourceAutoConfiguration.class )
@EnableCaching
//jetcache启用缓存的主开关
@EnableCreateCacheAnnotation
//jetcache开启方法注解缓存
@EnableMethodCache(basePackages = "com.know.knowboot")
public class KnowBootSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowBootSecurityApplication.class, args);
    }

}
