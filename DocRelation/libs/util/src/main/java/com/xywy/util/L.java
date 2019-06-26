package com.xywy.util;

import android.util.Log;

/**
 * Log管理工具类 统一用这个(内容包含行数和方法名) 需要手动关闭日志 stone
 */
public class L {

    public static final String TAG = "XYWY_LOG";
    //TODO stone 测试与线上要修改
//    private static int DEBUG = 5;
    private static int DEBUG = 0;
    public static final int LOG_LEVEL_I = 2;
    public static final int LOG_LEVEL_D = 1;
    public static final int LOG_LEVEL_E = 4;
    public static final int LOG_LEVEL_V = 0;
    public static final int LOG_LEVEL_W = 3;
    private static String INFO = "-->>";
    private static String INIO = "::";

    private L() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    public static void i(String msg) {
        if (DEBUG<=LOG_LEVEL_I)
            printMore(TAG, msg,LOG_LEVEL_I);
    }

    public static void i(int msg) {
        if (DEBUG<=LOG_LEVEL_I)
            printMore(TAG, msg+"",LOG_LEVEL_I);
    }

    public static void d(String msg) {
        if (DEBUG<=LOG_LEVEL_D)
            printMore(TAG, msg,LOG_LEVEL_D);
    }

    public static void d(int msg) {
        if (DEBUG<=LOG_LEVEL_D)
            printMore(TAG, msg+"",LOG_LEVEL_D);
    }

    public static void e(String msg) {
        if (DEBUG<=LOG_LEVEL_E)
            printMore(TAG, msg,LOG_LEVEL_E);
    }

    public static void v(String msg) {
        if (DEBUG<=LOG_LEVEL_V)
            printMore(TAG, msg,LOG_LEVEL_V);
    }

    public static void w(String msg) {
        if (DEBUG<=LOG_LEVEL_W)
            printMore(TAG, msg,LOG_LEVEL_W);
    }

    public static void i(String tag, String msg) {
        if (DEBUG<=LOG_LEVEL_I)
            printMore(tag, msg,LOG_LEVEL_I);
    }

    public static void d(String tag, String msg) {
        if (DEBUG<=LOG_LEVEL_D)
            printMore(tag, msg,LOG_LEVEL_D);
    }

    public static void e(String tag, String msg) {
        if (DEBUG<=LOG_LEVEL_E)
            printMore(tag, msg,LOG_LEVEL_E);
    }

    public static void v(String tag, String msg) {
        if (DEBUG<=LOG_LEVEL_V)
            printMore(tag, msg,LOG_LEVEL_V);
    }

    public static void w(String tag, String msg) {
        if (DEBUG<=LOG_LEVEL_W)
            printMore(tag, msg,LOG_LEVEL_W);
    }
    public final static void ex(Throwable e) {
        if (DEBUG<=LOG_LEVEL_E){
            Log.e(TAG,e.getMessage(), e);
        }
    }

    public static void print(String msg, int level) {
        switch (level) {
            case LOG_LEVEL_D:
                d(msg);
                break;
            case LOG_LEVEL_E:
                e(msg);
                break;
            case LOG_LEVEL_I:
                i(msg);
                break;
            case LOG_LEVEL_V:
                v(msg);
                break;
            case LOG_LEVEL_W:
                w(msg);
                break;
            default:
                i(msg);
                break;
        }
    }

    public static void printMore(String tag, String msg, int level) {
//        int index = 0;
//        StackTraceElement[] traces = new Throwable().getStackTrace();
//        for(index = 0; index < traces.length - 1; index++) {
//            if(traces[index].getFileName().toString().equals("L.java") != true) {
//                break;
//            };
//        }
//        StackTraceElement stackTrace = new Throwable().getStackTrace()[index];
        StackTraceElement stackTrace = new Throwable().getStackTrace()[2];
        String output = stackTrace.getClassName() + INIO + stackTrace.getLineNumber() + INIO + stackTrace.getMethodName()
                + INFO + msg;

        switch (level) {
            case LOG_LEVEL_D:
                Log.d(tag, output);
                break;
            case LOG_LEVEL_E:
                Log.e(tag, output);
                break;
            case LOG_LEVEL_I:
                Log.i(tag, output);
                break;
            case LOG_LEVEL_V:
                Log.v(tag, output);
                break;
            case LOG_LEVEL_W:
                Log.w(tag,output);
                break;
            default:
                Log.i(tag, output);
                break;
        }
    }
}
