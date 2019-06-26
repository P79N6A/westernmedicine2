package com.xywy.livevideo.debug;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xywy.base.XywyBaseActivity;
import com.xywy.livevideo.LiveManager;
import com.xywy.livevideo.RechargeType;
import com.xywy.livevideo.activity.RechargeActivity;
import com.xywy.livevideo.chat.model.LiveChatClient;
import com.xywy.livevideo.player.LiveShowFinishActivity;
import com.xywy.livevideo.player.VideoBroadcastActivity;
import com.xywy.livevideolib.R;

import java.util.Random;

public class DebugEnterActivity extends XywyBaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_enter);
        Button live = (Button) findViewById(R.id.bt_live_publish);
        live.setOnClickListener(this);
        Button play = (Button) findViewById(R.id.btn_play);
        play.setOnClickListener(this);
        Button btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
        Button bt_chat = (Button) findViewById(R.id.bt_recharge);
        bt_chat.setOnClickListener(this);
        Button btn_network = (Button) findViewById(R.id.btn_network);
        btn_network.setOnClickListener(this);
        Button btn_live_show_finished = (Button) findViewById(R.id.btn_live_show_finished);
        btn_live_show_finished.setOnClickListener(this);
        Button bt_login = (Button) findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);
        Button bt_init = (Button) findViewById(R.id.bt_init);
        bt_init.setOnClickListener(this);
        Button bt_register = (Button) findViewById(R.id.bt_register);
        bt_register.setOnClickListener(this);
//        LiveChatClient.getInstance().register(FakeHostData.username+ new Random().nextInt(), FakeHostData.password);
//        LiveChatClient.getInstance().addConnectionListener(new IChatCallBack() {
//            @Override
//            public void onUserException(String exception) {
////                DialogUtil.showConflictDialog(DebugEnterActivity.this,exception);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_live_show_finished) {
            LiveShowFinishActivity.start(DebugEnterActivity.this, null);

        } else if (i == R.id.bt_recharge) {//                LiveHostActivity.start(this);
            RechargeActivity.start(DebugEnterActivity.this, RechargeType.mypurse);

        } else if (i == R.id.bt_live_publish) {
            LiveManager.getInstance().startLive(this, FakeHostData.rtmpUrl,FakeHostData.roomId,FakeHostData.chatRoomId);

        } else if (i == R.id.btn_play) {//startActivity(new Intent(this,ThirdActivity.class));
            //LiveManager.getInstance().watchLive(this, FakeHostData.roomId,FakeHostData.rtmpUrl, FakeHostData.chatRoomId,XYPlayerActivity.PLAY_TYPE_REALTIME);

        } else if (i == R.id.btn_start) {//startActivity(new Intent(this, StartVideoBroadcastActivity.class));
            VideoBroadcastActivity.startVideoBroadcastActivity(this);

        } else if (i == R.id.btn_network) {
            startActivity(new Intent(this, NetworkTestActivity.class));

        }
        else if (i == R.id.bt_register) {
            LiveChatClient.getInstance().register(FakeHostData.username+new Random().nextInt(),FakeHostData.password);
        }
        else if (i == R.id.bt_login) {
            LiveChatClient.getInstance().login(FakeHostData.username,FakeHostData.password);

        } else if (i == R.id.bt_init) {
            LiveChatClient.getInstance().init(getApplication());
        }
    }
}
