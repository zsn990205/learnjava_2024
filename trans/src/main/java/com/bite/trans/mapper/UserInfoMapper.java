package com.bite.trans.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserInfoMapper {
    @Select("insert into user_info(user_name,password) values(#{userName},#{password})")
    Integer insert(String userName,String password);
}
