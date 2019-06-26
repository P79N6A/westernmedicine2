package com.xywy.oauth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.oauth.R;
import com.xywy.oauth.model.LoginModel;
import com.xywy.oauth.model.UserInfoCenter;
import com.xywy.oauth.utils.CommonUtils;
import com.xywy.oauth.utils.RexUtil;
import com.xywy.oauth.utils.XYImageLoader;


/**
 * Created by DongJr on 2016/3/21.
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    RelativeLayout Lback;
    TextView titleName;
    TextView tvTitle;
    FrameLayout userHead;
    FrameLayout userPhone;
    FrameLayout changePwd;
    FrameLayout userName;
    Button btnLogout;
    ImageView ivHead;
    ImageView ivBindPhone;
    TextView tvPhone;


    private LoginModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        bindView();
        initView();
        initListener();
    }

    private void bindView() {

        Lback = findView(R.id.Lback);
        titleName = findView(R.id.title_name);
        tvTitle = findView(R.id.tv_title);
        userHead = findView(R.id.user_head);
        userPhone = findView(R.id.user_phone);
        userName = findView(R.id.user_name);
        titleName = findView(R.id.title_name);
        changePwd = findView(R.id.change_pwd);
        btnLogout = findView(R.id.btn_logout);
        ivHead = findView(R.id.iv_head);
        ivBindPhone = findView(R.id.iv_bind_phone);
        tvPhone = findView(R.id.tv_phone);
    }

    protected void initView() {
        Lback.setVisibility(View.VISIBLE);
        titleName.setText(R.string.user_info);
        model = UserInfoCenter.getInstance().getLoginModel();
        initHead(model);
        initPhone(model);
    }

    private void initHead(LoginModel model) {
        if (model.getPhoto() == null || model.getPhoto().trim().equals("")) {
            ivHead.setImageResource(R.drawable.icon_default_head);
        } else {
            XYImageLoader.getInstance().displayUserHeadImage(model.getPhoto(), ivHead);
        }
    }

    private void initPhone(LoginModel model) {
        if (model.getUserphone() == null || model.getUserphone().trim().equals("")) {
            ivBindPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserInfoActivity.this, BindPhoneActivity.class));
                }
            });
        } else {
            tvPhone.setVisibility(View.VISIBLE);
            ivBindPhone.setVisibility(View.GONE);
            String userphone = model.getUserphone();
            if (RexUtil.isMobile(userphone)) {
                userphone = userphone.substring(0, 4) + "***" + userphone.substring(userphone.length() - 4, userphone.length());
            }
            tvPhone.setText(userphone);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initPhone(UserInfoCenter.getInstance().getLoginModel());
    }

    private void initListener() {
        Lback.setOnClickListener(this);
        userHead.setOnClickListener(this);
        userName.setOnClickListener(this);
        changePwd.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.Lback) {
            finish();
        } else if (id == R.id.user_name) {
            startActivity(new Intent(this, ChangeNameActivity.class));
        } else if (id == R.id.change_pwd) {
            if (model.getUserphone() == null || model.getUserphone().equals("")) {
                CommonUtils.showToast(R.string.please_bind_phone);
            } else {
                startActivity(new Intent(this, ChangePwdActivity.class));
            }
        } else if (id == R.id.btn_logout) {
            UserInfoCenter.getInstance().reset();
            finish();
        }
    }

}
