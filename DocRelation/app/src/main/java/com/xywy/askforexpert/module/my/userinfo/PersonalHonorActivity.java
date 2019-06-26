package com.xywy.askforexpert.module.my.userinfo;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;

/**
 * 个人荣誉 （选填）
 *
 * @author 王鹏
 * @2015-5-6下午7:40:33
 */
@Deprecated
public class PersonalHonorActivity extends YMBaseActivity {

    private EditText edit_info;



    @Override
    protected int getLayoutResId() {
        return R.layout.edittext_info;
    }

    @Override
    protected void beforeViewBind() {

    }

    @Override
    protected void initView() {
        hideCommonBaseTitle();
        ((TextView) findViewById(R.id.tv_title)).setText("个人荣誉");
        edit_info = (EditText) findViewById(R.id.edit_info);
        edit_info.setHint("请输入个人荣誉");

    }

    @Override
    protected void initData() {

    }


    /**
     * 返回监听
     *
     * @param view
     */
    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
                String str = edit_info.getText().toString().trim();
                if (!TextUtils.isEmpty(str)) {
                    if (str.length() > 200) {
                        ToastUtils.shortToast("内容限制在200字以内");
                    } else {
                        YMUserService.getPerInfo().setHonor(str);
                        finish();
                    }

                } else {
                    ToastUtils.shortToast("请输入个人荣誉");
                }
                break;
            default:
                break;
        }

    }
}
