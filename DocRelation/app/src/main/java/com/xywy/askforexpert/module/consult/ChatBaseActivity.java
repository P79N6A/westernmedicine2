package com.xywy.askforexpert.module.consult;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.model.websocket.msg.chatmsg.ChatMsg;
import com.xywy.askforexpert.model.websocket.rxevent.MsgReadEventBody;
import com.xywy.askforexpert.module.websocket.WebSocketApi;
import com.xywy.askforexpert.module.websocket.WebSocketImInterface;
import com.xywy.askforexpert.module.websocket.WebSocketRxBus;
import com.xywy.easeWrapper.utils.ImFileCacheUtil;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;

/**
 * Created by bailiangjin on 2017/4/28.
 */

public abstract class ChatBaseActivity extends YMBaseActivity implements WebSocketImInterface {
    public ImFileCacheUtil imFileCacheUtil;
    @Override
    protected void beforeViewBind() {
        super.beforeViewBind();
        imFileCacheUtil = ImFileCacheUtil.getInstance();
        WebSocketRxBus.registerChatStartedListener(new EventSubscriber<Integer>() {
            @Override
            public void onNext(Event<Integer> chatMsgEvent) {
                onStartChat(chatMsgEvent.getData());
            }
        }, this);
        WebSocketRxBus.registerWebSocketChatMagListener(new EventSubscriber<ChatMsg>() {
            @Override
            public void onNext(final Event<ChatMsg> chatMsgEvent) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onChatMsg(chatMsgEvent.getData());
                    }
                });

            }
        }, this);

        WebSocketRxBus.registerWebSocketChatMagReceivedListener(new EventSubscriber<String>() {
            @Override
            public void onNext(Event<String> chatMsgReceivedEvent) {
                onChatMsgReceived(chatMsgReceivedEvent.getData());
            }
        }, this);

        WebSocketRxBus.registerChatMagReadListener(new EventSubscriber<MsgReadEventBody>() {
            @Override
            public void onNext(Event<MsgReadEventBody> chatMsgReadEvent) {
                onChatMsgRead(String.valueOf(chatMsgReadEvent.getData().getId()), chatMsgReadEvent.getData().getQid());
                // TODO: 2018/5/29 websocket新版本 stone
//                onChatMsgRead(String.valueOf(chatMsgReadEvent.getData().getMsg_id()), String.valueOf(chatMsgReadEvent.getData().getQid()));
            }
        }, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void connect(String webAddress, String userId) {
        if (!isConnected()) {
            YMApplication.getInstance().initWebSocket();
        }
    }

    public boolean isConnected() {
        return WebSocketApi.INSTANCE.isConnected();
    }

    @Override
    public void sendChatMsg(ChatMsg chatMsg) {
        WebSocketApi.INSTANCE.sendMsg(chatMsg);
    }

    @Override
    public void sendAckMsg(String msgId) {
        WebSocketApi.INSTANCE.sendMsgAck(msgId);

    }

    @Override
    public void sendReadMsg(String msgId, String qid) {
        WebSocketApi.INSTANCE.sendMsgReadAck(msgId, qid);

    }


    /**
     * 发送聊天消息
     *
     * @param from    发送人id
     * @param to      接收人id
     * @param msgId   消息id
     * @param qid     问题id
     * @param content 消息内容
     */
    @Override
    public void sendChatMsg(String from, String to, String msgId, String qid, String content) {
        WebSocketApi.INSTANCE.sendChatMsg(from, to, msgId, qid, content);
    }

    /**
     * 发送字符串消息
     *
     * @param msg
     */
    @Override
    public void sendMsg(String msg) {
        WebSocketApi.INSTANCE.sendMsg(msg);
    }


    @Override
    public void onStartChat(int sequence) {
    }


    @Override
    public void onConnectError(Exception e) {
    }
}
