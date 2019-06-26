package com.xywy.askforexpert.module.my.userinfo;

import android.content.Intent;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.module.my.userinfo.service.UpdatePersonInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人信息 舒服名字，和个新签名，复用界面
 *
 * @author 王鹏
 * @2015-4-30上午11:19:27
 */
@Deprecated
public class InfoEditTextActivity extends YMBaseActivity {
    private String name_edit;
    /**
     * 个性签名
     */
    private String type;
    /**
     * 介绍
     */
    private String name_introduce;
    private String name_title;
    private EditText edit_info;
    private TextView tv_info;
    private TextView tv_title;
    private Map<String, String> map;

    private TextView tv_max;
    private TextView tv_min;
    private LinearLayout lin_num;



    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
                String str = edit_info.getText().toString().trim();
                if (TextUtils.isEmpty(str)) {
                    ToastUtils.shortToast("内容不能为空");
                } else {
                    if (type.equals("name")) {
                        if (str.length() < 2 & str.length() > 4) {
                            ToastUtils.shortToast(
                                    "姓名限制2-4个字符 ");
                        } else if (!YMOtherUtils.checkNameChese(str) | !YMOtherUtils.checkIsBQZF(str)) {
                            ToastUtils.shortToast(
                                    "请输入中文字符 ");
                        } else {
                            UpdatePersonInfo.update("realname", str, 100, uiHandler);
                        }
                    } else if (type.equals("sign")) {
                        if (str.length() > 20 & str.length() < 200) {
                            UpdatePersonInfo.update("synopsis", edit_info.getText()
                                    .toString().trim(), 100, uiHandler);
                        } else {
                            ToastUtils.shortToast(
                                    "字数限制在20~200字以内 ");

                        }
                    }

                }

                break;
            default:
                break;
        }
    }



    @Override
    protected int getLayoutResId() {
        return R.layout.edittext_info;
    }

    @Override
    protected void beforeViewBind() {
        type = getIntent().getStringExtra("type");
        name_introduce = getIntent().getStringExtra("introduce");
        name_title = getIntent().getStringExtra("title");
    }

    @Override
    protected void initView() {
        hideCommonBaseTitle();




        edit_info = (EditText) findViewById(R.id.edit_info);
        tv_info = (TextView) findViewById(R.id.tv_info);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_max = (TextView) findViewById(R.id.tv_max);
        tv_min = (TextView) findViewById(R.id.tv_min);
        lin_num = (LinearLayout) findViewById(R.id.lin_num);

        tv_info.setText(name_introduce);
        tv_title.setText(name_title);
        if (type.equals("name")) {
            edit_info.setHint("请输入名字");
        } else if (type.equals("sign")) {
            edit_info.setHint("请输入个人简介");
            lin_num.setVisibility(View.VISIBLE);
            edit_info.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                    if (arg0.length() < 20) {
                        tv_min.setTextColor(getResources()
                                .getColor(R.color.red));
                        tv_max.setTextColor(getResources().getColor(
                                R.color.tab_color_nomal));
                    } else {
                        tv_min.setTextColor(getResources().getColor(
                                R.color.tab_color_nomal));
                        tv_max.setTextColor(getResources().getColor(
                                R.color.tab_color_nomal));
                    }
                    if (arg0.length() > 200) {
                        tv_max.setTextColor(getResources()
                                .getColor(R.color.red));
                        tv_min.setTextColor(getResources().getColor(
                                R.color.tab_color_nomal));
                    }

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {

                }

                @Override
                public void afterTextChanged(Editable arg0) {

                }
            });
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);

        map = (HashMap<String, String>) msg.obj;
        switch (msg.what) {
            case 100:
                if (map.get("code").equals("0")) {

                    if (type.equals("name")) {
                        YMUserService.getPerInfo().getData().setRealname(
                                edit_info.getText().toString().trim());
                        YMApplication.getLoginInfo().getData().setRealname(
                                edit_info.getText().toString().trim());
                    } else if (type.equals("sign")) {
                        YMUserService.getPerInfo().getData().setSynopsis(
                                edit_info.getText().toString().trim());
                        YMApplication.getLoginInfo().getData().setSynopsis(
                                edit_info.getText().toString().trim());
                    }
                    // Intent intent = new Intent();
                    //
                    // setResult(Activity.RESULT_OK, intent);

                    finish();
                }
                ToastUtils.shortToast(map.get("msg"));
                break;

            default:
                break;
        }
    }
}
