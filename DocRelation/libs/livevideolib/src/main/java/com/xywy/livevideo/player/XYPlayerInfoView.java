package com.xywy.livevideo.player;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.livevideo.LiveManager;
import com.xywy.livevideo.common_interface.Constant;
import com.xywy.livevideo.common_interface.IDataResponse;
import com.xywy.livevideo.common_interface.LiveRequest;
import com.xywy.livevideo.entity.CommonResponseEntity;
import com.xywy.livevideo.entity.GetRoomInfoRespEntity;
import com.xywy.livevideo.entity.HostInfoNoListRespEntity;
import com.xywy.livevideolib.R;
import com.xywy.util.ImageLoaderUtils;
import com.xywy.util.XYImageLoader;

/**
 * Created by zhangzheng on 2017/2/18.
 */
public class XYPlayerInfoView extends RelativeLayout implements View.OnClickListener {

    private Context context;

    //左上角头像
    private ImageView ivHeadImg;

    //关闭
    private ImageView ivClose;

    //关注
    private TextView tvConcern;

    //人数
    private TextView tvFansNumber;

    //主播名称
    private TextView tvHostName;

    private RelativeLayout rlLeftTop;

    private boolean isRun;

    private int count;

    private Runnable updateUIRunnable;

    private static final String TAG = "XYPlayerInfoView";

    //view所处的不同状态
    public static final int INFO_VIEW_TYPE_RECORD = 2;    //录制
    public static final int INFO_VIEW_TYPE_PLAY_REALTIME = 1; //直播
    public static final int INFO_VIEW_TYPE_PLAY_REPLAY = 0;//点播

    private int viewType;

    private String hostId;

    private String userId;

    private int userType;

    private String roomId;


    public XYPlayerInfoView(Context context) {
        super(context);
        initView(context);
    }

