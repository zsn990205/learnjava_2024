package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController
public class UserController {
    @RequestMapping("/login")
    public Boolean login(String userName, String password, HttpSession session) {
        //1.校验参数的合法性
        if(userName == null || userName.length() == 0 || password == null ||
                password.length() == 0) {
            //当且仅当密码和用户一一对应的时候才返回true
            return false;
        }
        if(userName.equals("admin") && password.equals("admin")) {
            //下面要查询用户的信息,所以得使用session记录下来
            session.setAttribute("userName","admin");
            return true;
        }
        return false;
    }

    @RequestMapping("/getUserInfo")
    public String getUserInfo(HttpSession session) {
        //从上述的session中获取登录用户
        String userName = (String)session.getAttribute("userName");
        return "获取到的用户姓名是: " + userName;
    }
}
