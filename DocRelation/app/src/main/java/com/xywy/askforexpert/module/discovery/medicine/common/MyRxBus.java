package com.xywy.askforexpert.module.discovery.medicine.common;

import android.content.Context;

import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.retrofit.rxbus.RxBus;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/4/7 14:23
 */

public class MyRxBus {
    /**
     * 通知用户信息发生变化
     */
    public static void notifyUserInfoChanged(){
        postEvent("UserInfoChanged", null);
    }

    /**
     *注册用户信息改变监听
     * @param subscriber
     * @param tag  将事件和生命周期进行绑定
     */
    public static void registerUserInfoChanged(EventSubscriber subscriber, Context tag){
        registerEvent("UserInfoChanged", subscriber, tag);
    }

    /**
     * 通知患者信息发生变化
     */
    public static void notifyPatientInfoChanged(Object data){
        postEvent("PatientInfoChanged", data);
    }

    /**
     * 患者信息改变监听
     * @param subscriber
     * @param tag  将事件和生命周期进行绑定
     */
    public static void registerPatientInfoChanged(EventSubscriber subscriber, Context tag){
        registerEvent("PatientInfoChanged", subscriber, tag);
    }

    /**
     * 注销患者信息改变监听
     */
    public static void unRegisterPatientInfoChanged(Context tag){
        RxBus.getDefault().removeSubscriptions(tag);
    }

    /**
     * 通知最近咨询患者信息发生变化
     */
    public static void notifyLatestPatientInfoChanged(){
        postEvent("LatestPatientInfoChanged", null);
    }

    /**
     * 最近咨询患者信息改变监听
     * @param subscriber
     * @param tag  将事件和生命周期进行绑定
     */
    public static void registerLatestPatientInfoChanged(EventSubscriber subscriber, Context tag){
        registerEvent("LatestPatientInfoChanged", subscriber, tag);
    }

    /**
     * 注销患者信息改变监听
     */
    public static void unRegisterLatestPatientInfoChanged(Context tag){
        RxBus.getDefault().removeSubscriptions(tag);
    }
    /**
     * 通知消息有更新
     */
    public static void notifySystemMsgListener(){
        postEvent("SystemMsgArrived", null);
    }

    /**
     *
     * @param subscriber
     * @param tag  将事件和生命周期进行绑定
     */
    public static void registerSystemMsgListener(EventSubscriber subscriber, Context tag){
        registerEvent("SystemMsgArrived", subscriber, tag);
    }

    private static void postEvent(String eventId,Object data){
        RxBus.getDefault().post(new Event<>(eventId,data));
    }

    private static void registerEvent(String eventId, EventSubscriber subscriber, Context tag){
        RxBus.getDefault().registerEvent(eventId,subscriber, tag);
    }
}
