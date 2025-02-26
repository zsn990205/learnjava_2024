package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/calc")
@RestController
public class CalcController {
    @RequestMapping("/sum")
    public String sum(Integer num1,Integer num2) {
        Integer sum = num1 + num2;
        return "计算结果是: " + sum;
    }
}
