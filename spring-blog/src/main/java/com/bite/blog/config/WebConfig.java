package com.bite.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final List<String> excludepath = Arrays.asList(
            "/user/login",
            "/**/*.html",
            "/pic/**",
            "/js/**",
            "/css/**",
            "/blog-editormd/**"
    );
    @Autowired
    private LoginInterceptor interceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**")
                .excludePathPatterns(excludepath);
    }
}
