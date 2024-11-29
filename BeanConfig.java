package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class BeanConfig {
    @Primary
    @Bean
    public UserInfo userInfo1() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setAge(18);
        userInfo.setName("虎虎");
        return userInfo;
    }

    @Bean
    public UserInfo userInfo3() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(2);
        userInfo.setAge(17);
        userInfo.setName("朱朱");
        return userInfo;
    }

}
