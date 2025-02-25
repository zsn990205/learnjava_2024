package com.bit.demo.mapper;

import com.bit.demo.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class UserInfoXMLMapper2Test {
    @Autowired
    private UserInfoXMLMapper2 userInfoXMLMapper2;
    @Test
    void insertByXML() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("2023-2");
        userInfo.setPassword("2023pwd");
        userInfo.setAge(1);
        userInfo.setGender(null);
        userInfo.setPhone("12334566");
        userInfoXMLMapper2.insertByXML(userInfo);
    }

    @Test
    void selectByCondition() {
        UserInfo userInfo = new UserInfo();
//        userInfo.setUsername("2023-2");
//        userInfo.setGender(1);//
        userInfo.setAge(1);
        List<UserInfo> userInfos = userInfoXMLMapper2.selectByCondition(userInfo);
        log.info(userInfos.toString());
    }

    @Test
    void updateByCondition() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("2023-8");
        userInfoXMLMapper2.updateByCondition(userInfo);
    }
    @Test
    void updateByCondition2() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("2023-8");
        userInfoXMLMapper2.updateByCondition2(userInfo);
    }

    @Test
    void batchDelete() {
        userInfoXMLMapper2.batchDelete(Arrays.asList(17,18,19));
    }
}