package com.bit.demo.mapper;

import com.bit.demo.model.ArticleInfo;
import com.bit.demo.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleInfoMapper {
    //单表查询
    @Select("select * from articleinfo where id = #{articleid}")
    ArticleInfo select(Integer articleid);

    //多表查询
    @Select( "select * from articleinfo left join userinfo on articleinfo.uid " +
            "= userinfo.id where articleinfo.uid = 1")
    ArticleInfo selectArticleAndUserById(Integer articleId);


    //模糊匹配
    @Select("select * from userinfo where username like '%${username}%'")
    List<UserInfo> selectArticleByLike(String username);
}
