package com.bit.demo.mapper;

import com.bit.demo.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@Slf4j
@SpringBootTest
class UserInfoXMLMapperTest {
    @Autowired
    private UserInfoXMLMapper userInfoXMLMapper;
    @Test
    void selectAll() {
        List<UserInfo> userInfos = userInfoXMLMapper.selectAll();
        log.info(userInfos.toString());
    }


    @Test
    void insert() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("java108");
        userInfo.setPassword("java108");
        userInfo.setAge(2);
        userInfo.setGender(1);
        userInfo.setPhone("456789231");
        Integer result = userInfoXMLMapper.insert2(userInfo);
        log.info("自增的结果是: {}",userInfo.getId());
    }

    @Test
    void insert3() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("java");
        userInfo.setPassword("java");
        userInfo.setAge(2);
        userInfo.setGender(1);
        userInfo.setPhone("88888888");
        Integer result = userInfoXMLMapper.insert3(userInfo);
        log.info("自增的结果是: {}",userInfo.getId());
    }

    @Test
    void delete() {
        userInfoXMLMapper.delete(13);
    }

    @Test
    void update() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("zhuzhu");
        userInfo.setId(14);
        userInfoXMLMapper.update(userInfo);
    }

    @Test
    void selectAll2() {
        List<UserInfo> userInfos = userInfoXMLMapper.selectAll2();
        log.info(userInfos.toString());
    }
}