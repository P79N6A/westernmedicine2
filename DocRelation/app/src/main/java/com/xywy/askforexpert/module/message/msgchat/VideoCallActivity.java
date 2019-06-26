/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xywy.askforexpert.module.message.msgchat;

import android.hardware.Camera;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMCallManager;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.media.EMLocalSurfaceView;
import com.hyphenate.media.EMOppositeSurfaceView;
import com.hyphenate.util.PathUtil;
import com.xywy.askforexpert.R;
import com.xywy.easeWrapper.HXSDKHelper;

import java.util.UUID;

public class VideoCallActivity extends CallActivity implements OnClickListener {

    private boolean isMuteState;
    private boolean isHandsfreeState;
    private boolean isAnswered;
    private boolean endCallTriggerByMe = false;
    private boolean monitor = true;

    private TextView callStateTextView;

    private LinearLayout comingBtnContainer;
    private Button refuseBtn;
    private Button answerBtn;
    private Button hangupBtn;
    private ImageView muteImage;
    private ImageView handsFreeImage;
    private TextView nickTextView;
    private Chronometer chronometer;
    private LinearLayout voiceContronlLayout;
    private RelativeLayout rootContainer;
    private RelativeLayout btnsContainer;
    private LinearLayout topContainer;
    private LinearLayout bottomContainer;
    private TextView monitorTextView;
    private TextView netwrokStatusVeiw;

    private Handler uiHandler;

    private boolean isInCalling;
    boolean isRecording = false;
    private Button recordBtn;
    private Button switchCameraBtn;
    private SeekBar YDeltaSeekBar;
    private EMCallManager.EMVideoCallHelper callHelper;
    private Button toggleVideoBtn;

    private BrightnessDataProcess dataProcessor = new BrightnessDataProcess();

    // dynamic adjust brightness
    class BrightnessDataProcess implements EMCallManager.EMCameraDataProcessor {
        byte yDelta = 0;

        synchronized void setYDelta(byte yDelta) {
            Log.d("VideoCallActivity", "brigntness uDelta:" + yDelta);
            this.yDelta = yDelta;
        }

        // data size is width*height*2
        // the first width*height is Y, second part is UV
        // the storage layout detailed please refer 2.x demo CameraHelper.onPreviewFrame
        @Override
        public synchronized void onProcessData(byte[] data, Camera camera, int width, int height) {
            int wh = width * height;
            for (int i = 0; i < wh; i++) {
                int d = (data[i] & 0xFF) + yDelta;
                d = d < 16 ? 16 : d;
                d = d > 235 ? 235 : d;
                data[i] = (byte) d;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            finish();
            return;
        }
        setContentView(R.layout.em_activity_video_call);

        HXSDKHelper.getInstance().isVideoCalling = true;
        callType = 1;

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        uiHandler = new Handler();

        callStateTextView = (TextView) findViewById(R.id.tv_call_state);
        comingBtnContainer = (LinearLayout) findViewById(R.id.ll_coming_call);
        rootContainer = (RelativeLayout) findViewById(R.id.root_layout);
        refuseBtn = (Button) findViewById(R.id.btn_refuse_call);
        answerBtn = (Button) findViewById(R.id.btn_answer_call);
        hangupBtn = (Button) findViewById(R.id.btn_hangup_call);
        muteImage = (ImageView) findViewById(R.id.iv_mute);
        handsFreeImage = (ImageView) findViewById(R.id.iv_handsfree);
        callStateTextView = (TextView) findViewById(R.id.tv_call_state);
        nickTextView = (TextView) findViewById(R.id.tv_nick);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        voiceContronlLayout = (LinearLayout) findViewById(R.id.ll_voice_control);
        btnsContainer = (RelativeLayout) findViewById(R.id.ll_btns);
        topContainer = (LinearLayout) findViewById(R.id.ll_top_container);
        bottomContainer = (LinearLayout) findViewById(R.id.ll_bottom_container);
        monitorTextView = (TextView) findViewById(R.id.tv_call_monitor);
        netwrokStatusVeiw = (TextView) findViewById(R.id.tv_network_status);
        recordBtn = (Button) findViewById(R.id.btn_record_video);
        switchCameraBtn = (Button) findViewById(R.id.btn_switch_camera);
        YDeltaSeekBar = (SeekBar) findViewById(R.id.seekbar_y_detal);

        refuseBtn.setOnClickListener(this);
        answerBtn.setOnClickListener(this);
        hangupBtn.setOnClickListener(this);
        muteImage.setOnClickListener(this);
        handsFreeImage.setOnClickListener(this);
        rootContainer.setOnClickListener(this);
        recordBtn.setOnClickListener(this);
        switchCameraBtn.setOnClickListener(this);
        YDeltaSeekBar.setOnSeekBarChangeListener(new YDeltaSeekBarListener());

        msgid = UUID.randomUUID().toString();
        isInComingCall = getIntent().getBooleanExtra("isComingCall", false);
        username = getIntent().getStringExtra("username");

        nickTextView.setText(username);

        // local surfaceview
        localSurface = (EMLocalSurfaceView) findViewById(R.id.local_surface);
        localSurface.setZOrderMediaOverlay(true);
        localSurface.setZOrderOnTop(true);

        // remote surfaceview
        oppositeSurface = (EMOppositeSurfaceView) findViewById(R.id.opposite_surface);

        // set call state listener
        addCallStateListener();
        if (!isInComingCall) {// outgoing call
            soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
            outgoing = soundPool.load(this, R.raw.em_outgoing, 1);

            comingBtnContainer.setVisibility(View.INVISIBLE);
            hangupBtn.setVisibility(View.VISIBLE);
            String st = getResources().getString(R.string.Are_connected_to_each_other);
            callStateTextView.setText(st);
            EMClient.getInstance().callManager().setSurfaceView(localSurface, oppositeSurface);
            handler.sendEmptyMessage(MSG_CALL_MAKE_VIDEO);
        } else { // incoming call
            voiceContronlLayout.setVisibility(View.INVISIBLE);
            localSurface.setVisibility(View.INVISIBLE);
            Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            audioManager.setMode(AudioManager.MODE_RINGTONE);
            audioManager.setSpeakerphoneOn(true);
            ringtone = RingtoneManager.getRingtone(this, ringUri);
            ringtone.play();
            EMClient.getInstance().callManager().setSurfaceView(localSurface, oppositeSurface);
        }

        // get instance of call helper, should be called after setSurfaceView was called
        callHelper = EMClient.getInstance().callManager().getVideoCallHelper();

        EMClient.getInstance().callManager().setCameraDataProcessor(dataProcessor);
    }

