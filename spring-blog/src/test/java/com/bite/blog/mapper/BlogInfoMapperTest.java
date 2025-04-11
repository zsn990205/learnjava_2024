package com.bite.blog.mapper;

import com.bite.blog.model.BlogInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class BlogInfoMapperTest {

    @Autowired
    private BlogInfoMapper blogInfoMapper;

    @Test
    void queryBlogList() {
        System.out.println(blogInfoMapper.queryBlogList());
    }

    @Test
    void queryById() {
        System.out.println(blogInfoMapper.queryById(1));
    }

    @Test
    void updateBlog() {
        BlogInfo blogInfo = new BlogInfo();
        blogInfo.setTitle("test3");
        blogInfo.setContent("测试update专用");
        blogInfo.setId(3);
        blogInfoMapper.update(blogInfo);
    }

    @Test
    void deleteBlog() {
        blogInfoMapper.deleteBlog(6);
    }

    @Test
    void insertBlog() {
        BlogInfo blogInfo = new BlogInfo();
        blogInfo.setTitle("test");
        blogInfo.setContent("测试insert专用");
        blogInfo.setUserId(1);
        blogInfoMapper.insertBlog(blogInfo);
    }
}