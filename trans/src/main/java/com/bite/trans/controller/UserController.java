package com.bite.trans.controller;

import com.bite.trans.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/register")
    //在此处写用户的注册
    public String register(String userName,String password) {
        userService.insertUser(userName,password);
        return "注册成功";
    }
}
