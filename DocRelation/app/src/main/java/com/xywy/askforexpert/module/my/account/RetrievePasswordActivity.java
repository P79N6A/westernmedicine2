package com.xywy.askforexpert.module.my.account;

import android.content.Context;
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
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.utils.JudgeNetIsConnectedReceiver;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.module.discovery.medicine.common.RequestTool;
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
 * 找回密码/修改密码 stone
 *
 * @author shihao 2015-5-14
 */

public class RetrievePasswordActivity extends YMBaseActivity implements
        OnClickListener {

    private boolean mIsFindPwd = true;//是否是找回密码 否则就是修改密码

    private EditText edPhone, mEtPicCode,edYzm, edNpwd;

    private TextView tvYzm, tvSubmit;

    private InputMethodManager manager;

    private static final String FLAG = "APP_FLAG";
    private String mTimestamp;
    private ImageView mIvGetyzmPic;
    private TextView mTvGetyzmPic;
    private ProgressDialog mProgressDialog;
//    private static final String PROJECT_GPWD = "APP_YMGYH_GPWD";//APP_YMGYH_UPWD  app-医脉-找回密码
//    private static final String PROJECT_SJRZ = "APP_YMGYH_SJRZ";//APP_YMGYH_UPWD  app-医脉-手机认证
    private static final String PROJECT = "APP_YMGYH_GPWD";//APP_YMGYH_UPWD  app-医脉-找回密码
    private String mPhone,mPicCode,mCode, newPwd;
    /**
     * 倒计时
     */
    private int time_len = 60;

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
                tvYzm.setText("重新获取(" + (59 - msg.what) + ")");

                if (59 - msg.what == 0) {
                    tvYzm.setText("获取验证码");
                    tvYzm.setEnabled(true);
                    tvYzm.setTextColor(getResources().getColor(R.color.white));
                    tvYzm.setBackgroundDrawable(getResources().getDrawable(
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

    /**
     * 判断手机格式是否正确
     */
    public static boolean isMobileNO(String mobiles) {

        Pattern p = Pattern
                .compile(/* "^((13[0-9])|(14[5,7])|(17[6-7])|(15[^4,\\D])|(18[0-9]))\\d{8}$" */"^(1[0-9])\\d{9}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        CommonUtils.initSystemBar(this);
//
//        //stone 不传 就是true 即为修改密码
//        mIsFindPwd = getIntent().getBooleanExtra(Constants.KEY_VALUE, true);
//
//        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//
//        setContentView(R.layout.activity_retrieve_pwd);
//
//        initView();
//    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_retrieve_pwd;
    }

//    private void initView() {
//        tv_title_center = (TextView) findViewById(R.id.tv_title_center);
//        btnBack = (ImageButton) findViewById(R.id.btn_rpwd_back);
//        mIvGetyzmPic = (ImageView) findViewById(R.id.iv_getyzm_pic);
//        mTvGetyzmPic = (TextView) findViewById(R.id.tv_getyzm_pic);
//        mEtPicCode = (EditText) findViewById(R.id.et_pic_code);
//        tvYzm = (TextView) findViewById(R.id.tv_yzm);
//        tvSubmit = (TextView) findViewById(R.id.tv_submit);
//        edNpwd = (EditText) findViewById(R.id.edit_new_pwd);
//        edPhone = (EditText) findViewById(R.id.edit_phone);
//        edYzm = (EditText) findViewById(R.id.edit_yzm);
//
//        mTvGetyzmPic.setOnClickListener(this);
//        tvYzm.setOnClickListener(this);
//        tvSubmit.setOnClickListener(this);
//        btnBack.setOnClickListener(this);
//
//        if (!mIsFindPwd) {
//            tv_title_center.setText("修改密码");
//        }
//
//        getVerifyPicCode();
//
//    }


    @Override
    protected void initView() {
        //stone 不传 就是true 即为修改密码
        mIsFindPwd = getIntent().getBooleanExtra(Constants.KEY_VALUE, true);

        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mIvGetyzmPic = (ImageView) findViewById(R.id.iv_getyzm_pic);
        mTvGetyzmPic = (TextView) findViewById(R.id.tv_getyzm_pic);
        mEtPicCode = (EditText) findViewById(R.id.et_pic_code);
        tvYzm = (TextView) findViewById(R.id.tv_yzm);
        tvSubmit = (TextView) findViewById(R.id.tv_submit);
        edNpwd = (EditText) findViewById(R.id.edit_new_pwd);
        edPhone = (EditText) findViewById(R.id.edit_phone);
        edYzm = (EditText) findViewById(R.id.edit_yzm);

        mTvGetyzmPic.setOnClickListener(this);
        tvYzm.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        if (!mIsFindPwd) {
            titleBarBuilder.setTitleText("修改密码");
        }else {
            titleBarBuilder.setTitleText("找回密码");
        }
    }

    @Override
    protected void initData() {
        getVerifyPicCode();
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
        ImageLoadUtils.INSTANCE.loadImageView(mIvGetyzmPic,url, R.drawable.tuxingma_failed);
    }

    private boolean checkInputByGetCode(){
        mPhone = edPhone.getText().toString().trim();
        if(TextUtils.isEmpty(mPhone)){
            ToastUtils.shortToast("请输入手机号码");
            return false;
        }

        if(!isMobileNO(mPhone)){
            ToastUtils.shortToast("请输入正确的手机号");
            return false;
        }

        mPicCode = mEtPicCode.getText().toString().trim();
        if(TextUtils.isEmpty(mPicCode)){
            ToastUtils.shortToast("请输入图形验证码");
            return false;
        }
        return true;
    }

    private void resetPwd(String phone,String newPwd,String code){
        AccountRequest.getInstance().resetPwd(phone,newPwd,code,PROJECT).subscribe(new BaseRetrofitResponse<BaseData>(){
            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog("");
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(BaseData baseData) {
                tvSubmit.setEnabled(true);
                hideProgressDialog();
                ToastUtils.shortToast("密码设置"+baseData.getMsg());
                finish();
            }

            @Override
            public void onError(Throwable e) {
                tvSubmit.setEnabled(true);
                hideProgressDialog();
                if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
                    Toast.makeText(RetrofitClient.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    if("验证失败".equals(e.getMessage())){
                        Toast.makeText(RetrofitClient.getContext(), "验证码错误，请重新输入", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RetrofitClient.getContext(), "异常提示:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * 显示进度dialog
     *
     * @param content 提示文字内容
     */
    public void showProgressDialog(String content) {
        if (null == mProgressDialog) {
            mProgressDialog = new ProgressDialog(RetrievePasswordActivity.this, content);
            mProgressDialog.setCanceledOnTouchOutside(false);

        } else {
            mProgressDialog.setTitle(content);
        }
        mProgressDialog.showProgersssDialog();
    }

    /**
     * 隐藏进度dialog
     */
    public void hideProgressDialog() {
        if (null == mProgressDialog) {
            return;
        }
        if (mProgressDialog.isShowing()) {
            mProgressDialog.closeProgersssDialog();
        }
    }

    /**
     * 验证码
     * @param etphone
     */
    private void tv_GetYZM(final String etphone) {
        if (JudgeNetIsConnectedReceiver.judgeNetIsConnected(this)) {
            AccountRequest.getInstance().getVerifiCode(etphone,mTimestamp,mPicCode,FLAG,PROJECT).subscribe(new BaseRetrofitResponse<BaseData>(){
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
                    tvYzm.setEnabled(false);
                    tvYzm.setTextColor(getResources().getColor(R.color.tab_color_nomal));
                    tvYzm.setBackgroundColor(getResources().getColor(R.color.my_line));
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    getVerifyPicCode();
                    mEtPicCode.setText("");
                }
            });
            hideKeyboard();
        } else {
            ToastUtils.shortToast( "无网络，请检查网络连接");
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

    private void checkInputBySubmit(){
        mPhone = edPhone.getText().toString().trim();
        if(TextUtils.isEmpty(mPhone)){
            ToastUtils.shortToast("请输入手机号码");
            tvSubmit.setEnabled(true);
            return ;
        }

        if(!isMobileNO(mPhone)){
            ToastUtils.shortToast("请输入正确的手机号");
            tvSubmit.setEnabled(true);
            return;
        }
        mCode = edYzm.getText().toString().trim();
        newPwd = edNpwd.getText().toString().trim();
        if(TextUtils.isEmpty(mCode)){
            ToastUtils.shortToast("请输入验证码");
            tvSubmit.setEnabled(true);
            return;
        }
        if(TextUtils.isEmpty(newPwd)){
            ToastUtils.shortToast("请输入新密码");
            tvSubmit.setEnabled(true);
            return;
        }

        if(!isValidatePwd(newPwd)){
            ToastUtils.shortToast("密码设置需为6-15位字母+数字组合，请重新输入");
            tvSubmit.setEnabled(true);
            return;
        }

        resetPwd(mPhone,newPwd,mCode);
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        switch (v.getId()) {
            case R.id.tv_getyzm_pic:
                if (JudgeNetIsConnectedReceiver.judgeNetIsConnected(this)) {
                    getVerifyPicCode();
                }else {
                    ToastUtils.shortToast("无网络，请检查网络连接");
                }
                break;
            case R.id.tv_submit:
                tvSubmit.setEnabled(false);
                checkInputBySubmit();
                break;
            case R.id.tv_yzm:
                //获取验证码
                if(checkInputByGetCode()){
                    tv_GetYZM(mPhone);
                }
                break;
            default:
                break;
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    @Override
    protected void onPause() {
        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
