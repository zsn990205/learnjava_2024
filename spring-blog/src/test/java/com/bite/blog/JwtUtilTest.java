package com.bite.blog;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtilTest {
    //1.生产token
    //2.验证token
    //过期时间: 30分钟
    private static final long expiration = 30 * 60 * 1000;
    private static final String secretString = "yMIpy8KTC+XqJfXaicsZYU/40UsyA5fegkQ0wacImkM=";
    private static final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
    @Test
    public void genToken(){
        Map<String, Object> claim  = new HashMap<>();
        claim.put("username","zhangsan");
        claim.put("password","123456");

        String token = Jwts.builder()
                .setClaims(claim)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
        System.out.println(token);
    }

    @Test
    public void genKey(){
        //随机生成一个key
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String key = Encoders.BASE64.encode(secretKey.getEncoded());
        System.out.println(key);

    }

    @Test
    public void parseToken(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiemhhbmdzYW4iLCJpZCI6NSwiZXhwIjoxNzMzMzk4NTM1fQ._vWDVo3XA_OZRvXQCeHX8wLJkSfWRNRTUDxVcIl5PJk";
        JwtParser build = Jwts.parserBuilder().setSigningKey(key).build();
        Claims body = build.parseClaimsJws(token).getBody();
        System.out.println(body);
    }

    public static void main(String[] args) {
        long exp_timestamp = 1733399776;
        long current_timestamp = System.currentTimeMillis();
        if (current_timestamp > exp_timestamp) {
            System.out.println("令牌已过期");
        } else {
            System.out.println("令牌未过期");
        }

    }
}
