package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.util;

import android.app.Instrumentation;

/**
 * Created by zhangzheng on 2017/4/11.
 */

public class CommonUtil {
    /**
     * 模拟键盘事件方法
     *
     * @param keyCode
     */
    public static void actionKey(final int keyCode) {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(keyCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
