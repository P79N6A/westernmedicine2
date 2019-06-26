package com.xywy.askforexpert.module.websocket;

import com.xywy.askforexpert.model.websocket.msg.chatmsg.ChatMsg;

/**
 * Created by bailiangjin on 2017/4/28.
 */

public interface WebSocketImInterface {

    /**
     * 建立连接
     *
     * @param webAddress
     * @param userId
     */
    void connect(String webAddress,String userId);


    /**
     * 断开链接
     */
    //void disConnect();

    /**
     * socket会话链接是否已建立
     * @return
     */
    boolean isConnected();


    /**
     * 发送任意字符串
     *
     * @param json
     */
    void sendMsg(String json);

    /**
     * 发送聊天消息
     *
     * @param from
     * @param to
     * @param msgId
     * @param qid
     * @param content
     */
    void sendChatMsg(String from, String to, String msgId, String qid, String content);

    /**
     * 发送聊天消息
     *
     * @param chatMsg
     */
    void sendChatMsg(ChatMsg chatMsg);

    /**
     * 发送消息收到回执
     *
     * @param msgId
     */
    void sendAckMsg(String msgId);

    /**
     * 发送消息已读回执
     *
     * @param msgId
     * @param qid
     */
    void sendReadMsg(String msgId, String qid);

    /**
     * 建立连接
     */
    //void onConnected();

    /**
     * 开始会话
     *
     * @param sequence 消息序列起始id
     */
    void onStartChat(int sequence);

    /**
     * 连接断开
     */
    //void onDisconnect();


    /**
     * 连接出现异常
     *
     * @param e
     */
    void onConnectError(Exception e);

    /**
     * 收到消息 (用户发来的聊天消息)
     *
     * @param chatMsg
     */
    void onChatMsg(ChatMsg chatMsg);

    /**
     * 消息已读回执 服务端发来的
     *
     * @param msgId
     * @param qid
     */
    void onChatMsgRead(String msgId, String qid);

    /**
     * 服务端收到消息ck回执
     * @param msgId
     */
    void onChatMsgReceived(String msgId);


}
