package com.xywy.askforexpert.module.message.call;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.interfaces.callback.HttpCallback;
import com.xywy.askforexpert.appcommon.old.BaseActivity;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.SystemBarTintManager;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.api.BaseResultBean;
import com.xywy.askforexpert.model.call.CallResultBean;
import com.xywy.askforexpert.model.call.ConsultPageBean;


/**
 * Created by bailiangjin on 16/5/24.
 */
public class CallActivity extends BaseActivity {


    public static final String DOCTOR_ID_INTENT_KEY = "DOCTOR_ID_INTENT_KEY";
    public static final String HELP_TYPE_INTENT_KEY = "HELP_TYPE_INTENT_KEY";

    public static final String CALL_TYPE_KEY = "callType";
    public static final String HEAD_URL_KEY = "HEAD_URL_KEY";
    public static final String PATIENT_ID_KEY = "PATIENT_ID_KEY";

    private static final int CALL_STATE_UN_CALL = 0;
    private static final int CALL_STATE_CALLING = 1;
    private static final int CALL_STATE_FAILED = 2;


    private ImageView iv_bg;

    private RelativeLayout rl_root;
    private ImageView iv_head;
    private Button btn_call;
    private TextView tv_count;
    private TextView tv_notice;

    private CountDownTimer failedTick;
    private CountDownTimer callingTick;


    private ConsultPageBean consultPageBean;

    /**
     * 呼叫状态 0:未呼叫:1:呼叫中 2:呼叫失败
     */
    private int callState = CALL_STATE_UN_CALL;

    /**
     * 计时
     */
    private int count = 0;

