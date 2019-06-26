package com.xywy.askforexpert.module.websocket;

import com.xywy.askforexpert.model.websocket.msg.chatmsg.ChatMsg;
import com.xywy.askforexpert.model.websocket.rxevent.MsgReadEventBody;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.retrofit.rxbus.RxBus;

/**
 * Author blj
 */
public class WebSocketRxBus {

    public static void notifyReconnectionSocket(String close){
        postEvent("onChatMsgRead", close);
    }


    public static void registerReconnectionSocketListener(EventSubscriber<String> subscriber, Object tag){
        registerEvent("onChatMsgRead", subscriber, tag);
    }


    /**
     * notify websocket 聊天消息已读
     */
    public static void notifyChatMsgRead(MsgReadEventBody msgReadEventBody){
        postEvent("onChatMsgRead", msgReadEventBody);
    }


    public static void registerChatMagReadListener(EventSubscriber<MsgReadEventBody> subscriber, Object tag){
        registerEvent("onChatMsgRead", subscriber, tag);
    }

    /**
     * notify websocket 会话已经开始
     */
    public static void notifyChatStarted(Integer seq){
        postEvent("onChatStarted", seq);
    }

    public static void registerChatStartedListener(EventSubscriber<Integer> subscriber, Object tag){
        registerEvent("onChatStarted", subscriber, tag);
    }

    /**
     * notify websocket 聊天消息已收到
     */
    public static void notifyChatMsgReceived(String msgId){
        postEvent("onChatMsgReceived", msgId);
    }

    public static void registerWebSocketChatMagReceivedListener(EventSubscriber<String> subscriber, Object tag){
        registerEvent("onChatMsgReceived", subscriber, tag);
    }


    /**
     * notify websocket 聊天消息到来
     */
    public static void notifyChatMsg(ChatMsg chatMsg){
        postEvent("onWebSocketChatMsg", chatMsg);
    }

    /**
     * 注册 websocket 聊天消息到来监听
     * @param subscriber
     * @param tag
     * @return
     */
    public static void registerWebSocketChatMagListener(EventSubscriber<ChatMsg> subscriber, Object tag){
        registerEvent("onWebSocketChatMsg", subscriber, tag);
    }



    public static void postEvent(String eventId,Object data){
        RxBus.getDefault().post(new Event<>(eventId,data));
    }

    public static void registerEvent(String eventId, EventSubscriber subscriber, Object tag){
         RxBus.getDefault().registerEvent(eventId,subscriber, tag);
    }
}
