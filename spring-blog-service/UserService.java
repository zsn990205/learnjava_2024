package com.bite.blog.service;

import com.bite.blog.mapper.BlogInfoMapper;
import com.bite.blog.mapper.UserInfoMapper;
import com.bite.blog.model.BlogInfo;
import com.bite.blog.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private BlogInfoMapper blogInfoMapper;


    public UserInfo getUserByName(String userName) {
        return userInfoMapper.queryByName(userName);
    }


    public UserInfo queryByID(Integer userId) {
        return userInfoMapper.queryByID(userId);
    }

    public UserInfo getAuthorInfo(Integer blogId) {
        //根据blogId获取作者Id
        //根据作者ID获取作者信息
        UserInfo userInfo = new UserInfo();
        BlogInfo blogInfo = blogInfoMapper.queryById(blogId);
        if(blogInfo == null || blogInfo.getUserId() < 0) {
            return null;
        }
        return userInfoMapper.queryByID(blogInfo.getUserId());
    }
}
