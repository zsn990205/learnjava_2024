package com.example.mq.mqserver;

//BrokerServer就是消息队列的本体服务器

import com.example.mq.common.*;
import com.example.mq.mqserver.core.BasicProperties;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BrokerServer {
    private ServerSocket serverSocket = null;

    //当前考虑一个BrokerServer只有一个虚拟主机
    private VirtualHost virtualHost = new VirtualHost("default");
    // 使用这个 哈希表 表示当前的所有会话(也就是说有哪些客户端正在和咱们的服务器进行通信)
    // 此处的 key 是 channelId, value 为对应的 Socket 对象
    private ConcurrentHashMap<String, Socket> sessions = new ConcurrentHashMap<String, Socket>();
    // 引入一个线程池, 来处理多个客户端的请求.
    private ExecutorService executorService = null;
    // 引入一个 boolean 变量控制服务器是否继续运行
    private volatile boolean runnable = true;

    public BrokerServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    /*
    以下这个代码是通用的
     */
    public void start() throws IOException {
        System.out.println("[BrokerServer] 启动!");
        executorService = Executors.newCachedThreadPool();
        try {
            while (runnable) {
                Socket clientSocket = serverSocket.accept();
                //把处理连接的逻辑丢给这个线程池
                executorService.submit(() -> {
                    processConnection(clientSocket);
                });
            }
        }catch(SocketException e) {
            System.out.println("[BrokerServer] 服务器停止运行!");
        }
    }

    //一般来说停止服务器 就是直接kill掉对应的进程就行了
    //此处还需要搞一个单独的停止方法 主要是用于后续的单元测试
    public void stop() throws IOException {
        runnable = false;
        //把线程池的任务全部放弃 让线程都销毁
        executorService.shutdownNow();
        serverSocket.close();
    }

    //通过这个方法 处理客户端的连接
    //在这一个连接中 可能会设计多个请求和响应
    private void processConnection(Socket clientSocket) {
        try (InputStream inputStream = clientSocket.getInputStream();
             OutputStream outputStream = clientSocket.getOutputStream()) {
            //这里需要按照特定的格式读取并解析 此时就需要用到DataInputStream
            try (DataInputStream dataInputStream = new DataInputStream(inputStream);
                 DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
                while (true) {
                    //1.读取请求并解析
                    Request request = readRequest(dataInputStream);
                    //2.根据请求计算响应
                    Response response = process(request, clientSocket);
                    //3.把响应写回客户端
                    writeResponse(dataOutputStream, response);
                }
            }
        } catch (EOFException | SocketException e) {
            //对于这个代码, DataInputStream 如果读到 EOF , 就会抛出一个 EOFException 异常.
            //需要借助这个异常来结束循环
            //此处无须打印出异常信息栈
            System.out.println("[BrokerServer] connection 关闭! 客户端的地址: " + clientSocket.getInetAddress().toString()
                    + ":" + clientSocket.getPort());
        } catch (IOException | ClassNotFoundException | MqException e) {
            System.out.println("[BrokerServer] connection出现异常!");
            e.printStackTrace();
        } finally {
            //4.关闭socket对象
            try {
                clientSocket.close();
                // 一个 TCP 连接中, 可能包含多个 channel. 需要把当前这个 socket 对应的所有 channel 也顺便清理掉.
                clearClosedSession(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void clearClosedSession(Socket clientSocket) {
        List<String> toDeleteChannelId = new ArrayList<>();
        for(Map.Entry<String,Socket> entry : sessions.entrySet()) {
            if(entry.getValue() == clientSocket) {
                //不能直接删除 一边删除一边遍历会使迭代器直接失效
                toDeleteChannelId.add(entry.getKey());
            }
        }
        //在此处遍历的是toDeleteChannelId这个List  但是删除的sessions的数据
        //不能对同一个集合类又遍历又删除
        //但是可以对不同的集合类遍历+删除
        for(String channelId : toDeleteChannelId) {
                sessions.remove(channelId);
        }
        System.out.println("[BrokerServer] 清理 session 完成! 被清理的 channelId=" + toDeleteChannelId);

    }


    private void writeResponse(DataOutputStream dataOutputStream, Response response) throws IOException {
        //经检验必须先写type再写length才行
        dataOutputStream.writeInt(response.getType());
        dataOutputStream.writeInt(response.getLength());
        dataOutputStream.write(response.getPayload());
        //刷新缓冲区也是一个重要的操作
        //必须把内容刷入缓冲区才行
        dataOutputStream.flush();

    }

    private Response process(Request request, Socket clientSocket) throws IOException, ClassNotFoundException,MqException {
        //把response进行一个深度的解析
        BasicArguments basicArguments = (BasicArguments)BinaryTool.fromBytes(request.getPayload());
        System.out.println("[Request] rid=" + basicArguments.getRid() + ", channelId=" + basicArguments.getChannelId()
                + ", type=" + request.getType() + ", length=" + request.getLength());
        //2.根据 type的值区分下一步要干啥
        boolean ok = true;
        if(request.getType() == 0x1) {
            //此时要进行创建channel操作
            sessions.put(basicArguments.getChannelId(), clientSocket);
            System.out.println("[BrokerServer] 创建channel完成! channelId=" + basicArguments.getChannelId());
        } else if(request.getType() == 0x2) {
            //此时要进行销毁channel
            sessions.remove(basicArguments.getChannelId());
            System.out.println("[BrokerServer] 销毁channel完成! channelId=" + basicArguments.getChannelId());
        } else if(request.getType() == 0x3) {
           //此时要进行创建交换机 payload 就是ExchangeDeclareArguments对象
            ExchangeDeclareArguments arguments = (ExchangeDeclareArguments)basicArguments;
            ok = virtualHost.exchangeDeclare(arguments.getExchangeName(),arguments.getExchangeType(),arguments.isDurable(),
                    arguments.isAutoDelete(),arguments.getArguments());
        } else if(request.getType() == 0x4) {
            ExchangeDeleteArguments arguments = (ExchangeDeleteArguments)basicArguments;
            //删除交换机
            ok = virtualHost.exchangeDelete(arguments.getExchangeName());
        } else if(request.getType() == 0x5) {
            //创建队列
            QueueDeclareArguments arguments = (QueueDeclareArguments)basicArguments;
            ok = virtualHost.queueDeclare(arguments.getQueueName(),arguments.isDurable(),arguments.isExclusive(),
                    arguments.isAutoDelete(),arguments.getArguments());
        } else if(request.getType() == 0x6) {
            //删除队列
            QueueDeleteArguments arguments = (QueueDeleteArguments)basicArguments;
            ok = virtualHost.queueDelete(arguments.getQueueName());
        } else if(request.getType() == 0x7) {
            //创建绑定
            QueueBindArguments arguments = (QueueBindArguments) basicArguments;
            ok = virtualHost.queueBind(arguments.getQueueName(), arguments.getExchangeName(),arguments.getBindingKey());
        } else if(request.getType() == 0x8) {
            //删除绑定
            QueueUnBindArguments arguments = (QueueUnBindArguments) basicArguments;
            ok = virtualHost.queueUnBinding(arguments.getQueueName(), arguments.getExchangeName());
        } else if(request.getType() == 0x9) {
            //发送message
            BasicPublishArguments arguments = (BasicPublishArguments) basicArguments;
            ok = virtualHost.basicPublish(arguments.getExchangeName(), arguments.getRoutingKey(), arguments.getBasicProperties(),
                    arguments.getBody());
        } else if(request.getType() == 0xa) {
            BasicConsumeArguments arguments = (BasicConsumeArguments) basicArguments;
            ok = virtualHost.basicConsume(arguments.getConsumerTag(), arguments.getQueueName(), arguments.isAutoAck(),
                    new Consumer() {
                        //回调函数做的工作是将服务器收到的消息直接推送给对应的消费者客户端(处理的是0xc)
                        @Override
                        public void handleDelivery(String consumerTag, BasicProperties basicProperties, byte[] body) throws MqException, IOException {
                            //先知道当前的收到的消息 发送给哪个客户端
                            // 此处 consumerTag 其实是 channelId. 根据 channelId 去 sessions 中查询, 就可以得到对应的
                            // socket 对象了, 从而可以往里面发送数据了
                            // 1. 根据 channelId 找到 socket 对象
                            Socket clientSocket = sessions.get(consumerTag);
                            if(clientSocket == null || clientSocket.isClosed()) {
                                throw new MqException("[BrokerServer] 订阅消息的客户端已经关闭!");
                            }
                            SubscriberReturns returns = new SubscriberReturns();
                            returns.setChannelId(consumerTag);
                            returns.setRid("");  //由于这里只有响应没有请求 不需要对应 所以rid暂时不需要
                            returns.setOk(true);
                            returns.setConsumerTag(consumerTag);
                            returns.setBasicProperties(basicProperties);
                            returns.setBody(body);
                            byte[] payload = BinaryTool.toBytes(returns);

                            //2.构造响应数据
                            Response response = new Response();
                            //0xc表示服务器给消费者客户推送的消息数据
                            response.setType(0xc);
                            response.setLength(payload.length);
                            //response的payload就是一个SubscriberReturns
                            response.setPayload(payload);
                            //3.把数据写回给客户端
                            //注意此处的dataOutputStream不能close
                            //如果把 dataOutputStream 关闭, 就会直接把 clientSocket 里的 outputStream 也关了.
                            //此时就无法继续往 socket 中写入后续数据了.
                            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                            writeResponse(dataOutputStream,response);
                        }
                    });
        } else if(request.getType() == 0xb) {
            //调用basicAck来确认消息
            BasicAckArguments arguments = (BasicAckArguments) basicArguments;
            ok = virtualHost.basicAck(arguments.getQueueName(), arguments.getMessageId());
        } else {
            //当前的type是非法的
            throw new MqException("[BrokerServer] 当前的type非法 type=" + request.getType());
        }
        //3.构造响应
        BasicReturns basicReturns = new BasicReturns();
        basicReturns.setChannelId(basicArguments.getChannelId());
        basicReturns.setOk(ok);
        basicReturns.setRid(basicArguments.getRid());

        byte[] payload = BinaryTool.toBytes(basicReturns);
        Response response = new Response();
        response.setType(request.getType());
        response.setLength(payload.length);
        response.setPayload(payload);
        System.out.println("[Response] rid=" + basicReturns.getRid() + ", channelId=" + basicReturns.getChannelId() +
                ", type=" + response.getType() + ", length=" + response.getLength());
        return response;

    }

    private Request readRequest(DataInputStream dataInputStream) throws IOException {
        Request request = new Request();
        request.setType(dataInputStream.readInt());
        request.setLength(dataInputStream.readInt());
        byte[] payload = new byte[request.getLength()];
        int n = dataInputStream.read(payload);
        if(n != request.getLength()) {
            //读取的长度不符
            throw new IOException("读取请求格式出错!");
        }
        request.setPayload(payload);
        return request;

    }

}
