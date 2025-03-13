package com.example.mq.mqclient;

import com.example.mq.common.*;
import com.example.mq.mqserver.core.BasicProperties;
import com.example.mq.mqserver.core.ExchangeType;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Channel {
    private String channelId;
    //当前这个channel属于哪个链接
    private Connection connection;
    private ConcurrentHashMap<String, BasicReturns> basicReturnsMap = new ConcurrentHashMap<>();
    //如果当前的channel订阅了某个队列 需要在此处记录下对应的回调是啥 当前队列的消息返回来的时候 调用回调
    //此处约定一个channel中只能有一个回调
    private Consumer consumer = null;

    public Channel(String channelId, Connection connection) {
        this.channelId = channelId;
        this.connection = connection;
    }

    //在这个方法中和服务器进行交互
    public boolean createChannel() throws IOException {
        //对应创建channel来说 此处payload就是basicArguments对象
        BasicArguments basicArguments = new BasicArguments();
        basicArguments.setChannelId(channelId);
        basicArguments.setRid(generateRid());
        byte[] payload = BinaryTool.toBytes(basicArguments);

        Request request = new Request();
        request.setType(0x1);
        request.setLength(payload.length);
        request.setPayload(payload);

        //构造出完整请求之后 就可以发送请求了
        connection.writeRequest(request);
        //等待服务器的响应
        BasicReturns basicReturns = waitResult(basicArguments.getRid());
        return basicReturns.isOk();
    }

    //期望使用这个方法来阻塞等待服务器的响应
    private BasicReturns waitResult(String rid) {
        BasicReturns basicReturns = null;
        while((basicReturns = basicReturnsMap.get(rid)) == null) {
            //查询的结果为null 说明此时的包裹还没回来呢
            //此时需要阻塞等待
            //使用循环等顺序表的方式
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        //读取成功后 需要从hash中删除
        basicReturnsMap.remove(rid);
        return basicReturns;
    }

    private String generateRid() {
        return "R-" + UUID.randomUUID().toString();
    }

    //关闭channel 给服务器发送一个0x2的请求
     public boolean close() throws IOException {
        BasicArguments basicArguments = new BasicArguments();
        basicArguments.setRid(generateRid());
        basicArguments.setChannelId(channelId);
        byte[] payload = BinaryTool.toBytes(basicArguments);

        Request request = new Request();
        request.setType(0x2);
        request.setPayload(payload);
        request.setLength(payload.length);

        connection.writeRequest(request);
        BasicReturns basicReturns = waitResult(basicArguments.getRid());
        return  basicReturns.isOk();
     }

     //创建交换机
    public boolean exchangeDeclare(String exchangeName, ExchangeType exchangeType, boolean durable, boolean autoDelete,
                                   Map<String, Object> arguments) throws IOException {
        ExchangeDeclareArguments exchangeDeclareArguments = new ExchangeDeclareArguments();
        exchangeDeclareArguments.setExchangeName(exchangeName);
        exchangeDeclareArguments.setExchangeType(exchangeType);
        exchangeDeclareArguments.setDurable(durable);
        exchangeDeclareArguments.setAutoDelete(autoDelete);
        exchangeDeclareArguments.setRid(generateRid());
        exchangeDeclareArguments.setChannelId(channelId);
        exchangeDeclareArguments.setArguments(arguments);
        byte[] payload  = BinaryTool.toBytes(exchangeDeclareArguments);

        Request request = new Request();
        request.setType(0x3);
        request.setLength(payload.length);
        request.setPayload(payload);

        connection.writeRequest(request);
        BasicReturns basicReturns = waitResult(exchangeDeclareArguments.getRid());
        return basicReturns.isOk();
    }

    //删除交换机
    public boolean exchangeDelete(String exchangeName) throws IOException {
        ExchangeDeleteArguments exchangeDeleteArguments = new ExchangeDeleteArguments();
        exchangeDeleteArguments.setExchangeName(exchangeName);
        exchangeDeleteArguments.setChannelId(channelId);
        exchangeDeleteArguments.setRid(generateRid());

        byte[] payload = BinaryTool.toBytes(exchangeDeleteArguments);
        Request request = new Request();
        request.setType(0x4);
        request.setPayload(payload);
        request.setLength(payload.length);

        connection.writeRequest(request);
        BasicReturns basicReturns = waitResult(exchangeDeleteArguments.getRid());
        return basicReturns.isOk();
    }

    //创建队列
    public boolean queueDeclare(String queueName, boolean durable, boolean exclusive, boolean autoDelete,
                                Map<String,Object> arguments) throws IOException {
        QueueDeclareArguments queueDeclareArguments = new QueueDeclareArguments();
        queueDeclareArguments.setRid(generateRid());
        queueDeclareArguments.setChannelId(channelId);
        queueDeclareArguments.setAutoDelete(autoDelete);
        queueDeclareArguments.setDurable(durable);
        queueDeclareArguments.setQueueName(queueName);
        queueDeclareArguments.setExclusive(exclusive);
        queueDeclareArguments.setArguments(arguments);
        byte[] payload = BinaryTool.toBytes(queueDeclareArguments);

        Request request = new Request();
        request.setPayload(payload);
        request.setLength(payload.length);
        request.setType(0x5);

        connection.writeRequest(request);
        BasicReturns basicReturns = waitResult(queueDeclareArguments.getRid());
        return basicReturns.isOk();
    }

    //删除队列
    public boolean queueDelete(String queueName) throws IOException {
        QueueDeleteArguments arguments = new QueueDeleteArguments();
        arguments.setChannelId(channelId);
        arguments.setQueueName(queueName);
        arguments.setRid(generateRid());
        byte[] payload = BinaryTool.toBytes(arguments);

        Request request = new Request();
        request.setType(0x6);
        request.setPayload(payload);
        request.setLength(payload.length);

        connection.writeRequest(request);
        BasicReturns basicReturns = waitResult(arguments.getRid());
        return basicReturns.isOk();
    }

    //创建绑定
    public boolean queueBind(String queueName,String exchangeName,String bindingKey) throws IOException {
        QueueBindArguments arguments = new QueueBindArguments();
        arguments.setChannelId(channelId);
        arguments.setRid(generateRid());
        arguments.setExchangeName(exchangeName);
        arguments.setQueueName(queueName);
        arguments.setBindingKey(bindingKey);
        byte[] payload = BinaryTool.toBytes(arguments);

        Request request = new Request();
        request.setPayload(payload);
        request.setLength(payload.length);
        request.setType(0x7);

        connection.writeRequest(request);
        BasicReturns basicReturns = waitResult(arguments.getRid());
        return basicReturns.isOk();
    }

    //接触绑定
    public boolean queueUnbind(String queueName,String exchangeName) throws IOException {
        QueueUnBindArguments arguments = new QueueUnBindArguments();
        arguments.setChannelId(channelId);
        arguments.setRid(generateRid());
        arguments.setQueueName(queueName);
        arguments.setExchangeName(exchangeName);
        byte[] payload = BinaryTool.toBytes(arguments);

        Request request = new Request();
        request.setPayload(payload);
        request.setLength(payload.length);
        request.setType(0x8);

        connection.writeRequest(request);
        BasicReturns basicReturns = waitResult(arguments.getRid());
        return basicReturns.isOk();
    }

    //发送消息
    public boolean basicPublish(String exchangeName, String routingKey, BasicProperties basicProperties,
                                byte[] body) throws IOException {
        BasicPublishArguments arguments = new BasicPublishArguments();
        arguments.setBasicProperties(basicProperties);
        arguments.setChannelId(channelId);
        arguments.setRid(generateRid());
        arguments.setBody(body);
        arguments.setExchangeName(exchangeName);
        arguments.setRoutingKey(routingKey);
        byte[] payload = BinaryTool.toBytes(arguments);

        Request request = new Request();
        request.setPayload(payload);
        request.setLength(payload.length);
        request.setType(0x9);

        connection.writeRequest(request);
        BasicReturns basicReturns = waitResult(arguments.getRid());
        return basicReturns.isOk();
    }

    //订阅消息
    public boolean basicConsume(String queueName,boolean autoAck,Consumer consumer) throws MqException, IOException {
        //1.先设置回调
        if(this.consumer != null) {
            //该channel已经设置过消息的回调了
            throw new MqException("该channel已经设置过消息的回调了,不能再重复设置!");
        }
        this.consumer = consumer;

        BasicConsumeArguments arguments = new BasicConsumeArguments();
        arguments.setChannelId(channelId);
        //此处的consumerTag也使用channelID
        arguments.setConsumerTag(channelId);
        arguments.setRid(generateRid());
        arguments.setQueueName(queueName);
        arguments.setAutoAck(autoAck);
        byte[] payload = BinaryTool.toBytes(arguments);

        Request request = new Request();
        request.setPayload(payload);
        request.setLength(payload.length);
        request.setType(0xa);

        connection.writeRequest(request);
        BasicReturns basicReturns = waitResult(arguments.getRid());
        return basicReturns.isOk();
    }

    //确认消息
    public boolean basicAck(String queueName,String messageId) throws IOException {
        BasicAckArguments arguments = new BasicAckArguments();
        arguments.setChannelId(channelId);
        arguments.setRid(generateRid());
        arguments.setQueueName(queueName);
        arguments.setMessageId(messageId);
        byte[] payload = BinaryTool.toBytes(arguments);

        Request request = new Request();
        request.setPayload(payload);
        request.setLength(payload.length);
        request.setType(0xb);

        connection.writeRequest(request);
        BasicReturns basicReturns = waitResult(arguments.getRid());
        return basicReturns.isOk();
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ConcurrentHashMap<String, BasicReturns> getBasicReturnsMap() {
        return basicReturnsMap;
    }

    public void setBasicReturnsMap(ConcurrentHashMap<String, BasicReturns> basicReturnsMap) {
        this.basicReturnsMap = basicReturnsMap;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }


    public void putReturns(BasicReturns basicReturns) {
        basicReturnsMap.put(basicReturns.getRid(),basicReturns);
        synchronized (this) {
            //唤醒所有线程 因为不知道当前有多少个响应在等待这个线程
            notifyAll();
        }
    }


}
