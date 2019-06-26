package com.xywy.livevideo.publisher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.xywy.base.XywyBaseActivity;
import com.xywy.base.view.MessageDialog;
import com.xywy.livevideo.LiveManager;
import com.xywy.livevideo.chat.LiveChatFragment;
import com.xywy.livevideo.common_interface.IDataResponse;
import com.xywy.livevideo.common_interface.LiveRequest;
import com.xywy.livevideo.entity.BaseData;
import com.xywy.livevideo.entity.FinishLiveBean;
import com.xywy.livevideo.player.LiveShowFinishActivity;
import com.xywy.livevideo.player.XYPlayerInfoView;
import com.xywy.livevideo.view.DialogUtil;
import com.xywy.livevideolib.R;
import com.xywy.util.T;

import java.util.HashMap;
import java.util.Map;

public class LiveHostActivity extends XywyBaseActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    public static final int LivePublish = 1;
    public static final int ChatPublish = 2;
    LivePublisherFragment livePublisherFragment;
    private XYPlayerInfoView rlHostInfo;
    private boolean mFrontCamera = true;
    private LinearLayout mFaceBeautyLayout;
    private SeekBar mBeautySeekBar;
    private SeekBar mWhiteningSeekBar;
//    private RelativeLayout rlRoot;
//    private Button mBtnFaceBeauty;

    //    Button btnChat;
    public static void start(Context context,String rtmpUrl,String roomId,String chatRoomId) {
        Intent intent = new Intent(context, LiveHostActivity.class);
        intent.putExtra("rtmpUrl",rtmpUrl);
        intent.putExtra("roomId",roomId);
        intent.putExtra("chatRoomId",chatRoomId);
        context.startActivity(intent);
    }
    private  String rtmpUrl,roomId,chatRoomId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        roomId=getIntent().getStringExtra("roomId");
        chatRoomId=getIntent().getStringExtra("chatRoomId");
        rtmpUrl=getIntent().getStringExtra("rtmpUrl");
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        livePublisherFragment.getView().setOnClickListener(this);
    }

    private void initView() {
        hideCommonBaseTitle();
        initVideoView();
        initChatView();
        rlHostInfo = (XYPlayerInfoView) findViewById(R.id.rl_host_info);
        rlHostInfo.init(XYPlayerInfoView.INFO_VIEW_TYPE_RECORD, LiveManager.getInstance().getConfig().userId,"userId",roomId,LiveManager.getInstance().getConfig().userType);
    }

    private void initChatView() {
        ImageButton btnChat = (ImageButton) findViewById(R.id.btn_chat);
        final LiveChatFragment f = new LiveChatFragment();
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f.showMsgInputBox();
            }
        });
        f.addRoomId(chatRoomId);
        replaceFragment(f, R.id.chat_layout);
    }

    @Override
    public void onBackPressed() {
        DialogUtil.showFinishPublish(LiveHostActivity.this, new MessageDialog.OnClickListener() {
            @Override
            public void onClick(MessageDialog messageDialog) {
                        Map<String,String> postParams = new HashMap<>();
                        postParams.put("rid",roomId);
                        postParams.put("user_id",LiveManager.getInstance().getConfig().userId);
                        LiveRequest.finishLive(postParams, new IDataResponse<BaseData<FinishLiveBean>>() {
                            @Override
                            public void onReceived(BaseData<FinishLiveBean> baseData) {
                                LiveShowFinishActivity.start(LiveHostActivity.this,baseData.data);
                                finish();
                            }

                            @Override
                            public void onError(Throwable e) {
                                T.showShort(LiveHostActivity.this,e.getMessage());
                                finish();
                            }
                        });

            }
        });
    }

    private void initVideoView() {
        livePublisherFragment = new LivePublisherFragment();
        livePublisherFragment.setRtmpUrl(rtmpUrl);
        replaceFragment(livePublisherFragment, R.id.content_layout);

        final ImageButton btnChangeCam = (ImageButton) findViewById(R.id.btnCameraChange);
        btnChangeCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFrontCamera = !mFrontCamera;
                livePublisherFragment.switchCamara(mFrontCamera);
            }
        });

        mFaceBeautyLayout = (LinearLayout) findViewById(com.xywy.livevideolib.R.id.layoutFaceBeauty);
        mBeautySeekBar = (SeekBar) findViewById(com.xywy.livevideolib.R.id.beauty_seekbar);
        mBeautySeekBar.setOnSeekBarChangeListener(this);

        mWhiteningSeekBar = (SeekBar) findViewById(com.xywy.livevideolib.R.id.whitening_seekbar);
        mWhiteningSeekBar.setOnSeekBarChangeListener(this);

        ImageButton mBtnFaceBeauty = (ImageButton) findViewById(com.xywy.livevideolib.R.id.btnFaceBeauty);
        mBtnFaceBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFaceBeautyLayout.setVisibility(mFaceBeautyLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        final ImageButton btnFinish = (ImageButton) findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void replaceFragment(Fragment newFragment, int viewId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!newFragment.isAdded()) {
            transaction.replace(viewId, newFragment);
            transaction.commit();
        } else {
            transaction.show(newFragment);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == com.xywy.livevideolib.R.id.beauty_seekbar) {
            livePublisherFragment.setmBeautyLevel(progress);
        } else if (seekBar.getId() == com.xywy.livevideolib.R.id.whitening_seekbar) {
            livePublisherFragment.setmWhiteningLevel(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                mFaceBeautyLayout.setVisibility(View.GONE);
        }
    }

}
