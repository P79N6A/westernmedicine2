package com.xywy.im.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.xywy.im.sdk.tool.FileCache;
import com.xywy.im.sdk.tool.LogUtils;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by xugan on 2018/7/17.
 */

public class XywyIMService {
    private long currentUserId;

    public enum ConnectState {
        STATE_UNCONNECTED,
        STATE_CONNECTING,
        STATE_CONNECTED,
        STATE_CONNECTFAIL,
    }

    private ConnectState connectState = ConnectState.STATE_UNCONNECTED;

    ArrayList<IMServiceObserver> observers = new ArrayList<IMServiceObserver>();
    ArrayList<IMServiceLogOutObserver> logOutObservers = new ArrayList<IMServiceLogOutObserver>();
    ArrayList<PeerMessageObserver> peerObservers = new ArrayList<PeerMessageObserver>();
    ArrayList<GroupMessageObserver> groupObservers = new ArrayList<GroupMessageObserver>();
    private String userName = "";
    private String pwd = "";
    private int role;
    private String bsid;
    private String source;

    private byte reconnectCounts = 0;
    ExecutorService executorService ;

    private static XywyIMService im = new XywyIMService();

    public static XywyIMService getInstance() {
        return im;
    }

    public XywyIMService() {
        executorService = Executors.newSingleThreadExecutor();
    }


    private boolean isOnNet(Context context) {
        if (null == context) {
            Log.e("", "context is null");
            return false;
        }
        boolean isOnNet = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (null != activeNetInfo) {
            isOnNet = activeNetInfo.isConnected();
//            LogUtils.i("active net info:" + activeNetInfo);
        }
        return isOnNet;
    }

