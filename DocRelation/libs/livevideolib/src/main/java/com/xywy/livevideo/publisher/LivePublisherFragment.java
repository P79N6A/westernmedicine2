package com.xywy.livevideo.publisher;

import android.content.ContentResolver;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.xywy.livevideolib.R;
import com.xywy.util.T;

public class LivePublisherFragment extends Fragment implements ITXLivePushListener/*, ImageReader.OnImageAvailableListener*/ {
    private static final String TAG = "rtmpPublish";
//    String mRtmpUrl = "rtmp://7958.livepush.myqcloud.com/live/7958_2a0516be5f?bizid=7958&txSecret=a4bb8cfc19ac7a2743eb01f97e1e2875&txTime=58AB127F";
   private String mRtmpUrl ;

    private TXLivePushConfig mLivePushConfig;
    private TXLivePusher mLivePusher;
    private TXCloudVideoView mCaptureView;

    private boolean mVideoPublish;
    private int mBeautyLevel = 0;
    private int mWhiteningLevel = 0;

    private RotationObserver mRotationObserver = null;

    public void switchCamara(boolean mFrontCamera) {
        if (mLivePusher!=null) {
            mLivePusher.switchCamera();
        } else {
            mLivePushConfig.setFrontCamera(mFrontCamera);
        }
    }

    public void setmBeautyLevel(int beautyLevel) {
        this.mBeautyLevel = beautyLevel;
        applyBeauty();
    }

    private void applyBeauty() {
        if (mLivePusher != null) {
            if (!mLivePusher.setBeautyFilter(mBeautyLevel, mWhiteningLevel)) {
                Toast.makeText(getActivity().getApplicationContext(), "当前机型的性能无法支持美颜功能", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setmWhiteningLevel(int whiteningLevel) {
        this.mWhiteningLevel = whiteningLevel;
        applyBeauty();
    }


    private Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLivePusher = new TXLivePusher(getActivity());
        mLivePushConfig = new TXLivePushConfig();

        mRotationObserver = new RotationObserver(new Handler());
        mRotationObserver.startObserver();
    }
    public void setRtmpUrl(String rtmpUrl){
        this.mRtmpUrl=rtmpUrl;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publish, null);


        mCaptureView = (TXCloudVideoView) view.findViewById(R.id.video_view);
        mCaptureView.disableLog(true);

        mVideoPublish = false;

        if (mLivePushConfig != null) {
            mLivePushConfig.setHardwareAcceleration(true);
        }
        if (mLivePusher != null) {
            mLivePusher.setConfig(mLivePushConfig);
        }

        mVideoPublish =startPublishRtmp();
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mCaptureView != null) {
            mCaptureView.onResume();
        }

        if (mVideoPublish && mLivePusher != null) {
            mLivePusher.resumePusher();
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCaptureView != null) {
            mCaptureView.onPause();
        }

        if (mVideoPublish && mLivePusher != null) {
            mLivePusher.pausePusher();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPublishRtmp();
        if (mCaptureView != null) {
            mCaptureView.onDestroy();
        }

        mRotationObserver.stopObserver();


    }

    private boolean startPublishRtmp() {
        FixOrAdjustBitrate();
        String rtmpUrl = "";
        String inputUrl = mRtmpUrl;
        if (!TextUtils.isEmpty(inputUrl)) {
            String url[] = inputUrl.split("###");
            if (url.length > 0) {
                rtmpUrl = url[0];
            }
        }

        if (TextUtils.isEmpty(rtmpUrl) || (!rtmpUrl.trim().toLowerCase().startsWith("rtmp://"))) {
            Toast.makeText(getActivity().getApplicationContext(), "推流地址不合法，目前支持rtmp推流!", Toast.LENGTH_SHORT).show();
            return false;
        }

        mCaptureView.setVisibility(View.VISIBLE);

        mLivePushConfig.setWatermark(decodeResource(getResources(), R.drawable.watermark), 10, 10);

        int customModeType = 0;


        mLivePushConfig.setCustomModeType(customModeType);

        mLivePushConfig.setPauseImg(300, 10);
        Bitmap bitmap = decodeResource(getResources(), R.drawable.live_pause);
        mLivePushConfig.setPauseImg(bitmap);
        mLivePushConfig.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO | TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);

        mLivePushConfig.setBeautyFilter(mBeautyLevel, mWhiteningLevel);
        mLivePusher.setConfig(mLivePushConfig);
        mLivePusher.setPushListener(this);
        mLivePusher.startCameraPreview(mCaptureView);

        mLivePusher.startPusher(rtmpUrl.trim());


        int[] ver = TXLivePusher.getSDKVersion();
        if (ver != null && ver.length >= 3) {
            Log.d(getClass().getSimpleName(), String.format("rtmp sdk version:%d.%d.%d ", ver[0], ver[1], ver[2]));
        }

//        mBtnPlay.setBackgroundResource(R.drawable.play_pause);


        return true;
    }

    private void stopPublishRtmp() {


        mVideoPublish = false;

        mLivePusher.stopCameraPreview(true);
        mLivePusher.stopScreenCapture();
        mLivePusher.setPushListener(null);
        mLivePusher.stopPusher();
        mCaptureView.setVisibility(View.GONE);


//        mBtnPlay.setBackgroundResource(R.drawable.play_start);

        if (mLivePushConfig != null) {
            mLivePushConfig.setPauseImg(null);
        }
    }

    public void FixOrAdjustBitrate() {
//        自适应分辨率
        if (mLivePusher != null) {
            mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640);
            mLivePushConfig.setAutoAdjustBitrate(true);
            mLivePushConfig.setAutoAdjustStrategy(TXLiveConstants.AUTO_ADJUST_BITRATE_STRATEGY_1);
            mLivePushConfig.setMaxVideoBitrate(1000);
            mLivePushConfig.setMinVideoBitrate(400);
            mLivePushConfig.setVideoBitrate(700);
            mLivePusher.setConfig(mLivePushConfig);
        }
//        //540p高清
//        if (mLivePusher != null) {
//            mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_540_960);
//            mLivePushConfig.setAutoAdjustBitrate(false);
//            mLivePushConfig.setVideoBitrate(1000);
//            mLivePusher.setConfig(mLivePushConfig);
//        }

    }

