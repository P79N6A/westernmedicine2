package com.xywy.askforexpert.module.main.diagnose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.DiagnoseLogListInfo;
import com.xywy.askforexpert.module.main.diagnose.adapter.BaseDiagnoseloglistAdapter;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * 诊断纪录 列表
 *
 * @author 王鹏
 * @2015-5-22上午9:15:23
 */
public class DiagnoseLogListActivity extends Activity {

    private LinearLayout lin_nodata;
    private DiagnoseLogListInfo dl_listinfo;
    private BaseDiagnoseloglistAdapter adapter;
    private ListView list;
    private String uid;
    private ImageButton img_btn;
    private TextView tv_no_data_tielt;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (dl_listinfo != null) {
                        if (dl_listinfo.getCode().equals("0")
                                & dl_listinfo.getData().size() > 0) {
                            adapter = new BaseDiagnoseloglistAdapter(
                                    DiagnoseLogListActivity.this);
                            adapter.setData(dl_listinfo.getData());
                            list.setAdapter(adapter);

                            lin_nodata.setVisibility(View.GONE);

                            if (dl_listinfo.getIscreate().equals("1")) {
                                img_btn.setVisibility(View.GONE);
                            } else {
                                img_btn.setVisibility(View.VISIBLE);
                            }
                        } else {
                            lin_nodata.setVisibility(View.VISIBLE);
                            img_btn.setVisibility(View.VISIBLE);
                        }
                    } else {
                        lin_nodata.setVisibility(View.VISIBLE);
                        img_btn.setVisibility(View.VISIBLE);
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
        setContentView(R.layout.diagnose_log_list);

        tv_no_data_tielt = (TextView) findViewById(R.id.tv_no_data_tielt);
        lin_nodata = (LinearLayout) findViewById(R.id.lin_nodata);
        list = (ListView) findViewById(R.id.list_group);
        uid = getIntent().getStringExtra("uid");
        list.setDivider(null);

        img_btn = (ImageButton) findViewById(R.id.btn2);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(DiagnoseLogListActivity.this,
                        DiagnoseLogListInfoActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("id", dl_listinfo.getData().get(arg2).getId());
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO Auto-generated method stub
        if (NetworkUtil.isNetWorkConnected()) {
            getData(uid);
            tv_no_data_tielt.setText("TA还没有病程纪录哦，快去添加吧！");
        } else {
            lin_nodata.setVisibility(View.VISIBLE);
            ToastUtils.longToast("网络连接失败");
            tv_no_data_tielt.setText("网络连接失败");
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

    }

    public void getData(String uid) {
        final ProgressDialog dialog = new ProgressDialog(this, "正在加载中...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did + uid;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "treatmentIndex");
        params.put("did", did);
        params.put("uid", uid);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub

                        dl_listinfo = ResolveJson.R_dialoglist(t.toString());
                        handler.sendEmptyMessage(100);
                        dialog.dismiss();
                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        lin_nodata.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                        super.onFailure(t, errorNo, strMsg);
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
                intent = new Intent(DiagnoseLogListActivity.this,
                        DiagnoselogAddEditActiviy.class);
                // slickinfo
                intent.putExtra("slickinfo", dl_listinfo.getSickinfo());
                intent.putExtra("uid", uid);
                startActivityForResult(intent, 29);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK & requestCode == 29) {
            if (NetworkUtil.isNetWorkConnected()) {
                getData(uid);
                tv_no_data_tielt.setText("TA还没有病程纪录哦，快去添加吧！");
            } else {
                // lin_nodata.setVisibility(View.VISIBLE);
                ToastUtils.longToast("网络连接失败");
                // tv_no_data_tielt.setText("网络连接失败");
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {

        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
