package com.know.knowboot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 忽略授权设定
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "know.ignore")
public class IgnoredUrlsProperties {

    private List<String> urls = new ArrayList<>();
}
