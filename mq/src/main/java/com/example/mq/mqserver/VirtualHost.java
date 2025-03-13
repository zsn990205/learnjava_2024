package com.example.mq.mqserver;

import com.example.mq.common.Consumer;
import com.example.mq.common.MqException;
import com.example.mq.mqserver.core.*;
import com.example.mq.mqserver.datacenter.DiskDataCenter;
import com.example.mq.mqserver.datacenter.MemoryDataCenter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
通过这个类老表示虚拟主机
每个虚拟主机下面都管理自己的交换机 队列 绑定 消息 数据
为了保证不同虚拟主机的内容不要互相影响(在virtualHost类中做就行) VirtualHost1testExchange VirtualHost2testExchange
同时提供api供上层调用
此处的这个类 作为业务逻辑的整合者 需要对异常进行处理而不能交给上层处理
不要在往上报了
 */

public class VirtualHost {
    private String virtualHostName;
    private MemoryDataCenter memoryDataCenter = new MemoryDataCenter();
    private DiskDataCenter diskDataCenter = new DiskDataCenter();
    private Router router = new Router();
    //操作交换机的锁对象
    private final Object exchangeLocker = new Object();
    //操作队列的锁对象
    private final Object exchangeQueueLocker = new Object();
    private ConsumerManager consumerManager = new ConsumerManager(this);

    public VirtualHost(String name) {
        this.virtualHostName = name;

        //对于MemoryDataCenter来说 不需要额外的初始化操作 只需要将对象New出来
        //对于DiskDataCenter来说 需要进行初始化操作 建库建表和初始数据的设定
        diskDataCenter.init();

        //另外还需要针对硬盘的数据 进行恢复到内存中
        try {
            memoryDataCenter.recovery(diskDataCenter);
        } catch (IOException | MqException |ClassNotFoundException  e) {
            e.printStackTrace();
            System.out.println("[VirtualHost] 恢复内存数据失败!");
        }
    }

    public String getVirtualHostName() {
        return virtualHostName;
    }

    public MemoryDataCenter getMemoryDataCenter() {
        return memoryDataCenter;
    }

    public DiskDataCenter getDiskDataCenter() {
        return diskDataCenter;
    }


    //创建交换机
    //如果交换机不存在就创建 如果存在就直接返回
    //交换机的名字 = 虚拟主机的名字 + 交换机的实际名字
    //为啥api叫exchangeDeclare 且参数的实现 是参考rabbitMQ的
    public boolean exchangeDeclare(String exchangeName, ExchangeType exchangeType, boolean durable, boolean autoDelete,
                                   Map<String,Object> arguments) {
        //把交换机的名字 加上虚拟主机作为前提  区分不同的虚拟主机
        exchangeName = virtualHostName + exchangeName;
        try {
            synchronized (exchangeLocker) {
                //1.判定交换机是否存在 从内存中查询
                Exchange existsExchange = memoryDataCenter.getExchange(exchangeName);
                if(existsExchange != null) {
                    System.out.println("[VirtualHost] 交换机已经存在! exchangeName=" + exchangeName);
                    return true;
                }
                //2.交换机不存在 创建交换机
                Exchange exchange = new Exchange();
                exchange.setName(exchangeName);
                exchange.setDurable(durable);
                exchange.setArguments(arguments);
                exchange.setType(exchangeType);
                exchange.setAutoDelete(autoDelete);
                //3.把构造好的交换机类型写入硬盘
                //写入硬盘的操作更容易失败 所以放在前面
                if(durable) {
                    diskDataCenter.insertExchange(exchange);
                }
                //4.把交换机对象写入内存
                memoryDataCenter.insertExchange(exchange);
                System.out.println("[VirtualHost] 交换机创建完成! exchangeName=" + exchangeName);
            }
            return true;
        } catch (Exception e) {
            //直接抛出所有异常
            e.printStackTrace();
            System.out.println("[VirtualHost] 交换机创建失败! exchangeName=" + exchangeName);
            return false;
        }
    }

