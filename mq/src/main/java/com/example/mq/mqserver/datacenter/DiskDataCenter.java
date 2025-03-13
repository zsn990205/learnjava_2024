package com.example.mq.mqserver.datacenter;

import com.example.mq.common.MqException;
import com.example.mq.mqserver.core.Binding;
import com.example.mq.mqserver.core.Exchange;
import com.example.mq.mqserver.core.MSGQueue;
import com.example.mq.mqserver.core.Message;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/*
使用这个类来管理所有硬盘上的数据
1.数据库: 交换机 绑定 队列
2.数据文件: 消息
上层逻辑如果需要操作硬盘 统一使用这个类来处理(上层代码不关心当前数据是存储在数据库中的还是文件中的)
 */
public class DiskDataCenter {
    //这个实例用来管理数据库中的数据
    private DataBaseManager dataBaseManager = new DataBaseManager();
    //这个实例是用来管理数据文件中的数据
    private MessageFileManager messageFileManager = new MessageFileManager();

    public void init() {
        //针对上述两个实例进行初始化
        dataBaseManager.init();
        //当前messageFileManager.init()是空方法 暂时还没扩展
        messageFileManager.init();
    }

    //封装一下交换机操作
    public void insertExchange(Exchange exchange) {
        dataBaseManager.insertExchange(exchange);
    }

    public void deleteExchange(String exchangeName) {
        dataBaseManager.deleteExchange(exchangeName);
    }

    public List<Exchange> selectAllExchange() {
        return dataBaseManager.selectAllExchanges();
    }

    //封装队列操作
    public void insertQueue(MSGQueue queue) throws IOException {
        dataBaseManager.insertQueue(queue);
        //创建队列的时候不仅仅要把对象写入数据库中 还需要创建出对应的目录和文件
        messageFileManager.createQueueFiles(queue.getName());
    }

    public void deleteQueue(String queueName) throws IOException {
        dataBaseManager.deleteQueue(queueName);
        //删除队列的时候不仅仅要把对象写入数据库中 还需要创建出对应的目录和文件
        messageFileManager.destroyQueueFiles(queueName);
    }

    public  List<MSGQueue> selectAllQueue() {
        return dataBaseManager.selectAllQueue();
    }

    //封装绑定操作
    public void insertBinding(Binding binding) {
        dataBaseManager.insertBinding(binding);
    }

    public void deleteBinding(Binding binding) {
        dataBaseManager.deleteBinding(binding);
    }

    public List<Binding> selectAllBindings() {
        return dataBaseManager.selectAllBindings();
    }

    //封装消息操作
    public void sendMessage(MSGQueue queue, Message message) throws IOException, MqException {
        messageFileManager.sendMessage(queue,message);
    }

    public void deleteMessage(MSGQueue queue, Message message) throws IOException, ClassNotFoundException, MqException{
        messageFileManager.deleteMessage(queue,message);
        if(messageFileManager.checkGC(queue.getName())) {
            messageFileManager.gc(queue);
        }
    }

    public LinkedList<Message> loadAllMessageFromQueue(String queueName) throws IOException, ClassNotFoundException, MqException {
        return messageFileManager.loadAllMessageFromQueue(queueName);
    }
}
