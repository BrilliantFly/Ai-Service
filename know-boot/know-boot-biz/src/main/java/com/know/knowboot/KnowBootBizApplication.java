package com.know.knowboot;

import com.alicp.jetcache.autoconfigure.JetCacheAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;

/**
 * 客户与行业业务管理模块启动类
 */
@SpringBootApplication(scanBasePackages = {"com.know.knowboot"}, exclude = {RedisRepositoriesAutoConfiguration.class, JetCacheAutoConfiguration.class})
@MapperScan("com.know.knowboot.mapper")
public class KnowBootBizApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(KnowBootBizApplication.class, args);
    }
}