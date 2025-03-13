package com.example.mq.mqserver.datacenter;

import com.example.mq.common.MqException;
import com.example.mq.mqserver.core.Binding;
import com.example.mq.mqserver.core.Exchange;
import com.example.mq.mqserver.core.MSGQueue;
import com.example.mq.mqserver.core.Message;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/*
使用这个类统一管理内存中的数据
针对内存中的数据进行操作
 */
public class MemoryDataCenter {
    //key是exchangeName value是Exchange对象  ConcurrentHashMap为了保证线程安全
    private ConcurrentHashMap<String, Exchange> exchangeMap = new ConcurrentHashMap<>();
    //key是exchangeName value是MSGQueue对象
    private ConcurrentHashMap<String, MSGQueue> queueMap = new ConcurrentHashMap<>();
    //第一个key是exchangeName value是hashMap 第二个key是queueName value是Binding对象
    private ConcurrentHashMap<String, ConcurrentHashMap<String, Binding>> bindingsMap = new ConcurrentHashMap<>();
    //key是messageId value是Message对象
    private ConcurrentHashMap<String, Message> messageMap = new ConcurrentHashMap<>();
    //key是queueName value是Message链表
    //表示消息和队列之间的关联 LinkedList每一个元素是一个message对象
    private ConcurrentHashMap<String, LinkedList<Message>> queueMessageMap = new ConcurrentHashMap<>();
    //未被确认的消息 存储了当前队列中哪些消息被消费者取走了但是未被应答
    private ConcurrentHashMap<String,ConcurrentHashMap<String,Message>> queueMessageWaitAckMap = new ConcurrentHashMap<>();

    //向内存中插入交换机
    public void insertExchange(Exchange exchange) {
        exchangeMap.put(exchange.getName(),exchange);
        System.out.println("[MemoryDataCenter] 新交换机添加成功! exchangeName=" + exchange.getName());
    }

    public Exchange getExchange(String exchangeName) {
        return exchangeMap.get(exchangeName);
    }

    //删除某些交换机
    public void deleteExchange(String exchangeName) {
        exchangeMap.remove(exchangeName);
        System.out.println("[MemoryDataCenter] 交换机删除成功! exchangeName=" + exchangeName);
    }

    public void insertQueue(MSGQueue queue) {
        queueMap.put(queue.getName(),queue);
        System.out.println("[MemoryDataCenter] 新队列添加成功! queueName=" + queue.getName());
    }

    public MSGQueue getQueue(String queueName) {
        return queueMap.get(queueName);
    }

    public void deleteQueue(String queueName) {
        queueMap.remove(queueName);
        System.out.println("[MemoryDataCenter] 队列删除成功! queueName=" + queueName);
    }

    public void insertBinding(Binding binding) throws MqException {
        //先使用exchangeName检查对应的哈希表是否存在 不存在就创建一个
//        ConcurrentHashMap<String,Binding> bindingMap = bindingsMap.get(binding.getExchangeName());
//        if(bindingMap == null) {
//            bindingMap = new ConcurrentHashMap<>();
//            bindingMap.put(binding.getExchangeName(),bindingMap);
//        }
        //针对上述代码进行简化
        //如果不存在就调用一个lambda表达式New一个新的
        ConcurrentHashMap<String, Binding> bindingMap = bindingsMap.computeIfAbsent(binding.getExchangeName(),
                k -> new ConcurrentHashMap<>());

        synchronized (bindingMap) {
            //再根据queueName查一下 如果已经存在就抛出异常 不存在才能插入(因为此时的hash表应该是空的才行
            if(bindingMap.get(binding.getQueueName()) != null) {
                throw new MqException("[MemoryDataCenter] 绑定已经存在! exchangeName=" + binding.getExchangeName() +
                        ", queueName" + binding.getQueueName() );
            }

            //针对数据进行进一步插入
            bindingMap.put(binding.getQueueName(),binding);
            System.out.println("[MemoryDataCenter] 新绑定添加成功! exchangeName=" + binding.getExchangeName()
                    + ", queueName=" + binding.getQueueName());
        }
    }

    //获取绑定
    //1.根据exchangeName和queueName确定唯一的Binding
    //2.根据exchangeName获取所有的Binding
    public Binding getBinding(String exchangeName,String queueName) {
        ConcurrentHashMap<String,Binding> bindingMap = bindingsMap.get(exchangeName);
        if(bindingMap == null) {
            return null;
        }
        return bindingMap.get(queueName);
    }

    public ConcurrentHashMap<String,Binding> getBindings(String exchangeName) {
        return bindingsMap.get(exchangeName);
    }

    //删除
    public void deleteBinding(Binding binding) throws MqException {
        ConcurrentHashMap<String,Binding> bindingMap = bindingsMap.get(binding.getExchangeName());
        if(bindingMap == null) {
            throw new MqException("[MemoryDataCenter] 绑定已经存在! exchangeName=" + binding.getExchangeName() +
                    ", queueName" + binding.getQueueName() );
        }
        bindingMap.remove(binding.getQueueName());
        System.out.println("[MemoryDataCenter] 绑定删除成功! exchangeName=" + binding.getExchangeName()
                + ", queueName=" + binding.getQueueName());
    }

