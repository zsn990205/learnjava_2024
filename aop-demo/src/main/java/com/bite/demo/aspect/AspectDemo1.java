package com.bite.demo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

@Slf4j
@Aspect
//@Component
@Order(2)
public class AspectDemo1 {
    @Before("com.bite.demo.aspect.AspectDemo.pt()")
    public void doBefore(){
        log.info("执行AspectDemo1 before...");
    }
    @After("com.bite.demo.aspect.AspectDemo.pt()")
    public void doAfter(){
        log.info("执行AspectDemo1 doAfter...");
    }
}
