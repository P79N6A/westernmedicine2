package com.xywy.livevideo.player;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.xywy.base.XywyBaseActivity;
import com.xywy.livevideo.LiveManager;
import com.xywy.livevideo.chat.LiveChatFragment;
import com.xywy.livevideo.chat.interfaces.IChatView;
import com.xywy.livevideo.common_interface.CommonLiveResponse;
import com.xywy.livevideo.common_interface.LiveRequest;
import com.xywy.livevideo.entity.EnterLiveRoomRespEntity;
import com.xywy.livevideo.entity.LeaveRoomRespEntity;
import com.xywy.livevideolib.R;
import com.xywy.util.NetUtils;

import java.util.List;

/**
 * Created by zhangzheng on 2017/2/24.
 */
public class XYPlayerActivity extends XywyBaseActivity implements View.OnClickListener {

    private XYMediaPlayerView xyMediaPlayerView;
    private XYPlayerInfoView xyPlayerInfoView;

    //送礼物
    private ImageView ivSendGift;
    //发消息
    private ImageView ivSendMsg;

    private IChatView chatView;

    private FrameLayout flChatContainer;

    private String hostId; //主播ID

    private int healthIcon = LiveManager.getInstance().getConfig().healthIcon;

    public static final String INTENT_PARAMS_TAG = "INTENT_PARAMS_TAG";

    private IntentParams params = new IntentParams();

    private static final String TAG = "XYPlayerActivity";

