package com.example.mq.mqserver.datacenter;

import com.example.mq.MqApplication;
import com.example.mq.mqserver.core.Binding;
import com.example.mq.mqserver.core.Exchange;
import com.example.mq.mqserver.core.ExchangeType;
import com.example.mq.mqserver.core.MSGQueue;
import com.example.mq.mqserver.mapper.MetaMapper;

import java.io.File;
import java.util.List;

//整合数据库操作
public class DataBaseManager {

    private MetaMapper metaMapper;

    //数据库初始化-->建库建表
    public void init() {
        //手动的获取到mataMapper
        metaMapper = MqApplication.context.getBean(MetaMapper.class);
        if (!checkDBExists()) {
            //如果数据库不存在,就进行建库建表操作
            //先创建一个data目录
            File dataDir = new File("./data");
            dataDir.mkdirs();
            createTable();
            //插入默认数据
            createDefaultData();
            System.out.println("[DataBaseManager] 数据库初始化完成!");
        } else {
            //如果数据库存在就不进行操作
            System.out.println("[DataBaseManager] 数据库已经存在!");
        }
    }

    //删除数据库
    public void deleteDB() {
        File file = new File("./data/meta.db");
        boolean ret = file.delete();
        if(!ret) {
            System.out.println("[DataBaseManager] 删除数据库文件失败!");
        } else {
            System.out.println("[DataBaseManager] 删除数据库文件成功!");
        }
        //先删除目录中的文件 删完文件删目录
        //delete删除目录要保证目录是空的
        File dataDir = new File("./data");
        boolean ret2 = dataDir.delete();
        if(!ret2) {
            System.out.println("[DataBaseManager] 删除数据库目录失败!");
        } else {
            System.out.println("[DataBaseManager] 删除数据库目录成功!");
        }
    }

    //给数据库表中创建默认的交换机-->仿rabbitMQ
    private void createDefaultData() {
        //构造一个默认的交换机
        Exchange exchange = new Exchange();
        exchange.setAutoDelete(false);
        exchange.setName("");
        exchange.setType(ExchangeType.Direct);
        exchange.setDurable(true);
        metaMapper.insertExchange(exchange);
        System.out.println("[DataBaseManager] exchange创建初始数据成功!");
    }

    //使用这个方法建表
    //不需要手动执行 首次执行的时候就会自动创建
    private void createTable() {
        metaMapper.createExchangeTable();
        metaMapper.createQueueTable();
        metaMapper.createBindingTable();
        System.out.println("[DataBaseManager] 表格已完成创建!");
    }

    private boolean checkDBExists() {
        File file = new File("./data/meta.db");
        if(file.exists()) {
            return true;
        }
        return false;
    }

    //封装其他的类的数据库
    public void insertExchange(Exchange exchange) {
        metaMapper.insertExchange(exchange);
    }

    public void deleteExchange(String exchangeName) {
        metaMapper.deleteExchange(exchangeName);
    }

    public List<Exchange> selectAllExchanges() {
        return metaMapper.selectAllExchanges();
    }

    public void insertQueue(MSGQueue msgQueue) {
        metaMapper.insertQueue(msgQueue);
    }

    public void deleteQueue(String queueName) {
        metaMapper.deleteQueue(queueName);
    }

    public List<MSGQueue> selectAllQueue() {
        return metaMapper.selectAllQueues();
    }

    public void insertBinding(Binding binding) {
        metaMapper.insertBinding(binding);
    }

    public void deleteBinding(Binding binding) {
        metaMapper.deleteBinding(binding);
    }

    public List<Binding> selectAllBindings() {
        return metaMapper.selectAllBindings();
    }
}
