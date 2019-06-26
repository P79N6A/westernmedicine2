package com.xywy.oauth.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import com.xywy.component.datarequest.neworkWrapper.BaseData;
import com.xywy.component.datarequest.neworkWrapper.IDataResponse;
import com.xywy.oauth.R;
import com.xywy.oauth.login.DatabaseRequestType;
import com.xywy.oauth.service.DataRequestTool;
import com.xywy.oauth.service.LoginServiceProvider;
import com.xywy.oauth.service.NetworkConstants;
import com.xywy.oauth.utils.ButtonClickableUtil;
import com.xywy.oauth.utils.CommonUtils;
import com.xywy.oauth.utils.CountDownTimeUtil;
import com.xywy.oauth.utils.MsgForCode;
import com.xywy.oauth.utils.NetUtils;
import com.xywy.oauth.utils.RexUtil;

/**
 * Created by DongJr on 2016/3/21.
 */
public class FindPasswordActivity extends BaseActivity implements View.OnClickListener {

    RelativeLayout Lback;
    TextView titleName;
    EditText etPhoneNo;
    EditText etCode;
    TextView getCodeBtn;
    EditText etPwd;
    Button btnConfirm;

    private CountDownTimeUtil count;
    private String Find_Action = "FIND_FINISH";
    private MyBroadCast mBroadCast;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_find_password);
        bindView();
        initView();
        initListener();
        initBroadCast();
    }


    private void bindView() {
        Lback = findView(R.id.Lback);
        titleName = findView(R.id.title_name);
        etPhoneNo = findView(R.id.et_phoneNo);
        etCode = findView(R.id.et_code);
        getCodeBtn = findView(R.id.get_code_btn);
        etPwd = findView(R.id.et_pwd);
        btnConfirm = findView(R.id.btn_confirm);
    }

    private void initBroadCast() {
        mBroadCast = new MyBroadCast();
        IntentFilter IntentFilter = new IntentFilter();
        IntentFilter.addAction(Find_Action);
        registerReceiver(mBroadCast, IntentFilter);
    }

    protected void initView() {
        ButtonClickableUtil.setButtonClickable(etPhoneNo, etPwd, etCode, btnConfirm);
        titleName.setText(R.string.find_password);
        Lback.setVisibility(View.VISIBLE);
    }

    private void initListener() {
        getCodeBtn.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        Lback.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //一定要在与fragment绑定的activity中重写此方法，不然无法回调
        if (requestCode == Constants.REQUEST_API ||
                requestCode == Constants.REQUEST_LOGIN) {
            Tencent.handleResultData(data, ThirdLoginFragment.mTencentListener);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.get_code_btn) {
            isFormatRight(etPhoneNo.getText().toString().trim());
        } else if (i == R.id.btn_confirm) {
            findPassword(etPhoneNo.getText().toString().trim());
        } else if (i == R.id.Lback) {
            finish();
        }
    }

    private void findPassword(String phone) {
        if (NetUtils.isConnected(this)) {
            if (!RexUtil.isMobile(phone)) {
                CommonUtils.showToast(getResources().getString(R.string.input_right_phoneno));
            } else {
                getPassword(phone);
            }
        } else {
            CommonUtils.showToast(getResources().getString(R.string.no_network));
        }
    }

    private void getPassword(String phone) {
        String code = etCode.getText().toString().trim();
        String password = etPwd.getText().toString();
        if (!RexUtil.passwordMatch(password)) {
            CommonUtils.showToast(R.string.password_new_length);
        } else {
            showDialog();
            LoginServiceProvider.findPassword(phone, code, password, new FindPasswordResponse(), DatabaseRequestType.Find_password);
        }
    }

    /**
     * 验证手机号,并获取验证码
     */
    public void isFormatRight(String phone) {
        if (NetUtils.isConnected(this)) {
            if (phone.equals("") || phone.length() == 0) {
                CommonUtils.showToast(getResources().getString(R.string.input_phoneno_get_code));
            } else if (!RexUtil.isMobile(phone)) {
                CommonUtils.showToast(getResources().getString(R.string.input_right_phoneno_get_code));
            } else {
                getCode(phone);
                if (count == null) {
                    count = new CountDownTimeUtil(60 * 1000, 1000, getCodeBtn);
                    count.start();
                } else {
                    count.start();
                }
            }
        } else {
            CommonUtils.showToast(getResources().getString(R.string.no_network));
        }
    }

    private void getCode(String phone) {
        LoginServiceProvider.getCode(phone, new GetCodeResponse(), NetworkConstants.forget_project_value, DatabaseRequestType.Code);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (count != null) {
            count.cancel();
        }
        if (mBroadCast != null) {
            unregisterReceiver(mBroadCast);
        }
    }

    /**
     * 找回密码
     */
    class FindPasswordResponse implements IDataResponse {

        @Override
        public void onResponse(BaseData obj) {
            if (obj != null) {
                hideDialog();
                if (count != null) {
                    count.reSend();
                }
                boolean noError = DataRequestTool.noError(FindPasswordActivity.this, obj, false);
                if (noError) {
                    CommonUtils.showToast(R.string.find_password_success);
                    finish();
                } else {
                    /**
                     * 本接口返回码： 10000(成功)  30000(未知错误)  31001(行为不存在)  31002(project字段不能为空)  31003(手机号码不正确)  31005(N秒以内禁止重复发送（N为配置值）)  31006(其他原因导致发送失败)  31007(验证码为空)  31008(验证不通过)  31058(该用户不存在)  31059(修改失败)  31060(手机号没有绑定)
                     */
                    String code = String.valueOf(obj.getCode());
                    if ("31003".equals(code)) {
                        CommonUtils.showToast("手机号码不正确");
                    } else if ("31008".equals(code)) {
                        CommonUtils.showToast("验证码错误");
                    } else if ("31005".equals(code)) {
                        CommonUtils.showToast("验证码发送太频繁，请稍后再试");
                    } else if ("31058".equals(code)) {
                        CommonUtils.showToast("该用户不存在");
                    } else if ("31060".equals(code)) {
                        CommonUtils.showToast("手机号没有绑定");
                    } else {
                        CommonUtils.showToast(R.string.find_password_fail);
                    }
                }
            }

        }
    }

    /**
     * 获取验证码接口
     */
    class GetCodeResponse implements IDataResponse {
        @Override
        public void onResponse(BaseData obj) {
            if (obj != null) {
                boolean isSucess = DataRequestTool.noError(FindPasswordActivity.this, obj, false);
                if (isSucess) {
                    CommonUtils.showToast(getResources().getString(R.string.send_code));
                } else {
                    if (count != null) {
                        count.reSend();
                    }
                    CommonUtils.showToast(MsgForCode.getMsg(String.valueOf(obj.getCode())));
                }
            }
        }
    }

    //自定义广播，用来销毁activity
    class MyBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }
}
