package com.know.knowboot.config;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;

/**
 * Sa-Token配置
 * 配置会话管理相关功能
 */
@Configuration
public class SaTokenConfig {

    /**
     * 配置Sa-Token
     * 这个配置类用于设置Sa-Token的核心参数
     * 实际配置应在 application.yml 中完成
     */
    public SaTokenConfig() {
        // Sa-Token会在启动时自动从application.yml读取配置
        // 这里可以添加一些编程式配置（如果需要）

        // 设置token名称（默认为satoken）
        // StpUtil.setTokenName("satoken");

        // 设置token有效期（单位：秒，默认2592000 = 30天）
        // StpUtil.setTimeout(2592000);

        // 活跃时间（单位：秒，每次活跃后刷新有效期）
        // StpUtil.setActivityTimeout(3600);

        // 是否尝试读取cookie（默认true）
        // StpUtil.setIsReadCookie(true);

        // 是否返回token到json（默认true）
        // StpUtil.setIsReadPool(true);
    }
}