    class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive (Context context, Intent intent) {
            if (isOnNet(context)) {
                LogUtils.i("connectivity status:on  "+  XywyIMService.this.connectState+"  reCounts="+reconnectCounts);
                //如果用户是在无网的状态进入聊天页面，并且用户又切换到了有网的状态，则需要主动去连接服务端
                reconnectCounts = 0;
                if(0 == reconnectCounts && XywyIMService.this.connectState== ConnectState.STATE_UNCONNECTED){
                    LogUtils.i("connectivity bsid:     "+  bsid+"    source  "+source+"    currentUserId   "+currentUserId+"     pwd="+pwd);
                    if(0 !=currentUserId && !TextUtils.isEmpty(pwd)){
                        WebSocketClient webSocketClient = WebSocketApi.getInStance().getWebSocketClient();
                        if(null != webSocketClient){
                            webSocketClient.reconnect();
                        }
                    }
                }
            } else {
                LogUtils.i("connectivity status:off  "+  XywyIMService.this.connectState+"  recCounts="+reconnectCounts);
            }
        }
    };

    public void registerConnectivityChangeReceiver(Context context) {
        NetworkReceiver  receiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(receiver, filter);
    }

    public ConnectState getConnectState() {
        return connectState;
    }

    public void addObserver(IMServiceObserver ob) {
        if (observers.contains(ob)) {
            return;
        }
        observers.add(ob);
    }

    public void addLogOutObserver(IMServiceLogOutObserver ob){
        if(logOutObservers.contains(ob)){
            return;
        }
        logOutObservers.add(ob);
    }

    public void removeLogOutObserver(IMServiceLogOutObserver ob){
        logOutObservers.remove(ob);
    }

    public void removeObserver(IMServiceObserver ob) {
        observers.remove(ob);
    }


    public void addPeerObserver(PeerMessageObserver ob) {
        if (peerObservers.contains(ob)) {
            return;
        }
        peerObservers.add(ob);
    }

    public void removePeerObserver(PeerMessageObserver ob) {
        peerObservers.remove(ob);
    }

    public void sendPeerMessage(Message msg) {
        //        loadUserName(imsg);//暂时先省略
        if(msg.getMsgType() == Message.MSGTYPE_IMG){
            String content = msg.getContent();
            try {
                JSONObject jsonObject = new JSONObject(content);
                String filePath = jsonObject.getString("filePath");
                //发送消息之前去掉消息体中的本地图片的路径
                jsonObject.remove("filePath");
                msg.setContent(jsonObject.toString());
                realSendMessage(msg);
                jsonObject.put("filePath",filePath);
                msg.setContent(jsonObject.toString());
                //消息插入数据库后，通知UI页面，将消息插入的到对话列表中
                publicPeerMessageSending(msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            //消息插入数据库后，通知UI页面，将消息插入的到对话列表中
            publicPeerMessageSending(msg);
            realSendMessage(msg);
        }

    }

    /**
     * 发送透传消息
     * @param msg
     */
    public void sendRoomMessageByPassthrough(Message msg) {
        //        loadUserName(imsg);//暂时先省略
        if(msg.getMsgType() == Message.MSGTYPE_IMG){
            //聊天室消息，暂时不支持发送图片
        }else {
            sendMessage(msg);
        }
    }

    public void reSendRoomMessage(Message msg){
        msg.setCmd(5);//由于cmd这个字段未存入数据库，所以，这里需要将cmd重新赋值，否则cmd就是默认值0
        sendMessage(msg);
    }

    public void reSendPeerMessage(Message msg){
        msg.setCmd(3);//由于cmd这个字段未存入数据库，所以，这里需要将cmd重新赋值，否则cmd就是默认值0
        realSendMessage(msg);
    }

    private void realSendMessage(Message msg){
        if(XywyIMService.this.connectState != ConnectState.STATE_CONNECTED){
            //发送消息时，如果socket是处于未连接上的状态，则通知消息发送状态改变,并通知连接状态的改变
            LogUtils.i("连接失败，无法发送    "+XywyIMService.this.connectState);
            publishConnectState();
            if(MessageSendState.MESSAGE_SEND_LISTENED == msg.getSendState()){
                publishPeerMessageSendFailureNew(msg.getMsgId());
            }
        }else { //当前状态是连接上的,才发送消息
            if (!sendMessage(msg)) {
                //消息发送失败，改变消息发送状态
                if(MessageSendState.MESSAGE_SEND_LISTENED == msg.getSendState()){
                    publishPeerMessageSendFailureNew(msg.getMsgId());
                }
            }
        }
    }



    public void closeNew(){
        WebSocketApi.getInStance().close();
    }

    public void connect(final String url,final String bsid, final String source, final long userId, final String pwd, final int role) {
        this.userName = bsid+"|##|"+source+"|##|"+userId;
        this.bsid = bsid;
        this.source = source;
        this.pwd = pwd;
        this.role = role;
        this.currentUserId = userId;
        LogUtils .i("connect()"+"    userName=    "+userName+"     pwd=   "+pwd+"     currentUserId="+currentUserId);
//        LogUtils.i("connect()"+"    connectState="+"     publishConnectState    reconnectCounts="+reconnectCounts+"     currentUserId="+currentUserId);
        if(XywyIMService.this.connectState == ConnectState.STATE_CONNECTED){
            return;
        }
//        LogUtils.i("connect()     publishConnectState   reconnectCounts="+reconnectCounts);
        XywyIMService.this.publishConnectState();

        WebSocketApi.getInStance().setWebSocketStatusCallBack(new WebSocketStatusCallBack() {
            @Override
            public void onOpen() {
                LogUtils.i("connect()     publishConnectState="+XywyIMService.this.connectState+"   reconnectCounts="+reconnectCounts);
            //开始会话
                if(XywyIMService.this.connectState == ConnectState.STATE_UNCONNECTED){
                    try {
                        byte[] startBytes = CommonUtils.int2Bytes(Constant.CONNECT,1);
                        byte[] userNameLengthBytes = CommonUtils.intToByteArray(userName.length());
                        byte[] userNameBytes = userName.getBytes("utf-8");
                        byte[] pwdLengthBytes = CommonUtils.intToByteArray(pwd.length());
                        byte[] pwdBytes = pwd.getBytes("utf-8");
                        byte[] roleBytes = CommonUtils.int2Bytes(role,1);
                        byte[] clientIdBytes = CommonUtils.int2Bytes(Constant.CLIENTID,1);
                        byte[] connectBytes = CommonUtils.byteMergerAll(startBytes,userNameLengthBytes,
                                userNameBytes,pwdLengthBytes,pwdBytes,roleBytes,clientIdBytes);
                        XywyIMService.this.connectState = ConnectState.STATE_CONNECTING;
                        XywyIMService.this.publishConnectState();
                        WebSocketApi.getInStance().sendMsg(connectBytes);
                    } catch (UnsupportedEncodingException e) {
                        LogUtils.i(""+e.getMessage());
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.i(""+e.getMessage());
                    }
                }

            }

            @Override
            public void onMessage(ByteBuffer buf) {
                XywyIMService.this.connectState = ConnectState.STATE_CONNECTED;
                reconnectCounts = 0;//重连，连接上以后，reconnectCounts重置
                boolean b = XywyIMService.this.handleData(buf.array());
                if (!b) {
                    //接收的数据解析失败，需要将这些数据存入一个文件，方便查找问题原因
                    try {
                        FileCache.getInstance().storeByteArray(buf.array());
                    } catch (IOException e) {
                        LogUtils.e(""+e.getMessage());
                        e.printStackTrace();
                    }
                    XywyIMService.this.connectState = ConnectState.STATE_UNCONNECTED;
                    LogUtils.i("onMessage()     publishConnectState   ");
                    XywyIMService.this.closeNew();
                }
                XywyIMService.this.publishConnectState();
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                LogUtils.i(XywyIMService.this.connectState + "  remote=" + remote + "  code=" + code + "  isShutdown=" + executorService.isShutdown() + "  reCounts=" + reconnectCounts);
                XywyIMService.this.connectState = ConnectState.STATE_UNCONNECTED;
                publishConnectState();
                if (code == WebSocketsCode.CLOSE_NORMAL && !remote) {
                    //用户主动关闭连接，则不需要进行重连
                    return;
                }
                //断开连接，需要进行重连
                if (reconnectCounts == 5000) {
                    reconnectCounts = 0;
                    executorService.shutdown();
                    return;
                }
                if (!executorService.isShutdown()) {
//                    LogUtils.e("当前线程id       "+Thread.currentThread().getId()+"      "+XywyIMService.this.connectState);
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(reconnectCounts>=5){
                                    Thread.sleep(5 * 1000);
                                }else {
                                    Thread.sleep(reconnectCounts * 1000);
                                }
                                LogUtils.i("reconnectCounts  " + reconnectCounts + "  时间间隔" + ((reconnectCounts>=5)?5:reconnectCounts) + "秒后开始重连  " + XywyIMService.this.connectState);
                                if(XywyIMService.getInstance().connectState == ConnectState.STATE_UNCONNECTED){
                                    WebSocketClient webSocketClient = WebSocketApi.getInStance().getWebSocketClient();
                                    if(null != webSocketClient){
                                        webSocketClient.reconnect();
                                        reconnectCounts++;
                                    }
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } else {
                    //如果连接上服务端后，断开连接，并且经过3次重连后，依然未连接上，此时executorService会关闭,这时，
                    // 如果监听到网络状态改变，连接上了服务端，但是，再次关闭网络，此时会触发重连服务端的操作，但是由于之前
                    //executorService已经关闭，此时，executorService是不会进行重连的操作进行提交，那么就无法进行重连操作了
                    if (0 == reconnectCounts) {
                        executorService = Executors.newSingleThreadExecutor();

                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if(reconnectCounts>=5){
                                        Thread.sleep(5 * 1000);
                                    }else {
                                        Thread.sleep(reconnectCounts * 1000);
                                    }
                                    LogUtils.i("reconnectCounts  " + reconnectCounts + "  时间间隔" + ((reconnectCounts>=5)?5:reconnectCounts)  + "秒后开始重连  " + XywyIMService.this.connectState);
                                    if(XywyIMService.getInstance().connectState == ConnectState.STATE_UNCONNECTED){
                                        WebSocketClient webSocketClient = WebSocketApi.getInStance().getWebSocketClient();
                                        if(null != webSocketClient){
                                            webSocketClient.reconnect();
                                            reconnectCounts++;
                                        }
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                XywyIMService.this.connectState = ConnectState.STATE_CONNECTFAIL;
//                LogUtils.e("onError()     publishConnectState   "+""+e.getMessage());
                LogUtils.e(e.getMessage()+"      "+ Thread.currentThread().getId());
                publishConnectState();
            }
        });

        WebSocketApi.getInStance().start(url,userName,pwd);
    }

    private void handleMessage(Message msg) {
        LogUtils.i("message cmd:" + msg.getCmd()+"            threadId=  "+ Thread.currentThread().getId());
        if (msg.getCmd() == Constant.CONNECT_ACK) {  //连接上服务端后，服务端返回的数据中所带的cmd
            //可以不做任何处理
        } else if (msg.getCmd() == Constant.PUB_ACK) {   //消息发送后，服务端返回的数据中所带的cmd
//            LogUtils.i("handleAck(msg)    msg="+msg+"     msgId   "+msg.getMsgId());
            handleAck(msg);
        }  else if (msg.getCmd() == Constant.GROUP_PUB_ACK) {   //群消息发送后，服务端返回的数据中所带的cmd

            publishGroupMessageACK(msg.getMsgId(),msg.getGroupId());
        } else if (msg.getCmd() == Constant.PING) {
            handleHeartbeatAck();
        } else if (msg.getCmd() == Constant.PUBLISH) {      //服务端推送给客户端的消息中所带的cmd
            //过滤掉，即时问答推送过来的消息
            String content = msg.getContent();
            JSONObject body = null;
            try {
                body = new JSONObject(content);
                if(body.has("bsid")){
                    String bsid = body.getString("bsid");
                    if(!Constant.BSID_RTQA.equals(bsid)){
                        LogUtils.e("拦截了即时问答的消息   "+msg.getMsgId());
                        msg.setMsgId(msg.getMsgId().split("_")[0]);
                        handlePublishAck(msg);//发送应答消息给服务器
                        return ;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            publishPeerMessageNew(msg);//通知聊天页面更新消息
            handlePublishAck(msg);//发送应答消息给服务器

        }else if(msg.getCmd() == Constant.GROUP_PUBLISH){
            //服务端推送过来的群消息
            publishGroupMessage(msg);//通知群聊天页面更新消息
            handleGroupPublishAck(msg);//发送应答消息给服务器
        }else if(msg.getCmd() == Constant.DISCONNECT){

        }else if(msg.getCmd() == Constant.LOGOUT){
            //服务端通知客户端被挤掉线
            closeNew();
            publishLogOut();
        }else if(msg.getCmd() == Constant.INVALID_PACKET){
            LogUtils.i( "unknown message cmd:"+msg.getCmd());
        }else {
            LogUtils.i( "unknown message cmd:"+msg.getCmd());
        }
    }

    private void handleAck(Message msg) {
        LogUtils.i("handleAck()       msg="+msg+"   msgId=  "+msg.getMsgId()+"      qid="+msg.getQid());
        //模拟网络延时的消息发送状态改变的效果
//        try {
//            Thread.currentThread().sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        publishPeerMessageACKNew(msg.getMsgId());
    }


    private void handleHeartbeatAck() {
        Message ack = new Message();
        ack.setCmd(Constant.PING_RESP);
        sendMessage(ack);
    }

    private void handlePublishAck(Message msg) {
        Message ack = new Message();
        ack.setCmd(Constant.PUB_ACK);
        ack.setMsgId(msg.getMsgId());
        sendMessage(ack);
    }

    private void handleGroupPublishAck(Message msg) {
        Message ack = new Message();
        ack.setCmd(Constant.GROUP_PUB_ACK);
        ack.setMsgId(msg.getMsgId());
        sendMessage(ack);
    }

    private boolean handleData(byte[] data) {
        Message msg = new Message();
        if (!msg.unpack(data)) {
            LogUtils.i("unpack message error");
            return false;
        }
        handleMessage(msg);
        return true;
    }

    private boolean sendMessage(Message msg) {
//        // TODO: 2018/8/3  测试发送失败后的从发功能
//        if(msg.getContent() != null && msg.getContent().contains("5") && new Random().nextBoolean() ){
//            LogUtils.i("sendMessage()    return fasle    msg.getContent().contains(5) = "+msg.getContent().contains("5"));
//            return false;
//        }

        byte[] buf = msg.pack();
        try {
            WebSocketApi.getInStance().sendMsg(buf);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void publishPeerMessageNew(Message msg) {
        for (int i = 0; i < peerObservers.size(); i++ ) {
            PeerMessageObserver ob = peerObservers.get(i);
            ob.onPeerMessage(msg);
        }
    }

    private void publishPeerMessageACKNew(String msgLocalID) {
        //消息发送成功后，将从连的次数重新置为0
        reconnectCounts = 0;
        for (int i = 0; i < peerObservers.size(); i++ ) {
            PeerMessageObserver ob = peerObservers.get(i);
            ob.onPeerMessageACK(msgLocalID);
        }
    }

    private void publicPeerMessageSending(Message message){
        for (int i = 0; i < peerObservers.size(); i++) {
            PeerMessageObserver ob = peerObservers.get(i);
            ob.onPeerMessageSending(message);
        }
    }

    private void publishPeerMessageSendFailureNew(String msgLocalID) {
        for (int i = 0; i < peerObservers.size(); i++ ) {
            PeerMessageObserver ob = peerObservers.get(i);
            ob.onPeerMessageSendFailure(msgLocalID);
        }
    }

    private void publishConnectState() {
        for (int i = 0; i < observers.size(); i++ ) {
            IMServiceObserver ob = observers.get(i);
            ob.onConnectState(connectState);
        }
    }

    private void publishLogOut(){
        for (int i = 0; i < logOutObservers.size(); i++) {
            IMServiceLogOutObserver ob = logOutObservers.get(i);
            ob.onLogOut();
        }
    }


    private void publishGroupMessage(Message msg) {
        for (int i = 0; i < groupObservers.size(); i++ ) {
            GroupMessageObserver ob = groupObservers.get(i);
            ob.onGroupMessage(msg);
        }
    }

    private void publishGroupMessageACK(String msgLocalID, long gid) {
        for (int i = 0; i < groupObservers.size(); i++ ) {
            GroupMessageObserver ob = groupObservers.get(i);
            ob.onGroupMessageACK(msgLocalID, gid);
        }
    }


    private void publishGroupMessageFailure(String msgLocalID, long gid) {
        for (int i = 0; i < groupObservers.size(); i++ ) {
            GroupMessageObserver ob = groupObservers.get(i);
            ob.onGroupMessageFailure(msgLocalID, gid);
        }
    }

    public void addGroupObserver(GroupMessageObserver ob) {
        if (groupObservers.contains(ob)) {
            return;
        }
        groupObservers.add(ob);
    }

    public void removeGroupObserver(GroupMessageObserver ob) {
        groupObservers.remove(ob);
    }

}
