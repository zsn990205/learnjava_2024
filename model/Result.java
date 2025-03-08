package com.bite.book.model;

import lombok.Data;

@Data
public class Result {
    private ResultCode code;
    //错误信息
    private String errMsg;
    //数据
    private Object data;
}
