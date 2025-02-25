package com.bit.demo.model;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleInfo {
    //文章相关的信息
    private Integer id;
    private String title;
    private String content;
    private Integer uid;
    private Integer deleteFlag;
    private Date createTime;
    private Date updateTime;
    //用户相关信息
    private Integer age;
    private String username;
}
