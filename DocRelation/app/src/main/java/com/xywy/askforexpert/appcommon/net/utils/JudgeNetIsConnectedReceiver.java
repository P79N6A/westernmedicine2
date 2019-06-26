package com.xywy.askforexpert.appcommon.net.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


/**
 * 项目名称：D_Platform
 * 类名称：JudgeNetIsConnectedReceiver
 * 类描述：网络监听判断
 * 创建人：shihao
 * 创建时间：2015-6-5 下午5:15:26
 * 修改备注：
 */
public class JudgeNetIsConnectedReceiver extends BroadcastReceiver {

    public static boolean judgeNetIsConnected(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        }

        return networkInfo.isConnected();

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isNotConnected = intent.getBooleanExtra(
                ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if (!isNotConnected || judgeNetIsConnected(context)) {

        } else {
            Toast.makeText(context, "当前无网络", Toast.LENGTH_LONG).show();
        }
    }
}
