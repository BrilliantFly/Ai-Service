package com.know.knowboot.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 配置
 * 配置多租户插件和分页插件
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * MyBatis Plus 核心拦截器链
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 1. 多租户插件（必须放在最前面）
        // 会自动在SQL中注入 tenant_id 条件
        interceptor.addInnerInterceptor(
            new TenantLineInnerInterceptor(new CustomTenantLineHandler())
        );
        
        // 2. 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        
        return interceptor;
    }

}