    public XYPlayerInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public XYPlayerInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.live_info_view, this);
        ivHeadImg = (ImageView) findViewById(R.id.iv_info_head_img);
        ivHeadImg.setOnClickListener(this);
        ivClose = (ImageView) findViewById(R.id.iv_info_button_close);
        ivClose.setOnClickListener(this);
        tvConcern = (TextView) findViewById(R.id.tv_info_concern);
        tvConcern.setOnClickListener(this);
        tvFansNumber = (TextView) findViewById(R.id.tv_info_text_fans);
        tvHostName = (TextView) findViewById(R.id.tv_info_anchor_name);
        rlLeftTop = (RelativeLayout) findViewById(R.id.rl_info_left_top);
        ImageLoaderUtils.getInstance().init(context);
        updateUIRunnable = new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        };
    }

    /**
     * 初始化
     *
     * @param type 表示当前的状态，录制、直播或点播
     */
    public void init(int type, String hostId, String userId, String roomId, int userType) {
        this.hostId = hostId;
        this.userId = userId;
        this.userType = userType;
        this.roomId = roomId;
        this.viewType = type;
        switch (type) {
            case INFO_VIEW_TYPE_RECORD:
                tvConcern.setVisibility(GONE);
                ivClose.setVisibility(GONE);
                //主播录制状态
                break;
            case INFO_VIEW_TYPE_PLAY_REALTIME:
                //观看直播状态
                break;
            case INFO_VIEW_TYPE_PLAY_REPLAY:
                //观看回放状态
                break;
        }

        initData();
        startUpdate();
    }

    private void initData() {
        LiveRequest.getHostInfo(hostId, userId, 1, new IDataResponse<HostInfoNoListRespEntity>() {
            @Override
            public void onReceived(HostInfoNoListRespEntity hostInfoNoListRespEntity) {
                if (hostInfoNoListRespEntity.getCode() == 10000) {
                    tvConcern.setVisibility(hostInfoNoListRespEntity.getData().getFollow_state() == Constant.CONSTANT_UN_CONCERN ? VISIBLE : GONE);
                } else {
                    Toast.makeText(context, "获取主播信息失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onDetachedFromWindow() {
        stopUpdate();
        super.onDetachedFromWindow();
    }


    private void startUpdate() {
        this.isRun = true;
        updateUI();
    }

    private void stopUpdate() {
        this.isRun = false;
        removeCallbacks(updateUIRunnable);
    }

    private void updateUI() {
        LiveRequest.getRoomInfo(roomId, new IDataResponse<GetRoomInfoRespEntity>() {
            @Override
            public void onReceived(GetRoomInfoRespEntity getRoomInfoRespEntity) {
                if (getRoomInfoRespEntity.getCode() == 10000) {
                    Log.d(TAG, "getRoomInfo-->state:" + getRoomInfoRespEntity.getData().getState() + ",amount:" + getRoomInfoRespEntity.getData().getAmount());
                    if (getRoomInfoRespEntity.getData().getState() != LiveManager.TYPE_REALTIME && viewType == XYPlayerInfoView.INFO_VIEW_TYPE_PLAY_REALTIME) {
                        Toast.makeText(context, "主播已经结束直播", Toast.LENGTH_SHORT).show();
                    }
                    tvHostName.setText(getRoomInfoRespEntity.getData().getUser().getName());
                    tvFansNumber.setText("在线" + getRoomInfoRespEntity.getData().getAmount() + "");
                    XYImageLoader.getInstance().displayUserHeadImage(getRoomInfoRespEntity.getData().getUser().getPortrait(), ivHeadImg);
                } else {
                    Log.d(TAG, String.format("msg:%s,code:%s", getRoomInfoRespEntity.getMsg(), getRoomInfoRespEntity.getCode()));
                }
                if (isRun) {
                    postDelayed(updateUIRunnable, 60 * 1000);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.toString());
            }


        });
    }


    private void showHeadDialog() {
        HeadDialog headDialog = new HeadDialog(context);
        headDialog.setParams(hostId, userId, userType);
        headDialog.setOnCancelConcernListener(new HeadDialog.OnCancelConcernListener() {
            @Override
            public void onCancel(int visible) {
                tvConcern.setVisibility(visible);
            }
        });
        headDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_info_button_close) {
            //点击退出
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                activity.finish();
            }
        } else if (v.getId() == R.id.tv_info_concern) {
            //点击关注
            if (viewType != INFO_VIEW_TYPE_RECORD) {
                LiveRequest.concern(userId, hostId, userType, LiveRequest.CONCERN_ACTION, new IDataResponse<CommonResponseEntity>() {
                    @Override
                    public void onReceived(CommonResponseEntity commonResponseEntity) {
                        if (commonResponseEntity.getCode() == 10000) {
                            Toast.makeText(context, "关注成功", Toast.LENGTH_SHORT).show();
                            tvConcern.setVisibility(GONE);
                        } else {
                            Toast.makeText(context, "关注失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, "关注失败", Toast.LENGTH_SHORT).show();
                    }

                });
            } else {
                Toast.makeText(context, "不能关注自己", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.iv_info_head_img) {
            //点击主播头像
            showHeadDialog();
        }
    }


    public static class HeadDialog extends Dialog implements OnClickListener {

        private Activity context;

        //主页按钮
        private Button btnMainPage;
        //取消按钮
        private Button btnCancel;
        //关闭
        private ImageView ivClose;
        //医生姓名
        private TextView tvDocName;
        //简介
        private TextView tvInfo;
        //关注数量
        private TextView tvFollowNumber;
        //粉丝数量
        private TextView tvFansNumber;
        //头像
        private ImageView ivHead;

        private String hostId;

        private String userId;

        private int userType;

        private OnCancelConcernListener onCancelConcernListener;


        public HeadDialog(Context context) {
            super(context);
            init(context);
        }

        public HeadDialog(Context context, int themeResId) {
            super(context, themeResId);
            init(context);
        }

        protected HeadDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
            init(context);
        }

        private void init(Context context) {
            this.context = (Activity) context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.live_dialog_anchor_info);
            initView();
            initData();
        }


        public void setParams(String hostId, String userId, int userType) {
            this.hostId = hostId;
            this.userId = userId;
            this.userType = userType;
        }

        public void setOnCancelConcernListener(OnCancelConcernListener onCancelConcernListener) {
            this.onCancelConcernListener = onCancelConcernListener;
        }

        private void initView() {
            btnMainPage = (Button) findViewById(R.id.btn_dialog_main_page);
            btnCancel = (Button) findViewById(R.id.btn_dialog_cancel_attend);
            btnMainPage.setOnClickListener(this);
            btnCancel.setOnClickListener(this);
            ivClose = (ImageView) findViewById(R.id.iv_dialog_close);
            ivClose.setOnClickListener(this);
            tvDocName = (TextView) findViewById(R.id.tv_dialog_anchor_name);
            tvInfo = (TextView) findViewById(R.id.tv_dialog_anchor_job);
            tvFollowNumber = (TextView) findViewById(R.id.tv_dialog_attend_number);
            tvFansNumber = (TextView) findViewById(R.id.tv_dialog_fans_number);
            ivHead = (ImageView) findViewById(R.id.iv_dialog_head_img);
        }


        private void initData() {
            LiveRequest.getHostInfo(hostId, userId, 1, new IDataResponse<HostInfoNoListRespEntity>() {
                @Override
                public void onReceived(HostInfoNoListRespEntity hostInfoNoListRespEntity) {
                    if (hostInfoNoListRespEntity.getCode() == 10000) {
                        XYImageLoader.getInstance().displayUserHeadImage(hostInfoNoListRespEntity.getData().getPortrait(), ivHead);
                        tvDocName.setText(hostInfoNoListRespEntity.getData().getName());
                        tvInfo.setText(hostInfoNoListRespEntity.getData().getSynopsis());
                        tvFollowNumber.setText(hostInfoNoListRespEntity.getData().getFollowNum());
                        tvFansNumber.setText(hostInfoNoListRespEntity.getData().getFansNum());
                        if (hostInfoNoListRespEntity.getData().getFollow_state() == Constant.CONSTANT_UN_CONCERN) {
                            //如果为关注
                            btnCancel.setText("关注");
                        } else {
                            //如果已经关注
                            btnCancel.setText("取消关注");
                        }
                    } else {
                        Toast.makeText(context, "获取主播信息失败", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable e) {

                }
            });
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_dialog_main_page) {
                //转到医生主页
                HostMainPageActivity.startHostMainPageActivity(context, hostId, userId);
            } else if (v.getId() == R.id.btn_dialog_cancel_attend) {
                handleCancelClick();
            } else if (v.getId() == R.id.iv_dialog_close) {
                //关闭
                this.dismiss();
            }
        }

        private void handleCancelClick() {
            //取消关注
            if (userId != null && !"".equals(userId)) {
                final int action;
                final String textSuccess;
                final String textFailed;
                if (btnCancel.getText().equals("取消关注")) {
                    action = LiveRequest.CONCERN_ACTION_CANCEL;
                    textSuccess = "取消成功";
                    textFailed = "取消失败";
                } else {
                    action = LiveRequest.CONCERN_ACTION;
                    textSuccess = "关注成功";
                    textFailed = "关注失败";
                }
                LiveRequest.concern(userId, hostId, userType, action, new IDataResponse<CommonResponseEntity>() {
                    @Override
                    public void onReceived(CommonResponseEntity commonResponseEntity) {
                        if (commonResponseEntity.getCode() == 10000) {
                            Toast.makeText(context, textSuccess, Toast.LENGTH_SHORT).show();
                            if (onCancelConcernListener != null) {
                                onCancelConcernListener.onCancel(action == LiveRequest.CONCERN_ACTION ? GONE : VISIBLE);
                            }
                            initData();
                        } else {
                            Toast.makeText(context, textFailed, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

            }
        }

        public static interface OnCancelConcernListener {
            void onCancel(int visible);
        }
    }
}
