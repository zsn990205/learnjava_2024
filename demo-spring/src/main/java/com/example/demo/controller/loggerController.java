package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@Slf4j
@RestController
public class loggerController {

    //private static Logger logger = LoggerFactory.getLogger(loggerController.class);
    @PostConstruct
    public void print() {
        System.out.println("打印日志");
        log.info("---我是日志框架打印的日志---");
        log.error("我是error日志");
        log.info("我是info日志");
        log.debug("我是debug日志");
        log.warn("我是warn日志");
        log.trace("我是trace日志");
    }
}
