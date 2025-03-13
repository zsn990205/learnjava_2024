package com.example.mq;

import com.example.mq.mqserver.core.Binding;
import com.example.mq.mqserver.core.Exchange;
import com.example.mq.mqserver.core.ExchangeType;
import com.example.mq.mqserver.core.MSGQueue;
import com.example.mq.mqserver.datacenter.DataBaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DateBaseManagerTests {
    private DataBaseManager dataBaseManager = new DataBaseManager();

    //单元用例测试之间不能相互干扰
    //还需要写两个方法进行准备 一个是准备方法 另一个是收尾方法

    //使用这个方法进行准备 每个用例执行前都要调用这个方法
    @BeforeEach
    public void setUp() {
        //由于在init中需要通过context对象先拿到metamapper实例
        //所以先搞出context对象
        MqApplication.context = SpringApplication.run(MqApplication.class);
        dataBaseManager.init();

    }

    //使用这个方法进行收尾 每个用例执行后都要调用这个方法
    @AfterEach
    public void tearDown() {
        //这里要进行的工作就是删除数据库
        //此处不能直接删除 应该先关闭context后进行删除
        //如果meta.db被打开就没法被删除 获取context会占用8080端口 也是为了释放端口号
        MqApplication.context.close();
        dataBaseManager.deleteDB();
    }

    @Test
    public void testInitTable() {
        //由于init方法已经在上述setUp被调用过了 所以我们直接检查数据库状态即可
        //直接从数据库中查询看数据是否符和预期
        //eg:查交换机表-->exchange 对列表-->空 绑定表-->空
        List<Exchange> exchangeList = dataBaseManager.selectAllExchanges();
        List<MSGQueue> queueList = dataBaseManager.selectAllQueue();
        List<Binding>  bindingList = dataBaseManager.selectAllBindings();

        //assertEquals判断数据的结果是否相等
        //谁在前后无关紧要 但是第一个形参是预期的 第二个是实际的
        Assertions.assertEquals(1,exchangeList.size());
        Assertions.assertEquals("",exchangeList.get(0).getName());
        Assertions.assertEquals(ExchangeType.Direct,exchangeList.get(0).getType());
        Assertions.assertEquals(0,bindingList.size());
        Assertions.assertEquals(0,queueList.size());
    }

    private Exchange createTestExchange(String exchangeName) {
        Exchange exchange = new Exchange();
        exchange.setName(exchangeName);
        exchange.setType(ExchangeType.FANOUT);
        exchange.setAutoDelete(false);
        exchange.setDurable(true);
        exchange.setArguments("aaa", 1);
        exchange.setArguments("bbb", 2);
        return exchange;
    }

    @Test
    //测试插入交换机
    public void testInsertExchange() {
        //构造一个交换机类型进行插入,在进行查询查找结果是否符和预期
        Exchange exchange = createTestExchange("testExchange");
        dataBaseManager.insertExchange(exchange);
        //插入完毕之后去查询结果
        List<Exchange> exchangeList = dataBaseManager.selectAllExchanges();
        Assertions.assertEquals(2, exchangeList.size());
        Exchange newExchange = exchangeList.get(1);
        Assertions.assertEquals("testExchange", newExchange.getName());
        Assertions.assertEquals(ExchangeType.FANOUT, newExchange.getType());
        Assertions.assertEquals(false, newExchange.isAutoDelete());
        Assertions.assertEquals(true, newExchange.isDurable());
//        Assertions.assertEquals(1, newExchange.getArguments("aaa"));
//        Assertions.assertEquals(2, newExchange.getArguments("bbb"));

    }

    @Test
    public void testDeleteExchange() {
        //先构造一个交换机插入数据库然后按照名字进行删除即可
        Exchange exchange = createTestExchange("testExchange");
        dataBaseManager.insertExchange(exchange);
        List<Exchange> exchangeList = dataBaseManager.selectAllExchanges();
        Assertions.assertEquals(2,exchangeList.size());
        Assertions.assertEquals("testExchange",exchangeList.get(1).getName());

        //进行删除操作
        dataBaseManager.deleteExchange("testExchange");
        //删除完之后在进行查询
        //之前写代码的时候生成一个默认的无名氏交换机
        //所以当创建的时候就是两台交换机 第二台交换机的名字就是创建者的名字

        exchangeList = dataBaseManager.selectAllExchanges();
        Assertions.assertEquals(1,exchangeList.size());
        Assertions.assertEquals("",exchangeList.get(0).getName());
    }

    @Test
    public void testInsertQueue() {
        //先构造一个队列 然后进行插入 在检查插入的queue是否正确即可
        MSGQueue queue = createTestQueue("testQueue");
        dataBaseManager.insertQueue(queue);
        List<MSGQueue> queueList = dataBaseManager.selectAllQueue();
        Assertions.assertEquals(1,queueList.size());
        MSGQueue newQueue = queueList.get(0);
        Assertions.assertEquals("testQueue",newQueue.getName());
        Assertions.assertEquals(false,newQueue.isAutoDelete());
        Assertions.assertEquals(false,newQueue.isExclusive());
        Assertions.assertEquals(true,newQueue.isDurable());
        Assertions.assertEquals(1,newQueue.getArguments("aaa"));
        Assertions.assertEquals(2,newQueue.getArguments("bbb"));
    }

    private MSGQueue createTestQueue(String queueName) {
        MSGQueue queue = new MSGQueue();
        queue.setAutoDelete(false);
        queue.setDurable(true);
        queue.setName(queueName);
        queue.setExclusive(false);
        queue.setArguments("aaa",1);
        queue.setArguments("bbb",2);
        return queue;
    }

    @Test
    public void testDeleteQueue() {
        //插入queue
        MSGQueue queue = createTestQueue("testQueue");
        dataBaseManager.insertQueue(queue);
        List<MSGQueue> queueList = dataBaseManager.selectAllQueue();
        Assertions.assertEquals(1,queueList.size());
        //进行删除
        dataBaseManager.deleteQueue("testQueue");
        queueList = dataBaseManager.selectAllQueue();
        Assertions.assertEquals(0,queueList.size());

    }

    @Test
    public void testInsertBinding() {
        Binding binding = createTestBinding("testExchange","testQueue");
        dataBaseManager.insertBinding(binding);
        List<Binding> bindingList = dataBaseManager.selectAllBindings();
        Binding newBinding = bindingList.get(0);
        Assertions.assertEquals("testExchange",newBinding.getExchangeName());
        Assertions.assertEquals("testQueue",newBinding.getQueueName());
        Assertions.assertEquals("testBindingKey",newBinding.getBindingKey());
    }

    private Binding createTestBinding(String exchangeName,String queueName) {
        Binding binding = new Binding();
        binding.setBindingKey("testBindingKey");
        binding.setExchangeName(exchangeName);
        binding.setQueueName(queueName);
        return binding;
    }

    @Test
    public void testDeleteBinding() {
        Binding binding = createTestBinding("testExchange","testQueue");
        dataBaseManager.insertBinding(binding);
        List<Binding> bindingList = dataBaseManager.selectAllBindings();
        Assertions.assertEquals(1,bindingList.size());
        //进行删除
        dataBaseManager.deleteBinding(binding);
        bindingList = dataBaseManager.selectAllBindings();
        Assertions.assertEquals(0,bindingList.size());
    }
}
