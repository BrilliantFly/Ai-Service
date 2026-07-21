package com.know.knowboot.annotation.token.aspect;

import com.alibaba.fastjson.JSONObject;
import com.know.knowboot.annotation.token.annotation.TokenValidate;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class TokenValidateAspect {

    @Pointcut("@annotation(com.know.knowboot.annotation.token.annotation.TokenValidate)")
    public void token_point_cut(){
        log.info("自定义注解参数 -> {}","切点");
    }

    @Before("token_point_cut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //获取自定义注解
        TokenValidate annotation = method.getAnnotation(TokenValidate.class);
        String[] requestParams = annotation.token();
        if (requestParams != null && requestParams.length > 0){
            for (int i = 0;i < requestParams.length;i++){
                log.info("自定义注解参数 -> {}",requestParams[i]);
            }
        }else {
            log.info("自定义注解参数 -> {}","null");
        }

        JSONObject jsonObject = null;
        //获取接口的参数值
        Object[] o = joinPoint.getArgs();
        //获取参数
        String[] parameterNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        for(int i=0;i<parameterNames.length;i++){
            String paramName = parameterNames[i];

            if(paramName.equals("jsonObject")){
                jsonObject = (JSONObject) o[i];
                log.info("自定义注解参数 -> {}",jsonObject);
            }
        }

        //参数赋值，比如验证token之后，将token放入参数，controller可以获取
        if (jsonObject != null){
            jsonObject.put("jsonObject","jsonObject");
        }

    }

    /**
     * 后置通知
     * @param joinPoint
     */
    @After("token_point_cut()")
    public  void after(JoinPoint joinPoint) throws Exception{
        log.info("自定义注解参数 -> {}","After");
    }

    /**
     * 后置返回通知
     * 这里需要注意的是:
     *      如果参数中的第一个参数为JoinPoint，则第二个参数为返回值的信息
     *      如果参数中的第一个参数不为JoinPoint，则第一个参数为returning中对应的参数
     *       returning：限定了只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行，
     *       对于returning对应的通知方法参数为Object类型将匹配任何目标返回值
     *
     * @param obj
     * @throws Throwable
     */
    @AfterReturning(returning = "obj",pointcut = "token_point_cut()")
    public void doAfterReturning(Object obj) throws Throwable {
        //处理完请求，返回内容
        log.info("自定义注解参数 -> {}","AfterReturning");
    }

    /**
     * 后置异常通知
     *  定义一个名字，该名字用于匹配通知实现方法的一个参数名，当目标方法抛出异常返回后，将把目标方法抛出的异常传给通知方法；
     *  throwing:限定了只有目标方法抛出的异常与通知方法相应参数异常类型时才能执行后置异常通知，否则不执行，
     *           对于throwing对应的通知方法参数为Throwable类型将匹配任何异常。
     * @param joinPoint
     * @param exception
     */
    @AfterThrowing(value = "token_point_cut()",throwing = "exception")
    public void doAfterThrowing(JoinPoint joinPoint,Throwable exception){
        log.info("自定义注解参数 -> {}","AfterThrowing");
    }

    /**
     *
     *
     * 环绕通知：
     *   环绕通知非常强大，可以决定目标方法是否执行，什么时候执行，执行时是否需要替换方法参数，执行完毕是否需要替换返回值。
     *   环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
     *
     * @param proceedingJoinPoint
     * @return
     */
    @Around(value = "token_point_cut()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("自定义注解参数 -> {}","doAroundAdvice");
        Object obj = proceedingJoinPoint.proceed();
       return obj;
    }

}
