package com.example.demo.controller;

import com.example.demo.model.DBType;
import com.example.demo.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
public class ymlController {
    @Value("${demo.key}")
    public String k1;

    @Value("${demo.key2}")
    public String k2;

    @Value("${demo.key3}")
    public String k3;

    @Value("${String.str1}")
    public String str1;

    @Value("${String.str2}")
    public String str2;

    @Value("${String.str3}")
    public String str3;

    @RequestMapping("/readYml")
    public String ky1() {
        return "返回的yml值是: " + k1;
    }

    @Autowired
    public Student student;

    @Autowired
    public DBType dbType;

    @PostConstruct
    public void init() {
//        System.out.println(k1);
//        System.out.println(k2);
//        System.out.println(k3);
//        System.out.println(StringUtils.hasLength(k3));
//        System.out.println("==================");
//        System.out.println("str1 = " + str1);
//        System.out.println("str1 = " + str2);
//        System.out.println("str1 = " + str3);
//        System.out.println("student = " + student);
        System.out.println("DBtype = " + dbType);
    }
}
