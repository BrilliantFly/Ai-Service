package com.know.knowboot.applet.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "know.applet")
public class AppletProperties {

    private String appId = "wx-applet";

    private String appSecert = "wx-applet";

    /**
     * 登录
     */
    private String LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 获取小程序全局唯一后台接口调用凭据（access_token）
     */
    private String GET_ACCESS_TOKE = "https://api.weixin.qq.com/cgi-bin/token";

    /**
     * code换取用户手机号
     */
    private String GET_PHONE_NUMBER = "https://api.weixin.qq.com/wxa/business/getuserphonenumber";

    /**
     * 订阅消息 - 发送订阅消息
     */
    private String SEND_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send";

}
