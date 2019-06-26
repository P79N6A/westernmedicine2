package com.xywy.askforexpert.module.websocket;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.websocket.msg.chatmsg.ChatMsg;
import com.xywy.askforexpert.module.consult.ChatBaseActivity;
import com.xywy.uilibrary.dialog.bottompopupdialog.listener.BtnClickListener;
import com.xywy.uilibrary.dialog.middlelistpopupwindow.MiddleListPopupWindow;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by bailiangjin on 2017/4/27.
 */
@Deprecated
public class OnlineChatActivity
//        extends ChatBaseActivity
    {

//    @Bind(R.id.et_msg)
//    EditText et_msg;
//
//    String curUserId = "123";
//    String toUserId = "222";
//
//    int seq = 0;
//
//    public static void start(Activity activity) {
//        activity.startActivity(new Intent(activity, OnlineChatActivity.class));
//    }
//
//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_online_chat;
//    }
//
//    @Override
//    protected void initView() {
//        titleBarBuilder.setTitleText("WebSocket测试");
//
//    }
//
//    @Override
//    protected void initData() {
////        WebSocketImpl.DEBUG = true;
////        System.setProperty("java.net.preferIPv6Addresses", "false");
////        System.setProperty("java.net.preferIPv4Stack", "true");
//
//
//
//    }
//
//
//    @OnClick({R.id.btn_connect, R.id.btn_cut_off, R.id.btn_send, R.id.btn_begin_chat, R.id.btn_send_test})
//    public void onClick(View v) {
//        switch (v.getId()) {
//
//            case R.id.btn_connect:
//                String webAddress = "ws://172.16.201.25/websocket";
//                connect(webAddress, "123");
//                break;
//            case R.id.btn_begin_chat:
//                new MiddleListPopupWindow.Builder()
//                        .addItem("测试1", new BtnClickListener() {
//                            @Override
//                            public void onItemClick() {
//                                shortToast("点击了测试");
//                            }
//                        })
//                        .addItem("测试2", new BtnClickListener() {
//                            @Override
//                            public void onItemClick() {
//                                shortToast("点击了测试2");
//                            }
//                        })
//                        .addItem("测试3", new BtnClickListener() {
//                            @Override
//                            public void onItemClick() {
//                                shortToast("点击了测试3");
//                            }
//                        }).build(this).show();
//
////                String downloadFileUrl="http://bailiangjin.github.io/dev/download/release/SignApkTool_v0.93.jar";
////                XywyDataRequestApi.getInstance().downloadFile(downloadFileUrl, FilePathUtil.getSdcardPath(), new OkDownloadListener() {
////                    @Override
////                    public void onProgress(long totalBytes, long currentBytes) {
////                        if(0!=totalBytes){
////                            shortToast("下载进度："+currentBytes*100/totalBytes);
////                        }
////                    }
////
////                    @Override
////                    public void onSuccess(String absoluteFilePath) {
////
////                        shortToast("下载成功："+absoluteFilePath);
////                    }
////
////                    @Override
////                    public void onFailure(Exception e) {
////                        shortToast("下载异常："+e.getMessage());
////                    }
////                });
//                break;
//            case R.id.btn_cut_off:
//                //disConnect();
//                break;
//            case R.id.btn_send_test:
//                sendChatMsg(curUserId, toUserId, "" + (++seq), "1", "测试消息");
//                break;
//            case R.id.btn_send:
//                String msg = et_msg.getText().toString().trim();
//                sendMsg(msg);
//                break;
//            default:
//                break;
//        }
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // TODO: 2017/4/28 刷新页面数据
//    }
//
//
//    @Override
//    public void onStartChat(int sequence) {
//        //连接建立成功
//        shortToast("连接建立成功：sequence：" + sequence);
//
//    }
//
//    @Override
//    public void onConnectError(Exception e) {
//        // TODO: 2017/4/28 连接异常
//        shortToast("连接异常");
//
//    }
//
//    @Override
//    public void onChatMsg(final ChatMsg chatMsg) {
//        seq++;
//        // TODO: 2017/4/28 来消息啦
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                shortToast(com.xywy.askforexpert.appcommon.utils.GsonUtils.toJson(chatMsg));
//            }
//        });
//    }
//
//    @Override
//    public void onChatMsgRead(String msgId, String qid) {
//        // TODO: 2017/4/28 我发送给服务端的消息已读回执 处理 不在重发
//        shortToast("我的消息服务端已读：msgId=" + msgId + ",qid=" + qid);
//    }
//
//    @Override
//    public void onChatMsgReceived(String msgId) {
//
//    }
}