    //删除交换机
    public boolean exchangeDelete(String exchangeName) {
        exchangeName = virtualHostName + exchangeName;
        try {
            synchronized (exchangeLocker) {
                //1.先找到对应的交换机
                Exchange existsExchange = memoryDataCenter.getExchange(exchangeName);
                if(existsExchange == null) {
                    //表示此时查找的这个交换机不存在 那就不需要删除
                    throw new MqException("[VirtualHost] 交换机不存在,删除失败!");
                }
                //表示此时查找的交换机存在 根据指定的名字进行删除
                //现在磁盘上进行删除
                if(existsExchange.isDurable()) {
                    //不需要持久化的数据才能进行删除
                    diskDataCenter.deleteExchange(exchangeName);
                }
                //再在内存上进行删除
                memoryDataCenter.deleteExchange(exchangeName);
                System.out.println("[VirtualHost] 交换机删除成功! exchangeName=" + exchangeName);
            }
            return true;
        } catch (Exception e) {
            System.out.println("[VirtualHost] 交换机删除失败! exchangeName=" + exchangeName);
            e.printStackTrace();
            return false;
        }
    }

    //创建队列
    public boolean queueDeclare(String queueName,boolean durable,boolean exclusive,boolean autoDelete,
                                Map<String,Object> arguments) {
        //把队列的名字加上虚拟机的名字
        queueName = virtualHostName + queueName;
        try {
            synchronized (exchangeQueueLocker) {
                //1.判定队列是否存在
                MSGQueue existsQueue = memoryDataCenter.getQueue(queueName);
                if(existsQueue != null) {
                    //此时队列存在
                    System.out.println("[VirtualHost] 队列已经存在! queueName=" + queueName);
                    return true;
                }
                //此时队列不存在 创建队列
                MSGQueue queue = new MSGQueue();
                queue.setName(queueName);
                queue.setExclusive(exclusive);
                queue.setDurable(durable);
                queue.setAutoDelete(autoDelete);
                queue.setArguments(arguments);

                //存在就插入
                if(durable) {
                    diskDataCenter.insertQueue(queue);
                }
                memoryDataCenter.insertQueue(queue);
                System.out.println("[VirtualHost] 创建队列成功! queueName=" + queueName);
            }
            return true;
        } catch (Exception e) {
            System.out.println("[VirtualHost] 创建队列失败! queueName=" + queueName);
            e.printStackTrace();
            return false;
        }
    }

    //删除队列
    public boolean queueDelete(String queueName) {
        queueName = virtualHostName + queueName;
        try {
            synchronized (exchangeQueueLocker) {
                MSGQueue existsQueue = memoryDataCenter.getQueue(queueName);
                if(existsQueue == null) {
                    //此处的异常在下面会catch 所以也是打印异常并不是让程序崩溃!
                    throw  new MqException("[VirtualHost] 队列不存在,无法删除! queueName=" + queueName);
                }
                if(existsQueue.isDurable()) {
                    diskDataCenter.deleteQueue(queueName);
                }
                memoryDataCenter.deleteQueue(queueName);
                System.out.println("[VirtualHost] 删除队列成功! queueName=" + queueName);
            }
            return true;
        } catch (Exception e) {
            System.out.println("[VirtualHost] 删除队列失败!");
            e.printStackTrace();
            return false;
        }
    }

