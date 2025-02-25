package com.bit.demo.mapper;

import com.bit.demo.model.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserInfoXMLMapper2 {

    Integer insertByXML(UserInfo userInfo);

    List<UserInfo> selectByCondition(UserInfo userInfo);

    Integer updateByCondition(UserInfo userInfo);

    Integer updateByCondition2(UserInfo userInfo);

    Integer batchDelete(List<Integer> ids);


}
