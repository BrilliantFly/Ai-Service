//package com.know.knowboot.annotation.log.aspect;
//
//import com.alibaba.fastjson.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//
//@Aspect
//@Component
//@Slf4j
//public class LogAspect {
//
//    //定义切点 @Pointcut
//    //在注解的位置切入代码
//    @Pointcut("@annotation(com.know.knowboot.annotation.log.annotation.OperationLog)")
//    public void logPointCut() {
//    }
//
//    //切面 配置通知
//    @AfterReturning("logPointCut()")
//    public void saveSysLog(JoinPoint joinPoint) {
//
//        log.info("开始记录日志了..............");
//        //保存日志
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        //获取切入点所在的方法
//        Method method = signature.getMethod();
//        //获取操作
//        //获取请求的类名
//        String className = joinPoint.getTarget().getClass().getName();
//        //获取请求的方法名
//        String methodName = method.getName();
//        String methodStr = (className + "." + methodName);
//        // 构造参数组集合
//        JSONObject allParams = new JSONObject();
//
//        for (Object arg : joinPoint.getArgs()) {
//            if (arg instanceof JSONObject) {
//                allParams.putAll((JSONObject) arg);
//            }
//        }
//
//        //获取用户ip地址
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String url = request.getRequestURI();
//
//        String params = "";
//        //请求参数
//        if (!allParams.isEmpty()) {
//            params = allParams.toJSONString();
//        }
//        if(params.length()> 1000){
//            params = params.substring(0,997).concat("...");
//        }
//    }
//
//}
