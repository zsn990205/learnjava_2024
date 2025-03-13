package com.example.mq.common;

import java.io.Serializable;

/*
表示各个远程调用方法的返回值的公共信息
 */
public class BasicReturns implements Serializable {
    //用来标识唯一的请求和响应
    protected String rid;
    //用来标识一个channel
    protected String channelId;
    //表示当前这个远程调用方法的返回值
    protected boolean ok;

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

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
