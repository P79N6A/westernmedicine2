package com.xywy.askforexpert.module.my.clinic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;

/**
 * 文本框填写 stone
 *
 * @author 王鹏
 * @2015-5-20上午11:12:44
 */
public class AddNumberEditAcitvity extends Activity {
    private String title;
    private String type;
    private EditText edit_info;
    String type_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.edittext_info);
        edit_info = (EditText) findViewById(R.id.edit_info);
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        type_item = getIntent().getStringExtra("istype");
        String str = "";
        //stone 判空处理
        if (!TextUtils.isEmpty(type) && type.equals("plus_range")) {
            //stone 判空处理
            if (!TextUtils.isEmpty(type_item) && type_item.equals(Constants.FUWU_AUDIT_STATUS_1)) {
                findViewById(R.id.btn2).setVisibility(View.GONE);
                edit_info.setFocusable(false);
            }
            str = YMApplication.addnuminfo.getPlus_range();
            //stone 判空优化
            if (!TextUtils.isEmpty(str)) {
                edit_info.setText(str);
            }
        } else if (!TextUtils.isEmpty(type) && type.equals("plusline")) {

            //stone 判空处理
            if (!TextUtils.isEmpty(type_item) && type_item.equals(Constants.FUWU_AUDIT_STATUS_1)) {
                findViewById(R.id.btn2).setVisibility(View.GONE);
                edit_info.setFocusable(false);
            }
            str = YMApplication.addnuminfo.getPlusline();
            if (!TextUtils.isEmpty(str)) {
                edit_info.setText(str);
            }

        } else if (!TextUtils.isEmpty(type) && type.equals("server_fee")) {
            str = YMApplication.phosetinfo.getExplanation();
            if (!TextUtils.isEmpty(str)) {
                edit_info.setText(str);
            }

            //stone 新添加 只可以看 不可以编辑
            if (!TextUtils.isEmpty(type_item) && type_item.equals(Constants.FUWU_AUDIT_STATUS_1)) {
                findViewById(R.id.btn2).setVisibility(View.GONE);
                edit_info.setFocusable(false);
            }

        }

        ((TextView) findViewById(R.id.tv_title)).setText(title);
    }

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
                    //stone 判空处理
                    if (!TextUtils.isEmpty(type) && type.equals("plus_range")) {
                        YMApplication.addnuminfo.setPlus_range(str);
                    } else if (!TextUtils.isEmpty(type) && type.equals("plusline")) {
                        YMApplication.addnuminfo.setPlusline(str);
                    } else if (!TextUtils.isEmpty(type) && type.equals("server_fee")) {
                        YMApplication.phosetinfo.setExplanation(str);
                    }
                    finish();

                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }
}
