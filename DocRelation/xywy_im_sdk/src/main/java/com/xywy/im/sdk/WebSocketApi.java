package com.xywy.im.sdk;

import android.text.TextUtils;

import com.xywy.im.sdk.tool.LogUtils;

import org.java_websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by xugan on 2018/7/16.
 */

public class WebSocketApi {
    private String userName;//用户名
    private String pwd; //密码
    private WebSocketClient webSocketClient;
    private String webSocketUrl;
    private WebSocketStatusCallBack webSocketStatusCallBack;

    private WebSocketApi(){}

    private static WebSocketApi instance = new WebSocketApi();

    public static WebSocketApi getInStance(){
        return instance;
    }

    public void start(String webSocketUrl, String userName, String pwd) {
        if(TextUtils.isEmpty(webSocketUrl)){
            throw new RuntimeException("webSocketUrl can not be null");
        }

        if(TextUtils.isEmpty(userName)){
            throw new RuntimeException("userName can not be null");
        }

        if(TextUtils.isEmpty(pwd)){
            throw new RuntimeException("userName can not be null");
        }

        this.webSocketUrl = webSocketUrl;
        this.userName = userName;
        this.pwd = pwd;
        connectWebServer(webSocketUrl);
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
        XywyClient webSocketClient = null;
        try {
            webSocketClient = new XywyClient(new URI(webAddress));
            webSocketClient.setWebSocketStatusCallBack(webSocketStatusCallBack);
            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("TLS");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            try {
                sslContext.init(null, new TrustManager[]{
                        new X509TrustManager() {

                            @Override
                            public void checkClientTrusted(X509Certificate[] chain, String authType) {

                            }


                            @Override
                            public void checkServerTrusted(X509Certificate[] chain, String authType) {

                            }

                            @Override
                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[0];
                            }
                        }
                }, new SecureRandom());
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            SSLSocketFactory factory = sslContext.getSocketFactory();

            try {
                webSocketClient.setSocket(factory.createSocket());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return webSocketClient;
    }

    public WebSocketClient getWebSocketClient(){
        return webSocketClient;
    }

    /**
     * 发送 socket 消息
     *
     * @param socketMsg
     */
    public void sendMsg(byte[] socketMsg) throws Exception {
        if (null == socketMsg) {
            LogUtils.e("socketMsg can not be null ");
            return;
        }
        if (socketMsg.length == 0) {
            throw new RuntimeException("socketMsg lenght cannot be zero");
        }

//        // TODO: 2018/8/3 模拟消息发送失败后，抛出的异常
//            if(new Random().nextBoolean()){
//                LogUtils.i("Thread.currentThread().getName()     "+ Thread.currentThread().getName());
//                throw new NullPointerException("测试发送数据异常时");
//            }
        if (webSocketClient != null) {
//                unpackSendMessage(socketMsg);
//                LogUtil.e("发送消息:" + Arrays.toString(socketMsg)+"---当前线程 "+Thread.currentThread().getName());
            webSocketClient.send(socketMsg);
        }
    }

    public void setWebSocketStatusCallBack(WebSocketStatusCallBack webSocketStatusCallBack){
        this.webSocketStatusCallBack  = webSocketStatusCallBack;
    }

    /**
     * 当链接断开时
     */
    public void close() {
        if (webSocketClient != null) {
            webSocketClient.close();
            webSocketClient = null;
        }
    }

    public String UserName() {
        return userName;
    }

    public String Pwd() {
        return pwd;
    }
}
