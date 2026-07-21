package com.know.knowboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
//开启hystrix
@EnableCircuitBreaker
//可视化面板
@EnableHystrixDashboard
//汇总监控turbine
//@EnableTurbine
public class KnowBootCloudHystrixApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowBootCloudHystrixApplication.class, args);
    }

}
