package com.bite.blog.config;

import com.bite.blog.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Configuration
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //进行用户登录校验
        //1. 从header中获取token
        //2. 验证token
        String token = request.getHeader("user_token");
        log.info("从header中获取token, token:"+token);
        Claims claims = JwtUtils.parseToken(token);
        if (claims==null){
            //token是不合法的
            response.setStatus(401);
            return false;
        }
        return true;

    }
}
