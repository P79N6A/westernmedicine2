package com.xywy.im.sdk;

/**
 * Created by xugan on 2018/7/17.
 */

public interface PeerMessageObserver {
    public void onPeerMessage(Message msg);//收到消息
    public void onPeerMessageSending(Message msg);//消息处于发送中的状态
    public void onPeerMessageACK(String msgLocalID);//发送消息后收到服务端返回的回执,相当于消息发送成功了
    public void onPeerMessageSendFailure(String msgLocalID);//消息发送失败
}
