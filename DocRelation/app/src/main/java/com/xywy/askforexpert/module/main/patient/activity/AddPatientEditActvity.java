package com.xywy.askforexpert.module.main.patient.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.JudgeNetIsConnectedReceiver;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据手机号 添加好友
 *
 * @author 王鹏
 * @2015-5-21下午8:38:01
 */
public class AddPatientEditActvity extends Activity {

    private EditText edit_text;
    private Map<String, String> map = new HashMap<String, String>();
    private InputMethodManager manager;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 100:

                    if (map.get("code").equals("0")) {
                        ToastUtils.shortToast("短信已发送");
                    }
                    finish();
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.phone_edit);
        edit_text = (EditText) findViewById(R.id.edit_info);
        ((TextView) findViewById(R.id.tv_title)).setText("手机号码添加患者");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    public void getData(String mobile) {
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did + mobile;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "patientSendmsg");
        params.put("did", did);
        params.put("mobile", mobile);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
//		"http://test.api.app.club.xywy.com/app/1.0/index.interface.php"
        fh.get(CommonUrl.Patient_Manager_Url,
                params, new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        map = ResolveJson.R_Action(t.toString());
                        handler.sendEmptyMessage(100);
                        super.onSuccess(t);
                    }
                });

    }

    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
                String etphone = edit_text.getText().toString().trim();
                if (etphone != null && !etphone.equals("")) {
                    if (isMobileNO(etphone)) {
                        if (JudgeNetIsConnectedReceiver.judgeNetIsConnected(this)) {
                            getData(etphone);
                            hideKeyboard();
                        } else {
                            ToastUtils.shortToast( "无网络，请检查网络连接");
                        }
                    } else {
                        ToastUtils.shortToast("手机号格式错误");
                    }
                } else {
                    ToastUtils.shortToast("请输入手机号");
                }
                break;
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 判断是否全是数字
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 判断手机格式是否正确
     */
    public static boolean isMobileNO(String mobiles) {

        Pattern p = Pattern
                .compile(/* "^((13[0-9])|(14[5,7])|(17[6-7])|(15[^4,\\D])|(18[0-9]))\\d{8}$" */"^(1[0-9])\\d{9}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.onPause(AddPatientEditActvity.this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(AddPatientEditActvity.this);

    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
