package com.bit.demo.mapper;

import com.bit.demo.model.UserInfo;
import java.util.List;

import org.apache.ibatis.annotations.*;

//操作数据库的mapper
//定义一个接口
@Mapper
public interface UserInfoMapper {
//查询数据库的操作

    @Select("select * from userinfo")
    List<UserInfo> selectAll();

    @Select("select * from userinfo where id = #{id}")
    UserInfo selectOne(Integer id);

    @Insert("insert into userinfo (username,password,age,gender,phone)" +
    "values(#{username},#{password},#{age},#{gender},#{phone})")
    Integer insert(UserInfo userinfo);

    //返回自增id
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into userinfo (username,password,age,gender,phone)" +
            "values(#{username},#{password},#{age},#{gender},#{phone})")
    Integer insert2(UserInfo userinfo);

    //对insert的参数进行重命名
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into userinfo (username,password,age,gender,phone)" +
            "values(#{userinfo.username},#{userinfo.password},#{userinfo.age},#{userinfo.gender}" +
            ",#{userinfo.phone})")
    Integer insert3(@Param("userinfo") UserInfo userInfo);

    //删除
    @Delete("delete from userinfo where id = #{id}")
    Integer delete(Integer id);

    //修改
    @Update("update userinfo set age = #{age} where id = #{id}")
    Integer update(UserInfo userInfo);

    //查找
    //映射方法一: 对字段进行重命名
    @Select("select id,username,password,age,gender,phone," +
            "delete_flag,create_time as createTime,update_time as updateTime from userinfo")
    List<UserInfo> selectAll2();

    //映射方法二: 使用注解的方式
    @Results({
            @Result(column = "create_time",property = "createTime"),
            @Result(column = "update_time",property = "updateTime")
    })
    @Select("select * from userinfo")
    List<UserInfo> selectAll3();

    //映射方法三: 使用配置的方式自动转驼峰
    @Select("select * from userinfo")
    List<UserInfo> selectAll4();
}