    //创建绑定
    public boolean queueBind(String queueName,String exchangeName,String bindingKey) {
        queueName = virtualHostName + queueName;
        exchangeName = virtualHostName + exchangeName;
        //1.查找当前的绑定是否存在
        try {
            synchronized (exchangeLocker) {
                synchronized (exchangeQueueLocker) {
                    Binding existsBinding = memoryDataCenter.getBinding(exchangeName,queueName);
                    if(existsBinding != null) {
                        //表示当前的绑定存在
                        throw  new MqException("[VirtualHost] 绑定已经存在,不需要额外创建! queueName=" + queueName +
                                "exchangeName=" + exchangeName);
                    }
                    //2.验证bindingkey是否合法
                    if(!router.checkBindingKey(bindingKey)) {
                        //当bindingKey不合法的时候就要抛出异常
                        throw new MqException("[VirtualHost] bindingKey 非法 bindingKey" + bindingKey);
                    }
                    //3.创建绑定对象
                    Binding binding = new Binding();
                    binding.setExchangeName(exchangeName);
                    binding.setQueueName(queueName);
                    binding.setBindingKey(bindingKey);

                    //4.获取一下对应的交换机或队列
                    //如果不存在创建将毫无意义
                    MSGQueue queue = memoryDataCenter.getQueue(queueName);
                    if(queue == null) {
                        //表示当前的queue不存在 那么创建将毫无意义
                        throw new MqException("[VirtualHost] 队列不存在! queueName" + queueName);
                    }
                    Exchange exchange = memoryDataCenter.getExchange(exchangeName);
                    if(exchange == null) {
                        //表示当前的queue不存在 那么创建将毫无意义
                        throw new MqException("[VirtualHost] 交换机不存在! exchangeName" + exchangeName);
                    }

                    //当前的交换机和队列均存在进行插入即可
                    if(queue.isDurable() && exchange.isDurable()) {
                        //当交换机和硬盘都是持久化的时候才能创建
                        diskDataCenter.insertBinding(binding);
                    }
                    //写入内存
                    memoryDataCenter.insertBinding(binding);
                    System.out.println("[VirtualHost] 创建绑定成功! queueName=" + queueName +
                            "exchangeName=" + exchangeName);
                }
            }
            return true;

        } catch (Exception e) {
            System.out.println("[VirtualHost] 创建绑定失败!");
            e.printStackTrace();
            return false;
        }
    }

