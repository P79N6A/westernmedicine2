package com.letv.skin.v4;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lecloud.leutils.ReUtils;
import com.letv.controller.interfacev1.ISplayerController;
import com.letv.skin.BaseView;
import com.letv.skin.utils.UIPlayContext;
import com.letv.universal.iplay.ISplayer;
import com.letv.universal.notice.UIObserver;

import java.util.Observable;

/**
 * 顶部浮层
 *
 * @author pys
 */
public class V4TopTitleView extends BaseView implements UIObserver {

//    private ImageView netStateView;
//    private ImageView batteryView;
//    private TextView timeView;
//    private ImageView videoLock;
    /**
     * lockFlag 为false 表示不加锁
     */
    private boolean lockFlag = false;
    private TextView videoTitle;

    public V4TopTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4TopTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4TopTitleView(Context context) {
        super(context);
    }

    @Override
    protected void initPlayer() {
        player.attachObserver(this);
        setState();
    }

    @Override
    public void attachUIContext(UIPlayContext playContext) {
        super.attachUIContext(playContext);
        if (uiPlayContext != null) {
            if (uiPlayContext.isPlayingAd()) {
                setVisibility(View.GONE);
            } else {
                setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 设置状态栏
     */
    private void setState() {
        /**
         * 播放器可用的时候试着获取标题
         */
        if (uiPlayContext != null) {
            /**
             * 设置标题
             */
            String title = uiPlayContext.getVideoTitle();
            if (title != null) {
                Log.d("videoTitle", title);
                videoTitle.setText(title);
            }

//            /**
//             * 获取网络状态
//             */
//            netStateView.setImageLevel(StatusUtils.getWiFistate(context));
//            /**
//             * 电池
//             */
//            batteryView.setImageLevel(StatusUtils.getBatteryStatus(context));
//            /**
//             * 时间
//             */
//            timeView.setText(StatusUtils.getCurrentTime(context));
        }
    }

    @Override
    protected void initView(final Context context) {
        LayoutInflater.from(context).inflate(ReUtils.getLayoutId(context, "letv_skin_v4_top_layout"), this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
//            layoutParams.height = dip2px(context, 48) + getStatusBarHeight(context);
//            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            toolbar.setLayoutParams(layoutParams);
//            toolbar.setPadding(0, getStatusBarHeight(context), 0, 0);
//        }
//        videoLock = (ImageView) findViewById(ReUtils.getId(context, "iv_video_lock"));
        /**
         * 返回键
         */
        findViewById(ReUtils.getId(context, "full_back")).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("full_back", "is uiPlayContext null? " + (uiPlayContext == null));
                if (uiPlayContext != null) {
                    /**
                     * 返回键是否恢复竖屏状态
                     */
                    Log.d("full_back", "is uiPlayContext return? " + uiPlayContext.isReturnback());
                    if (uiPlayContext.isReturnback()) {
                        Log.d("full_back", "is player null? " + (player == null));
                        if (player != null) {
                            // 返回竖屏状态
                            player.setScreenResolution(ISplayerController.SCREEN_ORIENTATION_USER_PORTRAIT);
                        }
                    } else {
                        ((Activity) context).finish();
                    }
                }
            }
        });

//        videoLock.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lockFlag = !lockFlag;
//                if (lockFlag) {
//                    videoLock.setImageResource(ReUtils.getDrawableId(context, "letv_skin_v4_large_mult_live_action_lock"));
//                } else {
//                    videoLock.setImageResource(ReUtils.getDrawableId(context, "letv_skin_v4_large_mult_live_action_unlock"));
//                }
//                uiPlayContext.setLockFlag(lockFlag);
//            }
//        });

//        netStateView = (ImageView) findViewById(ReUtils.getId(context, "full_net"));
//        batteryView = (ImageView) findViewById(ReUtils.getId(context, "full_battery"));
//        timeView = (TextView) findViewById(ReUtils.getId(context, "full_time"));
        videoTitle = (TextView) findViewById(ReUtils.getId(context, "video_title"));
        videoTitle.setSelected(true);
    }

    @Override
    public void update(Observable observable, Object data) {
        Bundle bundle = (Bundle) data;
        switch (bundle.getInt("state")) {
            case ISplayer.MEDIA_EVENT_PREPARE_COMPLETE:
                setState();
                break;

            case ISplayer.PLAYER_EVENT_PREPARE:
            case ISplayer.PLAYER_EVENT_PREPARE_VIDEO_VIEW:
                String title = uiPlayContext.getVideoTitle();
                if (title != null)
                    this.videoTitle.setText("即将播放：" + title);
                break;

            default:
                break;
        }
    }

    public static int dip2px(Context context, float f) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (f * scale + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);

        return statusBarHeight;
    }

}
