package com.example.mq.mqserver.core;

import com.example.mq.common.Consumer;
import com.example.mq.common.ConsumerEnv;
import com.example.mq.common.MqException;
import com.example.mq.mqserver.VirtualHost;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/*
通过这个类实现消费的核心逻辑
 */
public class ConsumerManager {
    // 持有上层的 VirtualHost 对象的引用. 用来操作数据.
    private VirtualHost parent;
    // 指定一个线程池, 负责去执行具体的回调任务.
    private ExecutorService workerPool = Executors.newFixedThreadPool(4);
    //存放一个令牌的队列 -> 哪个队列收到了消息
    private BlockingQueue<String> tokenQueue = new LinkedBlockingQueue<>();
    //扫描线程
    private Thread scannerThread = null;

    public ConsumerManager(VirtualHost p) {
        parent = p;

        scannerThread = new Thread(() -> {
            while(true) {
                try {
                    //1.拿到令牌
                    String queueName = tokenQueue.take();
                    //2.根据令牌找到队列
                    MSGQueue queue = parent.getMemoryDataCenter().getQueue(queueName);
                    if(queue == null) {
                        throw new MqException("[ConsumerManager] 取令牌后发现,该队列名不存在! queueName=" + queueName);
                    }
                    //3.从队列中消费一个消息
                    synchronized (queue) {
                        consumerMessage(queue);
                    }
                } catch(InterruptedException | MqException e) {
                    e.printStackTrace();
                }
            }
        });

        //把线程设置为后台进程
        scannerThread.setDaemon(true);
        scannerThread.start();
    }

    //发送消息的时候
    public void notifyConsumer(String queueName) throws InterruptedException {
        tokenQueue.put(queueName);
    }

    //新增一个对象
    public void addConsumer(String consumerTag, String queueName, boolean autoAck, Consumer consumer) throws MqException {
        //找到相应的队列
        MSGQueue queue = parent.getMemoryDataCenter().getQueue(queueName);
        if(queue == null) {
            throw new MqException("[ConsumerManager] 队列不存在! queueName=" + queueName);
        }
        //构造一个consumerEnv对象 把这个对应的队列找到 再把这个consume对象添加到该队列中
        //找到相应的队列了
        ConsumerEnv consumerEnv = new ConsumerEnv(consumerTag,queueName,autoAck,consumer);
        synchronized (queue) {
            queue.addConsumerEnv(consumerEnv);
            //当前队列中已经有了一些消息 需要立即消费掉
            int n = parent.getMemoryDataCenter().getMessageCount(queueName);
            for(int i = 0; i < n; i++) {
                //这个方法调用一次就消费一条数据
                consumerMessage(queue);
            }
        }
    }

    private void consumerMessage(MSGQueue queue) {
        //1.按照轮询的方式 找个消费者出来
        ConsumerEnv luckyDog = queue.chooseConsumer();
        if(luckyDog == null) {
            //当前队列没有消费者 暂时不消费 等消费者出现后在进行消费
            return;
        }
        //2.从队列中取出一个消息
        Message message = parent.getMemoryDataCenter().pollMessage(queue.getName());
        if(message == null) {
            //当前队列中没有消息 没得消费
            return;
        }
        //3.把消息带入到消费者的回调方法中 丢给线程池执行
        workerPool.submit(() -> {
            try {
                //1.把消息放到待确认的集合中 这个操作势必在执行回调之前
                parent.getMemoryDataCenter().addMessageWaitAck(queue.getName(), message);
                //2.真正执行回调操作
                luckyDog.getConsumer().handleDelivery(luckyDog.getConsumerTag(), message.getBasicProperties(),
                        message.getBody());
                //3.如果当前是"自动应答" 就可以直接把消息删除了
                //如果当前是"手动应答" 先不处理 交给后续消费者调用basicAck方法
                if(luckyDog.isAutoAck()) {
                    //1)删除硬盘上的消息
                    if(message.getDeliverMode() == 2) {
                        parent.getDiskDataCenter().deleteMessage(queue,message);
                    }
                    //2)删除上面待确认的集合消息
                    parent.getMemoryDataCenter().removeMessageWaitAck(queue.getName(), message.getMessageId());
                    //3)删除内存中的消息中心的消息
                    parent.getMemoryDataCenter().deleteMessage(message.getMessageId());
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("[ConsumerManager] 消息被成功消费! queueName=" + queue.getName());
        });
    }
}
