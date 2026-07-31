package com.know.knowboot.config;

import com.know.knowboot.KnowBootSystemInterceptor;
import com.know.knowboot.common.GlobalConfig;
import com.know.knowboot.common.YmlUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Web配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    KnowBootSystemInterceptor likeAdminInterceptor;

    @Resource
    JwtPermissionFilter jwtPermissionFilter;

    /**
     * 登录拦截器 + 权限拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截器
        registry.addInterceptor(likeAdminInterceptor)
                .addPathPatterns("/api/**", "/adminapi/**")
                .excludePathPatterns("/api/login/**", "/adminapi/login/**",
                        "/api/index/decorate", "/api/index/config", "/api/index/init-plan-menu",
                        "/api/plan/**",
                        "/api/system/menu/list", "/api/system/menu/tree",
                        "/api/system/menu/config/tabbar", "/api/system/menu/config/home",
                        "/api/system/menu/config/tabbar/user", "/api/system/menu/config/home/user",
                        "/api/system/menu/config/user",
                        "/api/system/menu/config/type/**", "/api/system/menu/config/list",
                        "/api/system/menu/config/page",
                        "/api/system/dict/type/list", "/api/system/dict/list");

        // 权限拦截器 - 在登录拦截器之后
        registry.addInterceptor(jwtPermissionFilter)
                .addPathPatterns("/api/**", "/adminapi/**")
                .excludePathPatterns("/api/login/**", "/adminapi/login/**",
                        "/api/index/decorate", "/api/index/config", "/api/index/init-plan-menu",
                        "/api/plan/**",
                        "/api/system/menu/list", "/api/system/menu/tree",
                        "/api/system/menu/config/tabbar", "/api/system/menu/config/home",
                        "/api/system/menu/config/tabbar/user", "/api/system/menu/config/home/user",
                        "/api/system/menu/config/user",
                        "/api/system/menu/config/type/**", "/api/system/menu/config/list",
                        "/api/system/menu/config/page",
                        "/api/system/dict/type/list", "/api/system/dict/list");
    }

    /**
     * 资源目录映射
     */
    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        String directory = YmlUtils.get("like.upload-directory");
        registry.addResourceHandler("/"+ GlobalConfig.adminPublicPrefix +"/**")
                .addResourceLocations("file:" + directory);
    }

}
