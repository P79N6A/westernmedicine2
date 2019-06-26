package com.xywy.askforexpert.module.my.account;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
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
 * 项目名称：D_Platform
 * 类名称：ChangePwdActivity
 * 类描述：修改密码
 * 创建人：shihao
 * 创建时间：2015-5-22 下午2:46:37
 * 修改备注：
 */
public class ChangePwdActivity extends YMBaseActivity implements View.OnClickListener {

//    private static final String PROJECT_UPWD = "APP_YMGYH_UPWD";//APP_YMGYH_UPWD  app-医脉-修改密码
//    private static final String PROJECT_SJRZ = "APP_YMGYH_SJRZ";//APP_YMGYH_UPWD  app-医脉-手机认证
    private static final String PROJECT = "APP_YMGYH_UPWD";//APP_YMGYH_UPWD  app-医脉-修改密码
    private EditText mEtPhone,mEtPicCode,mEtCode,etOldPwd, etNewPwd;

    private String mCode,oldPwd, newPwd;

    private TextView tvSubmit,mTvGetyzmPic,mTvGetyzm;

    private ImageButton ivBtnback;

    private ImageView mIvGetyzmPic;

    private ProgressDialog mProgressDialog;
    private String mPhone;
    private String mPicCode;
    private static final String FLAG = "APP_FLAG";
    private String mTimestamp;
    private InputMethodManager manager;

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
                mTvGetyzm.setText("重新获取(" + (59 - msg.what) + ")");

                if (59 - msg.what == 0) {
                    mTvGetyzm.setText("获取验证码");
                    mTvGetyzm.setEnabled(true);
                    mTvGetyzm.setTextColor(getResources().getColor(R.color.white));
                    mTvGetyzm.setBackgroundDrawable(getResources().getDrawable(R.drawable.yzm_bg_selector));
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

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        CommonUtils.initSystemBar(this);
//        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        setContentView(R.layout.activity_change_pwd);
//
//        mEtPhone = (EditText) findViewById(R.id.et_phone);
//        mEtPicCode = (EditText) findViewById(R.id.et_pic_code);
//        mIvGetyzmPic = (ImageView) findViewById(R.id.iv_getyzm_pic);
//        mTvGetyzmPic = (TextView) findViewById(R.id.tv_getyzm_pic);
//        mTvGetyzm = (TextView) findViewById(R.id.tv_getyzm);
//        mEtCode = (EditText) findViewById(R.id.et_code);
//        etOldPwd = (EditText) findViewById(R.id.et_old_pwd);
//
//        etNewPwd = (EditText) findViewById(R.id.et_new_pwd);
//
//        tvSubmit = (TextView) findViewById(R.id.tv_submit);
//
//
//        ivBtnback = (ImageButton) findViewById(R.id.btn_change_pwd_back);
//        mTvGetyzmPic.setOnClickListener(this);
//        mTvGetyzm.setOnClickListener(this);
//        tvSubmit.setOnClickListener(this);
//
//        ivBtnback.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        getVerifyPicCode();
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_change_pwd;
    }
    @Override
    protected void initView() {
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        titleBarBuilder.setTitleText("修改密码");
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtPicCode = (EditText) findViewById(R.id.et_pic_code);
        mIvGetyzmPic = (ImageView) findViewById(R.id.iv_getyzm_pic);
        mTvGetyzmPic = (TextView) findViewById(R.id.tv_getyzm_pic);
        mTvGetyzm = (TextView) findViewById(R.id.tv_getyzm);
        mEtCode = (EditText) findViewById(R.id.et_code);
        etOldPwd = (EditText) findViewById(R.id.et_old_pwd);

        etNewPwd = (EditText) findViewById(R.id.et_new_pwd);

        tvSubmit = (TextView) findViewById(R.id.tv_submit);

        mTvGetyzmPic.setOnClickListener(this);
        mTvGetyzm.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        getVerifyPicCode();
    }

