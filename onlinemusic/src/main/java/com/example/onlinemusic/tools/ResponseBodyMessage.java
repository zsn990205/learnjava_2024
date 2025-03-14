package com.example.onlinemusic.tools;

import lombok.Data;

@Data
public class ResponseBodyMessage <T> {
    private int status;  //状态码说明

    private String message; //返回的信息[出错? 没出错?]

    //返回给前端的信息
    //返回给前端的信息可能是list也可能是别的类型 所以在这统一使用泛型类
    private T data;

    public ResponseBodyMessage(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
