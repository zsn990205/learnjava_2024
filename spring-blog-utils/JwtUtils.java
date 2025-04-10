package com.bite.blog.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Slf4j
//JWT公共的类
public class JwtUtils {
    //1.生产token
    //2.验证token
    //过期时间: 30分钟
    private static final long expiration = 60 * 60 * 1000;
    private static final String secretString = "7xnMWeSH05FhisAEeLIJW7XxqHxFkG3uJjszSn7Suiw=";
    private static final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));


    public static String genToken(Map<String,Object> claim) {
        String token = Jwts.builder()
                .setClaims(claim)  //设置载荷信息
                .setExpiration(new Date(System.currentTimeMillis() + expiration))  //设置过期时间
                .signWith(key)  //设置签名
                .compact();
        return token;
    }

    public static Claims parseToken(String token){
        if (token == null){
            return null;
        }
        JwtParser build = Jwts.parserBuilder().setSigningKey(key).build();
        Claims claims = null;
        try {
            claims = build.parseClaimsJws(token).getBody();
        } catch (Exception e){
            log.error("解析token失败, token:"+token);
        }
        return claims;
    }

    public static Integer getUserIdFromToken(String token){
        Claims claims = parseToken(token);
        if (claims == null){
            return null;
        }
        return (Integer) claims.get("id");
    }

}
