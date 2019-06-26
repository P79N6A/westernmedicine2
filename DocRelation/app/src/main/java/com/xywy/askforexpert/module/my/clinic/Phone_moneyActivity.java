package com.xywy.askforexpert.module.my.clinic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.PhoneMoneyInfo;
import com.xywy.askforexpert.module.my.clinic.adapter.BasePhoneMoneyAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务费用 stone
 *
 * @author 王鹏
 * @2015-5-20下午6:05:49
 */
public class Phone_moneyActivity extends Activity {
    private String type;
    private ListView list;
    private BasePhoneMoneyAdapter adapter;
    private PhoneMoneyInfo phonemoney;
    private List<PhoneMoneyInfo> data = new ArrayList<PhoneMoneyInfo>();
    //	String str_type = YMApplication.getLoginInfo().getData().getXiaozhan().getPhone();
    private String str_type; //开通状态
    private LinearLayout no_data;
    private TextView tv_nodata_title;
    private ImageView img_nodata;
    private Handler hander = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (phonemoney.getCode().equals("0")) {

                        data = phonemoney.getData();
                        if (data.size() > 0) {
                            no_data.setVisibility(View.GONE);
                            adapter.setData(data);
                            list.setAdapter(adapter);
                            adapter.selectionMap = YMApplication.phosetinfo
                                    .getSelectionMap();
                            setSelect();
                        } else {
                            no_data.setVisibility(View.VISIBLE);
                        }

                    } else {
                        no_data.setVisibility(View.VISIBLE);
                    }
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.my_job_type);

        ((TextView) findViewById(R.id.tv_title)).setText("服务费用");

        list = (ListView) findViewById(R.id.list_job);
        adapter = new BasePhoneMoneyAdapter(Phone_moneyActivity.this);
        str_type = getIntent().getStringExtra("str_type");
        data = YMApplication.phosetinfo.getList();

        no_data = (LinearLayout) findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) findViewById(R.id.tv_nodata_title);
        tv_nodata_title.setText("暂无数据");
        img_nodata = (ImageView) findViewById(R.id.img_nodate);
        img_nodata.setBackgroundResource(R.drawable.service_more_none);
        if (data != null & data.size() > 0) {
            adapter.setData(data);
            list.setAdapter(adapter);
            adapter.selectionMap = YMApplication.phosetinfo.getSelectionMap();
            setSelect();
        } else {
            if (NetworkUtil.isNetWorkConnected()) {
                getTimeMoney();
            } else {
                ToastUtils.shortToast("网络连接失败");
                no_data.setVisibility(View.VISIBLE);
                tv_nodata_title.setText("网络连接失败");
            }

        }

        if (str_type.equals(Constants.FUWU_AUDIT_STATUS_1)) {

        } else {
            list.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    // adapter.init();
                    boolean ischek = adapter.selectionMap.get(arg2);
                    adapter.selectionMap.put(arg2, !ischek);
                    adapter.notifyDataSetChanged();
                    YMApplication.phosetinfo.setPosition(arg2);

                }
            });

        }
    }

    public void setSelect() {
        if (str_type.equals(Constants.FUWU_AUDIT_STATUS_1)) {
            findViewById(R.id.btn2).setVisibility(View.GONE);
            List<String> time_list = YMApplication.phosetinfo.getTime();
            //判空 stone
            if (time_list != null) {
                for (int i = 0; i < data.size(); i++) {
                    for (int j = 0; j < time_list.size(); j++) {
                        if (data.get(i).getTime().contains(time_list.get(j))) {
                            adapter.selectionMap.put(i, true);
                            break;
                        }
                    }
                }
            }

        }

    }

    public void getTimeMoney() {
        String userid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(userid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("command", "doctime");
        params.put("userid", userid);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.MyClinic_Phone_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        Gson gson = new Gson();
                        phonemoney = gson.fromJson(t.toString(),
                                PhoneMoneyInfo.class);
                        hander.sendEmptyMessage(100);
                        // TODO Auto-generated method stub
                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        ToastUtils.shortToast(
                                "网络繁忙,请稍后重试");
                        super.onFailure(t, errorNo, strMsg);
                    }
                });
    }

    public String getChooseStr() {
        String key = "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.size(); i++) {
            if (adapter.selectionMap.get(i)) {
                sb.append(data.get(i).getKey() + ",");
            }
        }
        if (sb.length() > 0) {
            key = sb.substring(0, sb.lastIndexOf(","));

        }
        return key;
    }

    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
                if (data.size() > 0) {
                    getChooseStr();
                    if (!TextUtils.isEmpty(getChooseStr())) {
                        YMApplication.phosetinfo.setFee(getChooseStr());
                        YMApplication.phosetinfo.setSelectionMap(adapter.selectionMap);
                        finish();
                    } else {
                        ToastUtils.shortToast("请选择服务费用");
                    }

                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(Phone_moneyActivity.this);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        StatisticalTools.onPause(Phone_moneyActivity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }
}
