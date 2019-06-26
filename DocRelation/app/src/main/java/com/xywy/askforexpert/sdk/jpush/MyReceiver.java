package com.xywy.askforexpert.sdk.jpush;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.MainActivity;
import com.xywy.askforexpert.StartActivity;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.LoginInfo;
import com.xywy.askforexpert.model.QueData;
import com.xywy.askforexpert.model.push.PushExtraBean;
import com.xywy.askforexpert.module.consult.ConsultConstants;
import com.xywy.askforexpert.module.consult.activity.ConsultChatActivity;
import com.xywy.askforexpert.module.consult.activity.ConsultOnlineActivity;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.docotorcirclenew.activity.NewTopicDetailActivity;
import com.xywy.askforexpert.module.drug.OnlineRoomActivity;
import com.xywy.askforexpert.module.main.service.media.InfoDetailActivity;
import com.xywy.askforexpert.module.main.service.que.QueDetailActivity;
import com.xywy.askforexpert.module.main.service.que.QueMyReplyActivity;
import com.xywy.askforexpert.module.main.service.service.ServiceQueActivity;
import com.xywy.askforexpert.module.main.subscribe.video.VideoNewsActivity;
import com.xywy.askforexpert.module.my.integral.GoneScoresActivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * 自定义接收器 stone
 * <p/>
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    private List<QueData> mLables = null;

    private int first;

    /**
     * 跳转类型
     */
//    private int type = 0;

    private String infoDetail = "";
    /**
     * 话题Id
     */
    private String topicId = "";

    private int backNum;

    private int notifactionId;

    private boolean isRunning;

    private SharedPreferences sp = YMApplication.getInstance()
            .getSharedPreferences("save_user", Context.MODE_PRIVATE);

    private PushExtraBean mPushExtraBean;
    public static final String INTENT_KEY_ISFROM = "isFrom";
    public static final String INTENT_KEY_Q_TYPE = "q_type";//1 指定付费 2 悬赏 3绩效
    public static final String INTENT_KEY_RID = "rid";
    public static final String JPUSH = "JPush";
    private int mNofificationNum;

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        // 08-21 15:32:19.662: D/JPush(3747): key:cn.jpush.android.EXTRA,
        // value:{"t":1}
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:").append(key).append(", value:").append(bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:").append(key).append(", value:").append(bundle.getBoolean(key));
            } else {
                sb.append("\nkey:").append(key).append(", value:").append(bundle.getString(key));
            }
        }
        return sb.toString();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        DLog.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
                + ", extras: " + printBundle(bundle));
        if (YMUserService.isGuest() || YMApplication.getInstance().mAppIsForeGround) {    //如果是退出登录了，则将收到的通知清除
            mNofificationNum = 0;
            ShortcutBadger.applyCount(context, mNofificationNum);
            JPushInterface.clearAllNotifications(context);
            YMApplication.sPushExtraBean = null;
            return;
        }

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//            sp.edit().putString("jpush_regis_id", regId).apply();
            DLog.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            // send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            DLog.d(TAG,
                    "[MyReceiver] 接收到推送下来的自定义消息: "
                            + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            DLog.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            ShortcutBadger.applyCount(context, mNofificationNum++);
            notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            //stone
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            mPushExtraBean = GsonUtils.toObj(extras, PushExtraBean.class);
            if (mPushExtraBean != null) {
                YMApplication.getInstance().addNotificationId(mPushExtraBean.getType(), notifactionId);

                DLog.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId
                        + "TYPE:" + mPushExtraBean.getType());
            }
            
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            DLog.i(TAG, "[MyReceiver] 用户点击打开了通知");
            ShortcutBadger.applyCount(context, mNofificationNum--);
            isRunning = isBackgroundRunning(context);
            DLog.i(TAG, "ISOPEN===" + isRunning);
            //stone 5.3版本
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            mPushExtraBean = GsonUtils.toObj(extras, PushExtraBean.class);
            if (mPushExtraBean != null) {
                YMApplication.getInstance().remoNotification(mPushExtraBean.getType());
                if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
                    //用药助手没有推送
                } else {
                    //医脉的推送 stone
                    StatisticalTools.eventCount(YMApplication.getAppContext(), ConsultConstants.EVENT_YIMAIPUSH);
                    handleAction(context);
                }
            }

            //stone 5.3之前的老版本 废除
