package com.bite.blog.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserInfoMapperTest {
@Autowired
    private UserInfoMapper userInfoMapper;
    @Test
    void queryByName() {
    System.out.println(userInfoMapper.queryByName("zhangsan"));
    }

    @Test
    void queryByID() {
        System.out.println(userInfoMapper.queryByID(2));
    }
}