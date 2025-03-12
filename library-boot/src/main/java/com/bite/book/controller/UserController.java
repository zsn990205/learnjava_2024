package com.bite.book.controller;

import com.bite.book.model.UserInfo;
import com.bite.book.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public Boolean login(String username, String password,HttpSession session) {
        //1.校验参数
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            return false;
        }
        //2.验证账号和密码是否正确
        //把admin放在前面是如果userName为null会报空指针异常
        //所以把常量写在前面

        //2*.根据用户名去数据库查找用户信息
        //userName 是前端传过来的
        UserInfo userInfo = userService.getUserinfoByName(username);
        //userName为空或者userName的个数<=0表示此前的用户不存在直接返回false
        if (userInfo == null || userInfo.getId() <= 0) {
            return false;
        }
        //对比密码是否正确
        //正确的话返回true
        if (password.equals(userInfo.getPassword())) {
            //存session
            //密码正确的时候直接将userInfo对象存下来
            //为了安全起见,不保存用户密码 密码设为空
            //但session的值通常是看不到的 个人习惯
            userInfo.setPassword("");
            session.setAttribute("UserName",userInfo);
            return true;
        }
        return false;
    }
}
