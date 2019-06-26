package com.xywy.component.datarequest.uploadLog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/8/29 10:33
 * 用于接收log请求，并分发请求到专用Log线程
 */
public class LogProxy implements ILog {
    static final int UPLOAD = 4;
    static final int WRITE_LOG = 5;
    public volatile ILog loger;
    private final HandlerThread t;
    private final Handler h;


    public LogProxy(Context context) {
        t= new HandlerThread("uploadLog");
        t.start();
        h=new Handler(t.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WRITE_LOG:
                        String log = msg.getData().getString("msg");
                        loger.writeLog(log);
                        break;
                    case UPLOAD:
                        loger.uploadLog();
                        break;
                }
            }
        };
        loger=new LogImpl(context,h);
    }

    public  void writeLog(String log) {
        Message message = Message.obtain();
        message.what = WRITE_LOG;
        Bundle bundle = new Bundle();
        bundle.putString("msg", log);
        message.setData(bundle);
        h.sendMessage(message);
    }

    @Override
    public void uploadLog() {
        h.sendEmptyMessage(UPLOAD);
    }
}
