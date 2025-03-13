package com.example.mq.mqserver.core;

import com.example.mq.common.ConsumerEnv;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MSGQueue {
    //表示队列的身份标识
    private String name;
    //表示队列是否持久化 true表示持久化保存 false表示不持久化
    private boolean durable;
    //这个属性为true表示这个队列只能被一个消费者使用(别人用不了) false表示都能用
    //这个独占功能也不实现
    private boolean exclusive;
    //自动删除 先不实现
    private boolean autoDelete;
    //表示扩展参数(先不实现)
    private Map<String,Object> arguments = new HashMap<>();
    //当前队列都有哪些消费者订阅了
    private List<ConsumerEnv> consumerEnvList = new ArrayList<>();
    //记录当前取到的第几个消费者 便于实现轮询策略
    private AtomicInteger consumerSeq = new AtomicInteger(0);

    //添加一个新的订阅者元素
    public void addConsumerEnv(ConsumerEnv consumerEnv) {
        synchronized (this) {
            consumerEnvList.add(consumerEnv);
        }
    }
    //删除一个订阅者元素 后面再写

    //挑选一个订阅者处理当前的消息  按照轮询的方式挑选
    public ConsumerEnv chooseConsumer() {
        //判定当前的长度是0 说明当前没有人订阅
        if(consumerEnvList.size() == 0) {
            return null;
        }
        //计算一下当前要取的元素下标
        int index = consumerSeq.get() % consumerEnvList.size();
        consumerSeq.getAndIncrement();
        return consumerEnvList.get(index);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    public String getArguments() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(arguments);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }

    public void setArguments(String argumentsJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.arguments = mapper.readValue(argumentsJson, new TypeReference<HashMap<String, Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Object getArguments(String key) {
        return arguments.get(key);
    }

    public void setArguments(String key,Object value) {
        arguments.put(key,value);
    }

    public void setArguments(Map<String,Object> arguments) {
        this.arguments = arguments;
    }
}
