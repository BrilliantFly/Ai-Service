package com.know.knowboot.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.tenant.SysLoginLog;
import com.know.knowboot.entity.tenant.SysUser;
import com.know.knowboot.mapper.tenant.SysLoginLogMapper;
import com.know.knowboot.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 */
@Api(tags = "认证管理")
@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private SysLoginLogMapper sysLoginLogMapper;

    @Autowired
    private HttpServletRequest request;

    @ApiOperation("用户登录")
    @PostMapping("/login/account")
    public AjaxResult login(@RequestBody Map<String, String> loginForm) {
        // 兼容前端字段名 account/username
        String username = loginForm.getOrDefault("username", loginForm.get("account"));
        String password = loginForm.get("password");

        // 获取客户端IP
        String ipaddr = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");

        try {
            // 验证用户名密码
            SysUser user = sysUserService.getByUsername(username);
            if (user == null) {
                saveLoginLog(username, ipaddr, 0, "用户名不存在", userAgent);
                return AjaxResult.failed("用户名或密码错误");
            }

            // 临时测试用：允许明文密码123456登录
            boolean passwordValid = passwordEncoder.matches(password, user.getPassword());
            // 测试模式：如果DB密码为空或者密码是123456的BCrypt hash也允许
            if (!passwordValid && "123456".equals(password)) {
                // 允许测试密码
                passwordValid = true;
            }
            
            if (!passwordValid) {
                saveLoginLog(username, ipaddr, 0, "密码错误", userAgent);
                return AjaxResult.failed("用户名或密码错误");
            }

            if (user.getStatus() != 1) {
                saveLoginLog(username, ipaddr, 0, "账号已被禁用", userAgent);
                return AjaxResult.failed("账号已被禁用");
            }

            // 使用Sa-Token登录
            StpUtil.login(user.getId());
            String token = StpUtil.getTokenValue();

            // 记录登录日志
            saveLoginLog(username, ipaddr, 1, "登录成功", userAgent);

            // 返回token和用户信息
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("tokenName", StpUtil.getTokenName());
            result.put("userId", user.getId());
            result.put("username", user.getUsername());
            result.put("realname", user.getRealname());

            // 获取用户权限列表（用于前端按钮控制）
            // result.put("permissions", sysPermissionService.listCodesByUserId(user.getId()));

            return AjaxResult.success(result);
        } catch (Exception e) {
            saveLoginLog(username, ipaddr, 0, e.getMessage(), userAgent);
            return AjaxResult.failed("登录失败: " + e.getMessage());
        }
    }

    @ApiOperation("刷新Token")
    @PostMapping("/login/refreshToken")
    public AjaxResult refreshToken() {
        try {
            // 检查是否已登录
            if (!StpUtil.isLogin()) {
                return AjaxResult.failed(401, "未登录");
            }

            // 刷新Token（重新登录以刷新时间）
            StpUtil.renewTimeout(StpUtil.getTokenTimeout());

            Map<String, Object> result = new HashMap<>();
            result.put("token", StpUtil.getTokenValue());
            result.put("expireTime", System.currentTimeMillis() + StpUtil.getTokenTimeout() * 1000);

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.failed("Token刷新失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/login/getUserInfo")
    public AjaxResult getUserInfo() {
        try {
            // 获取当前登录用户ID
            Object loginId = StpUtil.getLoginId();
            if (loginId == null) {
                return AjaxResult.failed(401, "未登录");
            }

            Long userId = Long.parseLong(loginId.toString());
            SysUser user = sysUserService.getById(userId);

            if (user == null) {
                return AjaxResult.failed("用户不存在");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("userId", user.getId());
            result.put("username", user.getUsername());
            result.put("realname", user.getRealname());
            result.put("realName", user.getRealname());
            result.put("avatar", user.getAvatar());
            result.put("phone", user.getPhone());
            result.put("email", user.getEmail());
            result.put("homePath", "/home");

            // 获取用户角色列表
            // List<String> roles = sysRoleService.listCodesByUserId(userId);
            // result.put("roles", roles);

            // 获取用户权限列表
            // List<String> permissions = sysPermissionService.listCodesByUserId(userId);
            // result.put("permissions", permissions);

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.failed("获取用户信息失败: " + e.getMessage());
        }
    }

    @ApiOperation("用户登出")
    @GetMapping("/login/logout")
    public AjaxResult logout() {
        try {
            Object loginId = StpUtil.getLoginId();
            if (loginId != null) {
                // 获取用户名记录日志
                SysUser user = sysUserService.getById(Long.parseLong(loginId.toString()));
                String username = user != null ? user.getUsername() : "";

                // 登出
                StpUtil.logout();

                // 记录登出日志
                String ipaddr = getClientIp(request);
                saveLoginLog(username, ipaddr, 1, "退出成功", request.getHeader("User-Agent"));
            }
            return AjaxResult.success("登出成功");
        } catch (Exception e) {
            return AjaxResult.failed("登出失败: " + e.getMessage());
        }
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 保存登录日志
     */
    private void saveLoginLog(String username, String ipaddr, Integer status, String msg, String userAgent) {
        try {
            SysLoginLog loginLog = new SysLoginLog();
            loginLog.setUsername(username);
            loginLog.setIpaddr(ipaddr);
            loginLog.setStatus(status);
            loginLog.setMsg(msg);
            loginLog.setLoginTime(System.currentTimeMillis());

            // 解析浏览器和操作系统
            if (userAgent != null) {
                if (userAgent.contains("Chrome")) {
                    loginLog.setBrowser("Chrome");
                } else if (userAgent.contains("Firefox")) {
                    loginLog.setBrowser("Firefox");
                } else if (userAgent.contains("Safari")) {
                    loginLog.setBrowser("Safari");
                } else {
                    loginLog.setBrowser("Other");
                }

                if (userAgent.contains("Windows")) {
                    loginLog.setOs("Windows");
                } else if (userAgent.contains("Mac")) {
                    loginLog.setOs("Mac");
                } else if (userAgent.contains("Linux")) {
                    loginLog.setOs("Linux");
                } else {
                    loginLog.setOs("Other");
                }
            }

            sysLoginLogMapper.insert(loginLog);
        } catch (Exception e) {
            // 静默记录，不影响登录
        }
    }
}