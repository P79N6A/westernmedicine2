package com.xywy.im.sdk;


import com.xywy.im.sdk.tool.LogUtils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xugan on 2018/10/17.
 */

public class XywyClient extends WebSocketClient {
    private WebSocketStatusCallBack webSocketStatusCallBack;
    public XywyClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        //开始会话
        LogUtils.e("onOpen：" + "---当前线程 "+ Thread.currentThread().getName());
        if(null != webSocketStatusCallBack){
            webSocketStatusCallBack.onOpen();
        }
    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onMessage(ByteBuffer buf) {
        unpackRecevieMessage(buf.array());
//                    LogUtils.e("接收的数据：" + Arrays.toString(buf.array())+"---当前线程 "+Thread.currentThread().getName());
        if(null != webSocketStatusCallBack){
            webSocketStatusCallBack.onMessage(buf);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LogUtils.e("断开服务器连接【" + getURI() + "，状态码： " + code + "，断开原因：" + reason + "】"+"       remote="+remote);
//                    showLogDetail(code,reason,remote);
//                    onDisconnect();
        if(null != webSocketStatusCallBack){
            webSocketStatusCallBack.onClose(code,reason,remote);
        }
    }

    @Override
    public void onError(Exception e) {
        printErrorInfo(e);
        LogUtils.e("连接发生了异常【异常原因：" + e.getMessage() + "】");
//                    onSocketError(e);
//                    //异常断开重连 3次
//                    if (reconnectTimes < MAX_RECONNECT_TIMES && !TextUtils.isEmpty(webSocketUrl)) {
//                        reconnectTimes++;
//                        initWebSocketClient_binary(webSocketUrl);
//                    }

        if(null != webSocketStatusCallBack){
            webSocketStatusCallBack.onError(e);
        }
    }

    public void setWebSocketStatusCallBack(WebSocketStatusCallBack webSocketStatusCallBack){
        this.webSocketStatusCallBack  = webSocketStatusCallBack;

    }

    private void printErrorInfo(Exception ex){
        Map<String, String> infos = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        LogUtils.i(result);
    }

    /**
     * 收到服务端的应答的几种cmd
     * CONNECT_ACK,     2	连接上服务端后，服务端返回的数据中所带的cmd
     * PUB_ACK		    4	消息发送后，服务端返回的数据中所带的cmd
     * GROUP_PUB_ACK	6	群消息发送后，服务端返回的数据中所带的cmd
     * PING		        7	服务端发送的心跳包中所带的数据的cmd
     * PUBLISH		    3	服务端推送消息时，所带的cmd
     * @param data
     */
    public void unpackRecevieMessage(byte[] data){
        int cmd = data[0];
        byte[] msgIdByte = new byte[32];
        if(cmd==Constant.CONNECT_ACK){
            LogUtils.i("收到连接上服务器后，服务器返回的应答消息 "+cmd);
        }else if(cmd==Constant.PUB_ACK){
            System.arraycopy(data,1,msgIdByte,0,32);
            String msg_id = null;
            try {
                msg_id = new String(msgIdByte,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            LogUtils.i("向服务器发送消息后，服务器返回的应答消息 "+msg_id);
        }else if(cmd==Constant.GROUP_PUB_ACK){
            LogUtils.i("群消息发送后，服务端返回的数据中所带的消息 "+cmd);
        }else if(cmd==Constant.PING){
            LogUtils.i("收到连接上服务器后，服务器返回的心跳应答消息 "+cmd);
        }else if(cmd==Constant.PUBLISH){
            System.arraycopy(data,1,msgIdByte,0,32);
            String msgId = null;
            try {
                msgId = new String(msgIdByte,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] payLoadLenByte = new byte[2];
            System.arraycopy(data,33,payLoadLenByte,0,2);
            int payLoadLen = BytePacket.readInt16(payLoadLenByte,0);
            byte[] payLoad = new byte[payLoadLen];
            System.arraycopy(data,35,payLoad,0,payLoadLen);
            try {
                String content = new String(payLoad,"utf-8");
                LogUtils.i("服务器推送的消息id   "+msgId+"   "+content);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void showLogDetail(int code, String reason, boolean remote) {
        switch (code){
            case WebSocketsCode.CLOSE_NORMAL:
                LogUtils.i("用于期望收到状态码时连接非正常关闭 (也就是说, 没有发送关闭帧)");
                break;
            case WebSocketsCode.CLOSE_GOING_AWAY:
                LogUtils.i("终端离开, 可能因为服务端错误, 也可能因为浏览器正从打开连接的页面跳转离开");
                break;
            case WebSocketsCode.CLOSE_PROTOCOL_ERROR:
                LogUtils.i("由于协议错误而中断连接");
                break;
            case WebSocketsCode.CLOSE_UNSUPPORTED:
                LogUtils.i("由于接收到不允许的数据类型而断开连接 (如仅接收文本数据的终端接收到了二进制数据)");
                break;
            case WebSocketsCode.CLOSE_NO_STATUS:
                LogUtils.i("表示没有收到预期的状态码");
                break;
            case WebSocketsCode.CLOSE_ABNORMAL:
                LogUtils.i("用于期望收到状态码时连接非正常关闭 (也就是说, 没有发送关闭帧)");
                break;
            case WebSocketsCode.UNSUPPORTED_DATA:
                LogUtils.i("由于收到了格式不符的数据而断开连接 (如文本消息中包含了非 UTF-8 数据)");
                break;
            case WebSocketsCode.POLICY_VIOLATION:
                LogUtils.i("由于收到不符合约定的数据而断开连接。 这是一个通用状态码, 用于不适合使用 1003 和 1009 状态码的场景");
                break;
            case WebSocketsCode.CLOSE_TOO_LARGE:
                LogUtils.i("由于收到过大的数据帧而断开连接");
                break;
            case WebSocketsCode.MISSING_EXTENSION:
                LogUtils.i("客户端期望服务器商定一个或多个拓展, 但服务器没有处理, 因此客户端断开连接");
                break;
            case WebSocketsCode.INTERNAL_ERROR:
                LogUtils.i("客户端由于遇到没有预料的情况阻止其完成请求, 因此服务端断开连接");
                break;
            case WebSocketsCode.SERVICE_RESTART:
                LogUtils.i("服务器由于重启而断开连接");
                break;
            case WebSocketsCode.TRY_AGAIN_LATER:
                LogUtils.i("服务器由于临时原因断开连接, 如服务器过载因此断开一部分客户端连接");
                break;
            case WebSocketsCode.TLS_HANDSHAKE:
                LogUtils.i("表示连接由于无法完成 TLS 握手而关闭 (例如无法验证服务器证书)");
                break;
            default:
                LogUtils.i(" "+reason+"              remote  "+remote);
                break;

        }
    }
}
