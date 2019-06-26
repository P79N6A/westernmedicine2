package com.xywy.util;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * 切换到后台的工具类 *
 */
public class BackgroundUtils {
    public static final String APP_ENTER_BACKGROUND_BROADCAST = "APP_ENTER_BACKGROUND_BROADCAST";

    public static final String APP_ENTER_FOREGROUND_BROADCAST = "APP_ENTER_FOREGROUND_BROADCAST";

    private static BackgroundUtils mBackgroundUtils;

    private boolean mCurrenState;

    private boolean mOldState = false;// false为后台，true为前台
    private static Context mContext;
    private String mPackageName = "";


    private BackgroundUtils(Context context) {
        mPackageName = context.getPackageName();
        mContext = context;
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static BackgroundUtils getInstance(Context context) {
        if(mBackgroundUtils == null) {
            mBackgroundUtils = new BackgroundUtils(context);
        }
        mBackgroundUtils.setContext(context);
        return mBackgroundUtils;
    }

    public void dealAppRunState() {
        background();
    }

    /**
     * @param isActive 是否是主动运行
     */
    public void dealAppRunState(String awokeWay, boolean isActive) {
        boolean show = isAppOnForeground();
        if (show) {
            foreground(show, awokeWay, isActive);
        } else {
            background();
        }
    }

    private void foreground(boolean show, String awokeWay, boolean isActive) {
        mCurrenState = show;
        if (mCurrenState != mOldState) {// 切换到前台
            mOldState = mCurrenState;
            dealForeground(awokeWay, isActive);
        }
    }

    private void background() {
        mCurrenState = isAppOnForeground();
        if (!mCurrenState && mCurrenState != mOldState) {// 切换到后台
            mOldState = false;
            dealBackground();
        }
    }

    // 前台切换到后台
    private void dealBackground() {
        Intent intent = new Intent(APP_ENTER_BACKGROUND_BROADCAST);
        mContext.sendBroadcast(intent);
    }

    /**
     * 后台切换到前台
     *
     * @param awokeWay 唤醒方式（在isActive为false的情况下）
     * @param isActive 是否是主动运行
     */
    private void dealForeground(String awokeWay, boolean isActive) {
        Intent intent = new Intent(APP_ENTER_FOREGROUND_BROADCAST);
        mContext.sendBroadcast(intent);
    }


    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);

        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);

        // The name of the process that this object is associated with.
        if (tasksInfo.size() > 0 && mPackageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
            return true;
        }

        return false;
    }
}
