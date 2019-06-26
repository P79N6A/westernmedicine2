package com.xywy.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ContextUtil {
    public static List<Activity> mActivitys = Collections
            .synchronizedList(new LinkedList<Activity>());
    public static Application app;
    public static String lastPausedActivityName = "";

    private ContextUtil() {
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        return getTopActivity();
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public static void finishCurrentActivity() {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        Activity activity = mActivitys.get(mActivitys.size() - 1);
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        if (activity != null) {
            mActivitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        for (Activity activity : mActivitys) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 按照指定类名找到activity
     *
     * @param cls
     * @return
     */
    public static Activity findActivity(Class<?> cls) {
        Activity targetActivity = null;
        if (mActivitys != null) {
            for (Activity activity : mActivitys) {
                if (activity.getClass().equals(cls)) {
                    targetActivity = activity;
                    break;
                }
            }
        }
        return targetActivity;
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            activity.finish();
        }
        mActivitys.clear();
    }

    private static ContextUtil instance = new ContextUtil();

    public static ContextUtil getInstance() {
        return instance;
    }

    public static void init(Application context) {
        app = context;
        app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                L.d("::" + activity.getClass().getName());
                //Activity 入栈
                ContextUtil.pushActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                L.d("开始的的"+activity.getClass().getName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if(lastPausedActivityName.equals(activity.getClass().getName())){
                    lastPausedActivityName="";
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                lastPausedActivityName = activity.getClass().getName();
                L.d("暂停的"+lastPausedActivityName);
            }

            @Override
            public void onActivityStopped(Activity activity) {
                L.d("停止的"+activity.getClass().getName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (null == ContextUtil.mActivitys || ContextUtil.mActivitys.isEmpty()) {
                    return;
                }
                if (ContextUtil.mActivitys.contains(activity)) {
                    ContextUtil.popActivity(activity);
                }
            }
        });
    }

    public static Context getAppContext() {
        if (app == null)
            throw new RuntimeException("call init() first");
        return app;
    }

    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    public static void pushActivity(Activity activity) {
        mActivitys.add(activity);
        L.d("activityList:size:" + mActivitys.size());
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    public static void popActivity(Activity activity) {
        mActivitys.remove(activity);
        L.d("activityList:size:" + mActivitys.size());
    }

    /**
     * @return 作用说明 ：获取当前最顶部activity的实例
     */
    public static Activity getTopActivity() {
        Activity mBaseActivity = null;
        synchronized (mActivitys) {
            final int size = mActivitys.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActivitys.get(size);
        }
        return mBaseActivity;

    }

    /**
     * @return 作用说明 ：获取当前最顶部的acitivity 名字
     */
    public static String getTopActivityName() {
        Activity topActivity = getTopActivity();
        return null == topActivity ? "null" : topActivity.getClass().getName();
    }

    /**
     * top Activity 是否处于pause状态
     * @return
     */
    public static boolean isTopActiviyPauseed(){
        return lastPausedActivityName.equals(getTopActivityName());
    }
}