package com.bite.blog.controller;

import com.bite.blog.common.Constants;
import com.bite.blog.model.Result;
import com.bite.blog.model.UserInfo;
import com.bite.blog.service.BlogService;
import com.bite.blog.service.UserService;
import com.bite.blog.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    private BlogService blogService;

    @RequestMapping("/login")
    public Result login(String userName, String password) {
        //1.参数校验
        if(!StringUtils.hasLength(password) || !StringUtils.hasLength(userName)) {
            return Result.fail("用户名或密码为空!");
        }
        //2.密码校验
        //先拿到数据库中的密码
        UserInfo userInfo = userService.getUserByName(userName);
        if(userInfo == null || userInfo.getId() < 0) {
            return Result.fail("用户不存在!");
        }
        //和数据库的密码进行比对
        String realPass = userInfo.getPassword();
        if(!password.equals(realPass)) {
            return Result.fail("密码输入有误");
        }
        //密码正确
        //3.生成token并返回
        Map<String,Object> claim = new HashMap<>();
        claim.put("id",userInfo.getId());
        claim.put("userName",userInfo.getUserName());
        String token = JwtUtils.genToken(claim);
        return Result.success(token);
    }

    //获取用户的信息
    @RequestMapping("/getUserInfo")
    public UserInfo getUserInfo(HttpServletRequest request) {
        //从头token中获取用户ID
        String token = request.getHeader("user_token");
        Integer userId = JwtUtils.getUserIdFromToken(token);
        if(userId == null) {
            return null;
        }
        //根据用户ID获取用户信息
        return userService.queryByID(userId);
    }

    //获取当前作者的信息
    @RequestMapping("/getAuthorInfo")
    public UserInfo getAuthorInfo(Integer blogId) {
        //根据blogId获取作者Id
        //根据作者ID获取作者信息
        if(blogId == null || blogId < 0) {
            return null;
        }
        return userService.getAuthorInfo(blogId);
    }
}
