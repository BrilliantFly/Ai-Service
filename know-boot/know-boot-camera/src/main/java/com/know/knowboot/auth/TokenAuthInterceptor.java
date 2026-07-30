package com.know.knowboot.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Token 认证拦截器
 * <p>
 * 验证请求中的 Token，提取用户ID并设置到 CameraAuthContext。
 * Token 验证方式：查询 Redis 中 Sa-Token 存储的会话信息。
 * Sa-Token 在 Redis 中的 key 格式为: satoken:token:{tokenValue}
 * <p>
 * 单机模式下若 Redis 不可用，所有请求放行并使用默认用户ID。
 * 微服务模式下 Token 验证由 Gateway 统一处理，此拦截器仅提取用户信息。
 */
@Component
public class TokenAuthInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TokenAuthInterceptor.class);
    private static final String SA_TOKEN_KEY_PREFIX = "satoken:token:";
    private static final String LOGIN_ID_KEY = "loginId";

    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // OPTIONS 预检请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 从请求头中获取 Token（兼容 token 和 Authorization 两种方式）
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }

        if (token == null || token.isEmpty()) {
            // 无 Token 时使用默认用户（兼容非认证场景）
            CameraAuthContext.setUserId(request, 1L);
            return true;
        }

        // 从 Redis 中验证 Token 并获取用户ID
        Long userId = resolveUserIdFromToken(token);
        if (userId != null) {
            CameraAuthContext.setUserId(request, userId);
        } else {
            // Token 无效或 Redis 不可用时使用默认用户
            if (stringRedisTemplate != null) {
                log.warn("Token 验证失败，使用默认用户ID: 1L");
            }
            CameraAuthContext.setUserId(request, 1L);
        }

        return true;
    }

    /**
     * 从 Redis 中解析 Token 对应的用户ID
     * Sa-Token 的 Redis 存储结构: satoken:token:{token} -> { "loginId": "123" }
     */
    private Long resolveUserIdFromToken(String token) {
        if (stringRedisTemplate == null) {
            return null;
        }
        try {
            String redisKey = SA_TOKEN_KEY_PREFIX + token;
            Object loginIdObj = stringRedisTemplate.opsForHash().get(redisKey, LOGIN_ID_KEY);
            if (loginIdObj != null) {
                return Long.parseLong(loginIdObj.toString());
            }
        } catch (Exception e) {
            log.debug("Token 解析失败: {}", e.getMessage());
        }
        return null;
    }
}
