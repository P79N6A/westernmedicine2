package com.xywy.askforexpert.module.liveshow.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.xywy.askforexpert.appcommon.utils.ToastUtils;

import java.util.List;

/**
 * Created by bailiangjin on 2017/3/16.
 */

public class LiveShowUtils {



    public static void watchLive(Activity activity,  String liveShowID, String chatRoomId, String rtmp) {
        if ( TextUtils.isEmpty(rtmp)) {
            ToastUtils.shortToast("服务端问题，直播条目 服务端接口未返回直播源头数据");
            return;
        }

        if (TextUtils.isEmpty(liveShowID)) {
            ToastUtils.shortToast("服务端问题，接口返回直播室id为空");
            return;
        }

        if (TextUtils.isEmpty(chatRoomId)) {
            ToastUtils.shortToast("服务端问题，接口返回环信聊天室id为空");
            return;
        }

        //  2017/3/2 跳转直播回放界面
       // LiveManager.getInstance().watchLive(activity, liveShowID, chatRoomId, rtmp);
        return;
    }

    public static void watchLiveRecord(Activity activity,  String liveShowID, String chatRoomId, List<String> vod_list) {
        if (null==vod_list||vod_list.isEmpty()) {
            ToastUtils.shortToast("服务端问题，回放条目 服务端接口未返回直播源头数据");
            return;
        }

        if (TextUtils.isEmpty(liveShowID)) {
            ToastUtils.shortToast("服务端问题，接口返回直播室id为空");
            return;
        }

        if (TextUtils.isEmpty(chatRoomId)) {
            ToastUtils.shortToast("服务端问题，接口返回环信聊天室id为空");
            return;
        }

        //  2017/3/2 跳转直播回放界面
      // LiveManager.getInstance().watchLiveRecord(activity, liveShowID,chatRoomId,vod_list);
    }
}
