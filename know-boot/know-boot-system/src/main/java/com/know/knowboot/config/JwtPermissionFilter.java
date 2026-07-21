package com.know.knowboot.config;

import cn.dev33.satoken.stp.StpUtil;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.tenant.SysPermission;
import com.know.knowboot.mapper.tenant.SysPermissionMapper;
import com.know.knowboot.service.ISysPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * JWT权限拦截器
 * 用于动态权限校验 - 基于数据库中的权限配置
 */
@Component
public class JwtPermissionFilter implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtPermissionFilter.class);

    @Autowired
    private ISysPermissionService sysPermissionService;

    /**
     * 预检请求直接放行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS 请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 获取请求路径
        String requestURI = request.getRequestURI();

        // 非API请求放行
        if (!requestURI.startsWith("/api") && !requestURI.startsWith("/adminapi")) {
            return true;
        }

        // 公开API放行（不需要登录）
        if (this.isPublicApi(requestURI)) {
            return true;
        }

        // 获取当前登录用户ID
        Object loginId = StpUtil.getLoginId();
        if (loginId == null) {
            this.writeUnauthorized(response, "未登录");
            return false;
        }

        Long userId = Long.parseLong(loginId.toString());

        // 超级管理员(adminID=1)直接放行
        if (userId.equals(1L)) {
            return true;
        }

        // 获取用户的权限编码列表
        List<String> permissionCodes = sysPermissionService.listCodesByUserId(userId);

        // 获取请求的权限编码
        String requiredPermission = this.extractPermissionCode(requestURI, request.getMethod());

        // 权限匹配校验
        if (!this.hasPermission(permissionCodes, requiredPermission)) {
            log.warn("用户[{}]缺少权限[{}]，请求路径: {}", userId, requiredPermission, requestURI);
            this.writeUnauthorized(response, "没有权限访问该资源");
            return false;
        }

        return true;
    }

    /**
     * 从请求路径提取权限编码
     * 例如: /api/system/user/add -> system:user:add
     */
    private String extractPermissionCode(String requestURI, String method) {
        // 移除前缀
        String path = requestURI;
        if (path.startsWith("/api")) {
            path = path.substring(4);
        } else if (path.startsWith("/adminapi")) {
            path = path.substring(10);
        }

        // 替换斜杠为冒号，并转为小写
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        // 转换为权限编码格式: system:user:add
        String permissionCode = path.replace("/", ":");

        // 根据请求方法添加后缀
        String methodSuffix = method.toLowerCase();
        switch (methodSuffix) {
            case "get":
                // GET请求可能是查询，不需要额外后缀，或者添加:list/:view
                break;
            case "post":
                permissionCode = permissionCode + ":add";
                break;
            case "put":
                permissionCode = permissionCode + ":edit";
                break;
            case "delete":
                permissionCode = permissionCode + ":delete";
                break;
            default:
                break;
        }

        return permissionCode.toLowerCase();
    }

    /**
     * 检查是否具有权限
     */
    private boolean hasPermission(List<String> userPermissions, String requiredPermission) {
        if (userPermissions == null || userPermissions.isEmpty()) {
            return false;
        }

        // 精确匹配
        if (userPermissions.contains(requiredPermission)) {
            return true;
        }

        // 前缀匹配 - 例如有 system:user:add 权限，可以访问 system:user:*
        for (String perm : userPermissions) {
            if (requiredPermission.startsWith(perm.substring(0, perm.lastIndexOf(":")))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查是否是公开API（不需要登录）
     */
    private boolean isPublicApi(String requestURI) {
        // 租户管理公开API
        if (requestURI.contains("/tenant/list") || requestURI.contains("/tenant/page")) {
            return true;
        }
        // 租户角色关联API
        if (requestURI.contains("/tenant/") && requestURI.contains("/roles")) {
            return true;
        }
        // 角色管理公开API
        if (requestURI.contains("/role/list") || requestURI.contains("/role/page")) {
            return true;
        }
        // 菜单管理公开API
        if (requestURI.contains("/menu/list") || requestURI.contains("/menu/tree")) {
            return true;
        }
        // 其他公开API...
        return false;
    }

    /**
     * 写入未授权响应
     */
    private void writeUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        AjaxResult<Object> result = AjaxResult.failed(403, message);
        response.getWriter().write(com.alibaba.fastjson2.JSON.toJSONString(result));
    }
}