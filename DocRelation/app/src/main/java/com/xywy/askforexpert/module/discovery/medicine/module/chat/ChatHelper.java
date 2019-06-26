package com.xywy.askforexpert.module.discovery.medicine.module.chat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.xywy.askforexpert.module.discovery.medicine.common.MyConstant;
import com.xywy.askforexpert.module.discovery.medicine.common.MyRxBus;
import com.xywy.base.view.MessageDialog;
import com.xywy.easeWrapper.controller.EaseUI;
import com.xywy.easeWrapper.domain.EaseUser;
import com.xywy.easeWrapper.utils.EaseNotifier;
import com.xywy.retrofit.net.ApiException;
import com.xywy.util.AppUtils;
import com.xywy.util.ContextUtil;
import com.xywy.util.L;

import java.util.List;
import java.util.Map;

import rx.Subscriber;

public class ChatHelper {


    protected static final String TAG = "ChatHelper";

    private EaseUI easeUI;

    /**
     * EMEventListener
     */
    protected EMMessageListener messageListener = null;

    private Map<String, EaseUser> contactList;

    private static ChatHelper instance = null;

    private String username;

    private Context appContext;

    private LocalBroadcastManager broadcastManager;


    private ChatHelper() {
    }

    public synchronized static ChatHelper getInstance() {
        if (instance == null) {
            instance = new ChatHelper();
        }
        return instance;
    }

    /**
     * init helper
     *
     * @param context application context
     */
    public void init(Context context) {
        EMOptions options = initChatOptions();
        //use default options if options is null
        if (EaseUI.getInstance().init(context, options)) {
            appContext = context;

            //debug mode, you'd better set it to false, if you want release your App officially.
            EMClient.getInstance().setDebugMode(true);
            //get easeui instance
            easeUI = EaseUI.getInstance();

            easeUI.setUserProfileProvider(HxUserHelper.getUserProvider());
            setGlobalListeners();
            broadcastManager = LocalBroadcastManager.getInstance(appContext);
        }
    }

    public void Login(String username, String password, final Subscriber subscriber) {
        if (isLoggedIn()) {
            subscriber.onNext(null);
            return;
        }
//		username="qwer";
//		password="1234";
        EMClient.getInstance().login(username, password, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.e(TAG, "login: onSuccess");

                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                subscriber.onNext(null);
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.e(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.e(TAG, "login: onError: " + code);
                //// TODO: 17/3/23 重新登陆
                subscriber.onError(new ApiException(code, message));
            }
        });
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

        return options;
    }

    private void onHxException(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (ContextUtil.currentActivity() != null) {
                    new MessageDialog(ContextUtil.currentActivity())
                            .setMessage(msg)
                            .setCancelable(false)
                            .setOkButton("确定", new MessageDialog.OnClickListener() {

                                @Override
                                public void onClick(MessageDialog messageDialog) {
                                    logout(true,null);
                                    messageDialog.dismiss();
                                    AppUtils.restart(ContextUtil.currentActivity());
                                }
                            }).show();
                }
            }
        });
    }

    EMConnectionListener connectionListener;

    /**
     * set global listener
     */
    protected void setGlobalListeners() {

        // create the global connection listener
        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                L.e("global listener", "onDisconnect" + error);
                if (error == EMError.USER_REMOVED) {
                    Log.e(TAG, "已经被移除");
                    // 显示帐号已经被移除
                    onHxException("当前登录用户环信账号已经被移除，请重新登录");
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    Log.e(TAG, "被踢出");
                    // 显示帐号在其他设备登陆dialog
                    onHxException("用户在其他设备登录，请重新登录");
                }
            }

            @Override
            public void onConnected() {
                L.e("global listener", "onConnected");
            }
        };

        //register connection listener
        EMClient.getInstance().addConnectionListener(connectionListener);
        //register message event listener
        registerMessageListener();

    }

    void showToast(final String message) {
        Message msg = Message.obtain(handler, 0, message);
        handler.sendMessage(msg);
    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            Toast.makeText(appContext, str, Toast.LENGTH_LONG).show();
        }
    };

    /**
     * Global listener
     * If this event already handled by an activity, you don't need handle it again
     * activityList.size() <= 0 means all activities already in background or not in Activity Stack
     */
    protected void registerMessageListener() {
        messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    L.e(TAG, "onMessageReceived id : " + message.getMsgId());
                    // in background, do not refresh UI, notify it in notification bar
                    if (!easeUI.hasForegroundActivies()) {
                        getNotifier().onNewMsg(message);
                    }
                }
                for (EMMessage message : messages) {
                    if(MyConstant.HX_SYSTEM_ID.equals(message.getFrom())){
                        MyRxBus.notifySystemMsgListener();
                        break;
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    L.e(TAG, "receive command message");
                    //get message body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action();//获取自定义action

                    if (action.equals("__Call_ReqP2P_ConferencePattern")) {
                        String title = message.getStringAttribute("em_apns_ext", "conference call");
                        Toast.makeText(appContext, title, Toast.LENGTH_LONG).show();
                    }
                    //end of red packet code
                    //获取扩展属性 此处省略
                    //maybe you need get extension of your message
                    //message.getStringAttribute("");
                    L.e(TAG, String.format("Command：action:%s,message:%s", action, message.toString()));
                }
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> list) {

            }

//            @Override
//            public void onMessageRead(List<EMMessage> messages) {
//            }
//
//            @Override
//            public void onMessageDelivered(List<EMMessage> message) {
//            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                L.e(TAG, "change:");
                L.e(TAG, "change:" + change);
            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    /**
     * if ever logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected();
    }

    /**
     * logout
     *
     * @param unbindDeviceToken whether you need unbind your device token
     * @param callback          callback
     */
    public void logout(boolean unbindDeviceToken, final EMCallBack callback) {
        Log.d(TAG, "logout: " + unbindDeviceToken);
        EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onSuccess();
                }

            }

            @Override
            public void onProgress(int progress, String status) {
                if (callback != null) {
                    callback.onProgress(progress, status);
                }
            }

            @Override
            public void onError(int code, String error) {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onError(code, error);
                }
            }
        });
    }

    /**
     * get instance of EaseNotifier
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }

    /**
     * update contact list
     *
     * @param aContactList
     */
    public void setContactList(Map<String, EaseUser> aContactList) {
        if (aContactList == null) {
            if (contactList != null) {
                contactList.clear();
            }
            return;
        }

        contactList = aContactList;
    }

    synchronized void reset() {
    }

    public void pushActivity(Activity activity) {
        easeUI.pushActivity(activity);
    }

    public void popActivity(Activity activity) {
        easeUI.popActivity(activity);
    }

}
