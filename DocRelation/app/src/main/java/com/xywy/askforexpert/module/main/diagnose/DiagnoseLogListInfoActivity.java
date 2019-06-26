package com.xywy.askforexpert.module.main.diagnose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.TreatmentListInfo;
import com.xywy.askforexpert.module.main.diagnose.adapter.BaseTrenatmentAdapter;
import com.xywy.askforexpert.module.main.patient.activity.AddTreatmentListLogInfoActivity;
import com.xywy.askforexpert.widget.view.MyListView;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;

/**
 * 诊断纪录 列表可编辑详情页面
 *
 * @author 王鹏
 * @2015-5-22下午1:57:56
 */
public class DiagnoseLogListInfoActivity extends Activity implements
        OnClickListener {
    private EditText edit_check_info;
    private TextView tv_check_result;
    private MyListView list;
    private String id;
    private String uid;
    private TreatmentListInfo treate_info;
    private BaseTrenatmentAdapter adapter;
    private ImageButton btn2;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (treate_info.getCode().equals("0")) {
                        initview();

                    } else {
                        ToastUtils.shortToast(
                                "访问失败");
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
        setContentView(R.layout.diagnose_log_info);

        tv_check_result = (TextView) findViewById(R.id.tv_check_result);
        edit_check_info = (EditText) findViewById(R.id.edit_check_info);
        list = (MyListView) findViewById(R.id.list_log);
        btn2 = (ImageButton) findViewById(R.id.btn2);
        list.setDivider(null);
        id = getIntent().getStringExtra("id");
        uid = getIntent().getStringExtra("uid");

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(DiagnoseLogListInfoActivity.this,
                        TreatmentInfoActivity.class);
                intent.putExtra("content",
                        treate_info.getData().getList().get(arg2).getContent());
                intent.putStringArrayListExtra("list",
                        (ArrayList<String>) treate_info.getData().getList()
                                .get(arg2).getImgs());
                startActivity(intent);

            }
        });

    }

    public void initview() {
        tv_check_result.setText(treate_info.getData().getSlickname());
        edit_check_info.setText(treate_info.getData().getSlickinfo());
        adapter = new BaseTrenatmentAdapter(DiagnoseLogListInfoActivity.this,treate_info.getData().getList());
        list.setAdapter(adapter);
        if (treate_info.getData().getDid()
                .equals(YMApplication.getLoginInfo().getData().getPid())) {
            btn2.setVisibility(View.VISIBLE);
            tv_check_result.setOnClickListener(this);
        } else {
            btn2.setVisibility(View.GONE);

        }
    }

    public void getData(String uid, String id) {
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = id + uid;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "treatmentList");
        params.put("did", did);
        params.put("id", id);
        params.put("uid", uid);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        treate_info = ResolveJson.R_treatment(t.toString());
                        handler.sendEmptyMessage(100);
                        super.onSuccess(t);
                    }
                });

    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:

                finish();
                break;
            case R.id.btn2:
                Intent intent = new Intent(DiagnoseLogListInfoActivity.this,
                        AddTreatmentListLogInfoActivity.class);
                // treatmentid
                intent.putExtra("treatmentid", treate_info.getData().getId());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(uid, id);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        StatisticalTools.fragmentOnResume("DiagnoseLogListInfoActivity");
        StatisticalTools.onResume(this);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.fragmentOnPause("DiagnoseLogListInfoActivity");
        // onPageEnd 在onPause
        // 之前调用,因为 onPause 中会保存信息
        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.tv_check_result:
                Intent intent = new Intent(DiagnoseLogListInfoActivity.this,
                        UpdatePatientInfoActvity.class);
                intent.putExtra("id", treate_info.getData().getId());
                startActivity(intent);
                break;

            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
