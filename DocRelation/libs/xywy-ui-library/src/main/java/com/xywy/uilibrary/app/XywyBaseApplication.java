package com.xywy.uilibrary.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by bailiangjin on 16/7/29.
 */
abstract public class XywyBaseApplication extends Application implements Application.ActivityLifecycleCallbacks {

//    protected static Context appContext;

    public static Context getAppContext() {
        return ContextUtil.getAppContext();
    }
    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            Log.e("XywyBaseApplication","app exit");
            onAppExit();
            ContextUtil.finishAllActivity();
        } catch (Exception e) {
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityListener();
    }

    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    public static void pushActivity(Activity activity) {
        ContextUtil.pushActivity(activity);
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    public static void popActivity(Activity activity) {
        ContextUtil.popActivity(activity);
    }

    /**
     * @return 作用说明 ：获取当前最顶部activity的实例
     */
    public static Activity getTopActivity() {

        return ContextUtil.getTopActivity();
    }

    /**
     * @return 作用说明 ：获取当前最顶部的acitivity 名字
     */
    public String getTopActivityName() {
        return ContextUtil.getTopActivityName();
    }

    private void registerActivityListener() {
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    abstract protected void onAppExit();


}
