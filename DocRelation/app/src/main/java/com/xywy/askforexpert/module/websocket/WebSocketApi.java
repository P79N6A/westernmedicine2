package com.xywy.askforexpert.module.websocket;

import android.text.TextUtils;
import android.util.Log;

import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.websocket.msg.AckMsg;
import com.xywy.askforexpert.model.websocket.msg.BaseSocketMsg;
import com.xywy.askforexpert.model.websocket.msg.ConnectAckMsg;
import com.xywy.askforexpert.model.websocket.msg.ConnectMsg;
import com.xywy.askforexpert.model.websocket.msg.PongMsg;
import com.xywy.askforexpert.model.websocket.msg.ReadMsg;
import com.xywy.askforexpert.model.websocket.msg.chatmsg.ChatMsg;
import com.xywy.askforexpert.model.websocket.msg.chatmsg.TempChatMsgForSend;
import com.xywy.askforexpert.model.websocket.rxevent.MsgReadEventBody;
import com.xywy.askforexpert.module.consult.ConsultConstants;
import com.xywy.askforexpert.module.consult.activity.ConsultChatActivity;
import com.xywy.datarequestlibrary.utils.GsonUtils;
import com.xywy.util.LogUtils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by bailiangjin on 2017/4/28.
 */

public enum WebSocketApi {
    INSTANCE;

    private WebSocketClient webSocketClient;// 连接客户端

    /**
     * webSocket url
     */
    private String webSocketUrl;
    /**
     * 当前用户id
     */
    private String curUserId;

    private boolean isConnected;

    private String reconnectAddress;


    private int MAX_RECONNECT_TIMES = 3;

    /**
     * 重连次数
     */
    private int reconnectTimes = 0;

    private int connectFlag = 0;


    public WebSocketApi startSocket(String webSocketUrl, String curUserId) {
        this.webSocketUrl = webSocketUrl;
        this.curUserId = curUserId;
        connectWebServer(webSocketUrl);
        return this;
    }


    /**
     * 链接断开
     */
    public void disconnect() {
        reconnectTimes = 0;
        setConnected(false);
        if (webSocketClient != null) {
            webSocketClient.close();
            webSocketClient = null;
        }
    }

    /**
     * 当链接断开时
     */
    private void onDisconnect() {
        if (webSocketClient != null) {
            webSocketClient.close();
            webSocketClient = null;
        }
        setConnected(false);
    }

