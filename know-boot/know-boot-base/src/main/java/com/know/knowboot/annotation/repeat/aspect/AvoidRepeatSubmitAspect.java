package com.know.knowboot.annotation.repeat.aspect;

import com.know.knowboot.annotation.repeat.annotation.AvoidRepeatSubmit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class AvoidRepeatSubmitAspect {

    @Before("@annotation(com.know.knowboot.annotation.repeat.annotation.AvoidRepeatSubmit)")
    public void repeatSumbitIntercept(JoinPoint joinPoint) {
        // ip + 类名 + 方法 + timeout
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = null;

        String className = joinPoint.getTarget().getClass().getName();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();

        // 获取配置的过期时间
        AvoidRepeatSubmit annotation = method.getAnnotation(AvoidRepeatSubmit.class);
        long timeout = annotation.timeout();

        StringBuilder builder = new StringBuilder();
        builder.append(ip).append(",").append(className).append(",").append(methodName).append(",").append(timeout).append("s");
        String key = builder.toString();
        log.info(" --- >> 防重提交：key -- {}", key);

        // 判断是否已经超过重复提交的限制时间
//        String value = redisRepository.get(key);
//        if (StringUtils.isEmpty(value)) {
//            String messge = MessageFormat.format("请勿在{0}s内重复提交", timeout);
//            throw new WebException(messge);
//        }
//        this.redisRepository.setExpire(key, key, timeout);
    }


    //下面方法作为参考

//    /**
//     * 切点
//     * @param resubmit
//     */
//    @Pointcut("@annotation(resubmit)")
//    public void pointCut(Resubmit resubmit) {
//    }
//
//
//    /**
//     * 环绕
//     * @param joinPoint
//     * @param resubmit
//     * @return
//     * @throws Throwable
//     */
//    @Around("pointCut(resubmit)")
//    public Object repeatSubmit(ProceedingJoinPoint joinPoint, Resubmit resubmit) throws Throwable {
//
//        log.info("..........................防止重复提交.................................");
//
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String ip = IpUtils.getIpAddress(request);
//
//        String className = joinPoint.getTarget().getClass().getName();
//
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        String methodName = method.getName();
//
//        if (Objects.nonNull(resubmit)) {
//
//            StringBuilder builder = new StringBuilder();
//            builder.append(ip).append(",").append(className).append(",").append(methodName).append(",").append(resubmit.lockTime()).append("s");
//            String key = builder.toString();
//
//            // 公平加锁，lockTime后锁自动释放
//            boolean isLocked = false;
//            try {
//                isLocked = redissonLockClient.fairLock(key, TimeUnit.SECONDS, resubmit.lockTime());
//                // 如果成功获取到锁就继续执行
//                if (isLocked) {
//                    // 执行进程
//                    return joinPoint.proceed();
//                } else {
//                    // 未获取到锁
//                    throw new Exception("请勿重复提交");
//                }
//            } finally {
//                // 如果锁还存在，在方法执行完成后，释放锁
//                if (isLocked) {
//                    isLocked = redissonLockClient.existKey(key);
//                    if (isLocked){
//                        redissonLockClient.unlock(key);
//                    }
//                }
//            }
//        }
//
//        return joinPoint.proceed();
//    }

}