    class YDeltaSeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            dataProcessor.setYDelta((byte) (20.0f * (progress - 50) / 50.0f));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

    }

    /**
     * set call state listener
     */
    void addCallStateListener() {
        callStateListener = new EMCallStateChangeListener() {

            @Override
            public void onCallStateChanged(CallState callState, final CallError error) {
                switch (callState) {

                    case CONNECTING: // is connecting
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                callStateTextView.setText(R.string.Are_connected_to_each_other);
                            }

                        });
                        break;
                    case CONNECTED: // connected
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                callStateTextView.setText(R.string.have_connected_with);
                            }

                        });
                        break;

                    case ACCEPTED: // call is accepted
                        handler.removeCallbacks(timeoutHangup);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    if (soundPool != null)
                                        soundPool.stop(streamID);
                                } catch (Exception e) {
                                }
                                openSpeakerOn();
                                ((TextView) findViewById(R.id.tv_is_p2p)).setText(EMClient.getInstance().callManager().isDirectCall()
                                        ? R.string.direct_call : R.string.relay_call);
                                handsFreeImage.setImageResource(R.drawable.em_icon_speaker_on);
                                isHandsfreeState = true;
                                isInCalling = true;
                                chronometer.setVisibility(View.VISIBLE);
                                chronometer.setBase(SystemClock.elapsedRealtime());
                                // call durations start
                                chronometer.start();
                                nickTextView.setVisibility(View.INVISIBLE);
                                callStateTextView.setText(R.string.In_the_call);
                                recordBtn.setVisibility(View.VISIBLE);
                                callingState = CallingState.NORMAL;
                                startMonitor();
                            }

                        });
                        break;
                    case NETWORK_UNSTABLE:
                        runOnUiThread(new Runnable() {
                            public void run() {
                                netwrokStatusVeiw.setVisibility(View.VISIBLE);
                                if (error == CallError.ERROR_NO_DATA) {
                                    netwrokStatusVeiw.setText(R.string.no_call_data);
                                } else {
                                    netwrokStatusVeiw.setText(R.string.network_unstable);
                                }
                            }
                        });
                        break;
                    case NETWORK_NORMAL:
                        runOnUiThread(new Runnable() {
                            public void run() {
                                netwrokStatusVeiw.setVisibility(View.INVISIBLE);
                            }
                        });
                        break;
                    case VIDEO_PAUSE:
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "VIDEO_PAUSE", 0).show();
                            }
                        });
                        break;
                    case VIDEO_RESUME:
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "VIDEO_RESUME", 0).show();
                            }
                        });
                        break;
                    case VOICE_PAUSE:
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "VOICE_PAUSE", 0).show();
                            }
                        });
                        break;
                    case VOICE_RESUME:
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "VOICE_RESUME", 0).show();
                            }
                        });
                        break;
                    case DISCONNNECTED: // call is disconnected
                        handler.removeCallbacks(timeoutHangup);
                        final CallError fError = error;
                        runOnUiThread(new Runnable() {
                            private void postDelayedCloseMsg() {
                                uiHandler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        saveCallRecord();
                                        Animation animation = new AlphaAnimation(1.0f, 0.0f);
                                        animation.setDuration(800);
                                        rootContainer.startAnimation(animation);
                                        finish();
                                    }

                                }, 200);
                            }

                            @Override
                            public void run() {
                                chronometer.stop();
                                callDruationText = chronometer.getText().toString();
                                String s1 = getResources().getString(R.string.The_other_party_refused_to_accept);
                                String s2 = getResources().getString(R.string.Connection_failure);
                                String s3 = getResources().getString(R.string.The_other_party_is_not_online);
                                String s4 = getResources().getString(R.string.The_other_is_on_the_phone_please);
                                String s5 = getResources().getString(R.string.The_other_party_did_not_answer);

                                String s6 = getResources().getString(R.string.hang_up);
                                String s7 = getResources().getString(R.string.The_other_is_hang_up);
                                String s8 = getResources().getString(R.string.did_not_answer);
                                String s9 = getResources().getString(R.string.Has_been_cancelled);

                                if (fError == CallError.REJECTED) {
                                    callingState = CallingState.BEREFUESD;
                                    callStateTextView.setText(s1);
                                } else if (fError == CallError.ERROR_TRANSPORT) {
                                    callStateTextView.setText(s2);
                                    //stone 修改 环信版本升级3.1.4->3.1.5
//                                } else if (fError == CallError.ERROR_INAVAILABLE) {
                                } else if (fError == CallError.ERROR_UNAVAILABLE) {
                                    callingState = CallingState.OFFLINE;
                                    callStateTextView.setText(s3);
                                } else if (fError == CallError.ERROR_BUSY) {
                                    callingState = CallingState.BUSY;
                                    callStateTextView.setText(s4);
                                } else if (fError == CallError.ERROR_NORESPONSE) {
                                    callingState = CallingState.NORESPONSE;
                                    callStateTextView.setText(s5);
                                    //stone 修改 环信版本升级3.1.4->3.1.5
//                                }else if (fError == CallError.ERROR_LOCAL_VERSION_SMALLER || fError == CallError.ERROR_PEER_VERSION_SMALLER){
                                } else if (fError == CallError.ERROR_LOCAL_SDK_VERSION_OUTDATED || fError == CallError.ERROR_REMOTE_SDK_VERSION_OUTDATED) {
                                    callingState = CallingState.VERSION_NOT_SAME;
                                    callStateTextView.setText(R.string.call_version_inconsistent);
                                } else {
                                    if (isAnswered) {
                                        callingState = CallingState.NORMAL;
                                        if (endCallTriggerByMe) {
//                                        callStateTextView.setText(s6);
                                        } else {
                                            callStateTextView.setText(s7);
                                        }
                                    } else {
                                        if (isInComingCall) {
                                            callingState = CallingState.UNANSWERED;
                                            callStateTextView.setText(s8);
                                        } else {
                                            if (callingState != CallingState.NORMAL) {
                                                callingState = CallingState.CANCED;
                                                callStateTextView.setText(s9);
                                            } else {
                                                callStateTextView.setText(s6);
                                            }
                                        }
                                    }
                                }
                                postDelayedCloseMsg();
                            }

                        });

                        break;

                    default:
                        break;
                }

            }
        };
        EMClient.getInstance().callManager().addCallStateChangeListener(callStateListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refuse_call: // decline the call
                refuseBtn.setEnabled(false);
                handler.sendEmptyMessage(MSG_CALL_REJECT);
                break;

            case R.id.btn_answer_call: // answer the call
                answerBtn.setEnabled(false);
                openSpeakerOn();
                if (ringtone != null)
                    ringtone.stop();

                callStateTextView.setText("answering...");
                handler.sendEmptyMessage(MSG_CALL_ANSWER);
                handsFreeImage.setImageResource(R.drawable.em_icon_speaker_on);
                isAnswered = true;
                isHandsfreeState = true;
                comingBtnContainer.setVisibility(View.INVISIBLE);
                hangupBtn.setVisibility(View.VISIBLE);
                voiceContronlLayout.setVisibility(View.VISIBLE);
                localSurface.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_hangup_call: // hangup
                hangupBtn.setEnabled(false);
                chronometer.stop();
                endCallTriggerByMe = true;
                callStateTextView.setText(getResources().getString(R.string.hanging_up));
                if (isRecording) {
                    callHelper.stopVideoRecord();
                }
                handler.sendEmptyMessage(MSG_CALL_END);
                break;

            case R.id.iv_mute: // mute
                if (isMuteState) {
                    // resume voice transfer
                    muteImage.setImageResource(R.drawable.em_icon_mute_normal);
                    EMClient.getInstance().callManager().resumeVoiceTransfer();
                    isMuteState = false;
                } else {
                    // pause voice transfer
                    muteImage.setImageResource(R.drawable.em_icon_mute_on);
                    EMClient.getInstance().callManager().pauseVoiceTransfer();
                    isMuteState = true;
                }
                break;
            case R.id.iv_handsfree: // handsfree
                if (isHandsfreeState) {
                    // turn off speaker
                    handsFreeImage.setImageResource(R.drawable.em_icon_speaker_normal);
                    closeSpeakerOn();
                    isHandsfreeState = false;
                } else {
                    handsFreeImage.setImageResource(R.drawable.em_icon_speaker_on);
                    openSpeakerOn();
                    isHandsfreeState = true;
                }
                break;
            case R.id.btn_record_video: //record the video
                if (!isRecording) {
                    callHelper.startVideoRecord(PathUtil.getInstance().getVideoPath().getAbsolutePath());
                    isRecording = true;
                    recordBtn.setText(R.string.stop_record);
                } else {
                    String filepath = callHelper.stopVideoRecord();
                    isRecording = false;
                    recordBtn.setText(R.string.recording_video);
                    Toast.makeText(getApplicationContext(), String.format(getString(R.string.record_finish_toast), filepath), 1).show();
                }
                break;
            case R.id.root_layout:
                if (callingState == CallingState.NORMAL) {
                    if (bottomContainer.getVisibility() == View.VISIBLE) {
                        bottomContainer.setVisibility(View.GONE);
                        topContainer.setVisibility(View.GONE);

                    } else {
                        bottomContainer.setVisibility(View.VISIBLE);
                        topContainer.setVisibility(View.VISIBLE);

                    }
                }
                break;
            case R.id.btn_switch_camera: //switch camera
                handler.sendEmptyMessage(MSG_CALL_SWITCH_CAMERA);
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        HXSDKHelper.getInstance().isVideoCalling = false;
        stopMonitor();
        if (isRecording) {
            callHelper.stopVideoRecord();
            isRecording = false;
        }
        localSurface = null;
        oppositeSurface = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        callDruationText = chronometer.getText().toString();
        super.onBackPressed();
    }

    /**
     * for debug & testing, you can remove this when release
     */
    void startMonitor() {
        new Thread(new Runnable() {
            public void run() {
                while (monitor) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //stone 修改 环信版本升级3.1.4->3.1.5
//                            monitorTextView.setText("WidthxHeight："+callHelper.getVideoWidth()+"x"+callHelper.getVideoHeight()
//                                    + "\nDelay：" + callHelper.getVideoTimedelay()
//                                    + "\nFramerate：" + callHelper.getVideoFramerate()
//                                    + "\nLost：" + callHelper.getVideoLostcnt()
//                                    + "\nLocalBitrate：" + callHelper.getLocalBitrate()
//                                    + "\nRemoteBitrate：" + callHelper.getRemoteBitrate());
                            monitorTextView.setText("WidthxHeight：" + callHelper.getVideoWidth() + "x" + callHelper.getVideoHeight()
//                                    + "\nDelay：" + callHelper.getVideoTimeDelay()
                                    + "\nFramerate：" + callHelper.getVideoFrameRate()
                                    + "\nLost：" + callHelper.getVideoLostRate()
                                    + "\nLocalBitrate：" + callHelper.getLocalBitrate()
                                    + "\nRemoteBitrate：" + callHelper.getRemoteBitrate());

                        }
                    });
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }

    void stopMonitor() {
        monitor = false;
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (isInCalling) {
            EMClient.getInstance().callManager().pauseVideoTransfer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isInCalling) {
            EMClient.getInstance().callManager().resumeVideoTransfer();
        }
    }

}

