package com.know.knowboot.annotation.cache.aspect;

import com.know.knowboot.annotation.cache.annotation.CustomCache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;

@Aspect
@Component
@Slf4j
public class CustomCacheAspect {

    private static HashMap<String, Object> cacheMap = new HashMap<>();

    @Pointcut("@annotation(com.know.knowboot.annotation.cache.annotation.CustomCache)")
    public void cache() {
    }

    @Around("cache()")
    public Object printLog(ProceedingJoinPoint joinPoint) {

        log.info("自定义缓存开始..................");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        //获取操作
        CustomCache customCache = method.getAnnotation(CustomCache.class);
        String prefix = customCache.prefix();
        if (prefix == null || prefix.equals("")) {
            //如果前缀为空，默认使用类型+方法名作为缓存的前缀
            //获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            //获取请求的方法名
            String methodName = method.getName();
            prefix = className + "-" + methodName;
        }
        String key = customCache.key();
        if (key == null || key.equals("")) {
            //获取接口的参数
            Object[] o = joinPoint.getArgs();
            //如果key为空，默认使用参数名称为id的值作为id
            String[] parameterNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
            for (int i = 0; i < parameterNames.length; i++) {
                String paramName = parameterNames[i];
                if (paramName.equals("id")) {
                    key = o[i].toString();
                }
            }
        }
        String cacheKey = prefix + key;
        Object result = cacheMap.get(cacheKey);
        if (result != null) {
            //缓存不为空，直接返回缓存结果
            return result;
        }
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        cacheMap.put(cacheKey, result);
        return result;
    }

}
