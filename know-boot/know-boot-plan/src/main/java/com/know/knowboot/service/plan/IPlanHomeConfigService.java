package com.know.knowboot.service.plan;

import java.util.Map;

/**
 * 首页配置服务接口
 */
public interface IPlanHomeConfigService {

    /**
     * 获取首页配置
     */
    Map<String, Map<String, Object>> getConfig(Long userId, String roleId);
}
