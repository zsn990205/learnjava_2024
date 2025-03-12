package com.bite.book.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BookInfo {
    private Integer id;
    private String bookName;
    private String author;
    private Integer count;
    //精度调整 保证22.00在前端页面时仍可输出22.00而不是22
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;
    private String publish;
    private Integer status;  //--1表示正常  --0表示无效  --2不可借阅
    private String statusCN;
    private Date createTime;
    private Date updateTime;

}
