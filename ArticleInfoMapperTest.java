package com.bit.demo.mapper;

import com.bit.demo.model.ArticleInfo;
import com.bit.demo.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ArticleInfoMapperTest {
    @Autowired
    private ArticleInfoMapper articleInfomapper;
    @Test
    void select() {
        ArticleInfo result = articleInfomapper.selectArticleAndUserById(1);
        log.info(result.toString());
    }

    @Test
    void select3() {
        log.info(articleInfomapper.selectArticleByLike("java").toString());
    }
}