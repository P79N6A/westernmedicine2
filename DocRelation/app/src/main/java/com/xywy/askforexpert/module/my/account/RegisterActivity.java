package com.xywy.askforexpert.module.my.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.utils.JudgeNetIsConnectedReceiver;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.RegisterInfo;
import com.xywy.askforexpert.module.discovery.medicine.common.RequestTool;
import com.xywy.askforexpert.module.main.service.que.QuePerActivity;
import com.xywy.askforexpert.module.my.account.request.AccountRequest;
import com.xywy.base.view.ProgressDialog;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.util.T;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.adapter.rxjava.HttpException;

/**
 * 注册页面 stone
 *
 * @author 史浩
 * @2015-5-14下午2:47:40
 */
public class RegisterActivity extends YMBaseActivity implements OnClickListener {
    /**
     * 账号
     */
    private EditText edit_phone;
    /**
     * 验证码
     */
    private EditText edit_yzm;
    /**
     * 密码
     */
    private EditText edit_password;

    /**
     * 图形验证码 文字
     */
    private EditText edit_yzm_pic;
    /**
     * 获取图形验证码
     */
    private TextView tv_getyzm_pic;

    private ImageView iv_getyzm_pic;

    /**
     * 获取验证码
     */
    private TextView tv_getyzm;
    /**
     * 注册
     */
    private TextView tv_submit;
    /**
     * 倒计时
     */
    private int time_len = 60;

    private TextView tv_shuoming;

    private InputMethodManager manager;

    private SharedPreferences sp;

    private ProgressDialog nDialog;

    private static final String PROJECT = "APP_YMGYH_REG";
    private static final String FLAG = "APP_FLAG";
    /**
     * 身份类型
     */
    private String type;
    /**
     * 倒计时线程
     */
    private Thread time_thread;
    /**
     * hander
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what < 100) {
                tv_getyzm.setText("重新获取(" + (59 - msg.what) + ")");

                if (59 - msg.what == 0) {
                    tv_getyzm.setText("获取验证码");
                    tv_getyzm.setEnabled(true);
                    tv_getyzm.setTextColor(getResources().getColor(
                            R.color.white));
                    tv_getyzm.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.yzm_bg_selector));
                }
                if (msg.what == 59) {

                }
            }
            switch (msg.what) {
                case 110:
                    getStop();
                    break;
                default:
                    break;
            }

        }

    };
    private String mTimestamp;
    private ProgressDialog mDialog;
    /**
     * 判断手机格式是否正确
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile(/* "^((13[0-9])|(14[5,7])|(17[6-7])|(15[^4,\\D])|(18[0-9]))\\d{8}$" */"^(1[0-9])\\d{9}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    /**
     * 判断是否全是数字
     */
    public static boolean isValidatePwd(String str) {
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15}$";
        if (str == null || str.equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }



