package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class propertiesController {
    @Value("${demo.key1}")
    private String k1;

    @RequestMapping("/read")
    public String read() {
        return "读取到的k1是: " + k1;
    }

}
