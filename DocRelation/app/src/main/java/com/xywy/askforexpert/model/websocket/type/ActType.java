package com.xywy.askforexpert.model.websocket.type;

/**
 * Created by bailiangjin on 2017/4/27.
 */

public enum ActType {
    CONNECT,    //发起链接
    CONNECT_FAIL,    //链接失败
    CONNECT_ACK,    //链接成功
    PUB,        //消息发送，双向
    PUB_ACK,        //消息回执
    READ,        //消息已读
    PING,        //心跳
    PONG        //心跳回复

}