    /**
     * 调用直播播放接口
     *
     * @param url        直播连接
     * @param type       直播类型，直播(PLAY_TYPE_REALTIME)或者回放(PLAY_TYPE_REALTIME)
     * @param chatRoomId
     */
    public static void startPlayerActivity(Context activity, String roomId, String url, int type, String chatRoomId, List<String> vod) {
        IntentParams params = new IntentParams();
        params.setUserId(LiveManager.getInstance().getConfig().userId);
        params.setRoomId(roomId);
        params.setUrl(url);
        params.setChatRoomId(chatRoomId);
        params.setType(type);
        params.setVod(vod);
        Intent intent = new Intent(activity, XYPlayerActivity.class);
        intent.putExtra(INTENT_PARAMS_TAG, params);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_fragment_player);
        hideCommonBaseTitle();
//        ImageLoaderUtils.getInstance().init(this);
        checkNet();
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        init();
    }

    @Override
    protected void onDestroy() {
        if (checkParams()) {
            LiveRequest.leaveLiveRoom(params.getRoomId(), params.getUserId(), new CommonLiveResponse<LeaveRoomRespEntity>() {
                @Override
                public void onReceived(LeaveRoomRespEntity leaveRoomRespEntity) {
                    if (leaveRoomRespEntity.getCode() == 10000) {
                        Log.d(TAG, "离开房间成功");
                    } else {
                        Log.d(TAG, String.format("离开房间失败,code:%s,msg:%s" + leaveRoomRespEntity.getCode(), leaveRoomRespEntity.getMsg()));
                    }

                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, String.format("离开房间失败,e:%s" + e.toString()));
                }


            });
        }
        super.onDestroy();
    }

    private void init() {
        params = getIntent().getParcelableExtra(INTENT_PARAMS_TAG);
        if (!checkParams()) {
            //finish();

        }else {
            initView();
            initData();
        }
    }

    private boolean checkParams() {
        if (params.getUserId() == null || params.getUserId().equals("") ||
                params.getRoomId() == null || params.getRoomId().equals("")) {
            Toast.makeText(this, String.format("参数错误,userId:%s,roomId:%s",
                    params.getUserId(), params.getRoomId(), params.getUrl()), Toast.LENGTH_SHORT).show();
            return false;
        }
        if ((params.getType() == LiveManager.TYPE_REALTIME && (params.getUrl() == null || params.getUrl().equals("")))
                || (params.getType() == LiveManager.TYPE_REPLAY && (params.getVod() == null || params.getVod().isEmpty()))) {
            Toast.makeText(this, String.format("url错误,url:%s,vodList:%s", params.getUrl(), params.getVod()), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initView() {
        xyMediaPlayerView = (XYMediaPlayerView) findViewById(R.id.xympv_player_view);
        xyMediaPlayerView.init(params.url, params.getType(), params.getVod());
        xyPlayerInfoView = (XYPlayerInfoView) findViewById(R.id.xypiv_info_view);
        ivSendGift = (ImageView) findViewById(R.id.iv_info_send_gift);
        ivSendGift.setOnClickListener(this);
        ivSendMsg = (ImageView) findViewById(R.id.iv_info_send_msg);
        ivSendMsg.setOnClickListener(this);
        flChatContainer = (FrameLayout) findViewById(R.id.fl_chat_container);

        LiveChatFragment chatFragment = new LiveChatFragment();
        chatFragment.addRoomId(params.getChatRoomId());
        chatView = chatFragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_chat_container, chatFragment);
        transaction.commit();

        setViewState();
    }

    private void initData() {
        LiveRequest.enterLiveRoom(params.getRoomId(), params.getUserId(), new CommonLiveResponse<EnterLiveRoomRespEntity>() {
            @Override
            public void onReceived(EnterLiveRoomRespEntity enterLiveRoomRespEntity) {
                if (enterLiveRoomRespEntity.getCode() == 10000) {
                    Log.d(TAG, String.format("进入房间成功"));
                    xyPlayerInfoView.init(params.getType(), enterLiveRoomRespEntity.getData().getUserid() + "", params.getUserId(), params.getRoomId(), LiveManager.getInstance().getConfig().userType);
                    XYPlayerActivity.this.hostId = enterLiveRoomRespEntity.getData().getUserid() + "";
                } else {
                    String text = String.format("进入房间失败,code:%s,msg:%s", enterLiveRoomRespEntity.getCode(), enterLiveRoomRespEntity.getMsg());
                    Log.d(TAG, text);
                    Toast.makeText(XYPlayerActivity.this, text, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, String.format("进入直播间失败,msg:%s", e.toString()));
                Toast.makeText(XYPlayerActivity.this, "进入直播间失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setViewState() {
        switch (params.getType()) {
            case XYMediaPlayerView.PLAYER_TYPE_REALTIME:
                ivSendGift.setVisibility(View.VISIBLE);
                ivSendMsg.setVisibility(View.VISIBLE);
                flChatContainer.setVisibility(View.VISIBLE);
                break;
            case XYMediaPlayerView.PLAYER_TYPE_REPLAY:
                ivSendGift.setVisibility(View.GONE);
                ivSendMsg.setVisibility(View.GONE);
                flChatContainer.setVisibility(View.GONE);
                break;
        }
    }

    private void showGiftDialog() {
        GiftDialog mDialog = new GiftDialog(this);
        mDialog.setCanceledOnTouchOutside(true);
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setWindowAnimations(R.style.PopupAnimation);
        WindowManager.LayoutParams mParams = mDialog.getWindow()
                .getAttributes();
        mParams.dimAmount = 0;
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        mParams.width = (int) (display.getWidth() * 1.0);
        mDialog.getWindow().setAttributes(mParams);
        mDialog.setParams(params.getUserId(), hostId, params.roomId, healthIcon);
        mDialog.setOnSendGiftListener(new GiftDialog.OnSendGiftListener() {
            @Override
            public void onSuccess(String presentId, int count, int healthIcon) {
                chatView.sendGiftMsg(presentId, count);
                XYPlayerActivity.this.healthIcon = healthIcon;
            }
        });
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_info_send_gift) {
            //弹出礼物界面
            showGiftDialog();
        } else if (v.getId() == R.id.iv_info_send_msg) {
            //发送消息
            chatView.showMsgInputBox();
        }
    }

    private boolean checkNet() {
        if (!NetUtils.isConnected(this)) {
            Toast.makeText(this, "网络连接异常", Toast.LENGTH_SHORT).show();
        }
        if (!NetUtils.isWifiConnected(this)) {
            Toast.makeText(this, "您正在使用移动网络，会消耗流量", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public static class IntentParams implements Parcelable {
        private String userId;
        private String roomId;
        private String url;
        private String chatRoomId;
        private int type;
        private List<String> vod;

        public IntentParams() {

        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(userId);
            dest.writeString(roomId);
            dest.writeString(url);
            dest.writeString(chatRoomId);
            dest.writeInt(type);
            dest.writeList(vod);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<IntentParams> CREATOR = new Creator<IntentParams>() {
            @Override
            public IntentParams createFromParcel(Parcel in) {
                IntentParams params = new IntentParams();
                params.setUserId(in.readString());
                params.setRoomId(in.readString());
                params.setUrl(in.readString());
                params.setChatRoomId(in.readString());
                params.setType(in.readInt());
                params.setVod(in.readArrayList(String.class.getClassLoader()));
                return params;
            }

            @Override
            public IntentParams[] newArray(int size) {
                return new IntentParams[size];
            }
        };

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getChatRoomId() {
            return chatRoomId;
        }

        public void setChatRoomId(String chatRoomId) {
            this.chatRoomId = chatRoomId;
        }

        public List<String> getVod() {
            return vod;
        }

        public void setVod(List<String> vod) {
            this.vod = vod;
        }
    }

}
