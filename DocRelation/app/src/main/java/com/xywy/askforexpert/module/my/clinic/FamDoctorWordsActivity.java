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
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;

/**
 * 医生寄语
 *
 * @author 王鹏
 * @2015-5-26下午4:50:40
 */
public class FamDoctorWordsActivity extends Activity {

    private String title;
    private String type;
    private String hint;

    private EditText edit_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.edittext_info);
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        hint = getIntent().getStringExtra("hint");
        edit_info = (EditText) findViewById(R.id.edit_info);
        edit_info.setHint(hint);
        String str;
        if (type.equals("words")) {
            str = YMApplication.famdocinfo.getWords();
            if (str != null & !"".equals(str)) {
                edit_info.setText(str);
            }
        } else if (type.equals("special")) {
            str = YMApplication.famdocinfo.getSpecial();
            if (str != null & !"".equals(str)) {
                edit_info.setText(str);
            }
        }
        String type_str = YMApplication.getLoginInfo().getData().getXiaozhan().getFamily();
        if (type_str.equals("1")) {
            findViewById(R.id.btn2).setVisibility(View.GONE);
            edit_info.setFocusable(false);
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
                if (TextUtils.isEmpty(str) || str == null || str.equals("")) {
                    ToastUtils.shortToast("内容不能为空");
                } else {
                    if (type.equals("words")) {
                        if (str.length() < 20 || str.length() > 50) {
                            ToastUtils.shortToast("字数限制在20-50个字");
                        } else {
                            YMApplication.famdocinfo.setWords(str);
                            finish();
                        }

                    } else if (type.equals("special")) {
                        if (str.length() < 20 || str.length() > 200) {
                            ToastUtils.shortToast("字数限制在20-200个字");
                        } else {

                            YMApplication.famdocinfo.setSpecial(str);
                            finish();
                        }
                    }

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
        StatisticalTools.onResume(FamDoctorWordsActivity.this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        StatisticalTools.onPause(FamDoctorWordsActivity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }
}