    /**
     * 链接断开
     */
    public void onSocketError(Exception e) {
        e.printStackTrace();
        disconnect();
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    /**
     * 连接服务端
     *
     * @param webAddress
     */
    private void connectWebServer(String webAddress) {
        webSocketClient = initWebSocketClient(webAddress);
        webSocketClient.connect();
    }

    private WebSocketClient initWebSocketClient(final String webAddress) {
        WebSocketClient webSocketClient = null;
        try {
            Log.e("webSocket", "连接地址：" + webAddress);
            webSocketClient = new WebSocketClient(new URI(webAddress)) {
                @Override
                public void onOpen(final ServerHandshake serverHandshakeData) {
                    reconnectTimes = 0;
                    connectFlag = 0;
                    Log.e("webSocket", "已经连接到服务器【" + getURI() + "】");
                    //开始会话
                    sendConnectMsg(curUserId);
                }

                @Override
                public void onMessage(final String str) {
                    if (TextUtils.isEmpty(str)) {
                        LogUtils.e("消息为空");
                        return;
                    }
                    Log.e("webSocket", "收到到服务端信息【" + str + "】");

                    try {
                        BaseSocketMsg baseSocketMsg = GsonUtils.toObj(str, BaseSocketMsg.class);

                        switch (baseSocketMsg.getAct()) {
                            case CONNECT:
                                LogUtils.e("socket 连接请求");
                                break;
                            case CONNECT_ACK:
                                LogUtils.e("socket 连接成功");
                                final ConnectAckMsg connectAckMsg = getSocketMsg(str, ConnectAckMsg.class);
                                if (null != connectAckMsg) {
                                    LogUtils.e("socket 连接成功:sequenceId:" + connectAckMsg.getSequence());
                                    setConnected(true);
                                    WebSocketRxBus.notifyChatStarted(connectAckMsg.getSequence());
                                    //webSocketImInterface.onStartChat(connectAckMsg.getSequence());
                                }
                                break;
                            case CONNECT_FAIL:
                                LogUtils.e("socket 建立连接失败");
                                break;
                            case PUB:
                                LogUtils.e("收到会话消息");
                                final ChatMsg chatMsg = getSocketMsg(str, ChatMsg.class);
                                if (null != chatMsg) {
                                    String msgId = String.valueOf(chatMsg.getId());
                                    // TODO: 2018/5/29 websocket新版本 stone
//                                    String msgId = String.valueOf(chatMsg.getMsg_id());
                                    //发送消息收到回执,收到服务端的推送消息后，客户端需要向服务端发送一个收到消息的回执给服务端
                                    sendMsgAck(msgId);
                                    WebSocketRxBus.notifyChatMsg(chatMsg);
                                }
                                break;
                            case PUB_ACK:
                                //客户端发送消息后，收到服务端的应答消息，表示服务端已经收到客户端发送的消息了
                                LogUtils.e("服务端收到消息ack");
                                AckMsg ackMsg = getSocketMsg(str, AckMsg.class);
                                if (null != ackMsg) {
                                    WebSocketRxBus.notifyChatMsgReceived(ackMsg.getId());
                                    // TODO: 2018/5/29 websocket新版本 stone
//                                    WebSocketRxBus.notifyChatMsgReceived(String.valueOf(ackMsg.getMsg_id()));
                                }
                                break;
                            case PING:
                                LogUtils.e("心跳 问询");
                                //发送心跳答复
                                sendMsg(new PongMsg());
                                LogUtils.e("心跳 答复");
                                // TODO: 2018/4/9 测试stone 即时问答推问题模拟 忙线消息
//                                YMApplication.applicationHandler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        BodyBean bodyBean = new BodyBean(null, null);
//                                        bodyBean.setType(ChatMsg.RECV_MSG_TYPE_NEW_NOTICE);
//                                        ChatMsg chatMsg1 = new ChatMsg(null, null, null, bodyBean);
//                                        WebSocketRxBus.notifyChatMsg(chatMsg1);
//
//                                        BodyBean bodyBean2 = new BodyBean(null, null);
//                                        bodyBean2.setType(ChatMsg.RECV_MSG_TYPE_ASK);
//                                        ChatMsg chatMsg2 = new ChatMsg(null, null, null, bodyBean2);
//                                        WebSocketRxBus.notifyChatMsg(chatMsg2);
//                                    }
//                                }, 4000);
                                break;
                            case PONG:
                                LogUtils.e("服务端 心跳答复");
                                break;

                            case READ:
                                LogUtils.e("服务端已读消息 回执");
                                ReadMsg readMsg = getSocketMsg(str, ReadMsg.class);
                                if (null != readMsg) {
                                    // TODO: 2018/5/29 websocket新版本 stone
                                    WebSocketRxBus.notifyChatMsgRead(new MsgReadEventBody(readMsg.getId(), readMsg.getQid()));
//                                    WebSocketRxBus.notifyChatMsgRead(new MsgReadEventBody(readMsg.getMsg_id(), readMsg.getQid()));
                                }

                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.e("解析socket消息异常：" + e.getMessage());

                    }

                }

                @Override
                public void onClose(final int code, final String reason, final boolean remote) {
                    Log.e("webSocket", "断开服务器连接【" + getURI() + "，状态码： " + code + "，断开原因：" + reason + "】");
                    if(code == 1000 && !remote){
                        //本地主动断开连接，则不需要进行重连
                        return;
                    }
                    onDisconnect();
                    if (ConsultChatActivity.SOCKET_FLAG){
                        if (connectFlag <= 5) {
                            startSocket(ConsultConstants.WEBSOCKET_ADDRESS, YMUserService.getCurUserId());
                            Log.e("webSocket", "重连了" + YMUserService.getCurUserId());
                            connectFlag++;
                        }
                    }
//                    WebSocketRxBus.notifyReconnectionSocket("close");
//                    ImFileCacheUtil imFileCacheUtil = ImFileCacheUtil.getInstance();
//                    imFileCacheUtil.removeCahce(new File(imFileCacheUtil.baseDirPath));
                }

                @Override
                public void onError(final Exception e) {
                    e.printStackTrace();
                    Log.e("webSocket", "连接发生了异常【异常原因：" + e.getMessage() + "】");
                    onSocketError(e);
                    //异常断开重连 3次
                    if (reconnectTimes < MAX_RECONNECT_TIMES && !TextUtils.isEmpty(webSocketUrl)) {
                        reconnectTimes++;
                    }


                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return webSocketClient;
    }


    public void sendChatMsg(String from, String to, String msgId, String qid, String content) {
        if (TextUtils.isEmpty(from) || TextUtils.isEmpty(to) || TextUtils.isEmpty(msgId) || TextUtils.isEmpty(qid) || TextUtils.isEmpty(content)) {
            LogUtils.e("参数不完整");
            ToastUtils.shortToast("参数不完整 无法发送消息");
            return;
        }


        TempChatMsgForSend chatMsg = new TempChatMsgForSend(from, to, msgId, new TempChatMsgForSend.BodyBean(qid, content));
        // TODO: 2018/5/29 websocket新版本 stone
//        TempChatMsgForSend chatMsg = new TempChatMsgForSend(Integer.parseInt(from), Integer.parseInt(to), Integer.parseInt(msgId), new TempChatMsgForSend.BodyBean(Integer.parseInt(qid), content));
        sendMsg(chatMsg);
    }

    /**
     * 发送建立连接消息
     *
     * @param curUserId 用户id
     */

    public void sendConnectMsg(String curUserId) {
        if (TextUtils.isEmpty(curUserId)) {
            throw new RuntimeException("userId cannot be null");
        }
        sendMsg(new ConnectMsg(curUserId));
        // TODO: 2018/5/29 websocket新版本 stone
//        sendMsg(new ConnectMsg(Integer.parseInt(curUserId)));
    }

    public void sendMsgAck(String msgId) {
        //发送收到消息ack
        sendMsg(new AckMsg(msgId));
        // TODO: 2018/5/29 websocket新版本 stone
//        sendMsg(new AckMsg(Integer.parseInt(msgId)));
    }

    /**
     * 发送消息已读回执
     *
     * @param msgId 消息id
     * @param qid   问题id
     */
    public void sendMsgReadAck(String msgId, String qid) {
        //发送收到消息ack
        sendMsg(new ReadMsg(msgId, qid));
        // TODO: 2018/5/29 websocket新版本 stone
//        sendMsg(new ReadMsg(Integer.parseInt(msgId), Integer.parseInt(qid)));
    }

    /**
     * 发送 socket 消息
     *
     * @param socketMsg
     */
    public void sendMsg(BaseSocketMsg socketMsg) {
        if (null == socketMsg) {
            LogUtils.e("socketMsg can not be null ");
            return;
        }
        sendMsg(GsonUtils.toJson(socketMsg));
    }


    public void sendMsg(String msg) {
        try {
            if (webSocketClient != null) {
                webSocketClient.send(msg);
                Log.e("websocket", "发送消息:" + msg);
                //shortToast("发送:" + msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private <T extends BaseSocketMsg> T getSocketMsg(String str, Class<T> clazz) {

        T chatMsg = null;
        try {
            chatMsg = GsonUtils.toObj(str, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("解析BaseSocketMsg消息异常：" + e.getMessage());

        }
        return chatMsg;
    }

}
