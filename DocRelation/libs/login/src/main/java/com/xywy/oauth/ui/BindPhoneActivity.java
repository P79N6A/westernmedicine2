package com.xywy.oauth.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.component.datarequest.neworkWrapper.BaseData;
import com.xywy.component.datarequest.neworkWrapper.IDataResponse;
import com.xywy.oauth.R;
import com.xywy.oauth.login.DatabaseRequestType;
import com.xywy.oauth.model.LoginModel;
import com.xywy.oauth.model.UserInfoCenter;
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
public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {

    RelativeLayout Lback;
    TextView titleName;
    EditText etPhoneNo;
    EditText etCode;
    TextView getCodeBtn;
    Button btnBindPhone;
    private CountDownTimeUtil count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        bindView();
        initView();
    }

    private void bindView() {
        Lback = findView(R.id.Lback);
        titleName = findView(R.id.title_name);
        etPhoneNo = findView(R.id.et_phoneNo);
        etCode = findView(R.id.et_code);
        getCodeBtn = findView(R.id.get_code_btn);
        btnBindPhone = findView(R.id.btn_bind_phone);
    }

    private void initView() {
        ButtonClickableUtil.setButtonClickable(etPhoneNo, etCode, null, btnBindPhone);
        Lback.setVisibility(View.VISIBLE);
        titleName.setText(R.string.bind_phone);

        getCodeBtn.setOnClickListener(this);
        btnBindPhone.setOnClickListener(this);
        Lback.setOnClickListener(this);
    }


    private void bindPhone() {
        showDialog();
        String phoneNo = etPhoneNo.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        LoginServiceProvider.bind(phoneNo, code, new BindResponse(), DatabaseRequestType.Bind);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.get_code_btn) {
            isFormatRight(etPhoneNo.getText().toString().trim());
        } else if (id == R.id.btn_bind_phone) {
            bindPhone();
        } else if (id == R.id.Lback) {
            finish();
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
        LoginServiceProvider.getCode(phone, new getCodeResponse(), NetworkConstants.regist_project_value, DatabaseRequestType.Code);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (count != null) {
            count.cancel();
        }
    }

    class BindResponse implements IDataResponse {
        @Override
        public void onResponse(BaseData obj) {
            if (obj != null) {
                hideDialog();
                if (count != null) {
                    count.reSend();
                }
                boolean noError = DataRequestTool.noError(BindPhoneActivity.this, obj, false);
                if (noError) {
                    CommonUtils.showToast(R.string.bind_phone_success);
                    LoginModel loginModel = UserInfoCenter.getInstance().getLoginModel();
                    loginModel.setUserphone(etPhoneNo.getText().toString().trim());
                    UserInfoCenter.getInstance().setLoginModel(loginModel);
                    finish();
                } else {
                    String code = String.valueOf(obj.getCode());
                    if ("31014".equals(code)) {
                        CommonUtils.showToast("禁止重复绑定手机号");
                    } else if ("31008".equals(code)) {
                        CommonUtils.showToast("验证码错误");
                    } else if ("31015".equals(code)) {
                        CommonUtils.showToast("该手机号已被其他帐号绑定");
                    } else {
                        CommonUtils.showToast("绑定手机失败");
                    }
                }
            }
        }
    }

    /**
     * 获取验证码接口
     */
    class getCodeResponse implements IDataResponse {

        @Override
        public void onResponse(BaseData obj) {
            if (obj != null) {
                boolean isSucess = DataRequestTool.noError(BindPhoneActivity.this, obj, false);
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
}
