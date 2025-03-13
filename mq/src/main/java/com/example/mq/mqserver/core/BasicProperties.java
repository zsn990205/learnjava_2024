package com.example.mq.mqserver.core;

import java.io.Serializable;

public class BasicProperties implements Serializable {
    //消息的唯一身份标识,为了保证id的唯一性使用UUID
    private String messageId;
    //和bindingKey匹配
    private String routingKey;
    //表示消息是否要持久化 1表示不持久化2表示持久化
    private int deliverMode = 1;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public int getDeliverMode() {
        return deliverMode;
    }

    public void setDeliverMode(int deliverMode) {
        this.deliverMode = deliverMode;
    }

    @Override
    public String toString() {
        return "BasicProperties{" +
                "messageId='" + messageId + '\'' +
                ", routingKey='" + routingKey + '\'' +
                ", deliverMode=" + deliverMode +
                '}';
    }
}
