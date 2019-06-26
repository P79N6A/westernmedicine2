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
 * 服务电话展示或者编辑,取决于开通状态 stone
 *
 * @author 王鹏
 * @2015-5-20下午5:38:17
 */
public class Pho_Doc_Ser_PhoneActiviy extends Activity {
    private EditText edit_phone, edit_phone_1, edit_phone_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.phone_doc_ser_phone);
        ((TextView) findViewById(R.id.tv_title)).setText("接受服务电话");

        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_phone_1 = (EditText) findViewById(R.id.edit_phone_1);
        edit_phone_2 = (EditText) findViewById(R.id.edit_phone_2);
        String phone = YMApplication.phosetinfo.getPhone();
        if (phone != null && !"".equals(phone)) {
            edit_phone.setText(phone);
        }
        String phone1 = YMApplication.phosetinfo.getPhone1();
        if (phone1 != null && !"".equals(phone1)) {
            edit_phone_1.setText(phone1);
        }
        String phone2 = YMApplication.phosetinfo.getPhone2();
        if (phone2 != null && !"".equals(phone2)) {
            edit_phone_2.setText(phone2);
        }
        if (YMApplication.getLoginInfo().getData().getXiaozhan().getPhone().equals(Constants.FUWU_AUDIT_STATUS_1)) {
            edit_phone.setFocusable(false);
            edit_phone_1.setFocusable(false);
            edit_phone_2.setFocusable(false);
            findViewById(R.id.btn2).setVisibility(View.GONE);
        }
    }

    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
                String phone1 = edit_phone.getText().toString().trim();
                String phone2 = edit_phone_1.getText().toString().trim();
                String phone3 = edit_phone_2.getText().toString().trim();

                if (TextUtils.isEmpty(phone1)) {
                    ToastUtils.shortToast(
                            "您还没有编辑您的服务电话");
                } else {
                    YMApplication.phosetinfo.setPhone(phone1);
                    YMApplication.phosetinfo.setPhone1(phone2);
                    YMApplication.phosetinfo.setPhone2(phone3);
                    finish();
                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(Pho_Doc_Ser_PhoneActiviy.this);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        StatisticalTools.onPause(Pho_Doc_Ser_PhoneActiviy.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
