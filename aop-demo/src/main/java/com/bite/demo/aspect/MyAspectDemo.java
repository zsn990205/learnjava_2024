package com.bite.demo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MyAspectDemo {
    @Before("@annotation(com.bite.demo.aspect.MyAspect)")
    public void doBefore() {
        log.info("执行MyAspect before...");
    }

    @After("@annotation(com.bite.demo.aspect.MyAspect)")
    public void doAfter() {
        log.info("执行MyAspect after...");
    }
}
