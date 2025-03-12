package com.bite.book.controller;

import com.bite.book.model.BookInfo;
import com.bite.book.model.PageRequest;
import com.bite.book.model.pageResult;
import com.bite.book.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@RequestMapping("/book")
@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    @RequestMapping("/getBookListByPage")
    public pageResult<BookInfo> getBookListByPage(PageRequest pageRequest) {
        log.info("查询翻页信息,pageRequest:{}",pageRequest);
        if(pageRequest.getPageSize() < 0 || pageRequest.getCurrentPage() < 1) {
            //当每页条数<0 或者当前页数<1时
            return null;
        }
        pageResult<BookInfo> bookInfoPageResult = null;
        try {
            bookInfoPageResult = bookService.selectBookInfoBtPage(pageRequest);
        }
            catch(Exception e) {
            log.error("查询翻页信息错误!,e:{}",e);
            }

        return bookInfoPageResult;
    }

    @RequestMapping(value = "/addBook", produces = "application/json")
    public String addBook(BookInfo bookInfo) {
        log.info("接收到图书请求,bookInfo:{}",bookInfo);
        if(!StringUtils.hasLength(bookInfo.getBookName())
            || !StringUtils.hasLength(bookInfo.getAuthor())
            || bookInfo.getCount() < 0
            || bookInfo.getPrice() == null
            || !StringUtils.hasLength(bookInfo.getPublish())) {
            log.error("参数校验失败,请检查参数");
        }
        Integer result = bookService.addBook(bookInfo);
        if(result <= 0) {
            log.error("添加图书出错:bookInfo:{}",bookInfo);
            return "添加图书出错!请联系管理员";
        }
        //我们定义的是正确的情况下返回空字符串
        return "";
    }

    //根据图书的id查找对应的图书在进行修改
    @RequestMapping("/queryBookInfoById")
    public BookInfo queryBookInfoById(Integer bookId) {
        log.info("根据Id查询图书,bookId: " + bookId);
        BookInfo bookInfo = null;
        try {
            bookInfo = bookService.queryBookInfoById(bookId);
            return bookInfo;
        } catch(Exception e) {
            log.error("根据Id查询图书失败,e:{}",e);
        }
        return null;
    }

    @RequestMapping(value = "/updateBook",  produces = "application/json")
    public String updateBook(BookInfo bookInfo) {
        log.info("接收到更新图书的请求,bookInfo: {}",bookInfo);
        Integer result = bookService.updateBook(bookInfo);
        if(result == 0) {
            log.error("更新图书失败,请联系管理员!");
            return "更新图书失败,请联系管理员!";
        }
        return "";
    }

    @RequestMapping(value = "/batchDelete", produces = "application/json")
    public String batchDelete(@RequestBody List<Integer> ids) {
        log.info("批量删除图书开始ids:{}",ids);
        Integer result = bookService.batchDelete(ids);
        if(result <= 0) {
            //受影响的行数<0 表示此时没有批量删除成功
            log.error("批量删除失败,请联系管理员");
            return "批量删除失败,请联系管理员";
        }
        //删除成功的时候返回空字符串
        return "";
    }
}
