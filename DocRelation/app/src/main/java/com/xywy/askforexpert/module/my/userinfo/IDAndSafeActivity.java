package com.xywy.askforexpert.module.my.userinfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.module.my.account.ChangePwdActivity;
import com.xywy.askforexpert.module.my.account.RetrievePasswordActivity;

import butterknife.OnClick;

/**
 * 账号与安全 stone
 * 项目名称：D_Platform
 * 类名称：IDAndSafeActivity
 * 创建人：shihao
 * 创建时间：2015-5-22 下午1:53:12
 * 修改备注：
 */
public class IDAndSafeActivity extends YMBaseActivity {

    private TextView tvICard, tvPhoneState;

    private String userName = "";
    private String phoneState = "";
    private boolean isBind = false;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_id_and_safe;
    }

    @Override
    protected void beforeViewBind() {
        userName = YMApplication.getLoginInfo().getData().getUsername();
        if ("".equals(YMApplication.getLoginInfo().getData().getPhone())) {
            phoneState = "未绑定";
        } else {
            phoneState = "已绑定";
            isBind = true;
        }

    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("账号与安全");
        tvICard = (TextView) findViewById(R.id.tv_user_name);
        tvPhoneState = (TextView) findViewById(R.id.tv_phone_state);
//        hideCommonBaseTitle();
    }

    @Override
    protected void initData() {
        if (!"".equals(userName)) {
            tvICard.setText(userName);
        }
        if (!"".equals(phoneState)) {
            tvPhoneState.setText(phoneState);
        }
    }

    @OnClick({R.id.rl_phone_state, R.id.rl_edit_pwd})
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_safe_back:
//                finish();
//                break;

            case R.id.rl_phone_state:
                if (isBind) {
                    Intent intent = new Intent(IDAndSafeActivity.this,
                            BindTelSusActivity.class);
                    intent.putExtra("phone", YMApplication.getLoginInfo().getData().getPhone());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(IDAndSafeActivity.this,
                            BindPhoneActivity.class);
                    startActivity(intent);
                }

                break;

            case R.id.rl_edit_pwd:
                //stone 医生助手走忘记密码逻辑
                if (YMApplication.sIsYSZS) {
                    StatisticalTools.eventCount(this, "ForgetPwd");
                    Intent intent = new Intent(this, RetrievePasswordActivity.class);
                    intent.putExtra(Constants.KEY_VALUE, false);
                    startActivityForResult(intent, 1000);
                } else {
                    Intent intentPwd = new Intent(IDAndSafeActivity.this,
                            ChangePwdActivity.class);
                    startActivity(intentPwd);
                }
                break;
        }
    }


    //stone 新添加 再次进入到登录页面 密码需要自己重新输入
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            SharedPreferences sp = getSharedPreferences("save_user", MODE_PRIVATE);
            sp.edit().putString("pass_word", "").apply();
            YMUserService.ymLogout();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
