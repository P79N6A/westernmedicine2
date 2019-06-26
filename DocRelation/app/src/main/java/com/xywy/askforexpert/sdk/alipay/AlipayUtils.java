package com.xywy.askforexpert.sdk.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;


/**
 * Created by bailiangjin on 2017/2/28.
 */

public class AlipayUtils {

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
//            Result result = new Result((String) msg.obj);
//            Toast.makeText(DemoActivity.this, result.getResult(),
//                    Toast.LENGTH_LONG).show();
        }

        ;
    };

    private static final int SDK_PAY_FLAG = 100;

    public static void test(final Activity activity) {
        final String orderInfo = "";   // 订单信息

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                String result = alipay.payV2(orderInfo, true).toString();

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
               // mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
