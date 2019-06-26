package com.xywy.askforexpert.module.my.userinfo;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.module.message.friend.InviteNewFriendActivity;

/**
 * 绑定手机号成功
 *
 * @author shihao
 */
public class BindTelSusActivity extends YMBaseActivity {
    private String phone = "";
    private TextView tvPhone;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bind_success;
    }

    @Override
    protected void beforeViewBind() {
        try {
            phone = getIntent().getStringExtra("phone");
            if (TextUtils.isEmpty(phone)) {
                LogUtils.e("绑定手机号参数异常:null");
                shortToast("绑定失败 请重新绑定");
                finish();
            }
        } catch (Exception e) {
            LogUtils.e("绑定手机号参数异常:" + e.getMessage());
            shortToast("绑定失败 请重新绑定");
            e.printStackTrace();
            finish();
        }

    }

    @Override
    protected void initView() {
        tvPhone = (TextView) findViewById(R.id.tv_tel_phone);
    }

    @Override
    protected void initData() {
        titleBarBuilder.setTitleText("绑定手机号");
        tvPhone.setText(phone);
    }

    public void onBindSuccessListener(View v) {
        switch (v.getId()) {
            case R.id.btn_change_phone_num:
                StatisticalTools.eventCount(this, "phonenumber");
                Intent intent = new Intent(BindTelSusActivity.this,
                        BindPhoneActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_look_phone:
                StatisticalTools.eventCount(this, "addressbook");
                Intent intentlook = new Intent(BindTelSusActivity.this,
                        InviteNewFriendActivity.class);
                intentlook.putExtra("type", "add");
                startActivity(intentlook);

                break;
        }
    }
}
