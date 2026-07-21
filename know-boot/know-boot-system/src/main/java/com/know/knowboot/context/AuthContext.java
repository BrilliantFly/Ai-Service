package com.know.knowboot.context;

import cn.dev33.satoken.stp.StpUtil;
import lombok.Data;

import java.util.List;

/**
 * 认证上下文
 * 用于获取当前登录用户信息
 */
public class AuthContext {

    @Data
    public static class UserInfo {
        private Long userId;
        private String username;
        private List<Long> roleIds;
    }

    /**
     * 获取当前登录用户信息
     */
    public static UserInfo getCurrentUser() {
        try {
            Object loginId = StpUtil.getLoginId();
            if (loginId == null) {
                return null;
            }
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(Long.parseLong(loginId.toString()));
            return userInfo;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前用户ID
     */
    public static Long getUserId() {
        try {
            Object loginId = StpUtil.getLoginId();
            if (loginId != null) {
                return Long.parseLong(loginId.toString());
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}