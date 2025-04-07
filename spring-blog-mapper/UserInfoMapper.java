package com.bite.blog.mapper;

import com.bite.blog.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserInfoMapper {
    //1.根据用户名查询用户信息
    @Select("select * from user where user_name = #{userName} and delete_flag = 0")
    UserInfo queryByName(String userName);
    //2.根据用户ID查询用户信息
    @Select("select * from user where id = #{id} and delete_flag = 0")
    UserInfo queryByID(Integer id);
}