//    private SurfaceView localSurface;
//    private SurfaceHolder localSurfaceHolder;
//    private static SurfaceView oppositeSurface;
//    private SurfaceHolder oppositeSurfaceHolder;
//
//    private boolean isMuteState;
//    private boolean isHandsfreeState;
//    private boolean isAnswered;
//    private int streamID;
//    private boolean endCallTriggerByMe = false;
//    private boolean monitor = true;
//    private ImageView to_img;
//    EMCallManager.EMVideoCallHelper callHelper;
//    private TextView callStateTextView;
//
//    private Handler handler = new Handler();
//    private LinearLayout comingBtnContainer;
//    private Button refuseBtn;
//    private Button answerBtn;
//    private Button hangupBtn;
//    private ImageView muteImage;
//    private ImageView handsFreeImage, to_head_img;
//    private TextView nickTextView, text_info, tv_go_call;
//    String toImg;
//    private Chronometer chronometer;
//    private LinearLayout voiceContronlLayout, linear_layout;
//    private RelativeLayout rootContainer;
//    private RelativeLayout btnsContainer;
//    private EMCameraHelper cameraHelper;
//    private LinearLayout topContainer;
//    private LinearLayout bottomContainer;
//    //    private TextView monitorTextView;
//    private TextView netwrokStatusVeiw;
//    View  recevie_call;
//    boolean isRecording = false;
//    boolean Defult = true;
//    FrameLayout fm_top;
//    LinearLayout ll_surface_baseline;
//    /**
//     * 正在通话中
//     */
//    private boolean isInCalling;
//    private ACache mCache;
//    AddressBook pListInfo1;
//    FinalBitmap fb;
//    String userRealName;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(savedInstanceState != null) {
//            finish();
//            return;
//        }
//        initSystemBar();
//        mCache = ACache.get(this);
//        setContentView(R.layout.em_activity_video_call);
//        String did = DPApplication.getLoginInfo().getData().getPid();
//        pListInfo1 = (AddressBook) mCache.getAsObject("card" + did);
//        fb = FinalBitmap.create(VideoCallActivity.this, false);
//        getWindow().setFormat(PixelFormat.TRANSLUCENT);
//        //        DemoHelper.getInstance().isVideoCalling = true;
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//
//        recevie_call = (View) findViewById(R.id.receive_call);
//        callStateTextView = (TextView) findViewById(R.id.tv_call_state);
//        comingBtnContainer = (LinearLayout) findViewById(R.id.ll_coming_call);
//        rootContainer = (RelativeLayout) findViewById(R.id.root_layout);
//        refuseBtn = (Button) findViewById(R.id.btn_refuse_call);
//        answerBtn = (Button) findViewById(R.id.btn_answer_call);
//        hangupBtn = (Button) findViewById(R.id.btn_hangup_call);
//        muteImage = (ImageView) findViewById(R.id.iv_mute);
//        to_img = (ImageView) findViewById(R.id.to_img);
//        handsFreeImage = (ImageView) findViewById(R.id.iv_handsfree);
//        callStateTextView = (TextView) findViewById(R.id.tv_call_state);
//        nickTextView = (TextView) findViewById(R.id.tv_nick);
//        chronometer = (Chronometer) findViewById(R.id.chronometer);
//        voiceContronlLayout = (LinearLayout) findViewById(R.id.ll_voice_control);
//        //        btnsContainer = (RelativeLayout) findViewById(R.id.ll_btns);
//        topContainer = (LinearLayout) findViewById(R.id.ll_top_container);
//        bottomContainer = (LinearLayout) findViewById(R.id.ll_bottom_container);
//
//        linear_layout = (LinearLayout) findViewById(R.id.linear_layout);
//
//
//        to_head_img = (ImageView) findViewById(R.id.to_head_img);
//        text_info = (TextView) findViewById(R.id.text_info);
//        tv_go_call = (TextView) findViewById(R.id.tv_go_call);
//
//        //        monitorTextView = (TextView) findViewById(R.id.tv_call_monitor);
//        netwrokStatusVeiw = (TextView) findViewById(R.id.tv_network_status);
//        fm_top = (FrameLayout) findViewById(R.id.fam);
//        ll_surface_baseline = (LinearLayout) findViewById(R.id.ll_surface_baseline);
//        refuseBtn.setOnClickListener(this);
//        answerBtn.setOnClickListener(this);
//        hangupBtn.setOnClickListener(this);
//        muteImage.setOnClickListener(this);
//        handsFreeImage.setOnClickListener(this);
//        rootContainer.setOnClickListener(this);
//
//        msgid = UUID.randomUUID().toString();
//        // 获取通话是否为接收方向的
//        isInComingCall = getIntent().getBooleanExtra("isComingCall", false);
//        username = getIntent().getStringExtra("username");
//        toHeadImge = getIntent().getStringExtra("headimg");
//        toRealname = getIntent().getStringExtra("realname");
//
//        //        DemoModel dm = DemoHelper.getInstance().getModel();
//        //        if(dm.isAdaptiveVideoEncode()){
//        //        	EMChatManager.getInstance().setAdaptiveVideoFlag(true);
//        //        }else{
//        //        	EMChatManager.getInstance().setAdaptiveVideoFlag(false);
//        //        }
//
//        // 设置通话人
//
//        if(pListInfo1 != null && pListInfo1.getData() != null && pListInfo1.getData().size() > 0) {
//            int size = pListInfo1.getData().size();
//            for(int i = 0; i < size; i++) {
//                AddressBook data = pListInfo1.getData().get(i);
//                if(username.equals(data.getHxusername())) {
//                    userRealName = data.getRealname();
//                    toImg = data.getHeader();
//                    break;
//                }
//            }
//
//        }
//        if(TextUtils.isEmpty(userRealName)) {
//            userRealName = "用户" + username;
//        }
//
//
//        // 显示本地图像的surfaceview
//        localSurface = (SurfaceView) findViewById(R.id.local_surface);
//        localSurface.setZOrderMediaOverlay(true);
//        localSurface.setZOrderOnTop(true);
//        localSurfaceHolder = localSurface.getHolder();
//
//        // 获取callHelper,cameraHelper
//        callHelper = EMCallManager.EMVideoCallHelper.getInstance();
//        cameraHelper = new CameraHelper(callHelper, localSurfaceHolder);//
//
//        // 显示对方图像的surfaceview
//        oppositeSurface = (SurfaceView) findViewById(R.id.opposite_surface);
//        oppositeSurfaceHolder = oppositeSurface.getHolder();
//        oppositeSurface.setZOrderMediaOverlay(false);
//        oppositeSurface.setZOrderOnTop(false);
//
//        oppositeSurface.setOnClickListener(this);
//        localSurface.setOnClickListener(this);
//
//        // 设置显示对方图像的surfaceview
//        callHelper.setSurfaceView(oppositeSurface);
//
//        localSurfaceHolder.addCallback(new LocalCallback());
//        oppositeSurfaceHolder.addCallback(new OppositeCallback());
//
//        // 设置通话监听
//        addCallStateListener();
//        if(!isInComingCall) {// 拨打电话
//
//            if(!TextUtils.isEmpty(toImg)) {
//                linear_layout.setVisibility(View.VISIBLE);
//                fb.display(to_head_img, toImg);
//                text_info.setText("正在向" + userRealName + "发起视频请求");
//            }
//
//            tv_go_call.setText(getResources().getString(R.string.tv_go_call_info));
//            recevie_call.setVisibility(View.GONE);
//            soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
//            outgoing = soundPool.load(this, R.raw.em_outgoing, 1);
//
//            comingBtnContainer.setVisibility(View.INVISIBLE);
//            hangupBtn.setVisibility(View.VISIBLE);
//            String st = getResources().getString(R.string.Are_connected_to_each_other);
//            callStateTextView.setText(st);
//
//            handler.postDelayed(new Runnable() {
//                public void run() {
//                    streamID = playMakeCallSounds();
//                }
//            }, 300);
//            localSurface.setVisibility(View.VISIBLE);
//            SwitchImage(!Defult);
//        } else { // 有电话进来
//            tv_go_call.setText("我是" + userRealName + ", 快来跟我视频聊天吧");
//            nickTextView.setText(userRealName);
//            if(!TextUtils.isEmpty(toImg)) {
//                to_img.setVisibility(View.VISIBLE);
//                fb.display(to_img, toImg);
//            }
//            recevie_call.setVisibility(View.VISIBLE);
//            voiceContronlLayout.setVisibility(View.VISIBLE);
//            //            localSurface.setVisibility(View.INVISIBLE);
//            Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//            audioManager.setMode(AudioManager.MODE_RINGTONE);
//            audioManager.setSpeakerphoneOn(true);
//            ringtone = RingtoneManager.getRingtone(this, ringUri);
//            ringtone.play();
//        }
//    }
//
//    public void SwitchImage(boolean isDefult) {
//
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DensityUtil.dip2px(this, 80), DensityUtil.dip2px(this, 105));
//        layoutParams.addRule(RelativeLayout.BELOW, R.id.fam);
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        layoutParams.topMargin = DensityUtil.dip2px(this, 40);
//        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        //        layoutParams2.addRule(R.id.fam,RelativeLayout.BELOW);
//        //        layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        if(isDefult) {
//
//
////            oppositeSurface.setZ(0F);
////            localSurface.setZ(12f);
////            fm_top.setZ(0f);
//            rootContainer.removeView(localSurface);
//            rootContainer.removeView(oppositeSurface);
//            oppositeSurface.setLayoutParams(layoutParams2);
//            localSurface.setLayoutParams(layoutParams);
//            rootContainer.addView(oppositeSurface, 0);
//            rootContainer.addView(localSurface, 2);
//            oppositeSurface.setZOrderMediaOverlay(false);
//            oppositeSurface.setZOrderOnTop(false);
//            localSurface.setZOrderMediaOverlay(true);
//            localSurface.setZOrderOnTop(true);
//
//        } else {
//
//
//            rootContainer.removeView(oppositeSurface);
//            rootContainer.removeView(localSurface);
//
//            localSurface.setLayoutParams(layoutParams2);
//            oppositeSurface.setLayoutParams(layoutParams);
//            rootContainer.addView(localSurface, 0);
//            rootContainer.addView(oppositeSurface, 2);
//            localSurface.setZOrderMediaOverlay(false);
//            localSurface.setZOrderOnTop(false);
//            oppositeSurface.setZOrderMediaOverlay(true);
//            oppositeSurface.setZOrderOnTop(true);
//
////            localSurface.setZ(0F);
////            oppositeSurface.setZ(12f);
////            fm_top.setZ(12f);
//
//        }
//        Defult = isDefult;
//    }
//
//    /**
//     * 本地SurfaceHolder callback
//     */
//    class LocalCallback implements SurfaceHolder.Callback {
//
//        @Override
//        public void surfaceCreated(SurfaceHolder holder) {
//        }
//
//        @Override
//        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            //            if(Defult) {
//            //                oppositeSurface.setZ(0F);
//            //                localSurface.setZ(1f);
//            //                fm_top.setZ(0f);
//            //            } else {
//            //                localSurface.setZ(0F);
//            //                oppositeSurface.setZ(1f);
//            //                fm_top.setZ(1f);
//            //
//            //            }
//            cameraHelper.startCapture();
//        }
//
//        @Override
//        public void surfaceDestroyed(SurfaceHolder holder) {
//        }
//    }
//
//    /**
//     * 对方SurfaceHolder callback
//     */
//    class OppositeCallback implements SurfaceHolder.Callback {
//
//        @Override
//        public void surfaceCreated(SurfaceHolder holder) {
//            holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
//            callHelper.setRenderFlag(true);
//        }
//
//        @Override
//        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            callHelper.onWindowResize(width, height, format);
//            if(!cameraHelper.isStarted()) {
//                if(!isInComingCall) {
//                    try {
//                        // 拨打视频通话
//                        EMChatManager.getInstance().makeVideoCall(username);
//                        // 通知cameraHelper可以写入数据
//                        cameraHelper.setStartFlag(true);
//                    } catch (EMServiceNotReadyException e) {
//                        //                        Toast.makeText(VideoCallActivity.this, R.string.Is_not_yet_connected_to_the_server, 1).show();
//                    }
//                }
//
//            }
//        }
//
//        @Override
//        public void surfaceDestroyed(SurfaceHolder holder) {
//            callHelper.setRenderFlag(false);
//        }
//
//    }
//
//    /**
//     * 设置通话状态监听
//     */
//    void addCallStateListener() {
//        callStateListener = new EMCallStateChangeListener() {
//
//            @Override
//            public void onCallStateChanged(CallState callState, final CallError error) {
//                // Message msg = handler.obtainMessage();
//                switch (callState) {
//
//                    case CONNECTING: // 正在连接对方
//                        runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                callStateTextView.setText(R.string.Are_connected_to_each_other);
//                            }
//
//                        });
//                        break;
//                    case CONNECTED: // 双方已经建立连接
//                        runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                callStateTextView.setText(R.string.have_connected_with);
//                            }
//
//                        });
//                        break;
//
//                    case ACCEPTED: // 电话接通成功
//                        try {
//                            if(soundPool != null) soundPool.stop(streamID);
//                        } catch (Exception e) {
//                        }
//                        openSpeakerOn();
//                        runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//
//                                setHideView();
//                                handsFreeImage.setImageResource(R.drawable.em_icon_speaker_on);
//                                isHandsfreeState = true;
//                                isInCalling = true;
//                                chronometer.setVisibility(View.VISIBLE);
//                                chronometer.setBase(SystemClock.elapsedRealtime());
//                                // 开始记时
//                                chronometer.start();
//                                nickTextView.setVisibility(View.INVISIBLE);
//                                callStateTextView.setText(R.string.In_the_call);
//                                callingState = CallingState.NORMAL;
//                                //                            startMonitor();
//                            }
//
//                        });
//                        break;
//                    case DISCONNNECTED: // 电话断了
//                        final CallError fError = error;
//                        runOnUiThread(new Runnable() {
//                            private void postDelayedCloseMsg() {
//                                handler.postDelayed(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//                                        saveCallRecord(1);
//                                        Animation animation = new AlphaAnimation(1.0f, 0.0f);
//                                        animation.setDuration(800);
//                                        rootContainer.startAnimation(animation);
//                                        finish();
//                                    }
//
//                                }, 200);
//                            }
//
//                            @Override
//                            public void run() {
//                                chronometer.stop();
//                                callDruationText = chronometer.getText().toString();
//                                String s1 = getResources().getString(R.string.The_other_party_refused_to_accept);
//                                String s2 = getResources().getString(R.string.Connection_failure);
//                                String s3 = getResources().getString(R.string.The_other_party_is_not_online);
//                                String s4 = getResources().getString(R.string.The_other_is_on_the_phone_please);
//                                String s5 = getResources().getString(R.string.The_other_party_did_not_answer);
//
//                                String s6 = getResources().getString(R.string.hang_up);
//                                String s7 = getResources().getString(R.string.The_other_is_hang_up);
//                                String s8 = getResources().getString(R.string.did_not_answer);
//                                String s9 = getResources().getString(R.string.Has_been_cancelled);
//
//                                if(fError == CallError.REJECTED) {
//                                    callingState = CallingState.BEREFUESD;
//                                    callStateTextView.setText(s1);
//                                } else if(fError == CallError.ERROR_TRANSPORT) {
//                                    callStateTextView.setText(s2);
//                                } else if(fError == CallError.ERROR_INAVAILABLE) {
//                                    callingState = CallingState.OFFLINE;
//                                    callStateTextView.setText(s3);
//                                } else if(fError == CallError.ERROR_BUSY) {
//                                    callingState = CallingState.BUSY;
//                                    callStateTextView.setText(s4);
//                                } else if(fError == CallError.ERROR_NORESPONSE) {
//                                    callingState = CallingState.NORESPONSE;
//                                    callStateTextView.setText(s5);
//                                } else if(fError == CallError.ERROR_LOCAL_VERSION_SMALLER || fError == CallError.ERROR_PEER_VERSION_SMALLER) {
//                                    callingState = CallingState.VERSION_NOT_SAME;
//                                    callStateTextView.setText(R.string.call_version_inconsistent);
//                                } else {
//                                    if(isAnswered) {
//                                        callingState = CallingState.NORMAL;
//                                        if(endCallTriggerByMe) {
//                                            //                                        callStateTextView.setText(s6);
//                                        } else {
//                                            callStateTextView.setText(s7);
//                                        }
//                                    } else {
//                                        if(isInComingCall) {
//                                            if(callingState == CallingState.REFUESD) {
//                                                callStateTextView.setText(R.string.Refused);
//                                            } else {
//                                                callingState = CallingState.UNANSWERED;
//                                                callStateTextView.setText(s8);
//                                            }
//                                        } else {
//                                            if(callingState != CallingState.NORMAL) {
//                                                callingState = CallingState.CANCED;
//                                                callStateTextView.setText(s9);
//                                            } else {
//                                                callStateTextView.setText(s6);
//                                            }
//                                        }
//                                    }
//                                }
//                                postDelayedCloseMsg();
//                            }
//
//                        });
//
//                        break;
//                    case NETWORK_UNSTABLE:
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                netwrokStatusVeiw.setVisibility(View.VISIBLE);
//                                if(error == CallError.ERROR_NO_DATA) {
//                                    netwrokStatusVeiw.setText(R.string.no_call_data);
//                                    if(isRecording) {
//                                        callHelper.stopVideoRecord();
//                                    }
//                                    try {
//                                        EMChatManager.getInstance().endCall();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                        //                                        saveCallRecord(1);
//                                        finish();
//                                    }
//
//                                } else {
//                                    netwrokStatusVeiw.setText(R.string.network_unstable);
//                                }
//                            }
//                        });
//                        break;
//                    case NETWORK_NORMAL:
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                netwrokStatusVeiw.setVisibility(View.INVISIBLE);
//                            }
//                        });
//
//                    default:
//                        break;
//                }
//
//            }
//        };
//        EMChatManager.getInstance().addCallStateChangeListener(callStateListener);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_refuse_call: // 拒绝接听
//                refuseBtn.setEnabled(false);
//                if(ringtone != null) ringtone.stop();
//                try {
//                    EMChatManager.getInstance().rejectCall();
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                    saveCallRecord(1);
//                    finish();
//                }
//                callingState = CallingState.REFUESD;
//                break;
//
//            case R.id.btn_answer_call: // 接听电话
//                setHideView();
//                answerBtn.setEnabled(false);
//                if(ringtone != null) ringtone.stop();
//                if(isInComingCall) {
//                    try {
//                        callStateTextView.setText("正在接听...");
//                        if(NetUtils.hasDataConnection(VideoCallActivity.this)) {
//                            EMChatManager.getInstance().answerCall();
//                            cameraHelper.setStartFlag(true);
//                        } else {
//                            throw new EaseMobException();
//                        }
//
//                        openSpeakerOn();
//                        handsFreeImage.setImageResource(R.drawable.em_icon_speaker_on);
//                        isAnswered = true;
//                        isHandsfreeState = true;
//                    } catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                        saveCallRecord(1);
//                        finish();
//                        return;
//                    }
//                }
//                comingBtnContainer.setVisibility(View.INVISIBLE);
//                hangupBtn.setVisibility(View.VISIBLE);
//                voiceContronlLayout.setVisibility(View.VISIBLE);
//                localSurface.setVisibility(View.VISIBLE);
//                break;
//
//            case R.id.btn_hangup_call: // 挂断电话
//                hangupBtn.setEnabled(false);
//                if(soundPool != null) soundPool.stop(streamID);
//                chronometer.stop();
//                endCallTriggerByMe = true;
//                callStateTextView.setText("挂断中");
//                if(isRecording) {
//                    callHelper.stopVideoRecord();
//                }
//                try {
//                    EMChatManager.getInstance().endCall();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    saveCallRecord(1);
//                    finish();
//                }
//                break;
//
//            case R.id.iv_mute: // 静音开关
//                if(isMuteState) {
//                    // 关闭静音
//                    muteImage.setImageResource(R.drawable.em_icon_mute_normal);
//                    EMChatManager.getInstance().resumeVoiceTransfer();
//                    isMuteState = false;
//                } else {
//                    // 打开静音
//                    muteImage.setImageResource(R.drawable.em_icon_mute_on);
//                    EMChatManager.getInstance().pauseVoiceTransfer();
//                    isMuteState = true;
//                }
//                break;
//            case R.id.iv_handsfree: // 免提开关
//                if(isHandsfreeState) {
//                    // 关闭免提
//                    handsFreeImage.setImageResource(R.drawable.em_icon_speaker_normal);
//                    closeSpeakerOn();
//                    isHandsfreeState = false;
//                } else {
//                    handsFreeImage.setImageResource(R.drawable.em_icon_speaker_on);
//                    openSpeakerOn();
//                    isHandsfreeState = true;
//                }
//                break;
//            //            case R.id.btn_record_video: //视频录制
//            //                //            if(!isRecording){
//            //                //                callHelper.startVideoRecord(PathUtil.getInstance().getVideoPath().getAbsolutePath());
//            //                //                isRecording = true;
//            //                //                recordBtn.setText(R.string.stop_record);
//            //                //            }else{
//            //                //                String filepath = callHelper.stopVideoRecord();
//            //                //                isRecording = false;
//            //                //                recordBtn.setText("sssss");
//            //                //                Toast.makeText(getApplicationContext(), String.format(getString(R.string.record_finish_toast), filepath), 1).show();
//            //                //            }
//            //                SwitchImage(!Defult);
//            //                break;
//            case R.id.root_layout:
//                hideView();
//
//                break;
//
//            case R.id.opposite_surface:
//                if(!Defult) {
//                    SwitchImage(!Defult);
//                }else{
//                    hideView();
//                }
//                break;
//            case R.id.local_surface:
//                if(Defult) {
//                    SwitchImage(!Defult);
//                }else{
//                    hideView();
//                }
//                break;
//            default:
//                break;
//        }
//
//    }
//
//    private void hideView() {
//        if(callingState == CallingState.NORMAL) {
//            if(bottomContainer.getVisibility() == View.VISIBLE) {
//                bottomContainer.setVisibility(View.GONE);
//                topContainer.setVisibility(View.GONE);
//
//            } else {
//                bottomContainer.setVisibility(View.VISIBLE);
//                topContainer.setVisibility(View.VISIBLE);
//            }
//        }
//    }
//
//    /**
//     * 隐藏view
//     */
//    private void setHideView() {
//
//        recevie_call.setVisibility(View.GONE);
//        tv_go_call.setVisibility(View.GONE);
//        linear_layout.setVisibility(View.GONE);
//        to_img.setVisibility(View.GONE);
//        localSurface.setVisibility(View.VISIBLE);
//
//        if(!Defult){
//            SwitchImage(!Defult);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        //        DemoHelper.getInstance().isVideoCalling = false;
//        stopMonitor();
//        try {
//            callHelper.setSurfaceView(null);
//            cameraHelper.stopCapture();
//            oppositeSurface = null;
//            if(isRecording) {
//                callHelper.stopVideoRecord();
//                isRecording = false;
//            }
//            cameraHelper = null;
//        } catch (Exception e) {
//        }
//        super.onDestroy();
//    }
//
//    @Override
//    public void onBackPressed() {
//        EMChatManager.getInstance().endCall();
//        callDruationText = chronometer.getText().toString();
//        saveCallRecord(1);
//        finish();
//    }
//
//    /**
//     * 方便开发测试，实际app中去掉显示即可
//     */
//    void startMonitor() {
//        //        new Thread(new Runnable() {
//        //            public void run() {
//        //                while(monitor){
//        //                    runOnUiThread(new Runnable() {
//        //                        public void run() {
//        //                            monitorTextView.setText("宽x高："+callHelper.getVideoWidth()+"x"+callHelper.getVideoHeight()
//        //                                    + "\n延迟：" + callHelper.getVideoTimedelay()
//        //                                    + "\n帧率：" + callHelper.getVideoFramerate()
//        //                                    + "\n丢包数：" + callHelper.getVideoLostcnt()
//        //                                    + "\n本地比特率：" + callHelper.getLocalBitrate()
//        //                                    + "\n对方比特率：" + callHelper.getRemoteBitrate());
//        //
//        //                        }
//        //                    });
//        //                    try {
//        //                        Thread.sleep(1500);
//        //                    } catch (InterruptedException e) {
//        //                    }
//        //                }
//        //            }
//        //        }).start();
//    }
//
//    void stopMonitor() {
//        monitor = false;
//    }
//
//    @Override
//    protected void onUserLeaveHint() {
//        super.onUserLeaveHint();
//        if(isInCalling) {
//            EMChatManager.getInstance().pauseVideoTransfer();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(isInCalling) {
//            EMChatManager.getInstance().resumeVideoTransfer();
//        }
//    }
//
//    private void initSystemBar() {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//        }
//
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintColor(Color.TRANSPARENT);
//    }
//
//
//    @TargetApi(19)
//    private void setTranslucentStatus(boolean on) {
//        Window win = getWindow();
//        WindowManager.LayoutParams winParams = win.getAttributes();
//        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        if(on) {
//            winParams.flags |= bits;
//        } else {
//            winParams.flags &= ~bits;
//        }
//        win.setAttributes(winParams);
//    }
//}
