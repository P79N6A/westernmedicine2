package com.xywy.askforexpert.module.main.diagnose;

import android.os.Handler;
import android.widget.EditText;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改诊断
 *
 * @author 王鹏
 * @2015-5-25上午11:05:55
 */
public class UpdatePatientInfoActvity extends YMBaseActivity {

    private EditText edit_text;
    private Map<String, String> map = new HashMap<String, String>();
    private String id;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 100:

                    if (map.get("code").equals("0")) {
                        ToastUtils.shortToast("修改成功");
                        finish();
                    } else {
                        ToastUtils.shortToast(map.get("msg"));

                    }

                    break;

                default:
                    break;
            }
        }
    };

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.update_patient_log_info);
//
//        id = getIntent().getStringExtra("id");
//        edit_text = (EditText) findViewById(R.id.edit_info);
//        ((TextView) findViewById(R.id.tv_title)).setText("修改诊断");
//    }


    @Override
    protected int getLayoutResId() {
        return R.layout.update_patient_log_info;
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("修改诊断");
        titleBarBuilder.addItem("", R.drawable.sure, new ItemClickListener() {
            @Override
            public void onClick() {
                String str_mobile = edit_text.getText().toString().trim();
                if (str_mobile != null & !str_mobile.equals("")) {
                    if (NetworkUtil.isNetWorkConnected()) {
                        getData(id, str_mobile);
                    } else {
                        ToastUtils.shortToast("网络连接失败");
                    }
                } else {
                    ToastUtils.shortToast("请填写患者的诊断结果");
                }
            }
        }).build();
        id = getIntent().getStringExtra("id");
        edit_text = (EditText) findViewById(R.id.edit_info);
//        ((TextView) findViewById(R.id.tv_title)).setText("修改诊断");
    }

    @Override
    protected void initData() {
    }

    public void getData(String id, String sickname) {
        // String group_name1=URLEncoder.encode(group_name, "utf-8");
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = id + did;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("a", "chat");
        params.put("m", "treatmentEdit");

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("did", did);
        params.put("id", id);
        params.put("sickname", sickname);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        map = ResolveJson.R_Action(t.toString());
                        handler.sendEmptyMessage(100);
                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                    }
                });

    }

//    public void onClick_back(View view) {
//        Intent intent;
//        switch (view.getId()) {
//
//            case R.id.btn1:
//                finish();
//                break;
//            case R.id.btn2:
//                String str_mobile = edit_text.getText().toString().trim();
//                if (str_mobile != null & !str_mobile.equals("")) {
//                    if (NetworkUtil
//                            .isNetWorkConnected()) {
//                        getData(id, str_mobile);
//                    } else {
//                        ToastUtils.shortToast("网络连接失败");
//                    }
//
//                } else {
//                    ToastUtils.shortToast("请填写患者的诊断结果");
//                }
//                break;
//        }
//    }

    @Override
    protected void onDestroy() {

        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
