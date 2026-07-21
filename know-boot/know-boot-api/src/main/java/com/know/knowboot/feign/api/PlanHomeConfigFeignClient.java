package com.know.knowboot.feign.api;

import com.know.knowboot.base.Result;
import com.know.knowboot.feign.fallback.PlanHomeConfigFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "plan", path = "/plan/home/config", url = "localhost:8083", fallbackFactory = PlanHomeConfigFeignFallback.class)
public interface PlanHomeConfigFeignClient {

    @GetMapping
    Result<Map<String, Object>> getHomeConfig(@RequestParam(required = false) String roleId);
}
