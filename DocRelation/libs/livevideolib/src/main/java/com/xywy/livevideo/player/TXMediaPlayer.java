package com.xywy.livevideo.player;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;


/**
 * 播放器接口的实现类，基于腾讯云的sdk
 * Created by zhangzheng on 2017/2/15.
 */
public class TXMediaPlayer {

    private Context context;

    //播放类型，rtmp直播、rtmp点播、flv直播等
    private int type = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;

    //播放的view
    private TXCloudVideoView mPlayerView;

    //腾讯播放器的实例
    private TXLivePlayer mLivePlayer;

    //硬件加速
    private boolean isHardwareUsed = false;

    //视频平铺模式
    private int renderMode = TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN;

    private ITXLivePlayListener playListener;

    public TXMediaPlayer(Context context) {
        this.context = context;
        //初始化腾讯播放器的view
        mPlayerView = new TXCloudVideoView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPlayerView.setLayoutParams(params);

        //初始化播放器，
        mLivePlayer = new TXLivePlayer(context);
        mLivePlayer.enableHardwareDecode(isHardwareUsed);
        mLivePlayer.setRenderMode(renderMode);

        TXLivePlayConfig mPlayConfig = new TXLivePlayConfig();
        //自动模式
        mPlayConfig.setAutoAdjustCacheTime(true);
        mPlayConfig.setMinAutoAdjustCacheTime(1);
        mPlayConfig.setMaxAutoAdjustCacheTime(5);
        mLivePlayer.setConfig(mPlayConfig);

    }

    public boolean checkUrl(String url) {
        return !(url == null);
    }

    public boolean start(String url) {
        if (!checkUrl(url)) {
            return false;
        }
        mLivePlayer.setPlayerView(mPlayerView);
        if (type==XYMediaPlayerView.PLAYER_TYPE_REALTIME) {
            if(url.startsWith("rtmp")) {
                mLivePlayer.startPlay(url, TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
            }
            if(url.endsWith(".flv")){
                mLivePlayer.startPlay(url, TXLivePlayer.PLAY_TYPE_LIVE_FLV);
            }
        } else if (type==XYMediaPlayerView.PLAYER_TYPE_REPLAY) {
            mLivePlayer.startPlay(url, TXLivePlayer.PLAY_TYPE_VOD_FLV);
        }

        return true;
    }

    public boolean restart(String url) {
        return start(url);
    }

    public boolean seekProgress(int progress) {
        mLivePlayer.seek(progress);
        return true;
    }

    public void pause() {
        mLivePlayer.pause();
    }

    public void resume() {
        mLivePlayer.resume();
    }

    //停止，释放资源
    public void stop() {
        if (mLivePlayer != null) {
            mLivePlayer.setPlayListener(null);
            mLivePlayer.stopPlay(true);
        }
        if (mPlayerView != null) {
            mPlayerView.onDestroy();
        }
    }

    public View getView() {
        return mPlayerView;
    }

    //设置点播或直播
    public void setPlayMode(int mode) {
        type = mode;
    }

    //设置状态监听
    public void setPlayListener(ITXLivePlayListener livePlayListener) {
        if (mLivePlayer != null) {
            mLivePlayer.setPlayListener(livePlayListener);
        }
    }

    public boolean isPlaying() {
        return mLivePlayer.isPlaying();
    }
}
