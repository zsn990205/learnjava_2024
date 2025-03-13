package com.example.mq.mqserver.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class Exchange {
    //使用name作为交换机的唯一身份标识
    private String name;
    //交换机类型 Direct Fanout Topic
    private ExchangeType type = ExchangeType.Direct;
    //表示该交换机是否要持久化存储 true需要 false不需要
    private boolean durable = false;
    //如果当前交换机没人使用就自动删除(此处并未实现)
    private boolean autoDelete = false;
    //arguments表示创建代码时额外的参数选项->需要什么开启什么就行(此处也并未实现)
    //为了把arguments存入数据库中此处将其转换成json格式
    private Map<String,Object> arguments = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExchangeType getType() {
        return type;
    }

    public void setType(ExchangeType type) {
        this.type = type;
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    //为了和数据库进行交互使用
    public String getArguments() {
        //把当前的arguments从map类型转成string类型(使用json的形式)
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValueAsString(arguments);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //如果代码发生异常就返回空的字符串
        return "{}";
    }


    public void setArguments(String argumentsJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.arguments = mapper.readValue(argumentsJson, new TypeReference<HashMap<String,Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // 在这里针对 arguments, 再提供一组 getter setter , 用来去更方便的获取/设置这里的键值对.
    // 这一组在 java 代码内部使用 (比如测试的时候)
    public void setArguments(String key, Object value) {
        arguments.put(key, value);
    }

    public Object getArguments(String key) {
        System.out.println("初始化之前的key: " + key);
        System.out.println("Arguments map: " + arguments);
        Object value = arguments.get(key);
        System.out.println("初始化之后的value: " + value);
        return arguments.get(key);
    }

    public void setArguments(Map<String,Object> arguments) {
        this.arguments = arguments;
    }

}
