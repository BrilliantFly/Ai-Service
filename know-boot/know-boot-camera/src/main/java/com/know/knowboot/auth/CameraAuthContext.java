package com.know.knowboot.auth;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 摄像头模块认证上下文
 * <p>
 * 从当前请求中提取用户ID。
 * 优先从 TokenAuthInterceptor 设置的请求属性中获取，
 * 若未获取到则返回默认值 1L（兼容未登录的内部调用）。
 */
public class CameraAuthContext {

    private static final String USER_ID_ATTR = "camera_user_id";
    private static final Long DEFAULT_USER_ID = 1L;

    /**
     * 获取当前请求中的用户ID
     *
     * @return 用户ID，未登录时返回默认值 1L
     */
    public static Long getUserId() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes) {
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                Object userId = request.getAttribute(USER_ID_ATTR);
                if (userId instanceof Long) {
                    return (Long) userId;
                }
            }
        } catch (Exception ignored) {
            // 非 Web 环境或请求上下文不可用时返回默认值
        }
        return DEFAULT_USER_ID;
    }

    /**
     * 设置当前请求中的用户ID（由 TokenAuthInterceptor 调用）
     */
    public static void setUserId(HttpServletRequest request, Long userId) {
        if (request != null && userId != null) {
            request.setAttribute(USER_ID_ATTR, userId);
        }
    }
}
