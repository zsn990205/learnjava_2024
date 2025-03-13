package com.example.mq.mqserver.core;

//将交换机和队列建立好关系
public class Binding {
    private String exchangeName;
    private String queueName;
    //"出题"->要求领红包的人画个桌子出来
    private String bindingKey;

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getBindingKey() {
        return bindingKey;
    }

    public void setBindingKey(String bindingKey) {
        this.bindingKey = bindingKey;
    }
}