    @Override
    public void onPushEvent(int event, Bundle param) {
        String msg = param.getString(TXLiveConstants.EVT_DESCRIPTION);
        Log.w(TAG, msg);


        if (event < 0) {
            Toast.makeText(getActivity().getApplicationContext(), param.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();
            if (event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL) {
                stopPublishRtmp();
            }
        }

        if (event == TXLiveConstants.PUSH_ERR_NET_DISCONNECT) {
            stopPublishRtmp();
        } else if (event == TXLiveConstants.PUSH_WARNING_HW_ACCELERATION_FAIL) {
            Toast.makeText(getActivity().getApplicationContext(), param.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();
            mLivePushConfig.setHardwareAcceleration(false);

            mLivePusher.setConfig(mLivePushConfig);

        } else if (event == TXLiveConstants.PUSH_ERR_SCREEN_CAPTURE_UNSURPORT) {
            stopPublishRtmp();
        } else if (event == TXLiveConstants.PUSH_ERR_SCREEN_CAPTURE_START_FAILED) {
            stopPublishRtmp();
        } else if (event == TXLiveConstants.PUSH_EVT_CHANGE_RESOLUTION) {
            Log.d(TAG, "change resolution to " + param.getInt(TXLiveConstants.EVT_PARAM2) + ", bitrate to" + param.getInt(TXLiveConstants.EVT_PARAM1));
        } else if (event == TXLiveConstants.PUSH_EVT_CHANGE_BITRATE) {
            Log.d(TAG, "change bitrate to" + param.getInt(TXLiveConstants.EVT_PARAM1));
        }else if (event == TXLiveConstants.PUSH_WARNING_NET_BUSY ) {
            T.showShort(getActivity(),"主播网络不给力");
        }
    }

    @Override
    public void onNetStatus(Bundle status) {
        Log.d(TAG, getNetStatusString(status));
    }

    protected String getNetStatusString(Bundle status) {
        String str = String.format("%-14s %-14s %-12s\n%-14s %-14s %-12s\n%-14s %-14s %-12s\n%-14s %-12s",
                "CPU:" + status.getString(TXLiveConstants.NET_STATUS_CPU_USAGE),
                "RES:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) + "*" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT),
                "SPD:" + status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) + "Kbps",
                "JIT:" + status.getInt(TXLiveConstants.NET_STATUS_NET_JITTER),
                "FPS:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS),
                "ARA:" + status.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE) + "Kbps",
                "QUE:" + status.getInt(TXLiveConstants.NET_STATUS_CODEC_CACHE) + "|" + status.getInt(TXLiveConstants.NET_STATUS_CACHE_SIZE),
                "DRP:" + status.getInt(TXLiveConstants.NET_STATUS_CODEC_DROP_CNT) + "|" + status.getInt(TXLiveConstants.NET_STATUS_DROP_SIZE),
                "VRA:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE) + "Kbps",
                "SVR:" + status.getString(TXLiveConstants.NET_STATUS_SERVER_IP),
                "AVRA:" + status.getInt(TXLiveConstants.NET_STATUS_SET_VIDEO_BITRATE));
        return str;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        onActivityRotation();
    }

    protected void onActivityRotation() {

        int mobileRotation = this.getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
        switch (mobileRotation) {
            case Surface.ROTATION_0:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
                break;
            case Surface.ROTATION_90:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT;
                break;
            case Surface.ROTATION_270:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_LEFT;
                break;
            default:
                break;
        }
        mLivePusher.setRenderRotation(0);
        mLivePushConfig.setHomeOrientation(pushRotation);
        mLivePusher.setConfig(mLivePushConfig);
    }

    /**
     * 判断Activity是否可旋转。只有在满足以下条件的时候，Activity才是可根据重力感应自动旋转的。
     * 系统“自动旋转”设置项打开；
     *
     * @return false---Activity可根据重力感应自动旋转
     */
    protected boolean isActivityCanRotation() {

        int flag = Settings.System.getInt(this.getActivity().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        if (flag == 0) {
            return false;
        }
        return true;
    }


    private class RotationObserver extends ContentObserver {
        ContentResolver mResolver;

        public RotationObserver(Handler handler) {
            super(handler);
            mResolver = LivePublisherFragment.this.getActivity().getContentResolver();
        }


        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            if (isActivityCanRotation()) {

                onActivityRotation();
            } else {

                mLivePushConfig.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);

                mLivePusher.setRenderRotation(0);
                mLivePusher.setConfig(mLivePushConfig);
            }

        }

        public void startObserver() {
            mResolver.registerContentObserver(Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION), false, this);
        }

        public void stopObserver() {
            mResolver.unregisterContentObserver(this);
        }
    }


}