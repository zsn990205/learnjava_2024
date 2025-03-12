package com.bite.book.service;

import com.bite.book.mapper.UserInfoMapper;
import com.bite.book.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//操作数据库操作
@Service
public class UserService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    public UserInfo getUserinfoByName(String name) {
        return userInfoMapper.selectUserByName(name);
    }
}
