package com.know.knowboot;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

//@SpringBootApplication
@SpringBootApplication(exclude= DataSourceAutoConfiguration.class )
//扫描 system 模块的控制器
@ComponentScan(basePackages = {"com.know.knowboot"})
//jetcache启用缓存的主开关
@EnableCreateCacheAnnotation
//jetcache开启方法注解缓存
@EnableMethodCache(basePackages = "com.know.knowboot")
public class KnowBootApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowBootApiApplication.class, args);
    }

}
