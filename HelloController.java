package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/sayhi")
    public String sayHi() {
        return "hi springBoot";
    }

    @RequestMapping("/sayhello")
    public String sayHello() {
        return "hello springBoot";
    }
}
