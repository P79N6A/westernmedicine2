package com.xywy.component.datarequest.uploadLog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringDef;
import android.util.Log;

import com.google.gson.Gson;
import com.xywy.component.datarequest.network.RequestManager;
import com.xywy.component.datarequest.uploadLog.bean.AttackLog;
import com.xywy.component.datarequest.uploadLog.bean.LogBean;
import com.xywy.component.datarequest.uploadLog.bean.NetCrashLog;
import com.xywy.component.datarequest.uploadLog.bean.NetLog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/8/11 13:51
 * 用于客户端调用的Log接口，将原始数据转化为Json格式传递给实际写和上传Log的实现类
 */
public class UploadLogClient {

    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;
    public static ILog loger;
    private static Context context;

    private UploadLogClient() {
    }

    public static void init(Context application) {
        if(context==null){
            context = application.getApplicationContext();
            loger = new LogProxy(context);
            //初始化时默认上传
            loger.uploadLog();
        }
    }

    public static void net(String requestUrl, String response) {
        NetLog log = new NetLog();
        log.setNetType(ConnectivityUtil.GetNetworkType(context));
        log.setRequest(requestUrl);
        log.setResponse(response);

        writeLog(ERROR, NETWORK, log);
    }

    public static void netCrash(String requestUrl, String response, Throwable ex) {
        NetCrashLog log = new NetCrashLog();
        log.setNetType(ConnectivityUtil.GetNetworkType(context));
        log.setRequest(requestUrl);
        log.setResponse(response);
        //获取崩溃日志
        log.setStackStrace(getThrowableString(ex));
        writeLog(ERROR, NETCRASH, log);
    }

    //外部应用调用我们的StartActivity方法时如果造成崩溃，怀疑是恶意攻击，上传日志
    public static void attack(Intent attackIntent,Throwable ex) {
        if (context == null) {
            return;
        }
        AttackLog log = new AttackLog();
        log.setIntent(attackIntent);
        //获取崩溃日志
        log.setStackStrace(getThrowableString(ex));
        writeLog(ERROR, ATTACK, log);
    }

    private static String getThrowableString(Throwable ex) {
        if (ex==null)
            return "";
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            printWriter.println(cause.toString());
            cause = cause.getCause();
        }
        printWriter.close();
        return writer.toString();
    }


    /**
     * @param priority
     * @param tag
     * @param logContent
     */
    private static <T> void writeLog(final int priority, @LogType String tag, T logContent) {
        if (context == null) {
            if (RequestManager.isDebug()){
                Log.e("UploadLogClient","init not called");
            }
            return;
        }

        LogBean<T> log = new LogBean<>();

        log.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        log.setTag(tag.toString());
        log.setLogContent(logContent);

        loger.writeLog(new Gson().toJson(log));
    }

    public static final String NETWORK = "NETWORK";
    public static final String NETCRASH =  "NETCRASH";
    public static final String ATTACK =  "ATTACK";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({NETWORK, NETCRASH, ATTACK})
    public @interface LogType{}
//    private static enum LogType {
//        NETWORK {
//            @Override
//            public String toString() {
//                return "NETWORK";
//            }
//        },
//        CRASH {
//            @Override
//            public String toString() {
//                return "NETCRASH";
//            }
//        },
//        ATTACK {
//            @Override
//            public String toString() {
//                return "ATTACK";
//            }
//        };
//    }
}
