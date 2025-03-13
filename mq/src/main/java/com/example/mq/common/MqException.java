package com.example.mq.common;

//自定义异常类
//mq业务逻辑中的异常
//这里遇到一个问题: extends继承了IOException而不是Exception
//                 因此后面写的memoryDatacenter和diskDataCenter的MqException都没被显示化
public class MqException extends Exception {
    public MqException(String reason) {
        super(reason);
    }
}
