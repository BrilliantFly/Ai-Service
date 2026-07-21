package com.know.knowboot.aspect;

import com.alibaba.fastjson2.JSON;
import com.know.knowboot.aop.OperLog;
import com.know.knowboot.entity.tenant.SysLoginLog;
import com.know.knowboot.entity.tenant.SysOperLog;
import com.know.knowboot.mapper.tenant.SysLoginLogMapper;
import com.know.knowboot.mapper.tenant.SysOperLogMapper;
import cn.dev33.satoken.stp.StpUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 操作日志切面
 * 自动拦截带有 @OperLog 注解的方法并记录日志
 */
@Aspect
@Component
public class OperLogAspect {

    private static final Logger log = LoggerFactory.getLogger(OperLogAspect.class);

    @Autowired
    private SysOperLogMapper sysOperLogMapper;

    @Autowired
    private SysLoginLogMapper sysLoginLogMapper;

    /**
     * 切入点：所有带 @OperLog 注解的 Controller 方法
     */
    @Pointcut("@annotation(com.know.knowboot.aop.OperLog)")
    public void operLogPointcut() {
    }

    /**
     * 环绕通知
     */
    @Around("operLogPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();

        SysOperLog operLog = new SysOperLog();
        operLog.setOperTime(System.currentTimeMillis());

        try {
            // 获取方法签名
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            OperLog operLogAnnotation = method.getAnnotation(OperLog.class);

            // 获取请求信息
            HttpServletRequest request = getRequest(point);
            if (request != null) {
                // 设置操作基本信息
                operLog.setOperUrl(request.getRequestURI());
                operLog.setRequestMethod(request.getMethod());
                operLog.setOperIp(getClientIp(request));

                // 获取请求参数
                String operParam = getOperParam(point, request);
                operLog.setOperParam(operParam);
            }

            // 设置操作模块和业务类型
            if (operLogAnnotation != null) {
                operLog.setTitle(operLogAnnotation.value());
                operLog.setBusinessType(operLogAnnotation.businessType());
                operLog.setMethod(signature.getDeclaringTypeName() + "." + method.getName());
            }

            // 获取登录用户信息
            Object loginId = StpUtil.getLoginId();
            if (loginId != null) {
                operLog.setOperName(loginId.toString());
                operLog.setOperatorType(1); // 后台用户
            }

            // 执行方法
            Object result = point.proceed();

            // 设置返回结果
            operLog.setJsonResult(JSON.toJSONString(result));
            operLog.setStatus(0); // 正常

            return result;
        } catch (Throwable e) {
            operLog.setStatus(1); // 异常
            operLog.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            // 计算执行时间
            long endTime = System.currentTimeMillis();
            operLog.setOperTime(operLog.getOperTime());

            // 保存日志
            saveOperLog(operLog);

            log.debug("操作日志记录: {} - {} - {}ms",
                    operLog.getOperName(),
                    operLog.getTitle(),
                    endTime - startTime);
        }
    }

    /**
     * 获取 HttpServletRequest
     */
    private HttpServletRequest getRequest(ProceedingJoinPoint point) {
        for (Object arg : point.getArgs()) {
            if (arg instanceof HttpServletRequest) {
                return (HttpServletRequest) arg;
            }
        }
        return null;
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
        // 多个代理时取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 获取操作参数
     */
    private String getOperParam(ProceedingJoinPoint point, HttpServletRequest request) {
        try {
            List<String> paramList = new ArrayList<>();
            for (Object arg : point.getArgs()) {
                if (arg == null) continue;
                if (arg instanceof HttpServletRequest) continue;
                if (arg instanceof MultipartHttpServletRequest) continue;

                String json = JSON.toJSONString(arg);
                if (json.length() > 2000) {
                    json = json.substring(0, 2000) + "...";
                }
                paramList.add(json);
            }
            return paramList.isEmpty() ? null : String.join(", ", paramList);
        } catch (Exception e) {
            return "参数获取失败";
        }
    }

    /**
     * 保存操作日志
     */
    private void saveOperLog(SysOperLog operLog) {
        try {
            sysOperLogMapper.insert(operLog);
        } catch (Exception e) {
            log.error("保存操作日志失败: {}", e.getMessage());
        }
    }

    /**
     * 记录登录日志
     */
    public void saveLoginLog(String username, String ipaddr, Integer status, String msg) {
        try {
            SysLoginLog loginLog = new SysLoginLog();
            loginLog.setUsername(username);
            loginLog.setIpaddr(ipaddr);
            loginLog.setStatus(status);
            loginLog.setMsg(msg);
            loginLog.setLoginTime(System.currentTimeMillis());
            sysLoginLogMapper.insert(loginLog);
        } catch (Exception e) {
            log.error("保存登录日志失败: {}", e.getMessage());
        }
    }
}