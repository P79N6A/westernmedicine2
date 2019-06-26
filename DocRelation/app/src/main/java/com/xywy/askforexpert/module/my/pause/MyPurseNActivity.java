package com.xywy.askforexpert.module.my.pause;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.MyPointInfo;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * 专家医生钱包
 *
 * @author 王鹏
 * @2015-11-4下午4:07:48
 */
public class MyPurseNActivity extends Activity {
    private static final String TAG = "MyPurseNActivity";
    private MyPointInfo point;
    TextView tv_my_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.my_purse);
        ((TextView) findViewById(R.id.tv_title)).setText("我的钱包");
        tv_my_money = (TextView) findViewById(R.id.tv_my_money);

        if (NetworkUtil.isNetWorkConnected()) {
            getData();
        } else {
            ToastUtils.shortToast("网络连接失败");
        }
    }

    /**
     * 获取积分 和余额数据
     */
    public void getData() {
        String userid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(userid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("command", "mypoint");
        params.put("userid", userid);
        params.put("ismoney", "1");
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        DLog.i(TAG, "积分余额查询" + CommonUrl.ScoresPointUrl + "?"
                + params.toString());
        fh.post(CommonUrl.ScoresPointUrl, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                DLog.i(TAG, "积分余额查询" + t.toString());
                Gson gson = new Gson();
                point = gson.fromJson(t.toString(), MyPointInfo.class);
                if (point != null) {
                    if ("0".equals(point.getCode())) {
                        tv_my_money.setText(point.getData().getBalance());
                    }
                }
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                DLog.i(TAG, "积分错误日志" + strMsg);
                ToastUtils.shortToast("网络链接超时");
                super.onFailure(t, errorNo, strMsg);
            }
        });

    }

    public void onClick_back(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn1:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }
}
