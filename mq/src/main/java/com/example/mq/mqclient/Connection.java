package com.example.mq.mqclient;

import com.example.mq.common.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Connection {
    private Socket socket = null;
    //若需要管理多个channel 使用一个hash表把若干的channel组织起来
    private ConcurrentHashMap<String,Channel> channelMap = new ConcurrentHashMap<>();
    private InputStream inputStream;
    private OutputStream outputStream;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private ExecutorService callbackPool = null;

    public Connection(String host,int port) throws IOException {
        socket = new Socket(host, port);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        dataInputStream = new DataInputStream(inputStream);
        dataOutputStream = new DataOutputStream(outputStream);

        callbackPool = Executors.newFixedThreadPool(4);
        //创建一个扫描线程  由这个线程负责不停的从socket中读取响应数据 把这个响应数据交给对应的channel来处理
        Thread t = new Thread(() -> {
            try {
                while(!socket.isClosed()) {
                    Response response = readResponse();
                    dispatchResponse(response);
                }
            } catch (SocketException e) {
                //连接正常是断开的 此时这个异常直接忽略
                System.out.println("[Connection] 连接正常断开!");
            } catch (IOException | ClassNotFoundException | MqException e) {
                System.out.println("[Connection] 连接异常断开!");
                e.printStackTrace();
            }
        });
        t.start();
    }

    //使用方法来处理当前是针对控制请求的响应还是服务器推送的消息
    private void dispatchResponse(Response response) throws IOException, ClassNotFoundException, MqException {
        if(response.getType() == 0xc) {
            //服务器推送来的消息数据
            SubscriberReturns subscriberReturns = (SubscriberReturns) BinaryTool.fromBytes(response.getPayload());
            //根据channelId找到对应的channel对象
            Channel channel = channelMap.get(subscriberReturns.getChannelId());
            if(channel == null) {
                throw new MqException("[Connection] 该消息对应的channel在客户端中不存在!channelId=" + channel.getChannelId());
            }
            //执行该channel对象内部的回调
            callbackPool.submit(() -> {
                try {
                    channel.getConsumer().handleDelivery(subscriberReturns.getConsumerTag(),
                            subscriberReturns.getBasicProperties(), subscriberReturns.getBody());
                } catch (MqException | IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            //当前响应是针对刚才的控制请求的响应
            BasicReturns basicReturns = (BasicReturns) BinaryTool.fromBytes(response.getPayload());
            //把对应的结果放在channel的hash表中
            Channel channel = channelMap.get(basicReturns.getChannelId());
            if(channel == null) {
                throw new MqException("[Connection] 该消息对应的channel在客户端中不存在!channelId=" + channel.getChannelId());
            }
            channel.putReturns(basicReturns);

        }
    }
    public void close() {
        //关闭connection的所有资源
        try {
            callbackPool.shutdownNow();
            channelMap.clear();
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //发送请求
    public void writeRequest(Request request) throws IOException {
        dataOutputStream.writeInt(request.getType());
        dataOutputStream.writeInt(request.getLength());
        dataOutputStream.write(request.getPayload());
        dataOutputStream.flush();
        System.out.println("[Connection] 发送请求! type=" + request.getType() + ", length=" + request.getLength());
        }

    //读取响应
    public Response readResponse() throws IOException {
        Response response = new Response();
        response.setType(dataInputStream.readInt());
        response.setLength(dataInputStream.readInt());
        byte[] payload = new byte[response.getLength()];
        int n = dataInputStream.read(payload);
        if(n != response.getLength()) {
            throw new IOException("读取的响应不完整!");
        }
        response.setPayload(payload);
        System.out.println("[Connection] 收到响应! type=" + response.getType() + ", length=" + response.getLength());
        return response;
    }

    //通过这个方法在connection中创建出一个channel
    public Channel createChannel() throws IOException {
        String channelId = "C-" + UUID.randomUUID().toString();
        Channel channel = new Channel(channelId,this);
        //把这个channel对象放在Connection管理的channel的哈希表中
        channelMap.put(channelId,channel);
        //同时需要把创建的channel的消息也告诉服务器
        boolean ok = channel.createChannel();
        if(!ok) {
            //服务器这里创建失败了 整个创建channel操作不顺利
            //把刚才已经加入hash表的键值对 在删除了
            channelMap.remove(channelId);
            return null;
        }
        return channel;
    }

}
