package com.know.knowboot.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.cloud.nacos")
@ConditionalOnProperty(name = "know.mode", havingValue = "microservice", matchIfMissing = false)
public class CloudNacosConfig {
    private Discovery discovery = new Discovery();
    private Config config = new Config();

    @Data
    public static class Discovery {
        private String serverAddr;
    }

    @Data
    public static class Config {
        private String serverAddr;
        private String namespace;
        private String group;
        private String fileExtension = "yaml";
        private String prefix;
        private boolean refresh = true;
    }
}
