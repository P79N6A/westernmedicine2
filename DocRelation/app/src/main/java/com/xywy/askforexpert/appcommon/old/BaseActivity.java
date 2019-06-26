package com.xywy.askforexpert.appcommon.old;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.EasyUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.easeWrapper.EMChatManager;
import com.xywy.easeWrapper.controller.EaseUI;

/**
 * @author 王鹏
 * @2015-4-15上午10:44:06
 */
@Deprecated
public class BaseActivity extends AppCompatActivity {
    protected NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }



    @Override
    protected void onResume() {
        super.onResume();
        // onresume时，取消notification显示
        EMChatManager.getInstance().activityResumed();
        // umeng
        StatisticalTools.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // umeng
        StatisticalTools.onPause(this);
    }

    /**
     * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下 如果不需要，注释掉即可
     *
     * @param message
     */
    protected void notifyNewMessage(EMMessage message) {
        // 如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
        // 以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
        if (!EasyUtils.isAppRunningForeground(this)) {
            return;
        }

        EaseUI.getInstance().getNotifier().onNewMsg(message);

//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                this).setSmallIcon(getApplicationInfo().icon)
//                .setWhen(System.currentTimeMillis()).setAutoCancel(true);
//
//        String ticker = ImUtils.getMessageDigest(message, this);
//        String st = getResources().getString(R.string.expression);
//        if (message.getType() == Type.TXT) {
//            ticker = ticker.replaceAll("\\[.{2,3}\\]", st);
//        }
//        // 设置状态栏提示
//        try {
//            if (message.direct() == Direct.SEND) {
//                toChatUserRealname = message.getStringAttribute("toRealName");
//            } else {
//                toChatUserRealname = message.getStringAttribute("fromRealName");
//            }
//        } catch (HyphenateException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        mBuilder.setTicker(toChatUserRealname + ": " + ticker);
//
//        // 必须设置pendingintent，否则在2.3的机器上会有bug
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, notifiId,
//                intent, PendingIntent.FLAG_ONE_SHOT);
//        mBuilder.setContentIntent(pendingIntent);
//
//        Notification notification = mBuilder.build();
//        notificationManager.notify(notifiId, notification);
//        notificationManager.cancel(notifiId);
    }

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

}
