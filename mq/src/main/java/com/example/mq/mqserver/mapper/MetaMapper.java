package com.example.mq.mqserver.mapper;

import com.example.mq.mqserver.core.Binding;
import com.example.mq.mqserver.core.Exchange;
import com.example.mq.mqserver.core.MSGQueue;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
//提供三个核心建表方法
public interface MetaMapper {
    //建表操作
    void createExchangeTable();
    void createQueueTable();
    void createBindingTable();

    //插入和删除操作
    void insertExchange(Exchange exchange);
    void deleteExchange(String ExchangeName);
    List<Exchange> selectAllExchanges();
    void insertQueue(MSGQueue queue);
    void deleteQueue(String queueName);
    List<MSGQueue> selectAllQueues();
    void insertBinding(Binding binding);
    void deleteBinding(Binding binding);
    List<Binding> selectAllBindings();
}
