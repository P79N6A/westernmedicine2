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
import com.xywy.oauth.model.LoginModel;
import com.xywy.oauth.model.UserInfoCenter;
import com.xywy.oauth.model.entity.RegisterEntity;
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
 * Created by DongJr on 2016/3/14.
 */
public class RegistActivity extends BaseActivity implements View.OnClickListener {


    RelativeLayout Lback;
    TextView titleName;
    TextView tvTitle;
    EditText etPhoneNo;
    TextView getCodeBtn;
    EditText etPwd;
    EditText etCode;
    Button btnRegist;

    private CountDownTimeUtil count;
    private MyBroadCast mBroadCast;
    private String register_Action = "REGISTER_FINISH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        bindView();
        initView();
        initBroadCast();
        initListener();
    }

    private void bindView() {
        Lback = findView(R.id.Lback);
        titleName = findView(R.id.title_name);
        tvTitle = findView(R.id.tv_title);
        etPhoneNo = findView(R.id.et_phoneNo);
        getCodeBtn = findView(R.id.get_code_btn);
        etPwd = findView(R.id.et_pwd);
        etCode = findView(R.id.et_code);
        btnRegist = findView(R.id.btn_regist);
    }

    protected void initView() {
        ButtonClickableUtil.setButtonClickable(etPhoneNo, etPwd, etCode, btnRegist);
        titleName.setText(R.string.regist);
        Lback.setVisibility(View.VISIBLE);
    }

    private void initBroadCast() {
        mBroadCast = new MyBroadCast();
        IntentFilter IntentFilter = new IntentFilter();
        IntentFilter.addAction(register_Action);
        registerReceiver(mBroadCast, IntentFilter);
    }

    private void initListener() {
        Lback.setOnClickListener(this);
        getCodeBtn.setOnClickListener(this);
        btnRegist.setOnClickListener(this);
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
        } else if (i == R.id.btn_regist) {
            String password = etPwd.getText().toString();
            regist(etPhoneNo.getText().toString().trim(), password);
        } else if (i == R.id.Lback) {
            finish();
        }
    }

    private void finishActivity() {
        Intent intent = new Intent();
        intent.setAction("LOGIN_FINISH");
        sendBroadcast(intent);
        finish();
    }

    private void regist(String phone, String password) {
        if (NetUtils.isConnected(this)) {
            if (!RexUtil.isMobile(phone)) {
                CommonUtils.showToast(getResources().getString(R.string.input_right_phoneno));
                return;
            }
            if (!RexUtil.passwordMatch(password)) {
                CommonUtils.showToast(R.string.password_new_length);
                return;
            }
            startRegist(phone, password);

        } else {
            CommonUtils.showToast(getResources().getString(R.string.no_network));
        }
    }

    private void startRegist(String phone, String password) {
        showDialog();
        String code = etCode.getText().toString().trim();

        LoginServiceProvider.register(phone, code, password, new registResponse(), DatabaseRequestType.Register);
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
        LoginServiceProvider.getCode(phone, new getCodeResponse(), NetworkConstants.regist_project_value, DatabaseRequestType.Code);
    }

    /**
     * 获取验证码接口
     */
    class getCodeResponse implements IDataResponse {

        @Override
        public void onResponse(BaseData obj) {
            if (obj != null) {
                boolean isSucess = DataRequestTool.noError(RegistActivity.this, obj, false);
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

    /**
     * 注册接口
     */
    class registResponse implements IDataResponse {

        @Override
        public void onResponse(BaseData obj) {
            if (obj != null) {
                hideDialog();
                if (count != null) {
                    count.reSend();
                }
                boolean noError = DataRequestTool.noError(RegistActivity.this, obj, false);
                if (noError) {

                    LoginModel loginModel = new LoginModel();
                    RegisterEntity entity = ((RegisterEntity) obj.getData());
                    loginModel.setUserid(entity.getData().getId());
                    loginModel.setPhoto(entity.getData().getPhoto());
                    loginModel.setNickname(entity.getData().getNickname());
                    loginModel.setUserphone(entity.getData().getUserphone());
                    UserInfoCenter.getInstance().setLoginModel(loginModel);

                    CommonUtils.showToast(R.string.regist_success);

                    finishActivity();
                } else {
                    if (getResources().getString(R.string.repetition_code)
                            .equals(String.valueOf(obj.getCode()))) {
                        CommonUtils.showToast(getResources().getString(R.string.repetition_code_text));
                    } else if (getResources().getString(R.string.fail_code)
                            .equals(String.valueOf(obj.getCode()))) {
                        CommonUtils.showToast(getResources().getString(R.string.fail_code_text));
                    } else {
                        CommonUtils.showToast(getResources().getString(R.string.no_network));
                    }
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