    private void updatePwd(String phone,String pwd,String newPwd,String code,String uid){
        AccountRequest.getInstance().updatePwd(phone,pwd,newPwd,code,uid,PROJECT).subscribe(new BaseRetrofitResponse<BaseData>(){
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
                ToastUtils.shortToast("密码修改"+baseData.getMsg());
            }

            @Override
            public void onError(Throwable e) {
                tvSubmit.setEnabled(true);
                hideProgressDialog();
                if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
                    Toast.makeText(RetrofitClient.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    mEtPicCode.setText("");
                    mEtCode.setText("");
                    getVerifyPicCode();
                    if("验证失败".equals(e.getMessage())){
                        Toast.makeText(RetrofitClient.getContext(), "验证码错误，请重新输入", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RetrofitClient.getContext(),"异常提示:" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            mProgressDialog = new ProgressDialog(ChangePwdActivity.this, content);
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

    private void checkInputByGetCode(){
        mPhone = mEtPhone.getText().toString().trim();
        if(TextUtils.isEmpty(mPhone)){
            ToastUtils.shortToast("请输入手机号码");
            return ;
        }

        if(!isMobileNO(mPhone)){
            ToastUtils.shortToast("请输入正确的手机号");
            return ;
        }

        mPicCode = mEtPicCode.getText().toString().trim();
        if(TextUtils.isEmpty(mPicCode)){
            ToastUtils.shortToast("请输入图形验证码");
            return ;
        }
    }

    private void checkInputBySubmit(){
        mPhone = mEtPhone.getText().toString().trim();
        if(TextUtils.isEmpty(mPhone)){
            ToastUtils.shortToast("请输入手机号码");
            tvSubmit.setEnabled(true);
            return;
        }
        if(!isMobileNO(mPhone)){
            ToastUtils.shortToast("请输入正确的手机号");
            tvSubmit.setEnabled(true);
            return;
        }
        mCode = mEtCode.getText().toString().trim();
        oldPwd = etOldPwd.getText().toString().trim();
        newPwd = etNewPwd.getText().toString().trim();
        if(TextUtils.isEmpty(mCode)){
            ToastUtils.shortToast("请输入验证码");
            tvSubmit.setEnabled(true);
            return;
        }
        if(TextUtils.isEmpty(oldPwd)){
            ToastUtils.shortToast("原密码不能为空");
            tvSubmit.setEnabled(true);
            return;
        }
        if(TextUtils.isEmpty(newPwd)){
            ToastUtils.shortToast("新密码不能为空");
            tvSubmit.setEnabled(true);
            return;
        }
        if (oldPwd.equals(newPwd)) {
            ToastUtils.shortToast("新旧密码不能相同");
            tvSubmit.setEnabled(true);
            return;
        }

        if(!isValidatePwd(newPwd)){
            ToastUtils.shortToast("密码设置需为6-15位字母+数字组合，请重新输入");
            tvSubmit.setEnabled(true);
            return;
        }

        updatePwd(mPhone,oldPwd,newPwd,mCode, YMUserService.getCurUserId());
    }

    /**
     * 判断是否全是数字或全是字母
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
                    mTvGetyzm.setEnabled(false);
                    mTvGetyzm.setTextColor(getResources().getColor(R.color.tab_color_nomal));
                    mTvGetyzm.setBackgroundColor(getResources().getColor(R.color.my_line));
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
     * 判断手机格式是否正确
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile(/* "^((13[0-9])|(14[5,7])|(17[6-7])|(15[^4,\\D])|(18[0-9]))\\d{8}$" */"^(1[0-9])\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
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

    public void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    public void onPause() {
        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_getyzm_pic:
                if (JudgeNetIsConnectedReceiver.judgeNetIsConnected(this)) {
                    getVerifyPicCode();
                }else {
                    ToastUtils.shortToast("无网络，请检查网络连接");
                }
                break;
            case R.id.tv_getyzm:
                //获取验证码
                checkInputByGetCode();
                tv_GetYZM(mPhone);
                break;
            case R.id.tv_submit:
                //确定
                tvSubmit.setEnabled(false);
                checkInputBySubmit();
                break;
            default:
                break;
        }
    }
}
