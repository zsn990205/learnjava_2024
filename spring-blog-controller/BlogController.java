package com.bite.blog.controller;

import com.bite.blog.model.BlogInfo;
import com.bite.blog.model.Result;
import com.bite.blog.service.BlogService;
import com.bite.blog.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/blog")
@RestController
public class BlogController {

    @Autowired
    private BlogService blogService;

    @RequestMapping("/getlist")
    public List<BlogInfo> getBlogList() {
        return blogService.getBlogList();
    }

    @RequestMapping("/getBlogDetail")
    public BlogInfo getBlogDetail(Integer blogId,HttpServletRequest request) {
        BlogInfo blogInfo = new BlogInfo();
        blogInfo = blogService.getBlogDetail(blogId);

        //从token中获取userId
        String token = request.getHeader("user_token");
        Integer userId = JwtUtils.getUserIdFromToken(token);
        //判断作者是否为登录用户
        if(userId != null && userId == blogInfo.getUserId()) {
            //如果uerId不为空并且此时的userId和blog中的userId一样
            blogInfo.setLoginUser(true);
        }
        return blogInfo;
    }

    @RequestMapping("/add")
    public Result publishBlog(String title, String content, HttpServletRequest request) {
        //从token中获取userId
        String token = request.getHeader("user_token");
        Integer userId = JwtUtils.getUserIdFromToken(token);
        if(userId == null || userId < 0) {
            return Result.fail("用户未登录");
        }
        //插入博客
        BlogInfo blogInfo = new BlogInfo();
        blogInfo.setUserId(userId);
        blogInfo.setTitle(title);
        blogInfo.setContent(content);
        blogService.insertBlog(blogInfo);
        return Result.success("用户登录成功");
    }

    //编辑博客
    @RequestMapping("/update")
    public boolean updateBlog(Integer id,String title,String content) {
        BlogInfo blogInfo = new BlogInfo();
        blogInfo.setTitle(title);
        blogInfo.setContent(content);
        blogInfo.setId(id);
        //根据博客id去更新数据
        blogService.updateBlog(blogInfo);
        return true;
    }

    //删除博客
    @RequestMapping("/delete")
    public boolean updateBlog(Integer blogId) {
        blogService.deleteBlog(blogId);
        return true;
    }
}
