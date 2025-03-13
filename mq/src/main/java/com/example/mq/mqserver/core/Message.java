package com.example.mq.mqserver.core;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

//表示一个要传递的消息
//一个message包含属性和正文部分
public class Message implements Serializable {
    private BasicProperties basicProperties = new BasicProperties();
    private byte[] body;

    //下面是一些辅助属性
    //message后续会存在文件中(如果持久化的话)
    //一个文件会存很多消息 如何找到某个消息在文件的具体位置呢?
    //使用偏移量表示[offsetBeg,offsetEnd)
    private transient long offsetBeg = 0;  //消息数据的开头距离文件开头的位置偏移(字节)
    private transient long offsetEnd = 0;  //消息数据的结尾距离文件开头的位置偏移(字节)
    //使用这个属性表示该消息在文件中是否是有效消息(文件中的消息的删除方式使用逻辑删除)
    //0x1表示有效(未删除) 0x0表示无效(已删除)
    private byte isValid = 0x1;

    //创建一个工厂方法,让工厂方法帮我们封装一个message对象的过程
    //这个方法中创建的message对象会自动生成唯一的messageId
    public static Message createMessageWithId(String routingKey,BasicProperties basicProperties,byte[] body) {
        Message message = new Message();
        if(basicProperties != null) {
            message.setBasicProperties(basicProperties);
        }
        //此处生成的messageId是以M开头的
        message.setMessageId("M-" + UUID.randomUUID());
        message.basicProperties.setRoutingKey(routingKey);
        message.body = body;
        return message;
    }

    public String getMessageId() {
        return basicProperties.getMessageId();
    }

    public void setMessageId(String messageId) {
        basicProperties.setMessageId(messageId);
    }

    public String getRoutingKey() {
        return basicProperties.getRoutingKey();
    }

    public void setRoutingKey(String routingKey) {
        basicProperties.setRoutingKey(routingKey);
    }

    public int getDeliverMode() {
        return basicProperties.getDeliverMode();
    }

    public void setDeliverMode(int mode) {
        basicProperties.setDeliverMode(mode);
    }

    public BasicProperties getBasicProperties() {
        return basicProperties;
    }

    public void setBasicProperties(BasicProperties basicProperties) {
        this.basicProperties = basicProperties;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public long getOffsetBeg() {
        return offsetBeg;
    }

    public void setOffsetBeg(long offsetBeg) {
        this.offsetBeg = offsetBeg;
    }

    public long getOffsetEnd() {
        return offsetEnd;
    }

    public void setOffsetEnd(long offsetEnd) {
        this.offsetEnd = offsetEnd;
    }

    public byte getIsValid() {
        return isValid;
    }

    public void setIsValid(byte isValid) {
        this.isValid = isValid;
    }

    @Override
    public String toString() {
        return "Message{" +
                "basicProperties=" + basicProperties +
                ", body=" + Arrays.toString(body) +
                ", offsetBeg=" + offsetBeg +
                ", offsetEnd=" + offsetEnd +
                ", isValid=" + isValid +
                '}';
    }
}
