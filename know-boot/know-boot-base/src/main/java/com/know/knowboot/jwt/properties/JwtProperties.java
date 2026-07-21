package com.know.knowboot.jwt.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "know.jwt")
public class JwtProperties {

    /**
     * 密钥
     */
    private String secret;

    /**
     * token有效期 (S)
     */
    private Long expiration;

}
