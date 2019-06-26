
package com.xywy.askforexpert.module.main.service.downFile;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 服务基类
 *
 * @author 王鹏
 * @2015-6-5下午4:27:59
 */
public class BaseDownService extends Service implements ContentValue,
        AgentConstant {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