    /**
     * 启动Activity
     *
     * @param context
     * @param patientHeadUrl
     */
    public static void start(Context context, String patientId, String patientHeadUrl) {
        Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra(PATIENT_ID_KEY, patientId);
        intent.putExtra(HEAD_URL_KEY, patientHeadUrl);
        intent.putExtra(CALL_TYPE_KEY, ConsultPageBean.CALL_TYPE_DOCTOR_CALL_USER);
        //intent.putExtra(DOCTOR_ID_INTENT_KEY, doctorId);
        //intent.putExtra(HELP_TYPE_INTENT_KEY, helpType);
        context.startActivity(intent);
    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(Color.TRANSPARENT);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);
        initSystemBar();
        initView();
        initMyData();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //界面到后台结束Activity
        existCurActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTick();
    }


    //-------以上为生命周期---------------------


    private void initView() {
        iv_bg = (ImageView) findViewById(R.id.iv_bg);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        btn_call = (Button) findViewById(R.id.btn_call);
        tv_count = (TextView) findViewById(R.id.tv_count);
        tv_notice = (TextView) findViewById(R.id.tv_notice);
    }


    private void initMyData() {
        int callType = getIntent().getIntExtra(CALL_TYPE_KEY, 1);

        String headUrl = getIntent().getStringExtra(HEAD_URL_KEY);
        String doctorId = getIntent().getStringExtra(DOCTOR_ID_INTENT_KEY);
        String patientId = getIntent().getStringExtra(PATIENT_ID_KEY);
        String helpType = getIntent().getStringExtra(HELP_TYPE_INTENT_KEY);

        consultPageBean = new ConsultPageBean(callType, headUrl, doctorId, patientId, helpType);


        switch (callType) {
            case ConsultPageBean.CALL_TYPE_FROM_HELP:
                //设置默认头像背景
                iv_head.setImageResource(R.drawable.icon_help_user_head);
                iv_bg.setImageResource(R.drawable.call_bg_for_help);
                //StatisticalTools.eventCount(this, StatisticalConstants.dialhelp);

                break;
            case ConsultPageBean.CALL_TYPE_USER_CALL_DOCTOR:
                //加载医生头像 及头像生成的背景
                ImageLoadUtils.INSTANCE.loadCircleImageView(iv_head, consultPageBean.getHeadUrl(), R.drawable.icon_help_user_head);
                ImageLoadUtils.INSTANCE.loadImageView(iv_bg, consultPageBean.getHeadUrl(), R.drawable.call_bg_for_help);
                //StatisticalTools.eventCount(this, StatisticalConstants.dial1);
                break;

            //当前只有这一种可能性
            case ConsultPageBean.CALL_TYPE_DOCTOR_CALL_USER:
                //加载用户头像 及头像生成的背景
                ImageLoadUtils.INSTANCE.loadCircleImageView(iv_head, consultPageBean.getHeadUrl(), R.drawable.icon_help_user_head);
                ImageLoadUtils.INSTANCE.loadImageView(iv_bg, consultPageBean.getHeadUrl(), R.drawable.call_bg_for_help);
                break;
            default:
                break;
        }
        //设置背景透明度
        iv_bg.setAlpha(60);

        initTick();

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StatisticalTools.eventCount(CallActivity.this, "HangUp");
                switch (callState) {
                    case CALL_STATE_UN_CALL:
                        startCall();
                        break;
                    case CALL_STATE_CALLING:
                        existCurActivity();
                        break;
                    case CALL_STATE_FAILED:
                        existCurActivity();
                        break;
                    default:
                        break;
                }

            }
        });

        startCall();

    }

    /**
     * 开始呼叫
     */
    private void startCall() {
        callState = CALL_STATE_CALLING;
        //初始化呼叫计时
        callingTick.start();
        if (TextUtils.isEmpty(consultPageBean.getPatientId())) {
            switchToFailedState("用户id为空");
            return;
        }
        CallRequestUtils.call(consultPageBean.getPatientId(), new HttpCallback() {
            @Override
            public void onSuccess(Object obj) {

                if (!(obj instanceof String)) {
                    ToastUtils.shortToast("服务返回数据为空或格式错误");
                    return;
                }
                CallResultBean result = null;
                try {
                    result = GsonUtils.toObj(obj.toString(),CallResultBean.class);
                    if (result.getCode() == BaseResultBean.CODE_SUCCESS) {
                        LogUtils.e("callresult:success" + obj.toString());
                    } else {
                        switchToFailedState("呼叫失败:" + result.getMsg());
                    }
                } catch (Exception e) {
                    LogUtils.e("callresult:failed:" + obj.toString());
                    ToastUtils.shortToast("服务端返回json数据格式错误");
                    e.printStackTrace();
                    switchToFailedState("呼叫失败:服务端返回json数据格式错误");
                    return;
                }
            }

            @Override
            public void onFailed(Throwable throwable, int errorNo, String strMsg) {
                LogUtils.e("callresult:failed:" + errorNo + ":" + strMsg);
                switchToFailedState("doctor，先检查一下您的网络吧！");

            }
        });
    }

    private void existCurActivity() {
        cancelTick();
        finish();
    }


    /**
     * 初始化 呼叫计时
     */
    private void initTick() {
        failedTick = new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                count++;
                //LogUtils.d("tick:" +count );
            }

            @Override
            public void onFinish() {
                LogUtils.d("finish:" + count);
                existCurActivity();
            }
        };

        callingTick = new CountDownTimer(20000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                count++;
                //LogUtils.d("tick:" + count);
                refreshUI(callState, count, consultPageBean.getCallingNotice());
            }

            @Override
            public void onFinish() {
                LogUtils.e("finish:" + count);
                switchToFailedState("呼叫失败");
            }
        };
    }

    private void switchToFailedState(String notice) {
        cancelTick();
        callState = CALL_STATE_FAILED;
        refreshUI(callState, count, notice);
        failedTick.start();
    }

    private void refreshUI(int callState, int count, String notice) {

        switch (callState) {
            case CALL_STATE_CALLING:
                if (count > 0) {
//                    tv_count.setVisibility(View.VISIBLE);
                    tv_count.setText(count + " 秒");
                }
                tv_notice.setText(TextUtils.isEmpty(consultPageBean.getCallingNotice()) ? "" : consultPageBean.getCallingNotice());
                btn_call.setBackgroundResource(R.drawable.iv_call);
                break;
            case CALL_STATE_FAILED:
                tv_notice.setText(notice);
                tv_count.setVisibility(View.INVISIBLE);
                btn_call.setBackgroundResource(R.drawable.iv_call_failed);
                break;
            default:
                break;

        }
    }

    /**
     * 停止tick
     */
    private void cancelTick() {
        count = 0;
        if (null != callingTick) {
            callingTick.cancel();
        }
        if (null != failedTick) {
            failedTick.cancel();
        }
    }
}
