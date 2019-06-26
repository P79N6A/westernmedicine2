package com.xywy.askforexpert.module.my.userinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.JudgeNetIsConnectedReceiver;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.module.discovery.medicine.common.RequestTool;
import com.xywy.askforexpert.module.my.account.request.AccountRequest;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.util.T;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.adapter.rxjava.HttpException;

/**
 * 绑定手机号
 *
 * @author shihao
 */
public class BindPhoneActivity extends YMBaseActivity implements View.OnClickListener {

    private EditText edPhone,mEtPicCode,edYzm;

    private ImageView mIvGetyzmPic;

    private TextView tvYzm, mTvGetyzmPic,tvSubmit;


    private InputMethodManager manager;
    private String etphone, mPicCode,etYzm;

    private static final String FLAG = "APP_FLAG";
    private String mTimestamp;
    private static final String PROJECT_SJRZ = "APP_YMGYH_SJRZ";//APP_YMGYH_UPWD  app-医脉-手机认证
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



    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void beforeViewBind() {

    }


    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("绑定手机号");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        edPhone = (EditText) findViewById(R.id.edit_phone);
        mEtPicCode = (EditText) findViewById(R.id.et_pic_code);
        mIvGetyzmPic = (ImageView) findViewById(R.id.iv_getyzm_pic);
        mTvGetyzmPic = (TextView) findViewById(R.id.tv_getyzm_pic);
        edYzm = (EditText) findViewById(R.id.edit_yzm);
        tvYzm = (TextView) findViewById(R.id.tv_yzm);
        mTvGetyzmPic.setOnClickListener(this);
        tvYzm.setOnClickListener(this);
        tvSubmit = (TextView) findViewById(R.id.tv_submit);
        tvSubmit.setOnClickListener(this);

//        tvYzm.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                etphone = edPhone.getText().toString().trim();
//                hideKeyboard();
//
//                if (!"".equals(etphone)) {
//                    if (isMobileNO(etphone)) {
//                        tvYzm.setEnabled(false);
//                        sendCode(etphone);
//
//                        hideKeyboard();
//                    } else {
//                        ToastUtils.shortToast(
//                                "手机号码有误，请重新输入！");
//                    }
//                } else {
//                    ToastUtils.shortToast("请输入手机号");
//                }
//            }
//        });

//        tvSubmit.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                etphone = edPhone.getText().toString().trim();
//                etYzm = edYzm.getText().toString().trim();
//                hideKeyboard();
//                if (!"".equals(etphone)) {
//                    if (isMobileNO(etphone)) {
//                        if (!"".equals(etYzm)) {
//                            submitData(etphone);
//                        } else {
//                            ToastUtils.shortToast(
//                                    "请输入验证码");
//                        }
//                    } else {
//                        ToastUtils.shortToast("手机号码有误，请重新输入！");
//                    }
//                } else {
//                    ToastUtils.shortToast("请输入手机号");
//                }
//
//            }
//        });
    }

    @Override
    protected void initData() {
        getVerifyPicCode();
    }

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
     * 绑定手机
     *
     * @param str
     */
    private void submitData(final String str) {
        String yzm = edYzm.getText().toString().trim();
        AjaxParams params = new AjaxParams();
        String md5 = MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid() + str
                + Constants.MD5_KEY);
        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put("phone", str);
        params.put("sign", md5);
        params.put("code", yzm);

        FinalHttp ft = new FinalHttp();
        ft.post(CommonUrl.BIND_PHONE, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 0) {
                        ToastUtils.shortToast( "绑定成功");
                        Intent intent = new Intent(BindPhoneActivity.this,
                                BindTelSusActivity.class);
                        intent.putExtra("phonenum", str);
                        startActivity(intent);
                    } else {
                        ToastUtils.shortToast( msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }

        });
    }

//    /**
//     * 获取验证码
//     *
//     * @param str 手机号
//     */
//    public void sendCode(String str) {
//        String keyCode = MD5Util.MD5(str + Constants.MD5_KEY);
//        AjaxParams params = new AjaxParams();
//        params.put("phone", str);
//        params.put("from", "bind");
//        params.put("sign", keyCode);
//        FinalHttp ft = new FinalHttp();
//        ft.post(CommonUrl.SEND_CODE, params, new AjaxCallBack() {
//            @Override
//            public void onSuccess(String t) {
//                super.onSuccess(t);
//                try {
//                    JSONObject jsonObject = new JSONObject(t);
//                    int code = jsonObject.getInt("code");
//                    String msg = jsonObject.getString("msg");
//                    if (code == 0) {
//                        handler.sendEmptyMessage(110);
//                        ToastUtils.shortToast("短信验证码已发送至手机： " + edPhone.getText().toString()
//                                        + "，请在三分钟内完成验证。");
//                        tvYzm.setEnabled(false);
//                        tvYzm.setTextColor(getResources().getColor(
//                                R.color.tab_color_nomal));
//                        tvYzm.setBackgroundColor(getResources().getColor(
//                                R.color.my_line));
//                    } else if (code == -1) {
//                        ToastUtils.shortToast( msg);
//                        tvYzm.setEnabled(true);
//                    } else {
//                        ToastUtils.shortToast( msg);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Throwable t, int errorNo, String strMsg) {
//                super.onFailure(t, errorNo, strMsg);
//            }
//
//        });
//    }

    /**
     * 验证码
     * @param etphone
     */
    private void tv_GetYZM(final String etphone) {
        if (isMobileNO(etphone)) {
            if (JudgeNetIsConnectedReceiver.judgeNetIsConnected(this)) {
                AccountRequest.getInstance().getVerifiCode(etphone,mTimestamp,mPicCode,FLAG,PROJECT_SJRZ).subscribe(new BaseRetrofitResponse<BaseData>(){
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
        } else {
            ToastUtils.shortToast("手机号码有误，请重新输入！");
        }

    }
    /**
     * 倒计时60秒线程
     */
    private void getStop() {
        time_len = 60;
        // tv_getyzm.setBackgroundResource(R.drawable.yuanjiao_huis);// 按钮背景改变
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
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
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

    private boolean checkInputByGetCode(){
        etphone = edPhone.getText().toString().trim();
        if(TextUtils.isEmpty(etphone)){
            ToastUtils.shortToast("请输入手机号码");
            return false;
        }

        if (!isMobileNO(etphone)) {
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
            case R.id.tv_yzm:
                //获取验证码
                if(checkInputByGetCode()){
                    tv_GetYZM(etphone);
                }
                break;
            case R.id.tv_submit:
                view.setEnabled(false);
                etphone = edPhone.getText().toString().trim();
                if(TextUtils.isEmpty(etphone)){
                    ToastUtils.shortToast("请输入手机号码");
                    view.setEnabled(true);
                    return;
                }

                if (!isMobileNO(etphone)) {
                    ToastUtils.shortToast("请输入正确的手机号");
                    view.setEnabled(true);
                    return;
                }
                etYzm = edYzm.getText().toString().trim();
                if(TextUtils.isEmpty(etYzm)){
                    ToastUtils.shortToast("请输入验证码");
                    view.setEnabled(true);
                    return;
                }
                bindPhone(etphone, YMUserService.getCurUserId(),etYzm);
                break;
            default:
                break;
        }



    }

    private void bindPhone(String phone, String uid, String code) {
        //调用新的获取验证码的接口
        AccountRequest.getInstance().bindPhone(phone, uid,code,PROJECT_SJRZ).subscribe(new BaseRetrofitResponse<BaseData>(){
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
                ToastUtils.shortToast("绑定"+baseData.getMsg());
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
}
