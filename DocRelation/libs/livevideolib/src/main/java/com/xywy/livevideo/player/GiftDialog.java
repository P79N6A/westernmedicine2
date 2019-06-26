package com.xywy.livevideo.player;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.livevideo.RechargeType;
import com.xywy.livevideo.activity.RechargeActivity;
import com.xywy.livevideo.adapter.GiftAdapter;
import com.xywy.livevideo.common_interface.CommonLiveResponse;
import com.xywy.livevideo.common_interface.IDataResponse;
import com.xywy.livevideo.common_interface.LiveRequest;
import com.xywy.livevideo.entity.GiftListRespEntity;
import com.xywy.livevideo.entity.GiveGiftRespEntity;
import com.xywy.livevideolib.R;

/**
 * Created by zhangzheng on 2017/2/21.
 */
public class GiftDialog extends Dialog implements View.OnClickListener {

    private Context context;

    //礼物列表
    private RecyclerView rlvGiftList;
    //充值
    private Button btnRecharge;
    //发送
    private Button btnSend;
    //剩余虚拟币
    private TextView tvLeft;

    private GiftAdapter giftAdapter;

    private RelativeLayout rlRoot;

    private String userId;//用户ID

    private String hostId; //主播ID

    private String roomId;  //房间ID

    private int healthIcon;//最初的虚拟币

    private OnSendGiftListener onSendGiftListener;

//    private String presentId; //发送的礼物ID

    private int presentCount;//发送的礼物数量

    public GiftDialog(Context context) {
        super(context);
        init(context);
    }

    public GiftDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected GiftDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }


    public void setParams(String userId, String hostId, String roomId, int healthIcon) {
        this.userId = userId;
        this.hostId = hostId;
        this.roomId = roomId;
        this.healthIcon = healthIcon;
        tvLeft.setText("健康币" + healthIcon);
    }

    public void setOnSendGiftListener(OnSendGiftListener onSendGiftListener) {
        this.onSendGiftListener = onSendGiftListener;
    }

    private void init(Context context) {
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.live_gift_dialog);
        initView();
        initData();
    }

    private void initView() {
        rlvGiftList = (RecyclerView) findViewById(R.id.rlv_gift_list);
        btnRecharge = (Button) findViewById(R.id.btn_gift_recharge);
        btnRecharge.setOnClickListener(this);
        btnSend = (Button) findViewById(R.id.btn_gift_send);
        btnSend.setOnClickListener(this);
        tvLeft = (TextView) findViewById(R.id.tv_gift_balance);
        giftAdapter = new GiftAdapter(context);

        rlRoot = (RelativeLayout) findViewById(R.id.rl_gift_dialog);
        rlRoot.setVisibility(View.INVISIBLE);
    }

    private void initData() {
        rlvGiftList.setAdapter(giftAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
        rlvGiftList.setLayoutManager(gridLayoutManager);

        LiveRequest.getGiftList(1, new IDataResponse<GiftListRespEntity>() {
            @Override
            public void onReceived(GiftListRespEntity giftListRespEntity) {
                    giftAdapter.setData(giftListRespEntity.getData());
                    giftAdapter.notifyDataSetChanged();
                    rlRoot.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Throwable e) {

            }

        });


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_gift_recharge) {
            //充值
            RechargeActivity.start(context, RechargeType.recharge);
        } else if (v.getId() == R.id.btn_gift_send) {
            //送礼物
            sendGift();
        }
    }

    private void sendGift() {
        if (giftAdapter.getSelectedItem() != null) {
            this.presentCount = 1;
            final String presentId = giftAdapter.getSelectedItem().getId();
            if (healthIcon < giftAdapter.getSelectedItem().getPrice()) {
                Toast.makeText(context, "健康币不足", Toast.LENGTH_SHORT).show();
                return;
            }
            LiveRequest.giveGift(roomId, giftAdapter.getSelectedItem().getId() + "", userId, hostId, new CommonLiveResponse<GiveGiftRespEntity>() {
                @Override
                public void onReceived(GiveGiftRespEntity giveGiftRespEntity) {
                    if (giveGiftRespEntity.getCode() == 10000) {
                        if (onSendGiftListener != null) {
                            onSendGiftListener.onSuccess(presentId, presentCount, giveGiftRespEntity.getData().getBalance());
                        }
                        tvLeft.setText("健康币" + giveGiftRespEntity.getData().getBalance());
                        healthIcon = giveGiftRespEntity.getData().getBalance();
                        Toast.makeText(context, "发送礼物成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "发送礼物失败", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(context, "发送礼物失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "未选择礼物", Toast.LENGTH_SHORT).show();
        }
    }

    public static interface OnSendGiftListener {
        void onSuccess(String presentId, int count, int healthIcon);
    }
}