    //添加消息
    public void addMessage(Message message) {
        messageMap.put(message.getMessageId(),message);
        System.out.println("[MemoryDataCenter] 新消息添加成功! messageId=" + message.getMessageId());
    }

    //根据id查询消息
    public Message getMessages(String messageID) {
        return messageMap.get(messageID);
    }

    //根据Id删除消息
    public void deleteMessage(String messageId) {
        messageMap.remove(messageId);
        System.out.println("[MemoryDataCenter] 消息被移除! messageId=" + messageId);
    }

    //发送消息到指定队列
    public void sendMessage(MSGQueue queue,Message message) {
        //把消息放到对应的队列数据结构中
        //先根据队列的名字找到对应的链表
        LinkedList<Message> messages = queueMessageMap.computeIfAbsent(queue.getName(),k -> new LinkedList<>());
        synchronized (messages) {
            messages.add(message);
        }
        //插入总消息中心中
        //假设message已经存在 那么重复插入也没事的
        addMessage(message);
        System.out.println("[MemoryDataCenter] 消息被投递到队列中! messageId=" + message.getMessageId());
    }

    //从队列中读取消息 -> 消费数据,读取后要进行删除
    public Message pollMessage(String queueName) {
        //根据队列名 查找一下对应的队列消息链表
        LinkedList<Message> messages = queueMessageMap.get(queueName);
        if(messages == null ) {
            return null;
        }
        synchronized (messages) {
            if(messages.size() == 0) {
                return null;
            }
            //链表中有元素就进行头删
            Message currentMessage = messages.remove(0);
            System.out.println("[MemoryDataCenter] 消息从队列中取出! messageId=" + currentMessage.getMessageId());
            return currentMessage;
        }
    }

    //获取指定队列中的消息个数
    public int getMessageCount(String queueName) {
        LinkedList<Message> messages = queueMessageMap.get(queueName);
        if(messages == null) {
            return 0;
        }
        synchronized(messages) {
            return messages.size();
        }
    }

    //添加未确认消息
    public void addMessageWaitAck(String queueName,Message message) {
        ConcurrentHashMap<String,Message> messageHashMap = queueMessageWaitAckMap.computeIfAbsent(
                queueName,k -> new ConcurrentHashMap<>());
        messageHashMap.put(message.getMessageId(),message);
    }

    //删除未确认消息(消息已经确认了)
    public void removeMessageWaitAck(String queueName,String messageId) {
        ConcurrentHashMap<String,Message> messageHashMap = queueMessageWaitAckMap.get(queueName);
        if(messageHashMap == null) {
            //表示当前查找的hashMap不存在
            return;
        }
        messageHashMap.remove(messageId);
        System.out.println("[MemoryDataCenter] 消息从待确认队列删除! messageId=" + messageId);
    }

    //获取指定的未确认消息
    public Message getMessageWaitAck(String queueName,String messageId) {
        ConcurrentHashMap<String,Message> messageHashMap = queueMessageWaitAckMap.get(queueName);
        if(messageHashMap == null) {
            //表示当前查找的hashMap不存在
            return null;
        }
        return messageHashMap.get(messageId);
    }

    //从硬盘上读取数据 把数据之前持久化存储的各个维度的数据都恢复到内存中
    public void recovery(DiskDataCenter diskDataCenter) throws IOException, MqException,ClassNotFoundException {
        //0.先清空所有的数据
        exchangeMap.clear();
        queueMap.clear();
        bindingsMap.clear();
        messageMap.clear();
        queueMessageMap.clear();
        //1.恢复所有的交换机数据
        List<Exchange> exchanges = diskDataCenter.selectAllExchange();
        for(Exchange exchange: exchanges) {
            exchangeMap.put(exchange.getName(),exchange);
        }
        //2.恢复所有的队列数据
        List<MSGQueue> msgQueues = diskDataCenter.selectAllQueue();
        for(MSGQueue msgQueue: msgQueues) {
            queueMap.put(msgQueue.getName(), msgQueue);
        }
        //3.恢复所有的绑定数据
        List<Binding> bindings = diskDataCenter.selectAllBindings();
        for(Binding binding: bindings) {
            //根据binding.getExchangeName()查找是否有hash表 没有的话需要创建这个hash表
            ConcurrentHashMap<String,Binding> bindingMap = bindingsMap.computeIfAbsent(binding.getExchangeName(),
                    k -> new ConcurrentHashMap<>());
            bindingMap.put(binding.getQueueName(),binding);
        }

        //4.恢复所有的消息数据
        //遍历所有队列 根据每个队列的名字 读取到所有的消息
        for(MSGQueue queue : msgQueues) {
            LinkedList<Message> messages = diskDataCenter.loadAllMessageFromQueue(queue.getName());
            queueMessageMap.put(queue.getName(),messages);
            for(Message message : messages) {
                messageMap.put(message.getMessageId(),message);
            }
        }
        // 注意!! 针对 "未确认的消息" 这部分内存中的数据, 不需要从硬盘恢复. 之前考虑硬盘存储的时候, 也没设定这一块.
        // 一旦在等待 ack 的过程中, 服务器重启了, 此时这些 "未被确认的消息", 就恢复成 "未被取走的消息" .
        // 这个消息在硬盘上存储的时候, 就是当做 "未被取走"
    }
}
