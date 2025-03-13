package com.example.mq.common;

import java.io.Serializable;

/*
表示方法的公共参数/辅助的字段
后续的每个方法又会有一些不同的参数 不同的参数再分别使用不同的子类
 */
public class BasicArguments implements Serializable {
    //表示一次请求/响应 身份标识 可以把请求和响应对上
    protected String rid;
    //这次通信使用的channel身份标识
    protected String channelId;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
