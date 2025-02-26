package com.example.demo.controller;

import com.example.demo.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/return")
@Controller
public class ReturnController {
    @RequestMapping("/index")
    public String returnRequest() {
        return "/index.html";
    }

    @ResponseBody
    @RequestMapping("/data")
    public String returnData() {
        return "返回视图的数据";
    }

    @ResponseBody
    @RequestMapping("/html")
    public String returnHTML() {
        return "<h1>返回html片段</h1>";
    }

    @ResponseBody
    @RequestMapping("/json")
    public Person returnJson() {
        Person person = new Person();
        person.setId(1);
        person.setAge(18);
        person.setName("huhu");
        return person;
    }

    @ResponseBody
    @RequestMapping("/json2")
    public Map returnJson2() {
        Map<String,String> kv = new HashMap<>();
        kv.put("k1","v1");
        kv.put("k2","v2");
        kv.put("k3","v3");
        return kv;
    }

    @ResponseBody
    @RequestMapping("/status")
    public String returnStatus(HttpServletResponse response) {
        //状态码一般是http设置的,一般不需要管
        response.setStatus(401);
        return "当前状态未登录!";
    }

    @ResponseBody
    //@RequestMapping(value="/r1",produces = "application/json;charset=utf-8")
    @RequestMapping(value="/r1")
    public String r1() {
        return "{'OK':1}";
    }
}
