package com.bite.demo.controller;

import com.bite.demo.aspect.MyAspect;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloControllerT1 {
    @MyAspect
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @MyAspect
    @RequestMapping("/h1")
    public String h1() {
        return "h1";
    }

    @RequestMapping("/h2")
    public String h2() {
        return "h2";
    }
}
