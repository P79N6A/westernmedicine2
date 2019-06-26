package com.xywy.util;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.List;


/**
 * App相关辅助类
 */
public class AppUtils {

    private AppUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps != null) {
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName;
                }
            }
        }
        return null;
    }
    public static boolean isMainProcess(Context cxt) {
        String processName = getProcessName(cxt, android.os.Process.myPid());
        if (cxt.getApplicationInfo().packageName.equals(processName)) {
            //非主进程直接返回
            return true;
        }
        return false;
    }
    /**
     * 获取应用程序版本名称信息
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检查是否为Debug版本
     */
    public static boolean isDebug(Context context) {
        return getBuildInfo(context,"DEBUG",false);
    }
    public static boolean isTestServer(Context context) {
        return getBuildInfo(context,"isTestServer",false);
    }
    /**
     * 检查主工程BuildConfig的属性值
     */
    public static <T> T getBuildInfo(Context context, String paraName, T defaultValue) {
        if(context != null) {
            String name = getAppName(context);
            name = context.getPackageName();
            name = name + ".BuildConfig";
            Class<?> c = null;
            Object obj = null;
            Field field = null;
            try {
                c = Class.forName(name);
                obj = c.newInstance();
                field = c.getField(paraName);
                return  (T)field.get(obj);
            } catch (Exception e) {
                Log.d(L.TAG,"Error when printing log " + e.toString());
            }
        }
        return defaultValue;
    }
    public static void  restart(Context context){
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, restartIntent); // 1000ms后重启应用
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    /**
     * 获取状态栏高度
     *
     * @param context 上下文
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }

        return statusBarHeight;
    }
}
