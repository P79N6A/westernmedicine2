package com.xywy.askforexpert.appcommon.utils.others;

import android.util.Log;

import com.xywy.askforexpert.BuildConfig;

/**
 * 项目名称：D_Platform
 * 类名称：DLog
 * 类描述：日志类封装
 * 创建人：shihao
 * 创建时间：2015-10-10 下午4:52:17
 * 修改备注：
 *
 * @modified by Jack Fang
 *
 * 旧的Log 工具类 不建议使用 建议使用 LogUtils 能更清楚地定位 日志所在类 行数
 */
@Deprecated
public final class DLog {

    //TODO stone 测试与线上要修改 动态控制
    public static final boolean DEBUG = BuildConfig.DEBUG;

    private DLog() {
        throw new AssertionError();
    }

    public static void d(String tag, String desc) {
        if (DEBUG) {
            Log.d(tag, desc);
        }
    }

    public static void d(String tag, String desc, Throwable tr) {
        if (DEBUG) {
            Log.d(tag, desc, tr);
        }
    }

    public static void v(String tag, String desc) {
        if (DEBUG) {
            Log.v(tag, desc);
        }
    }

    public static void v(String tag, String desc, Throwable tr) {
        if (DEBUG) {
            Log.v(tag, desc);
        }
    }

    public static void w(String tag, String desc) {
        if (DEBUG) {
            Log.w(tag, desc);
        }
    }

    public static void w(String tag, Throwable ioe) {
        if (DEBUG) {
            Log.w(tag, ioe);
        }
    }

    public static void w(String tag, String desc, Throwable e) {
        if (DEBUG) {
            Log.w(tag, desc, e);
        }
    }

    public static void i(String tag, String desc) {
        if (DEBUG) {
            Log.i(tag, desc);
        }
    }

    public static void i(String tag, String desc, Throwable tr) {
        if (DEBUG) {
            Log.i(tag, desc, tr);
        }
    }

    public static void e(String tag, String desc) {
        if (DEBUG) {
            Log.e(tag, desc);
        }
    }

    public static void e(String tag, String desc, Throwable tr) {
        if (DEBUG) {
            Log.e(tag, desc, tr);
        }
    }
}
