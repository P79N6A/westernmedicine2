package com.xywy.askforexpert.module.my.setting;

import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.easeWrapper.HXSDKHelper;

/**
 * 项目名称：D_Platform 类名称：MsgManagerActivity 类描述：消息管理 创建人：shihao 创建时间：2015-5-23
 * 上午11:01:45 修改备注：
 */
public class MsgManagerActivity extends YMBaseActivity implements OnClickListener {
    private SharedPreferences mPreferences;

    private ImageButton allMsg, msgVoice, msgShaken, diturbMode;

    private RelativeLayout rel_disturb;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        CommonUtils.initSystemBar(this);
//
//        setContentView(R.layout.activity_msg_manager);
//        mPreferences = getSharedPreferences("msg_manager", MODE_PRIVATE);
//        msgBack = (ImageButton) findViewById(R.id.btn_msg_back);
//        allMsg = (ImageButton) findViewById(R.id.ib_all_msg);
//        msgVoice = (ImageButton) findViewById(R.id.ib_msg_voice);
//        msgShaken = (ImageButton) findViewById(R.id.ib_msg_shaken);
//        rel_disturb = (RelativeLayout) findViewById(R.id.rel_disturb);
//        diturbMode = (ImageButton) findViewById(R.id.ib_disturb_mode);
//
//        if (mPreferences.getBoolean("all_msg", true)) {
//            allMsg.setBackgroundResource(R.drawable.radio_off);
//            rel_disturb.setVisibility(View.VISIBLE);
//        } else {
//            allMsg.setBackgroundResource(R.drawable.radio_on);
//            rel_disturb.setVisibility(View.GONE);
//        }
//
//        if (mPreferences.getBoolean("msg_voice", true)) {
//            msgVoice.setBackgroundResource(R.drawable.radio_off);
//        } else {
//            msgVoice.setBackgroundResource(R.drawable.radio_on);
//        }
//
//        if (mPreferences.getBoolean("msg_shaken", true)) {
//            msgShaken.setBackgroundResource(R.drawable.radio_off);
//        } else {
//            msgShaken.setBackgroundResource(R.drawable.radio_on);
//
//        }
//
//        if (mPreferences.getBoolean("msg_disturb", true)) {
//            diturbMode.setBackgroundResource(R.drawable.radio_off);
//        } else {
//            diturbMode.setBackgroundResource(R.drawable.radio_on);
//        }
//
//        msgBack.setOnClickListener(this);
//        allMsg.setOnClickListener(this);
//        msgVoice.setOnClickListener(this);
//        msgShaken.setOnClickListener(this);
//        diturbMode.setOnClickListener(this);
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_msg_manager;
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("消息设置");
        mPreferences = getSharedPreferences("msg_manager", MODE_PRIVATE);
        allMsg = (ImageButton) findViewById(R.id.ib_all_msg);
        msgVoice = (ImageButton) findViewById(R.id.ib_msg_voice);
        msgShaken = (ImageButton) findViewById(R.id.ib_msg_shaken);
        rel_disturb = (RelativeLayout) findViewById(R.id.rel_disturb);
        diturbMode = (ImageButton) findViewById(R.id.ib_disturb_mode);

        if (mPreferences.getBoolean("all_msg", true)) {
            allMsg.setBackgroundResource(R.drawable.radio_off);
            rel_disturb.setVisibility(View.VISIBLE);
        } else {
            allMsg.setBackgroundResource(R.drawable.radio_on);
            rel_disturb.setVisibility(View.GONE);
        }

        if (mPreferences.getBoolean("msg_voice", true)) {
            msgVoice.setBackgroundResource(R.drawable.radio_off);
        } else {
            msgVoice.setBackgroundResource(R.drawable.radio_on);
        }

        if (mPreferences.getBoolean("msg_shaken", true)) {
            msgShaken.setBackgroundResource(R.drawable.radio_off);
        } else {
            msgShaken.setBackgroundResource(R.drawable.radio_on);

        }

        if (mPreferences.getBoolean("msg_disturb", true)) {
            diturbMode.setBackgroundResource(R.drawable.radio_off);
        } else {
            diturbMode.setBackgroundResource(R.drawable.radio_on);
        }

        allMsg.setOnClickListener(this);
        msgVoice.setOnClickListener(this);
        msgShaken.setOnClickListener(this);
        diturbMode.setOnClickListener(this);
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor edit = getSharedPreferences("msg_manager",
                MODE_PRIVATE).edit();
        switch (v.getId()) {
            case R.id.ib_all_msg:
                if (mPreferences.getBoolean("all_msg", true)) {
                    edit.putBoolean("all_msg", false);
                    rel_disturb.setVisibility(View.GONE);
                    allMsg.setBackgroundResource(R.drawable.radio_on);
                    edit.putBoolean("msg_disturb", false);
                } else {
                    edit.putBoolean("all_msg", true);
                    allMsg.setBackgroundResource(R.drawable.radio_off);
                    rel_disturb.setVisibility(View.VISIBLE);

                }
                edit.commit();
                break;
            case R.id.ib_msg_voice:
                boolean soundEndState = false;
                soundEndState = !mPreferences.getBoolean("msg_voice", true);
                msgVoice.setBackgroundResource(soundEndState ? R.drawable.radio_off : R.drawable.radio_on);
                HXSDKHelper.getInstance().onInit(YMApplication.getAppContext());
                HXSDKHelper.getInstance().setMsgSoundAllowed(soundEndState);
                edit.putBoolean("msg_voice", soundEndState);
                edit.commit();
                break;
            case R.id.ib_msg_shaken:
                boolean vibrateEndState = false;
                vibrateEndState = !mPreferences.getBoolean("msg_shaken", true);
                msgShaken.setBackgroundResource(vibrateEndState ? R.drawable.radio_off : R.drawable.radio_on);
                edit.putBoolean("msg_shaken", vibrateEndState);
                edit.commit();
                HXSDKHelper.getInstance().onInit(YMApplication.getAppContext());
                HXSDKHelper.getInstance().setMsgVibrateAllowed(vibrateEndState);
                break;
            case R.id.ib_disturb_mode:
                if (mPreferences.getBoolean("msg_disturb", true)) {
                    edit.putBoolean("msg_disturb", false);
                    diturbMode.setBackgroundResource(R.drawable.radio_on);

                } else {
                    edit.putBoolean("msg_disturb", true);
                    diturbMode.setBackgroundResource(R.drawable.radio_off);
                }
                edit.commit();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    public void onPause() {
        StatisticalTools.onPause(this);
        super.onPause();
    }
}
