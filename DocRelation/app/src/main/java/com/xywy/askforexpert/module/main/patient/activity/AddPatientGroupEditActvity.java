package com.xywy.askforexpert.module.main.patient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 添加患者管理分组
 *
 * @author 王鹏
 * @2015-5-21下午8:38:01
 */
public class AddPatientGroupEditActvity extends Activity {

    private final String reg = "^([a-z]|[A-Z]|[0-9]|[\u2E80-\u9FFF]){1,}|@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?|[wap.]{4}|[www.]{4}|[blog.]{5}|[bbs.]{4}|[.com]{4}|[.cn]{3}|[.net]{4}|[.org]{4}|[http://]{7}|[ftp://]{6}$";
    private EditText edit_text;
    private Map<String, String> map = new HashMap<String, String>();
    private String type;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 100:

                    if (map.get("code").equals("0")) {
                        finish();
                        ToastUtils.shortToast("添加成功");
                    } else {
                        ToastUtils.shortToast(
                                map.get("msg"));
                    }

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
        setContentView(R.layout.patient_group_edit);
        edit_text = (EditText) findViewById(R.id.edit_info);
        type = getIntent().getStringExtra("type");

        if ("quickreplyadd".equals(type)) {
            ((TextView) findViewById(R.id.tv_title)).setText("添加快捷回复");
            edit_text.setHint("请编辑快捷回复内容");
        } else {
            ((TextView) findViewById(R.id.tv_title)).setText("添加分组");
        }
    }

    public void getData(String group_name, String m, String name) {
        // String group_name1=URLEncoder.encode(group_name, "utf-8");
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", did);
        params.put("a", "chat");
        params.put("m", m);
        params.put("did", did);
        params.put(name, group_name);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
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
                String str_mobile = edit_text.getText().toString().trim();
                if (NetworkUtil.isNetWorkConnected()) {

                    if (type.equals("quickreplyadd")) {
                        if (!TextUtils.isEmpty(str_mobile)) {

                            getData(str_mobile, type, "word");
                        } else {

                            ToastUtils.shortToast(
                                    "请编辑快捷回复内容");
                        }
                    } else {
                        StatisticalTools.eventCount(this, "Patientgroups");
                        if (!TextUtils.isEmpty(str_mobile)) {
                            Pattern p2 = Pattern.compile(reg);
                            Matcher m2 = p2.matcher(str_mobile);
                            if (m2.matches()) {
                                getData(str_mobile, type, "name");
                            } else {
                                ToastUtils.shortToast("不允许输入表情或者字符");
                            }

                        } else {

                            ToastUtils.shortToast(
                                    "请编辑分组名");
                        }
                    }

                } else {
                    ToastUtils.shortToast("网络连接失败");
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.onPause(AddPatientGroupEditActvity.this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(AddPatientGroupEditActvity.this);

    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