//            try {
//                JSONObject jsonObject = new JSONObject(extras);
//                type = jsonObject.getInt("t");
//                if (type == 5 || type == 7) {
//                    infoDetail = jsonObject.getString("c");
//                } else if (type == 6) {
//                    topicId = jsonObject.getString("c");
//                }
//                YMApplication.getInstance().remoNotification(type);
//                if(YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)){
//                    //用药助手暂时不用处理医圈的问题
//                }else {
//                    startAction(type, context);
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            DLog.d(TAG,
                    "[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
                            + bundle.getString(JPushInterface.EXTRA_EXTRA));
            // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
            // 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
                .getAction())) {
            boolean connected = intent.getBooleanExtra(
                    JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction()
                    + " connected state change to " + connected);
        } else {
            DLog.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        // if (MainActivity.isForeground) {
        // String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        // String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        // Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
        // msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
        // if (!ExampleUtil.isEmpty(extras)) {
        // try {
        // JSONObject extraJson = new JSONObject(extras);
        // if (null != extraJson && extraJson.length() > 0) {
        // msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
        // }
        // } catch (JSONException e) {
        //
        // }
        //
        // }
        // context.sendBroadcast(msgIntent);
        // }
    }

    //stone  5.3开始启用
    private void handleAction(Context context) {
        // 1正在咨询  2问题广场
        Intent intent = new Intent();
        intent.putExtra("jpush", true);

//        ActivityManager am = (ActivityManager) context
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//        DLog.d(TAG, "pkg:" + cn.getPackageName());
//        DLog.d(TAG, "cls:" + cn.getClassName());
//        DLog.i(TAG, "当前type" + mPushExtraBean.getType());
//        DLog.d(TAG, "jpush is running = " + isRunning);
//
//        if (cn.getPackageName() == null) {
//            return;
//        }
//        DLog.i(TAG, "cn.getpackagename=" + cn.getPackageName());
        if (isRunning) {
            //置空
            YMApplication.sPushExtraBean = null;
            DLog.i(TAG, "当前type" + isRunning + mPushExtraBean.getType() + "当前detail" + infoDetail);
            if (mPushExtraBean.getType() == 1 || mPushExtraBean.getType() == 3) {
                //即时问答详情
                if (YMApplication.sQuestionId != null
                        && YMApplication.sQuestionId.size() > 0
                        && !TextUtils.isEmpty(YMApplication.sQuestionId.get(0))
                        && YMApplication.sQuestionId.get(0).equals(String.valueOf(mPushExtraBean.getQid()))) {
                    //同一个
                    ConsultChatActivity.startActivity2(true, false, context,
                            String.valueOf(mPushExtraBean.getQid()), String.valueOf(mPushExtraBean.getUid()),
                            null, (!TextUtils.isEmpty(mPushExtraBean.getQtype()) && mPushExtraBean.getQtype().equals("3")) ? true : false
                    );
                } else {
                    //不同id 或者没有打开过
                    ConsultChatActivity.startActivity(true, false, context,
                            String.valueOf(mPushExtraBean.getQid()), String.valueOf(mPushExtraBean.getUid()),
                            null, (!TextUtils.isEmpty(mPushExtraBean.getQtype()) && mPushExtraBean.getQtype().equals("3")) ? true : false
                    );
                }
            } else if (mPushExtraBean.getType() == 2) {
                //问题广场咨询详情页
                if (YMApplication.sWTGCQuestionId != null
                        && YMApplication.sWTGCQuestionId.size() > 0
                        && !TextUtils.isEmpty(YMApplication.sWTGCQuestionId.get(0))
                        && YMApplication.sWTGCQuestionId.get(0).equals(String.valueOf(mPushExtraBean.getQid()))) {
                    //同一个id
                    forwordToQueDetailActivity2(context, JPUSH, mPushExtraBean.getQid() + "", mPushExtraBean.getWaitType(), mPushExtraBean.getRid(), mPushExtraBean.getQtype());

                } else {
                    //不同id 或者木有打开过
                    //直接获取问题详情数据,跳转到问题详情页面
                    forwordToQueDetailActivity(context, JPUSH, mPushExtraBean.getQid() + "", mPushExtraBean.getWaitType(), mPushExtraBean.getRid(), mPushExtraBean.getQtype());

                }
//                else {
//                    DLog.i(TAG, "再serviceque");
//                    intent.setAction("com.questate.change");
//                    intent.putExtra("questate", mPushExtraBean.getType());
//                    context.sendBroadcast(intent);
//                }
            } else if(mPushExtraBean.getType() == 6){
                context.startActivity(new Intent(context, OnlineRoomActivity.class).putExtra("pageItem",1));
            }else if(mPushExtraBean.getType() == 7){
                context.startActivity(new Intent(context, OnlineRoomActivity.class).putExtra("pageItem",0));
            }
//            else if (mPushExtraBean.getType() == 3){
//                if (YMApplication.sQuestionId != null
//                        && YMApplication.sQuestionId.size() > 0
//                        && !TextUtils.isEmpty(YMApplication.sQuestionId.get(0))
//                        && YMApplication.sQuestionId.get(0).equals(String.valueOf(mPushExtraBean.getQid()))) {
//                    //同一个
//                    ConsultChatActivity.startActivity2(true, false, context,
//                            String.valueOf(mPushExtraBean.getQid()), String.valueOf(mPushExtraBean.getUid()),
//                            null, (!TextUtils.isEmpty(mPushExtraBean.getQtype()) && mPushExtraBean.getQtype().equals("3")) ? true : false
//                    );
//                } else {
//                    //不同id 或者没有打开过
//                    ConsultChatActivity.startActivity(true, false, context,
//                            String.valueOf(mPushExtraBean.getQid()), String.valueOf(mPushExtraBean.getUid()),
//                            null, (!TextUtils.isEmpty(mPushExtraBean.getQtype()) && mPushExtraBean.getQtype().equals("3")) ? true : false
//                    );
//                }
////                goIMChat();
//            }
        } else {
            //没有在运行 此时打开 进去跳转
            YMApplication.sPushExtraBean = mPushExtraBean;
            intent.setClass(context, StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }

    }


    //去往在线咨询 stone
    public static void goIMChat(final Context context) {
        DialogUtil.showUserCenterCertifyDialog(context, new MyCallBack() {
            @Override
            public void onClick(Object data) {
                StatisticalTools.eventCount(context, "Onlineconsulting");
                LoginInfo login1 = YMApplication.getLogin1();
                if (null != login1) {
                    LoginInfo.UserData userData = login1.getData();
                    if (null != userData) {
                        if (userData.getImwd() == 1) {
                            Intent intent = new Intent(context, ConsultOnlineActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            DialogUtil.NewDialog(context, "请联系客服帮你开通，客服电话4008591200");
                        }
                    } else {
                        DialogUtil.NewDialog(context, "请联系客服帮你开通，客服电话4008591200");
                    }
                } else {
                    DialogUtil.NewDialog(context, "请联系客服帮你开通，客服电话4008591200");
                }
            }
        }, null, "即时问答");
    }

    private void forwordToQueDetailActivity(Context context, String isFrom, String qid, String type, String rid, String q_type) {
        Intent intent = new Intent(context, QueDetailActivity.class);
        intent.putExtra("tag", "other");
        intent.putExtra(INTENT_KEY_ISFROM, isFrom);
        intent.putExtra("type", type);
        intent.putExtra(INTENT_KEY_Q_TYPE, q_type);
        intent.putExtra(INTENT_KEY_RID, rid);
        intent.putExtra("id", qid);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void forwordToQueDetailActivity2(Context context, String isFrom, String qid, String type, String rid, String q_type) {
        Intent intent = new Intent(context, QueDetailActivity.class);
        intent.putExtra("tag", "other");
        intent.putExtra(INTENT_KEY_ISFROM, isFrom);
        intent.putExtra("type", type);
        intent.putExtra(INTENT_KEY_Q_TYPE, q_type);
        intent.putExtra(INTENT_KEY_RID, rid);
        intent.putExtra("id", qid);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //stone 老版本的 5.3之前 后台说废除
    private void startAction(int type, Context context) {
        // 1 对我提问 2 对我追问 3 驳回
        Intent intent = new Intent();
        intent.putExtra("jpush", true);

        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        DLog.d(TAG, "pkg:" + cn.getPackageName());
        DLog.d(TAG, "cls:" + cn.getClassName());
        DLog.i(TAG, "当前type" + type);
        DLog.d(TAG, "jpush is running = " + isRunning);

        if (cn.getPackageName() == null) {
            return;
        }
        DLog.i(TAG, "cn.getpackagename=" + cn.getPackageName());
//        if (isRunning) {
        DLog.i(TAG, "当前type" + isRunning + type + "当前detail" + infoDetail);
        if (type == 1 || type == 2) {
            if (!cn.getClassName().equals("com.xywy.askforexpert.Activity.Service.ServiceQueActivity")) {
                DLog.i(TAG, "不再serviceque");
                getContentData(context, intent, type);
            } else {
                DLog.i(TAG, "再serviceque");
                intent.setAction("com.questate.change");
                intent.putExtra("questate", type);
                context.sendBroadcast(intent);
            }
        } else if (type == 3) {
            // 如果不在我的回复界面
            if (!cn.getClassName().equals("com.xywy.askforexpert.Activity.Service.QueMyReplyActivity")) {
                intent.setAction("com.refresh.list");
                intent.putExtra("index", 0);
                context.sendBroadcast(intent);
                intent.setClass(context, QueMyReplyActivity.class);
                intent.putExtra("backNum", 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            } // 如果在问题广场页面，此时来通知，更新侧边栏的值
            // else if(cn.getClassName()
            // .equals("com.xywy.askforexpert.Activity.Service.ServiceQueActivity")){
            // intent.setAction("com.questate.change");
            // intent.putExtra("questate", type);
            // context.sendBroadcast(intent);
            // }
            else {
                intent.setAction("com.refresh.list");
                intent.putExtra("index", 0);
                context.sendBroadcast(intent);
            }
        } else if (4 == type) {
            Intent intent2 = new Intent(context, GoneScoresActivity.class);
            intent2.putExtra("userid", "");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent2);
        } else if (5 == type) {
            Intent intent3 = new Intent(context, InfoDetailActivity.class);
            DLog.i(TAG, "当前type" + type + "当前detail" + infoDetail);
            String[] infoStrings = infoDetail.split("\\|");
            if (infoStrings.length != 4) {
                return;
            }
            intent3.putExtra("ids", infoStrings[0]);
            intent3.putExtra("url", infoStrings[1]);
            intent3.putExtra("title", infoStrings[2]);
            intent3.putExtra("imageurl", infoStrings[3]);
            intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent3);
        } else if (6 == type) {            //话题
            Intent intent4 = new Intent(context, NewTopicDetailActivity.class);
            intent4.putExtra(NewTopicDetailActivity.TOPICID_INTENT_PARAM_KEY, Integer.parseInt(topicId));
            intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent4);
        } else if (7 == type) {
            // 原生视频
            String[] infoStrings = infoDetail.split("\\|");
            DLog.d(TAG, "push infoString = " + Arrays.toString(infoStrings));
            if (infoStrings.length != 6) {
                return;
            }
            Intent videoIntent = new Intent(context, VideoNewsActivity.class);
            videoIntent.putExtra(VideoNewsActivity.NEWS_ID_INTENT_KEY, infoStrings[0]);
            videoIntent.putExtra(VideoNewsActivity.VIDEO_TITLE_INTENT_KEY, infoStrings[2]);
            videoIntent.putExtra(VideoNewsActivity.VIDEO_UUID_INTENT_KEY, infoStrings[4]);
            videoIntent.putExtra(VideoNewsActivity.VIDEO_VUID_INTENT_KEY, infoStrings[5]);
            videoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(videoIntent);
        } else {
            intent.putExtra("type", type);
            intent.setClass(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }

    }

    private void getContentData(final Context context, final Intent intent,
                                final int jump) {
        AjaxParams params = new AjaxParams();
        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put(
                "sign",
                MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid()
                        + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.QUE_TITLE, params, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                Toast.makeText(context, "网络连接失败，请稍后再试", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                DLog.i(TAG, "Myreceiver jpush");
                parseJsonData(t, context);
                if (mLables.size() != 0) {
                    YMApplication.getInstance().setmLables(mLables);

                    intent.setAction("com.questate.change");
                    intent.putExtra("questate", mPushExtraBean.getType());
                    context.sendBroadcast(intent);

                    Bundle bundle = new Bundle();
                    bundle.putInt("first", jump);
                    bundle.putInt("isJump", jump);
                    bundle.putBoolean("jpush", true);
                    bundle.putInt("backNum", backNum);
                    bundle.putInt("from", 1);
                    intent.setClass(context, ServiceQueActivity.class);
                    intent.putExtra("data", bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                } else {
                    Toast.makeText(context, "数据加载出错，请稍后再试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void parseJsonData(String json, Context context) {
        mLables = new ArrayList<QueData>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            if (code == 0) {
                JSONObject jsonElement = jsonObject.getJSONObject("data");
                // first = jsonElement.getInt("first");
                // isJump = jsonElement.getInt("isJump");
                backNum = jsonElement.getInt("backNum");
                JSONArray array = jsonElement.getJSONArray("menu");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonChild = array.getJSONObject(i);
                    QueData queData = new QueData();
                    queData.setName(jsonChild.getString("name"));
                    queData.setUrl(jsonChild.getString("url"));
                    queData.setNum(jsonChild.getInt("num"));
                    mLables.add(queData);
                }

            } else {
                ToastUtils.shortToast(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断app是否正在运行
     *
     * @param context
     * @return 运行状态
     */
    private boolean isBackgroundRunning(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> appTasks = activityManager.getRunningTasks(100);

        boolean isAppRunning = false;
        String MY_PKG_NAME = "com.xywy.askforexpert";
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            MY_PKG_NAME = "com.xywy.medicine_super_market";
        }

        for (RunningTaskInfo info : appTasks) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME) || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                isAppRunning = true;
                Log.i(TAG, info.topActivity.getPackageName() + " info.baseActivity.getPackageName()=" + info.baseActivity.getPackageName());
                break;
            }
        }

        return isAppRunning;

    }
}
