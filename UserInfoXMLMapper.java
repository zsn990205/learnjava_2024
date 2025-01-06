package com.bit.demo.mapper;

import com.bit.demo.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserInfoXMLMapper {
    List<UserInfo> selectAll();

    Integer insert(UserInfo userInfo);

    Integer insert2(UserInfo userInfo);

    Integer insert3(@Param("userinfo") UserInfo userinfo);

    Integer delete(Integer id);

    Integer update(UserInfo userInfo);

    List<UserInfo> selectAll2();
}
