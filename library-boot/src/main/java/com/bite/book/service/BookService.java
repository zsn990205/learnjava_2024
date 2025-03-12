package com.bite.book.service;

import com.bite.book.enums.BookStatusEnum;
import com.bite.book.mapper.BookInfoMapper;
import com.bite.book.model.BookInfo;
import com.bite.book.model.PageRequest;
import com.bite.book.model.pageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BookService {
    @Autowired
    private BookInfoMapper bookInfoMapper;

    public pageResult selectBookInfoBtPage(PageRequest pageRequest) {
        if (pageRequest == null) {
            return null;
        }
        //获取总记录数
        Integer count = bookInfoMapper.count();
        //获取当前记录
        List<BookInfo> bookInfos = bookInfoMapper.selectBookInfoByPage(pageRequest.getOffset(),
                pageRequest.getPageSize());
        if (bookInfos != null && bookInfos.size() > 0) {
            for (BookInfo bookinfo : bookInfos) {
                bookinfo.setStatusCN(BookStatusEnum.getNameByCode(bookinfo.getStatus()).getName());
            }

        }
        return new pageResult<BookInfo>(bookInfos, count, pageRequest);
    }

    public Integer addBook(BookInfo bookInfo) {
        Integer result = 0;
       try {
        result = bookInfoMapper.insertBook(bookInfo);
        if(result <= 0) {
            log.error("添加图书失败!");
        }
       }
       catch (Exception e) {
           log.error("打印日志出错,e:{}",e);
       }
        return result;
    }

    public BookInfo queryBookInfoById(Integer bookId) {
        return bookInfoMapper.selectById(bookId);
    }

    public Integer updateBook(BookInfo bookInfo) {
        Integer result = 0;
        try {
            result = bookInfoMapper.updateBook(bookInfo);
        } catch(Exception e) {
            log.error("更新图书失败,e:{}",e);
        }
        return result;
    }

    //批量删除
    public Integer batchDelete(List<Integer> ids) {
        Integer result = 0;
        log.info("接收请求,批量删除图书, 图书id:{}",ids);
        try{
            result = bookInfoMapper.batchDelete(ids);
        } catch(Exception e) {
            log.error("批量删除失败,ids:{}",e);
        }
        return result;
    }
}
