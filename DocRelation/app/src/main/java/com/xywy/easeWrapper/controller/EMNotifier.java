package com.xywy.easeWrapper.controller;

import android.content.Context;

/**
 * Created by shijiazi on 16/8/17.
 */
public class EMNotifier {
    private static EMNotifier instance;
    private EMNotifier() {

    }
    public static EMNotifier getInstance(Context applicationContext) {
        if(instance == null) {
            instance = new EMNotifier();
        }
        return instance;
    }

    public void notifyOnNewMsg() {
//        EaseUI.getInstance().getNotifier().onNewMsg(mesage);
        //// TODO: 16/8/17 shijiazi 发送一个消息
    }
}
