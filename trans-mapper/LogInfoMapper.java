package com.bite.trans.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LogInfoMapper {
    @Select("insert into log_info(user_name,op) values(#{userName},#{op})")
    Integer insert(String userName,String op);
}
