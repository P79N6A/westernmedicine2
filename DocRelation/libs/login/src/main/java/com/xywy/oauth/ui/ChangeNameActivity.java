package com.xywy.oauth.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.xywy.oauth.utils.CommonUtils;
import com.xywy.oauth.utils.NetUtils;

/**
 * Created by DongJr on 2016/3/21.
 */
public class ChangeNameActivity extends BaseActivity implements View.OnClickListener {

    EditText etChangeName;
    ImageView ivClear;
    RelativeLayout Lback;
    TextView titleName;
    Button btnChangeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        bindView();
        initView();
        initListener();
    }

    private void bindView() {
        etChangeName = findView(R.id.et_change_name);
        ivClear = findView(R.id.iv_clear);
        Lback = findView(R.id.Lback);
        titleName = findView(R.id.title_name);
        btnChangeName = findView(R.id.btn_change_name);
    }

    protected void initView() {
        Lback.setVisibility(View.VISIBLE);
        titleName.setText(R.string.user_info);
        String username = UserInfoCenter.getInstance().getLoginModel().getNickname();
        etChangeName.setText(username);
    }

    private void initListener() {
        ivClear.setOnClickListener(this);
        btnChangeName.setOnClickListener(this);
        Lback.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_clear) {
            String text = etChangeName.getText().toString();
            if (!TextUtils.isEmpty(text)) {
                etChangeName.setText("");
            }
        } else if (i == R.id.btn_change_name) {
            changeName();
        } else if (i == R.id.Lback) {
            finish();
        }
    }

    private void changeName() {
        if (NetUtils.isConnected(this)) {
            String nickName = etChangeName.getText().toString().trim();
            LoginModel model = UserInfoCenter.getInstance().getLoginModel();
            if (model.getNickname().equals(nickName)) {
                CommonUtils.showToast(R.string.change_name);
//            }else if (!RexUtil.nicknameMatch(nickName)){
//                CommonUtils.showToast(R.string.change_name_rex);
            } else if (nickName.equals("")) {
                CommonUtils.showToast(R.string.change_name_rex);
            } else {
                showDialog();
                LoginServiceProvider.changeUserName(Integer.parseInt(model.getUserid()), nickName,
                        new ChangeNameResponse(), DatabaseRequestType.Change_Name);
            }
        } else {
            CommonUtils.showToast(R.string.no_network);
        }
    }

    class ChangeNameResponse implements IDataResponse {

        @Override
        public void onResponse(BaseData obj) {
            if (obj != null) {
                hideDialog();
                boolean noError = DataRequestTool.noError(ChangeNameActivity.this, obj, false);
                if (noError) {
                    CommonUtils.showToast(obj.getMsg());
                    LoginModel loginModel = UserInfoCenter.getInstance().getLoginModel();
                    loginModel.setNickname(etChangeName.getText().toString().trim());
                    UserInfoCenter.getInstance().setLoginModel(loginModel);
                    finish();
                } else {
                    String code = String.valueOf(obj.getCode());
                    if ("31050".equals(code) || "31058".equals(code)) {
                        CommonUtils.showToast(obj.getMsg());
                    } else {
                        CommonUtils.showToast(R.string.change_name_fail);
                    }
                }
            }
        }
    }
}
