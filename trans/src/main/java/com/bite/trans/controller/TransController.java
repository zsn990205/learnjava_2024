package com.bite.trans.controller;

import com.bite.trans.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RequestMapping("/trans")
@RestController
public class TransController {
    @Autowired
    private UserService userService;

    //@Transactional 进⼊⽅法时⾃动开启事务, ⽅法执⾏完会⾃动提交事务
    //正常情况下,没有抛出异常
    @Transactional
    @RequestMapping("/register")
    public String register(String name,String password) {
        //⽤户注册
        Integer result = userService.insertUser(name,password);
        log.info("用户插入成功, result :" + result);
        return "注册成功";
    }

    //抛出异常情况下
    @Transactional
    @RequestMapping("/r2")
    public String r2(String name,String password) {
        //⽤户注册
        Integer result = userService.insertUser(name,password);
        log.info("用户插入成功, result :" + result);
        int a = 10 / 0;
        return "注册成功";
    }

    //抛出异常情况下,手动回滚
    @Transactional
    @RequestMapping("/r3")
    public String r3(String name,String password) {
        //⽤户注册
        Integer result = userService.insertUser(name,password);
        log.info("用户插入成功, result :" + result);
        try {
            int a = 10 / 0;
        } catch (Exception e) {
            log.info("程序出错");
            //当前事务的回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return "注册成功";
    }


    @Transactional(rollbackFor = {Exception.class,Error.class})
    @RequestMapping("/r4")
    public String r4(String name,String password) throws IOException {
        //⽤户注册
        Integer result = userService.insertUser(name,password);
        log.info("用户插入成功, result :" + result);
        try {
            int a = 10 / 0;
        } catch (Exception e) {
            log.info("程序出错");
            //当前事务的回滚
            throw new IOException();
        }
        return "注册成功";
    }

}