    //删除绑定
    public boolean queueUnBinding(String queueName,String exchangeName) {
        queueName = virtualHostName + queueName;
        exchangeName = virtualHostName + exchangeName;
        try {
            synchronized (exchangeLocker) {
                synchronized (exchangeQueueLocker) {
                    //1.先查找绑定是否存在
                    Binding existBinding = memoryDataCenter.getBinding(exchangeName,queueName);
                    if(existBinding == null) {
                        //此时existBinding不存在 无需删除
                        throw new MqException("[VirtualHost] 绑定不存在,不需要删除!");
                    }
//            //2.获取对应的队列和交换机是否存在
//            Exchange exchange = memoryDataCenter.getExchange(exchangeName);
//            if(exchange == null) {
//                throw new MqException("[VirtualHost] 交换机不存在,无法删除! exchangeName=" + exchangeName);
//            }
//            MSGQueue queue = memoryDataCenter.getQueue(queueName);
//            if(queue == null) {
//                throw new MqException("[VirtualHost] 队列不存在,无法删除! queueName=" + queueName);
//            }
//            //此时的交换机和队列均存在
//            if(exchange.isDurable() && queue.isDurable()) {
//                //如果均为持久化操作
//                diskDataCenter.deleteBinding(existBinding);
//            }
                    //无论持久化是否存在均进行删除
                    diskDataCenter.deleteBinding(existBinding);
                    memoryDataCenter.deleteBinding(existBinding);
                    System.out.println("[VirtualHost] 绑定删除成功!");
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("[VirtualHost] 绑定删除失败!");
            e.printStackTrace();
            return false;
        }
    }

    //发送消息到指定的队列中
    public boolean basicPublish(String exchangeName,String routingKey,BasicProperties basicProperties,
                                byte[] body) {
//        try {
//            //1.转换交换机的名字
//            exchangeName = exchangeName + virtualHostName;
//            //2.检查routingKey是否合法
//            if(!(router.checkRoutingKey(routingKey))) {
//                throw new MqException("[VirtualHost] routingKey非法! routingKey=" + routingKey);
//            }
//            //3.查找交换机对象
//            Exchange exchange = memoryDataCenter.getExchange(exchangeName);
//            if(exchange == null) {
//                throw new MqException("[VirtualHost] 交换机不存在! exchangeName=" + exchangeName);
//            }
//            //4.判断交换机的类型
//            if(exchange.getType() == ExchangeType.Direct) {
//                //此时的交换机是直接交换机
//                //以routingKey作队列的名字 直接把消息写入指定的队列中 此时可以无视绑定关系
//                String queueName = virtualHostName + routingKey;
//                //5.构造消息对象
//                Message message = Message.createMessageWithId(routingKey,basicProperties,body);
//                //6.查找该队列名对应的对象
//                MSGQueue queue = memoryDataCenter.getQueue(queueName);
//                if(queue == null) {
//                    throw new MqException("[VirtualHost] 队列不存在! queueName=" + queueName);
//                }
//                //7.队列存在,直接给队列中写入消息
//                sendMessage(queue,message);
//
//            } else {
//                //此时的交换机是Fanout或者topic
//                //5.找到该交换机对应的所有绑定并遍历这些绑定对象
//                ConcurrentHashMap<String,Binding> bindingsMap = memoryDataCenter.getBindings(exchangeName);
//                for(Map.Entry<String,Binding> entry : bindingsMap.entrySet()) {
//                    //1)获取到绑定对象 判定对应的队列是否存在
//                    Binding binding = entry.getValue();
//                    MSGQueue queue = memoryDataCenter.getQueue(binding.getQueueName());
//                    if(queue == null) {
//                        //此处咱们就不需要抛出异常了 可能此处会有多个队列
//                        //不要因为一个队列的失败 影响到其他消息的传输
//                        System.out.println("[VirtualHost] basicPublish 发送消息时,发现队列不存在! queueName=" +
//                                binding.getQueueName());
//                        continue;
//                    }
//                    //2)构造消息对象
//                    Message message = Message.createMessageWithId(routingKey,basicProperties,body);
//                    //3)判定这个消息是否能转发给该队列
//                    //  如果fanout 所有绑定的队列都要转发的
//                    //  如果是topic 还需要判定bindingKey和routingKey是不匹配
//                    if(!router.route(exchange.getType(),binding,message)) {
//                        continue;
//                    }
//                    //真正转发消息给队列
//                    sendMessage(queue,message);
//                }
//            }
//
//            return true;
//        } catch (Exception e) {
//            System.out.println("[VirtualHost] 发送消息到指定的队列失败!");
//            e.printStackTrace();
//            return false;
//        }

        try {
            // 1. 转换交换机的名字
            exchangeName = virtualHostName + exchangeName;
            // 2. 检查 routingKey 是否合法.
            if (!router.checkRoutingKey(routingKey)) {
                throw new MqException("[VirtualHost] routingKey 非法! routingKey=" + routingKey);
            }
            // 3. 查找交换机对象
            Exchange exchange = memoryDataCenter.getExchange(exchangeName);
            if (exchange == null) {
                throw new MqException("[VirtualHost] 交换机不存在! exchangeName=" + exchangeName);
            }
            // 4. 判定交换机的类型
            if (exchange.getType() == ExchangeType.Direct) {
                // 按照直接交换机的方式来转发消息
                // 以 routingKey 作为队列的名字, 直接把消息写入指定的队列中.
                // 此时, 可以无视绑定关系.
                String queueName = virtualHostName + routingKey;
                // 5. 构造消息对象
                Message message = Message.createMessageWithId(routingKey, basicProperties, body);
                // 6. 查找该队列名对应的对象
                MSGQueue queue = memoryDataCenter.getQueue(queueName);
                if (queue == null) {
                    throw new MqException("[VirtualHost] 队列不存在! queueName=" + queueName);
                }
                // 7. 队列存在, 直接给队列中写入消息
                sendMessage(queue, message);
            } else {
                // 按照 fanout 和 topic 的方式来转发.
                // 5. 找到该交换机关联的所有绑定, 并遍历这些绑定对象
                ConcurrentHashMap<String, Binding> bindingsMap = memoryDataCenter.getBindings(exchangeName);
                for (Map.Entry<String, Binding> entry : bindingsMap.entrySet()) {
                    // 1) 获取到绑定对象, 判定对应的队列是否存在
                    Binding binding = entry.getValue();
                    MSGQueue queue = memoryDataCenter.getQueue(binding.getQueueName());
                    if (queue == null) {
                        // 此处咱们就不抛出异常了. 可能此处有多个这样的队列.
                        // 希望不要因为一个队列的失败, 影响到其他队列的消息的传输.
                        System.out.println("[VirtualHost] basicPublish 发送消息时, 发现队列不存在! queueName=" + binding.getQueueName());
                        continue;
                    }
                    // 2) 构造消息对象
                    Message message = Message.createMessageWithId(routingKey, basicProperties, body);
                    // 3) 判定这个消息是否能转发给该队列.
                    //    如果是 fanout, 所有绑定的队列都要转发的.
                    //    如果是 topic, 还需要判定下, bindingKey 和 routingKey 是不是匹配.
                    if (!router.route(exchange.getType(), binding, message)) {
                        continue;
                    }
                    // 4) 真正转发消息给队列
                    sendMessage(queue, message);
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("[VirtualHost] 消息发送失败!");
            e.printStackTrace();
            return false;
        }
    }

    private void sendMessage(MSGQueue queue, Message message) throws IOException, MqException, InterruptedException {
        //此处发送消息就是把消息写入内存和硬盘中
        int deliverMode = message.getDeliverMode();
        //deliverMode为1的话不需要持久化 为2的话需要持久化
        if(deliverMode == 2) {
            diskDataCenter.sendMessage(queue,message);
        }
        //写入内存
        memoryDataCenter.sendMessage(queue,message);

        // 还需要补充消费者可以消费消息了
        consumerManager.notifyConsumer(queue.getName());
    }

    //订阅消息
    //添加一个队列的订阅者 当队列收到消息之后 就把消息推送给对应的订阅者
    // consumerTag: 消费者的身份标识
    // autoAck: 消息被消费完成后, 应答的方式. 为 true 自动应答. 为 false 手动应答.
    // consumer: 是一个回调函数. 此处类型设定成函数式接口. 这样后续调用 basicConsume 并且传实参的时候, 就可以写作 lambda 样子了.
    public boolean basicConsume(String consumerTag, String queueName, boolean autoAck, Consumer consumer) {
        //构造一个consumerEnv对象 把这个对应的队列找到 再把这个consume对象添加到该队列中
        //一定要先把queueName 进行修改和上述逻辑一致
        queueName = virtualHostName + queueName;
        try {
            consumerManager.addConsumer(consumerTag,queueName,autoAck,consumer);
            System.out.println("[VirtualHost] basicConsumer成功! queueName=" + queueName);
            return true;
        } catch (Exception e) {
            System.out.println("[VirtualHost] basicConsumer失败! queueName=" + queueName);
            e.printStackTrace();
            return false;
        }
    }

    public boolean basicAck(String queueName,String messageId) {
        queueName = virtualHostName + queueName;
        try {
            //1.获取到消息和队列
            Message message = memoryDataCenter.getMessages(messageId);
            if(message == null) {
                throw new MqException("[VirtualHost] 要确认的消息不存在! messageId=" + messageId);
            }
            MSGQueue queue = memoryDataCenter.getQueue(queueName);
            if(queue == null) {
                throw new MqException("[VirtualHost] 要确认的队列不存在! queueName=" + queueName);
            }
            //2.删除硬盘上的数据
            if(message.getDeliverMode() == 2) {
                diskDataCenter.deleteMessage(queue,message);
            }
            //3.删除消息中心的数据
            memoryDataCenter.deleteMessage(messageId);
            //4.删除待确认集合中的数据
            memoryDataCenter.removeMessageWaitAck(queueName,messageId);
            System.out.println("[VirtualHost] basicAck成功! 消息被成功确认! queueName=" + queueName
                    + ", messageId=" +messageId);
            return true;
        } catch (Exception e) {
            System.out.println("[VirtualHost] basicAck失败! 消息确认失败! queueName=" + queueName
                    + ", messageId=" +messageId);
            e.printStackTrace();
            return false;
        }
    }
}
