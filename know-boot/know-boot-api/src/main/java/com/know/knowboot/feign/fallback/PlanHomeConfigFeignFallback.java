package com.know.knowboot.feign.fallback;

import com.know.knowboot.base.Result;
import com.know.knowboot.feign.api.PlanHomeConfigFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class PlanHomeConfigFeignFallback implements PlanHomeConfigFeignClient {

    @Override
    public Result<Map<String, Object>> getHomeConfig(String roleId) {
        log.warn("PlanHomeConfigFeign getHomeConfig fallback");
        return Result.ok(new HashMap<>());
    }
}
