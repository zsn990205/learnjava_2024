package com.bite.book.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TimeAspect {

    @Around("execution(* com.bite.book.controller.*.*(..))")
    public Object timeCost(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        //执行目标方法
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        log.info(joinPoint + "消耗时间: " + (end-start) + "ms");
        return result;
    }
}
