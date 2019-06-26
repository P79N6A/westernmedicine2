package com.xywy.askforexpert.module.message.friend;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.PatientListInfo;
import com.xywy.askforexpert.module.main.diagnose.Patient_ServerGoneActiviy;
import com.xywy.askforexpert.module.main.patient.activity.PatientPersonInfoActiviy;
import com.xywy.askforexpert.module.main.patient.adapter.PatientContactAdapter;
import com.xywy.askforexpert.module.main.patient.service.PatientFriend;

/**
 * 新患者 stone
 *
 * @author 王鹏
 * @2015-5-21下午3:12:17
 */
public class NewPatientActiviy extends YMBaseActivity {

    private ListView list_new_patient;
    private PatientListInfo pListInfo;
    private PatientContactAdapter adapter;
    private TextView tv_newpatient;

    private LinearLayout no_data;
    private TextView tv_nodata_title;
    private ImageView img_nodata;
    private Handler hander = new Handler() {
        public void handleMessage(android.os.Message msg) {
            pListInfo = (PatientListInfo) msg.obj;
            switch (msg.what) {
                case 100:
                    if (pListInfo.getCode().equals("0")) {
                        if (pListInfo.getData().getList().size() > 0) {
                            list_new_patient.setVisibility(View.VISIBLE);
                            no_data.setVisibility(View.GONE);
                            adapter = new PatientContactAdapter(NewPatientActiviy.this,
                                    R.layout.row_contact, pListInfo.getData().getList());
                            list_new_patient.setAdapter(adapter);
                        } else {
                            no_data.setVisibility(View.VISIBLE);
                            list_new_patient.setVisibility(View.GONE);
                        }
                        tv_newpatient.setText(pListInfo.getData().getServered());
                        // tv_mypatient.setText(pListInfo.getData().getListcount());

                    } else

                    {
                        list_new_patient.setVisibility(View.GONE);
                        no_data.setVisibility(View.VISIBLE);
                    }

                    break;
                case 500:
                    ToastUtils.shortToast("网络连接超时");
                    no_data.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkUtil.isNetWorkConnected()) {

            PatientFriend.getData(NewPatientActiviy.this, "1", hander, 100);
        } else {
            ToastUtils.shortToast("网络连接失败");
            no_data.setVisibility(View.VISIBLE);
            tv_nodata_title.setText("网络连接失败");
        }

    }

    @Override
    protected void initView() {

        titleBarBuilder.setTitleText("新患者");


        no_data = (LinearLayout) findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) findViewById(R.id.tv_nodata_title);
        tv_nodata_title.setText("暂无新患者");
        img_nodata = (ImageView) findViewById(R.id.img_nodate);
        img_nodata.setBackgroundResource(R.drawable.log_nodata);
        list_new_patient = (ListView) findViewById(R.id.list_new_patient);
//        ((TextView) findViewById(R.id.tv_title)).setText("新患者");
        tv_newpatient = (TextView) findViewById(R.id.tv_newpatient);


        list_new_patient.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                YMApplication.isrefresh = true;
                Intent intent = new Intent(NewPatientActiviy.this,
                        PatientPersonInfoActiviy.class);
                intent.putExtra("hx_userid", adapter.getItem(arg2)
                        .getHxusername());
                intent.putExtra("uid", adapter.getItem(arg2).getId());
                startActivity(intent);

            }
        });
    }

    @Override
    protected void initData() {

    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:

                finish();
                break;
            case R.id.re_server_gone_frident:
                Intent intent1 = new Intent(NewPatientActiviy.this,
                        Patient_ServerGoneActiviy.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.new_patient;
    }
}
