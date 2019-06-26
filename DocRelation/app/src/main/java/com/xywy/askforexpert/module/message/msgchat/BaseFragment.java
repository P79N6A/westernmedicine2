package com.xywy.askforexpert.module.message.msgchat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.Direct;
import com.hyphenate.chat.EMMessage.Type;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EasyUtils;
import com.xywy.askforexpert.MainActivity;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.easeWrapper.EMChatManager;
import com.xywy.easeWrapper.utils.ImUtils;

public class BaseFragment extends Fragment {

    private static final int notifiId = 11;
    protected NotificationManager notificationManager;
    String toChatUserRealname;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        notificationManager = (NotificationManager) getActivity()
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("BaseFragment");
        // onresume时，取消notification显示
        EMChatManager.getInstance().activityResumed();
        // umeng
        StatisticalTools.onResume(getActivity());
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        StatisticalTools.onPause(getActivity());
    }

    /**
     * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下 如果不需要，注释掉即可
     *
     * @param message
     */
    protected void notifyNewMessage(EMMessage message) {
        // 如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
        // 以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
        if (!EasyUtils.isAppRunningForeground(getActivity())) {
            return;
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getActivity())
                .setSmallIcon(getActivity().getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis()).setAutoCancel(true);

        String ticker = ImUtils.getMessageDigest(message, getActivity());
        String st = getResources().getString(R.string.expression);
        if (message.getType() == Type.TXT) {
            ticker = ticker.replaceAll("\\[.{2,3}\\]", st);
        }
        // 设置状态栏提示

        try {
            if (message.direct() == Direct.SEND) {
                toChatUserRealname = message.getStringAttribute("toRealName");
            } else {
                toChatUserRealname = message.getStringAttribute("fromRealName");
            }
        } catch (HyphenateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mBuilder.setTicker(toChatUserRealname + ": " + ticker);

        // 必须设置pendingintent，否则在2.3的机器上会有bug
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(),
                notifiId, intent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pendingIntent);

        Notification notification = mBuilder.build();
        notificationManager.notify(notifiId, notification);
        notificationManager.cancel(notifiId);
    }

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        getActivity().finish();
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("BaseFragment");
    }

}
