package com.bit.demo.mapper;

import com.bit.demo.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@Slf4j
@SpringBootTest
class UserInfoMapTest {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Test
    void selectOne() {
        log.info(userInfoMapper.selectOne(1).toString());
    }

    @Test
    void insert() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("zhaoliu");
        userInfo.setPassword("zhaoliu");
        userInfo.setAge(6);
        userInfo.setGender(0);
        userInfo.setPhone("123456789");
        Integer result =  userInfoMapper.insert3(userInfo);
        log.info("insert 方法, 执行结果: {}, 自增ID:{}",result,userInfo.getId());
    }

    @Test
    void delete() {
        Integer result = userInfoMapper.delete(11);
        log.info("delete 方法, 执行结果: {}",result);
    }

    @Test
    void update() {
        UserInfo user = new UserInfo();
        user.setAge(8);
        user.setId(10);
        Integer result = userInfoMapper.update(user);
        if(result > 0) {
            log.info("数据修改成功!");
        } else {
            log.info("数据修改失败!");
        }
    }

    @Test
    void selectAll3() {
        List<UserInfo> list = userInfoMapper.selectAll4();
        log.info(list.toString());
    }
}