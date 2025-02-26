package com.example.demo.controller;

import com.example.demo.model.Person;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

//加上类路径(开发建议)
//1.避免重复
//2.方便查找代码
@RequestMapping("/param")
@RestController
public class ParamController {
    @RequestMapping("/m1")
    public String m1(String name) {
        //传递单个参数的时候直接接收
        return "接收到的参数name: " + name;
    }

    @RequestMapping("/m2")
    public String m2(String name,Integer age) {
        //传递多个参数
        //开发时建议使用包装类
        return "接收到的参数name: " + name + " 接收到的参数age: " + age;
    }

    @RequestMapping("/m4")
    public String m4(Person person) {
        return "接收到的参数: " + person.toString();
    }

    @RequestMapping("/m5")
    public String m5(@RequestParam(value = "name",required = false) String username) {
        //前端给的name太广泛了,我想修改name为具体的name
        return "接收到的参数name: " + username;
    }

    @RequestMapping("/m6")
    public String m6(String[] arrParam) {
        return "传递的数组是: " + Arrays.toString(arrParam) + ",长度是: " + arrParam.length;
    }

    @RequestMapping("/m7")
    //json格式
    public String m7(@RequestParam List<String> listParam) {
        return "传递的集合是: " + listParam+ ",长度是: " + listParam.size();
    }

    @RequestMapping("/m8")
    public String m8(@RequestBody Person person) {
        return "接收到的数据是person: " + person.toString();
    }

    @RequestMapping("/m9/{userId}")
    public String m9(@PathVariable Integer userId) {
        return "接收到的userId " + userId;
    }

    @RequestMapping("/m10/{userId}/{username}")
    public String m10(@PathVariable Integer userId,@PathVariable String username) {
        return "接收到的userId " + userId + " " + username;
    }

    @RequestMapping("/m11")
    public String m11(@RequestPart MultipartFile file) {
        System.out.println(file.getOriginalFilename());
        return "success";
    }

    @RequestMapping("/getCookie")
    public String getCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {
            System.out.println(cookie.getName() + " " + cookie.getValue());
        }
        return "success";
    }

    @RequestMapping("/getCookie2")
    public String m12(@CookieValue String bit, @CookieValue String aaa) {
        return "cookie存的bite: " + bit + " ,aaa: " + aaa;
    }

    @RequestMapping("/setSession")
    public String setSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("username","huhu");
        return "success";
    }

    @RequestMapping("/getSession")
    public String getSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            String username = (String)session.getAttribute("username");
            return "登录用户是: " + username;
        }
        return "session为空!";
    }

    @RequestMapping("/getSession2")
    public String getSession2(@SessionAttribute(required = false) String username) {
        return "username: " + username;
    }

    @RequestMapping("/getHeader")
    public String getHeader(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        return "获取的header是: " + header;
    }

    @RequestMapping("/getHeader2")
    public String getHeader2(@RequestHeader("User-Agent") String userAgent) {
        return "获取的header是: " + userAgent;
    }
}
