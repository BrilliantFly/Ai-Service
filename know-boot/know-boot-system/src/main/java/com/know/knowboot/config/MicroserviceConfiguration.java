package com.know.knowboot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "know.mode", havingValue = "microservice")
public class MicroserviceConfiguration {
}