    /**
     * 初始化控件
     */
    public void initview() {
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_yzm = (EditText) findViewById(R.id.edit_yzm);
        edit_password = (EditText) findViewById(R.id.edit_newmima);
        nDialog = new ProgressDialog(this, "正在登录中...");
        edit_yzm_pic = (EditText) findViewById(R.id.edit_yzm_pic);
        iv_getyzm_pic = (ImageView) findViewById(R.id.iv_getyzm_pic);
        tv_getyzm_pic = (TextView) findViewById(R.id.tv_getyzm_pic);
        tv_getyzm_pic.setOnClickListener(this);
        tv_getyzm = (TextView) findViewById(R.id.tv_getyzm);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_shuoming = (TextView) findViewById(R.id.tv_shuoming);
        tv_getyzm.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        tv_shuoming.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,
                        QuePerActivity.class);
                intent.putExtra("isfrom", "服务协议");
                startActivity(intent);
            }
        });
        getVerifyPicCode();
    }

    private void registerNew(final String phone, final String pwd, String yzm){
        AccountRequest.getInstance().register(phone,pwd,yzm).subscribe(new BaseRetrofitResponse<BaseData<RegisterInfo>>(){
            @Override
            public void onStart() {
                super.onStart();
                 tv_submit.setClickable(false);
                mDialog = new ProgressDialog(RegisterActivity.this, "正在注册，请稍后...");
                mDialog.showProgersssDialog();
                hideKeyboard();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(BaseData<RegisterInfo> loginInfo_newBaseData) {
                tv_submit.setClickable(true);
                if(null != mDialog){
                    mDialog.dismiss();
                }
                ToastUtils.shortToast( "注册"+loginInfo_newBaseData.getMsg());
                goToLogin(phone, pwd);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
                    Toast.makeText(RetrofitClient.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    if("验证失败".equals(e.getMessage())){
                        Toast.makeText(RetrofitClient.getContext(), "验证码错误，请重新输入", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RetrofitClient.getContext(), "异常提示:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                tv_submit.setClickable(true);
                if(null != mDialog){
                    mDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        String etphone = edit_phone.getText().toString().trim();
        switch (v.getId()) {
            case R.id.tv_getyzm_pic:
                hideKeyboard();
                if (JudgeNetIsConnectedReceiver.judgeNetIsConnected(this)) {
                    getVerifyPicCode();
                }else {
                    ToastUtils.shortToast("无网络，请检查网络连接");
                }
                break;
            case R.id.tv_getyzm:
                hideKeyboard();
                tv_GetYZM(etphone);
                break;

            case R.id.tv_submit:
                StatisticalTools.eventCount(RegisterActivity.this, "register1");
                hideKeyboard();
                tv_Submit(etphone);
                break;
            default:
                break;
        }
    }

    private void getVerifyPicCode() {
        Map<String,String> getParams = RequestTool.getCommonParams("1423");
        getParams.put("flag",FLAG);
        mTimestamp = System.currentTimeMillis()+"";
        mTimestamp = mTimestamp.substring(0,10);
        getParams.put("timestamp",mTimestamp);
        getParams.put("sign",RequestTool.getSign(getParams));
        String url = BuildConfig.WWS_XYWY_BASE_URL+"common/captcha/getCaptcha?sign="+RequestTool.getSign(getParams)
                +"&source="+RequestTool.REQUEST_SOURCE
                +"&pro="+RequestTool.REQUEST_PRO
                +"&version=1.0&api=1423&os="+RequestTool.REQUEST_OS
                +"&flag="+FLAG
                +"&timestamp="+mTimestamp;
        ImageLoadUtils.INSTANCE.loadImageView(iv_getyzm_pic,url, R.drawable.tuxingma_failed);
    }

    /**
     * 验证码
     * @param etphone
     */
    private void tv_GetYZM(final String etphone) {
        if(TextUtils.isEmpty(etphone)){
            ToastUtils.shortToast("请输入手机号");
            return;
        }
        if(!isMobileNO(etphone)){
            ToastUtils.shortToast("请输入正确的手机号");
            return;
        }
        if(TextUtils.isEmpty(edit_yzm_pic.getText())){
            ToastUtils.shortToast("请输入图形验证码");
            return;
        }

        if (JudgeNetIsConnectedReceiver.judgeNetIsConnected(this)) {
            AccountRequest.getInstance().getVerifiCode(etphone,mTimestamp,edit_yzm_pic.getText().toString().trim(),FLAG,PROJECT).subscribe(new BaseRetrofitResponse<BaseData>(){
                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onCompleted() {
                    super.onCompleted();
                }

                @Override
                public void onNext(BaseData baseData) {
                    handler.sendEmptyMessage(110);
                    ToastUtils.shortToast(
                            "短信验证码已发送至手机： " + etphone + "，请在一分钟内完成验证。");
                    tv_getyzm.setEnabled(false);
                    tv_getyzm.setTextColor(getResources().getColor(
                            R.color.tab_color_nomal));
                    tv_getyzm.setBackgroundColor(getResources().getColor(
                            R.color.my_line));
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    getVerifyPicCode();
                    edit_yzm_pic.setText("");
                }
            });
            hideKeyboard();
        } else {
            ToastUtils.shortToast( "无网络，请检查网络连接");
        }
    }

    /**
     * 提交
     * @param etphone
     */
    private void tv_Submit(String etphone) {
        String pwd = edit_password.getText().toString().trim();
        String yzm = edit_yzm.getText().toString().trim();
        if (etphone != null && !etphone.equals("")) {
            if (isMobileNO(etphone)) {
                if(TextUtils.isEmpty(yzm)){
                    ToastUtils.shortToast( "请输入验证码");
                    return;
                }

                if(TextUtils.isEmpty(pwd)){
                    ToastUtils.shortToast( "请输入密码");
                    return;
                }

                if(!isValidatePwd(pwd)){
                    ToastUtils.shortToast("密码设置需为6-15位字母+数字组合，请重新输入");
                }else {
                    if (JudgeNetIsConnectedReceiver.judgeNetIsConnected(this)) {
                        registerNew(etphone, pwd, yzm);
                    } else {
                        ToastUtils.shortToast( "无网络，请检查网络连接");
                    }
                }
            } else {
                ToastUtils.shortToast("请输入正确的手机号");
            }
        } else {
            ToastUtils.shortToast("请输入手机号");
        }
    }

    /**
     * 倒计时60秒线程
     */
    private void getStop() {
        time_len = 60;
        time_thread = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < time_len; i++) {
                    handler.sendEmptyMessage(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        time_thread.start();
    }


    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    @Override
    protected void initView() {

        titleBarBuilder.setTitleText("注册");

        sp = getSharedPreferences("save_user", MODE_PRIVATE);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initview();
    }

    @Override
    protected void initData() {

    }

    private void goToLogin(String username, String pwd) {
        if (JudgeNetIsConnectedReceiver.judgeNetIsConnected(this)) {
            YMUserService.ymLogin_New(RegisterActivity.this,username, pwd);
        } else {
            Toast.makeText(this, "无网络，请检查网络连接", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (nDialog != null && nDialog.isShowing()) {
            nDialog.dismiss();
        }
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected int getLayoutResId() {
        return  R.layout.register;
    }
}
