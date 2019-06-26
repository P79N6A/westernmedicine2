package com.xywy.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Toast管理工具类
 */
public class T {

    //静态变量导致context泄漏问题,使用App Context
    private static Toast toast = null;
    private static Handler h = new Handler(Looper.getMainLooper());

    private T() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    /**
     * 判断时间间隔提示Toast by String
     *  @param str      文字
     * @param duration
     */
    private static void toastString(final String str, final int duration) {
        h.post(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.setText(str);
                    toast.setDuration(duration);
                } else {
                    toast = Toast.makeText(ContextUtil.getAppContext(), str, duration);
                }
                toast.show();
            }
        });
    }

    /**
     * 判断时间间隔提示Toast by resId
     *  @param resId
     * @param duration
     */
    private static void toastResId(int resId, int duration) {
        toastString(ContextUtil.getAppContext().getString(resId),duration);
    }

    /**
     * 短提示 by resId
     *
     * @param resId
     */
    public static void showShort(int resId) {
        toastResId(resId, Toast.LENGTH_SHORT);
    }

    /**
     * 短提示 by String
     *
     * @param string
     */
    public static void showShort(String string) {
        toastString(string, Toast.LENGTH_SHORT);
    }
    public static void showShort(Context context, CharSequence message) {
        toastString(message.toString(),Toast.LENGTH_SHORT);
    }

    public static void showShort(Context context, int message) {
        toastResId(message,Toast.LENGTH_SHORT);
    }
    /**
     * 长提示 by resId
     *
     * @param resId
     */
    public static void showLong(int resId) {
        toastResId(resId, Toast.LENGTH_LONG);
    }

    /**
     * 常提示 by String
     *
     * @param string
     */
    public static void showLong(String string) {
        toastString(string, Toast.LENGTH_LONG);
    }
    public static void showLong(Context context, CharSequence message) {
        toastString(message.toString(), Toast.LENGTH_LONG);
    }

    public static void showLong(Context context, int message) {
        toastResId(message, Toast.LENGTH_LONG);
    }

    public static void show(Context context, CharSequence message, int duration) {
        toastString(message.toString(), duration);
    }

    public static void show(Context context, int message, int duration) {
        if (context != null) {
            if (toast == null) {
                toast = Toast.makeText(context.getApplicationContext(), message, duration);
            } else {
                toast.setText(message);
            }
            toast.show();
        }
    }

    //显示给开发的
    public static void debugToast(Context context, CharSequence message) {
        if (AppUtils.isDebug(context))
            showShort(context, message);
    }

    //显示给测试的
    public static void testToast(Context context, CharSequence message) {
        if (AppUtils.isDebug(context))
            showShort(context, message);
    }

    //显示给用户的
    public static void userToast(Context context, CharSequence message, int duration) {
        show(context, message, duration);
    }
}