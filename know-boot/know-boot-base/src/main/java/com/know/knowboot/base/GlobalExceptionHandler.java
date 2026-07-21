package com.know.knowboot.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理
 * 自定义注解，可能会导致全局异常处理失效
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 权限不足
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public Result<String> handler(AccessDeniedException e) {
        log.info("--------------- 权限不足 ---------------- e -> {}", e.getMessage());
        return Result.error("权限不足");
    }

    /**
     * 校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<String> handler(MethodArgumentNotValidException e) {
        log.info("--------------- 校验异常 ---------------- e -> {}", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return Result.error(objectError.getDefaultMessage());
    }


    /**
     * 校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    public Result validationExceptionHandler(BindException e) {
        log.info("--------------- 校验异常 ---------------- e -> {}", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        String errorMsg = "";
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMsg += fieldError.getDefaultMessage() + "!";
        }
        return Result.error(errorMsg);
    }

    /**
     * 校验异常
     * @param ex
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result ConstraintViolationExceptionHandler(ConstraintViolationException ex) {
        log.info("--------------- 校验异常 ---------------- e -> {}", ex.getMessage());
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
        List<String> msgList = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<?> cvl = iterator.next();
            msgList.add(cvl.getMessageTemplate());
        }
        return Result.error(String.join(",", msgList));
    }

    /**
     * 非法参数异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result<String> handler(IllegalArgumentException e) {
        log.info("--------------- 非法参数异常 ---------------- e -> {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 运行时异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    public Result<String> handler(RuntimeException e) {
        log.info("--------------- 运行时异常 ---------------- e -> {}", e);
        return Result.error(e.getMessage());
    }

    /**
     * 请求方式不支持
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public Result handleException(HttpRequestMethodNotSupportedException e) {
        log.info("--------------- 请求方式不支持 ---------------- e -> {}", e.getMessage());
        return Result.error("不支持' " + e.getMethod() + "'请求");
    }

    /**
     * 异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public Result<String> handler(Exception e) {
        log.info("--------------- 异常 ---------------- e -> {}", e);
        return Result.error(e.getMessage());
    }

}
