package com.bite.book.mapper;

import com.bite.book.model.BookInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BookInfoMapper {
    @Select("select * from book_info where status !=0" +
            " order by id desc limit #{offset},#{pageSize}")
    List<BookInfo> selectBookInfoByPage(Integer offset,Integer pageSize);

    //返回总记录数
    @Select("select count(1) from book_info where status !=0")
    Integer count();

    //添加图书操作
    @Insert("insert into book_info(book_name,author,count,price,publish,status) " +
            "values(#{bookName},#{author},#{count},#{price},#{publish},#{status})")
    Integer insertBook(BookInfo bookInfo);

    @Select("select * from book_info where id = #{booId}")
    BookInfo selectById(Integer bookId);

    Integer updateBook(BookInfo bookInfo);

    //传进去一组id
    Integer batchDelete(List<Integer> ids);
}
