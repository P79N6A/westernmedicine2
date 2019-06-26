package com.xywy.oauth.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import com.xywy.component.datarequest.neworkWrapper.BaseData;
import com.xywy.component.datarequest.neworkWrapper.IDataResponse;
import com.xywy.oauth.R;
import com.xywy.oauth.login.DatabaseRequestType;
import com.xywy.oauth.model.LoginModel;
import com.xywy.oauth.model.UserInfoCenter;
import com.xywy.oauth.model.entity.LoginEntity;
import com.xywy.oauth.service.DataRequestTool;
import com.xywy.oauth.service.LoginServiceProvider;
import com.xywy.oauth.utils.ButtonClickableUtil;
import com.xywy.oauth.utils.CommonUtils;
import com.xywy.oauth.utils.NetUtils;
import com.xywy.oauth.utils.RexUtil;
import com.xywy.oauth.utils.TimeUtils;

/**
 * Created by DongJr on 2016/3/14.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    EditText etPhoneNo;
    EditText etPwd;
    Button btnLogin;
    TextView tvTitle;
    TextView tvForgetPwd;


    private LoginModel loginModel = new LoginModel();
    private String Login_Action = "LOGIN_FINISH";
    private MyBroadCast mBroadCast;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_login);
        bindView();
        initView();
        initBroadCast();
        initListener();
    }

    private void bindView() {
        etPhoneNo = findView(R.id.et_phoneNo);
        etPwd = findView(R.id.et_pwd);
        btnLogin = findView(R.id.btn_login);
        tvTitle = findView(R.id.tv_title);
        tvForgetPwd = findView(R.id.tv_forget_pwd);
    }

    private void initBroadCast() {
        mBroadCast = new MyBroadCast();
        IntentFilter IntentFilter = new IntentFilter();
        IntentFilter.addAction(Login_Action);
        registerReceiver(mBroadCast, IntentFilter);
    }

    protected void initView() {
        ButtonClickableUtil.setButtonClickable(etPhoneNo, etPwd, null, btnLogin);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.regist);
    }

    private void initListener() {
        btnLogin.setOnClickListener(this);
        tvTitle.setOnClickListener(this);
        tvForgetPwd.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
//        if (ThirdLoginFragment.mSsoHandler != null) {
//            ThirdLoginFragment.mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
//        }

    }

    @Override
    public void onClick(View view) {

        int viewId = view.getId();
        if (viewId == R.id.btn_login) {//普通登录
            login();
        } else if (viewId == R.id.tv_title) {//注册
            startActivity(new Intent(this, RegistActivity.class));
        } else if (viewId == R.id.tv_forget_pwd) {//忘记密码
            startActivity(new Intent(this, FindPasswordActivity.class));
        }

    }

    private void login() {
        if (NetUtils.isConnected(this)) {
            String phone = etPhoneNo.getText().toString().trim();
            String password = etPwd.getText().toString();
            if (phone.equals("") || phone.length() == 0) {
                CommonUtils.showToast(getResources().getString(R.string.input_phoneno));
                return;
            } else if (!RexUtil.isMobile(phone)) {
                CommonUtils.showToast(getResources().getString(R.string.input_right_phoneno));
                return;
            }
            showDialog();
            LoginServiceProvider.login(phone, password, new LoginResponse(), DatabaseRequestType.Login);
        } else {
            CommonUtils.showToast(getResources().getString(R.string.no_network));
        }
    }

    class LoginResponse implements IDataResponse {

        @Override
        public void onResponse(BaseData obj) {
            if (obj != null) {
                hideDialog();
                boolean isSuccess = DataRequestTool.noError(LoginActivity.this, obj, false);
                if (isSuccess) {
                    LoginEntity data = (LoginEntity) obj.getData();
                    LoginEntity.DataEntity info = data.getData();
                    loginModel.setPhoto(info.getPhoto());
                    loginModel.setBirthday(info.getBirthday());
                    loginModel.setAge(info.getAge());
                    loginModel.setBlood_pressure(info.getBlood_pressure());
                    loginModel.setBlood_type(info.getBlood_type());
                    loginModel.setDevid(info.getDevid());
                    loginModel.setFirst_login_time(info.getFirst_login_time());
                    loginModel.setHeight(info.getHeight());
                    loginModel.setIdcard(info.getIdcard());
                    loginModel.setInsert_data(info.getInsert_data());
                    loginModel.setNickname(info.getNickname());
                    loginModel.setPoints(info.getPoints());
                    loginModel.setRealname(info.getRealname());
                    loginModel.setUserid(info.getUserid());
                    loginModel.setSex(info.getSex());
                    loginModel.setUserphone(info.getUserphone());
                    loginModel.setUsersource(info.getUsersource());
                    loginModel.setWeight(info.getWeight());
                    loginModel.setUseremail(info.getUseremail());
                    loginModel.setType(info.getType());
                    loginModel.setLasttime(info.getLasttime());
                    loginModel.setUsername(info.getUsername());
                    loginModel.setQz_login_time(TimeUtils.getSecondTime());

                    //保存登录状态
                    UserInfoCenter.getInstance().setLoginModel(loginModel);
                    CommonUtils.showToast(R.string.login_success);
                    finish();
                } else {
                    if ("network error".equals(obj.getMsg())) {
                        CommonUtils.showToast(getResources().getString(R.string.login_net_work_error_msg));
                    } else {
                        CommonUtils.showToast(obj.getMsg());
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
