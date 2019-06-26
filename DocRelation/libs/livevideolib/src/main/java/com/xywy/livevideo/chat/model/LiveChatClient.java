package com.xywy.livevideo.chat.model;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.xywy.livevideo.LiveManager;
import com.xywy.livevideo.chat.interfaces.IChatCallBack;
import com.xywy.livevideo.view.DialogUtil;
import com.xywy.util.ContextUtil;
import com.xywy.util.L;

import java.util.List;
import java.util.Set;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/2/22 9:33
 * 直播聊天功能入口
 */

public class LiveChatClient {
    protected static final int pagesize = 20;
    private String TAG = "LiveChatClient";
    private static LiveChatClient instance=new LiveChatClient();
    private EMConnectionListener connectionListener;

    private LiveChatClient(){

    }
    public static LiveChatClient getInstance(){
        return instance;
    }
    public void init(Application context) {
        EMOptions options = initChatOptions();
        EMClient.getInstance().init(context, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        addConnectionListener(null);
    }

    private EMOptions initChatOptions() {
        Log.d(TAG, "init HuanXin Options");

        EMOptions options = new EMOptions();
        // set if accept the invitation automatically
        options.setAcceptInvitationAlways(false);
        // set if you need read ack
        options.setRequireAck(true);
        // set if you need delivery ack
        options.setRequireDeliveryAck(false);

        //you need apply & set your own id if you want to use google cloud messaging.
        options.setGCMNumber("324169311137");
        //you need apply & set your own id if you want to use Mi push notification
        options.setMipushConfig("2882303761517426801", "5381742660801");
        //you need apply & set your own id if you want to use Huawei push notification
        options.setHuaweiPushAppId("10492024");

        options.allowChatroomOwnerLeave(false);
        options.setDeleteMessagesAsExitGroup(false);
        options.setAutoAcceptGroupInvitation(false);
        return options;
    }
//    private String userName;
//    private String password;
    public boolean isLogin(){
        return EMClient.getInstance().getCurrentUser()!=null;
    }

    public void register(final String username, final String pwd) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // call method in SDK
                    EMClient.getInstance().createAccount(username, pwd);
                            // save current user
                           login(username,pwd);
//                            setPassword(password);
                            L.e(TAG, "注册环信账号成功");
                } catch (final HyphenateException e) {
                            L.e(TAG, e.getMessage());
                }
            }
        }).start();
    }
    public void login(final String username, final String password) {
        EMClient.getInstance().login(username, password, new EMCallBack() {
            @Override
            public void onSuccess() {
//                setUserName(username);
//                setPassword(password);
                addGlobalMsgListener();
                L.e(TAG, "login success : ");
            }

            @Override
            public void onError(int code, String message) {
                L.e(TAG, "login failure : " + message);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    /**
     * 监听透传消息，解析聊天室透传
     */
    public void addGlobalMsgListener() {
        EMMessageListener msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
                Set<String> chatRoomIds = LiveManager.getInstance().getConfig().chatRoomIds;
                chatRoomIds.addAll(ChatMessageHelper.resolveLiveChatCmdMsg(messages));
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
                //收到已读回执
//            refreshMsgs();
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
                //收到已送达回执
//            refreshMsgs();
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
//            refreshMsgs();
            }
        };
        registerMessageListener(msgListener);
    }
    /**
     * 环信连接发生变化时如何处理
     * @param callBack
     */
    public void addConnectionListener(final IChatCallBack callBack) {
        if (connectionListener!=null){
            removeConnectionListener(connectionListener);
        }
        connectionListener = new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(final int error) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (error == EMError.USER_REMOVED) {
                            Log.e(TAG, "已经被移除");
                            // 显示帐号已经被移除
                            onHxException("当前登录用户环信账号已经被移除，请重新登录");
                        } else if (error == EMError.USER_ALREADY_LOGIN) {
                            Log.e(TAG, "已登录");
                        } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                            Log.e(TAG, "被踢出");
                            // 显示帐号在其他设备登陆dialog
                            onHxException("当前登录用户环信账号已经被移除，请重新登录");
                        }
                    }
                });
            }
        };
        EMClient.getInstance().addConnectionListener(connectionListener);
    }

    private void onHxException(String msg) {
        if (ContextUtil.getTopActivity()!=null){
            DialogUtil.showConflictDialog(ContextUtil.getTopActivity(),msg);
        }
    }

    public void removeConnectionListener(EMConnectionListener myConnectionListener) {
        EMClient.getInstance().removeConnectionListener(myConnectionListener);
    }
    public void logOut() {
//        removeConnectionListener(connectionListener);
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
//                setUserName(null);
//                setPassword(null);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.e(TAG, "login failure : " + message);
            }
        });
    }

    public void joinChatRoom(final String roomId, final EMValueCallBack<EMChatRoom> callback) {
        EMClient.getInstance().chatroomManager().joinChatRoom(roomId, callback);
    }
    public void leaveChatRoom(final String roomId) {
        EMClient.getInstance().chatroomManager().leaveChatRoom(roomId);
    }

    protected void addChatRoomChangeListenr(EMChatRoomChangeListener chatRoomChangeListener) {
        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomChangeListener);
    }
//    int chatType=EMMessage.ChatType.ChatRoom;

    protected void registerMessageListener(EMMessageListener msgListener) {
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    protected void removeMessageListener(EMMessageListener msgListener) {
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }
    private EMCallBack emCallBack;
    public   void registerMessageStatusCallback(final EMCallBack callBack) {
        this.emCallBack=callBack;
    }
    /**
     * 发送聊天消息
     */
    protected  void sendMessage(final EMMessage message) {
        message.setMessageStatusCallback(emCallBack);
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    protected EMConversation getConversation(String roomId) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(roomId, EMConversation.EMConversationType.ChatRoom, true);
//        conversation.markAllMessagesAsRead();
//        // the number of messages loaded into conversation is getChatOptions().getNumberOfMessagesLoaded
//        // you can change this number
//        final List<EMMessage> msgs = conversation.getAllMessages();
//        int msgCount = msgs != null ? msgs.size() : 0;
//        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
//            String msgId = null;
//            if (msgs != null && msgs.size() > 0) {
//                msgId = msgs.get(0).getMsgId();
//            }
//            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
//        }
        return conversation;
    }


    public String getHxUserName() {
        return EMClient.getInstance().getCurrentUser();
    }

}

