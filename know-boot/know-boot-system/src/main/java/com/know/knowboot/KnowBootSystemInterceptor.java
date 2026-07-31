package com.know.knowboot;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.exception.NotLoginException;
import com.alibaba.fastjson2.JSON;
import com.know.knowboot.service.IAdminRoleService;
import com.know.knowboot.aop.NotLogin;
import com.know.knowboot.aop.NotPower;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.tenant.SysUser;
import com.know.knowboot.enums.ErrorEnum;
import com.know.knowboot.exception.LoginException;
import com.know.knowboot.service.ISysUserService;
import com.know.knowboot.util.StringUtils;
import com.know.knowboot.common.YmlUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 拦截器
 */
@Component
public class KnowBootSystemInterceptor implements HandlerInterceptor {

    @Resource
    private ISysUserService sysUserService;

    @Resource
    IAdminRoleService iAdminRoleService;

    /**
     * 前置处理器
     *
     * @param request 请求
     * @param response 响应
     * @param handler 处理
     * @return boolean
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) throws Exception {

        // 请求方法类型
        String reqUri = request.getRequestURI();
        if (!(handler instanceof HandlerMethod) || ( !reqUri.startsWith("/api") &&  !reqUri.startsWith("/adminapi"))) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        // 登录权限校验
        try {
            response.setContentType("application/json;charset=utf-8");
            Method method = this.obtainAop(handler);
            this.checkLogin(method, reqUri);
        } catch (LoginException e) {
            AjaxResult<Object> result = AjaxResult.failed(e.getCode(), e.getMsg());
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        } catch (NotLoginException e) {
            // Sa-Token登录校验异常（token无效/过期），统一返回TOKEN_INVALID
            Integer errCode = ErrorEnum.TOKEN_INVALID.getCode();
            String errMsg = ErrorEnum.TOKEN_INVALID.getMsg();
            AjaxResult<Object> result = AjaxResult.failed(errCode, errMsg);
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }

        // 演示环境拦截
        String env = YmlUtils.get("like.production");
        if (StringUtils.isNotNull(env) && env.equals("true")) {
            String prefix = "/adminapi/";
            String route = request.getRequestURI().replaceFirst(prefix, "");
//            String auths = route.replace("/", ":");
            String auths = route;
            List<String> ignoreUrl = Arrays.asList("login/account", "login/logout");
            if (request.getMethod().equals("POST") && !ignoreUrl.contains(auths)) {
                String message = "演示环境不支持修改数据，请下载源码本地部署体验";
                AjaxResult<Object> result = AjaxResult.failed(ErrorEnum.NO_PERMISSION.getCode(), message);
                response.getWriter().print(JSON.toJSONString(result));
                return false;
            }
        }

        // 验证通过继续
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * 后置处理器
     *
     * @param request 请求
     * @param response 响应
     * @param handler 处理
     * @param ex 异常
     * @throws Exception 异常
     */
    @Override
    public void afterCompletion(@NotNull HttpServletRequest request,
                                @NotNull HttpServletResponse response,
                                @NotNull Object handler, Exception ex) throws Exception {
        KnowBootSystemThreadLocal.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    /**
     * 提取注解
     *
     * @param handler 处理器
     * @return Method
     * @throws Exception 异常
     */
    private Method obtainAop(@NotNull Object handler) throws Exception {
        String[] objArr = handler.toString().split("#");
        String methodStr = objArr[1].split("\\(")[0];
        String classStr = objArr[0];
        Class<?> clazz = Class.forName(classStr);

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodStr)) {
                return method;
            }
        }

        return null;
    }

    /**
     * 登录验证
     *
     * @param method 方法类
     * @author fzr
     */
    private void checkLogin(Method method, String reqUri) {
        for (int i=0; i<=0; i++) {
            // 免登校验
            if (StringUtils.isNotNull(method) && method.isAnnotationPresent(NotLogin.class)) {
                break;
            }

            // 令牌校验
            String token = StpUtil.getTokenValue();
            if (StringUtils.isNull(token) || StringUtils.isBlank(token)) {
                Integer errCode = ErrorEnum.TOKEN_EMPTY.getCode();
                String errMsg = ErrorEnum.TOKEN_EMPTY.getMsg();
                throw new LoginException(errCode, errMsg);
            }

            // 登录校验
            Object id = StpUtil.getLoginId();
            if (StringUtils.isNull(id)) {
                Integer errCode = ErrorEnum.TOKEN_INVALID.getCode();
                String errMsg = ErrorEnum.TOKEN_INVALID.getMsg();
                throw new LoginException(errCode, errMsg);
            }

            // 用户校验 (使用新版 sys_user 表)
            SysUser sysUser = sysUserService.getById(Long.parseLong(id.toString()));
            if (StringUtils.isNull(sysUser)) {
                Integer errCode = ErrorEnum.TOKEN_INVALID.getCode();
                String errMsg = ErrorEnum.TOKEN_INVALID.getMsg();
                throw new LoginException(errCode, errMsg);
            }

            // 禁用校验 (status: 1=正常, 2=冻结)
            if (sysUser.getStatus() != null && sysUser.getStatus().equals(2)) {
                Integer errCode = ErrorEnum.LOGIN_DISABLE_ERROR.getCode();
                String errMsg = ErrorEnum.LOGIN_DISABLE_ERROR.getMsg();
                throw new LoginException(errCode, errMsg);
            }

            // 写入线程
            KnowBootSystemThreadLocal.put("adminId", id);
            KnowBootSystemThreadLocal.put("username", sysUser.getRealname());
            String roleIds = StringUtils.join(iAdminRoleService.getRoleIdAttr(Integer.parseInt(String.valueOf(id))), ",");
            KnowBootSystemThreadLocal.put("roleIds", roleIds);

            // 权限校验
            if (!sysUser.getId().equals(1L)) {
                this.checkAuth(method, reqUri);
            }
        }
    }

    /**
     * 权限验证
     *
     * @param method 方法类
     * @param uri 请求路由
     * @author fzr
     */
    private void checkAuth(Method method, String uri) {
        for (int i=0; i<=0; i++) {
            // 免权限校验
            if (StringUtils.isNotNull(method) && method.isAnnotationPresent(NotPower.class)) {
                break;
            }

            // 路由转权限
            String prefix = "/adminapi/";
            String route = uri.replaceFirst(prefix, "");
//            String auths = route.replace("/", ":");
            String auths = route;
            // 校验权限
            StpUtil.checkPermission(auths);
        }
    }
}
