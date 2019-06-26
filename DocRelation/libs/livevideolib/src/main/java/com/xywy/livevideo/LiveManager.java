package com.xywy.livevideo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.xywy.livevideo.activity.RechargeActivity;
import com.xywy.livevideo.chat.model.LiveChatClient;
import com.xywy.livevideo.common_interface.Constant;
import com.xywy.livevideo.common_interface.IDataRequest;
import com.xywy.livevideo.player.VideoBroadcastActivity;
import com.xywy.livevideo.player.XYPlayerActivity;
import com.xywy.livevideo.publisher.LiveHostActivity;
import com.xywy.util.AppUtils;

import java.util.List;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/2/24 15:36
 * 外部主工程使用直播库的入口类，facade模式
 */

public class LiveManager {
    public static LiveManager getInstance() {
        return instance;
    }

    private static LiveManager instance = new LiveManager();


    private LiveManager() {

    }

    public IDataRequest getRequest() {
        return config.request;
    }

    private LiveConfig config;

    /**
     * 初始化
     * LiveConfig config= new LiveConfig.Builder()
     * .setRequest(request)
     * .setUserId(FakeHostData.userId)
     * .setUserName(FakeHostData.username)
     * .build();
     */
    public void init(Context contxt, LiveConfig liveConfig) {
        this.config = liveConfig;
        initUrl(contxt);
        if (!TextUtils.isEmpty(config.hxPassword)) {
            LiveChatClient.getInstance().init((Application) contxt.getApplicationContext());
            LiveChatClient.getInstance().login(config.hxUserName, config.hxPassword);
        } else {
            LiveChatClient.getInstance().addGlobalMsgListener();
        }
    }

    public static void initUrl(Context context) {
        if (AppUtils.isTestServer(context)) {
            Constant.PRIMARY_URL = Constant.testApiUrl;
            Constant.H5_URL = Constant.testH5Url;
        } else {
            Constant.PRIMARY_URL = Constant.formalApiUrl;
            Constant.H5_URL = Constant.formalH5Url;
        }
    }

    public LiveConfig getConfig() {
        return config;
    }

    /**
     * 主播开始直播
     */
    public void startLive(Activity activity, String rtmpUrl, String roomId, String chatRoomId) {
        LiveHostActivity.start(activity, rtmpUrl, roomId, chatRoomId);
    }

    /**
     * 主播开始直播
     */
    public void startCharge(Activity activity, @RechargeType int type) {
        RechargeActivity.start(activity, type);
    }

    public void loginHx(final String hxUsername, final String password) {
        LiveChatClient.getInstance().login(hxUsername, password);
    }

    public void logout() {
        LiveChatClient.getInstance().logOut();
    }


    public LiveChatClient getChatClient() {
        return LiveChatClient.getInstance();
    }


    /**
     * 进入房间观看直播或回放
     *
     * @param activity
     * @param roomId     房间号
     * @param rtmpVod    直播，回放的URL，rtmp或者.flv
     * @param chatRoomId 聊天室ID
     * @param type       标记直播或回放，0：直播，1：回放
     */
    public static final int TYPE_REALTIME = 1;
    public static final int TYPE_REPLAY = 0;

    public void watchLive(Context activity, String roomId, String rtmpVod, String chatRoomId, int type) {
        XYPlayerActivity.startPlayerActivity(activity, roomId, rtmpVod, type, chatRoomId, null);
    }

//    public void watchReplay(Context context, List<String> vod) {
//        XYPlayerActivity.startPlayerActivity(context, "roomId", "rtmp", TYPE_REPLAY, "chatRoomId", vod);
//    }

    public static void watchLiveRecord(Context activity, String liveShowID, String chatRoomId, List<String> vod_list) {
        XYPlayerActivity.startPlayerActivity(activity, liveShowID, "rtmp", TYPE_REPLAY, chatRoomId, vod_list);
    }

    public static void watchLive(Context activity, String liveShowID, String chatRoomId, String rtmp) {
        XYPlayerActivity.startPlayerActivity(activity, liveShowID, rtmp, TYPE_REALTIME, chatRoomId, null);
    }

    /**
     * 开始直播
     *
     * @param context
     */
    public void startBroadcast(Context context) {
        VideoBroadcastActivity.startVideoBroadcastActivity(context);
    }
}
