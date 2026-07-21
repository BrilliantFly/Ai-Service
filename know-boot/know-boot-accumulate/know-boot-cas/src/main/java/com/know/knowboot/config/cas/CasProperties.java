package com.know.knowboot.config.cas;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class CasProperties {

    /**
     * cas服务端地址
     */
    @Value("${cas.server.host}")
    private String casServerUrl;

    /**
     * cas服务端登录地址
     */
    @Value("${cas.server.login_url}")
    private String casServerLoginUrl;

    /**
     * cas服务端登出地址 并回跳到制定页面
     */
    @Value("${cas.server.logout_url}")
    private String casServerLogoutUrl;

    /**
     * cas客户端开启cas
     */
    @Value("${app.caseEnable}")
    private String caseEnable;

    /**
     * 秘钥
     */
    @Value("${app.key}")
    private String casKey;

    /**
     * cas客户端地址
     */
    @Value("${app.server.url}")
    private String appServerUrl;

    /**
     * cas客户端地址登录地址
     */
    @Value("${app.server.login_url}")
    private String appServerLoginUrl;

    /**
     * cas客户端地址登出地址
     */
    @Value("${app.server.logout_url}")
    private String casServiceLogoutUrl;

    /**
     * cas客户端地址登出地址
     */
    @Value("${app.server.web_url}")
    private String webUrl;

}
