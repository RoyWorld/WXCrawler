package com.wxcrawler.util;

import com.wxcrawler.annotation.LogAfter;
import com.wxcrawler.annotation.LogBefore;
import com.wxcrawler.service.impl.ILogServiceImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

/**
 * Created by RoyChan on 2018/6/6.
 */
@Aspect
public class LoggerAspect {

    private Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    @Autowired
    ILogServiceImpl iLogService;

//    @Pointcut("within(com.wxcrawler.util.LoggerAspect)")
//    public void logBefore() {
//    }    //通过执行不同的Pointcut是执行不同的切面方法

    //通过执行不同的Pointcut是执行不同的切面方法
    @Pointcut("@annotation(com.wxcrawler.annotation.LogBefore)")
    public void logBeforeOnAnnotation() {
    }

    //通过执行不同的Pointcut是执行不同的切面方法
    @Pointcut("@annotation(com.wxcrawler.annotation.LogAfter)")
    public void logAfterOnAnnotation() {
    }

    @Around(value = "logBeforeOnAnnotation()")
    public void logBefore(ProceedingJoinPoint pjp/*, String... paramter*/) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Class clazz = method.getDeclaredAnnotation(LogBefore.class).clazz();
        String msg = method.getDeclaredAnnotation(LogBefore.class).msg();
        Logger logger = LoggerFactory.getLogger(clazz);
        logger.debug(msg);
        pjp.proceed();
    }

    @Around(value = "logAfterOnAnnotation()")
    public void logAfter(ProceedingJoinPoint pjp/*, String... paramter*/) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Class clazz = method.getDeclaredAnnotation(LogAfter.class).clazz();
        String msg = method.getDeclaredAnnotation(LogAfter.class).msg();
        Logger logger = LoggerFactory.getLogger(clazz);
        pjp.proceed();
        logger.debug(msg);
    }

}
