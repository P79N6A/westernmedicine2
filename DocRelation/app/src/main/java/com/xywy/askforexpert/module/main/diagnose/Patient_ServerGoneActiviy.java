package com.xywy.askforexpert.module.main.diagnose;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.PatientListInfo;
import com.xywy.askforexpert.module.main.patient.service.PatientFriend;
import com.xywy.askforexpert.module.message.friend.adapter.BaseNewPatientAdapter;

/**
 * 服务过的患者 stone
 *
 * @author 王鹏
 * @2015-5-21上午10:27:23
 */
public class Patient_ServerGoneActiviy extends YMBaseActivity {
    private PatientListInfo pListInfo;
    private BaseNewPatientAdapter adapter;
    private ListView listView;
    private LinearLayout no_data;
    private TextView tv_nodata_title;
    private ImageView img_nodata;

    private Handler hander = new Handler() {
        public void handleMessage(android.os.Message msg) {
            pListInfo = (PatientListInfo) msg.obj;
            switch (msg.what) {
                case 100:
                    if (pListInfo.getCode().equals("0")
                            & pListInfo.getData().getList().size() > 0) {
                        no_data.setVisibility(View.GONE);
                        adapter = new BaseNewPatientAdapter(
                                Patient_ServerGoneActiviy.this);
                        adapter.setData(pListInfo.getData().getList());
                        listView.setAdapter(adapter);
                        no_data.setVisibility(View.GONE);
                        // tv_mypatient.setText(pListInfo.getData().getListcount());
                        // tv_newpatient.setText(pListInfo.getData().getNewpatient());
                    } else {
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
    protected int getLayoutResId() {
        return R.layout.patient_server_gone;
    }

//    public void onClick_back(View view) {
//        switch (view.getId()) {
//            case R.id.btn1:
//
//                finish();
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    protected void initView() {

        titleBarBuilder.setTitleText("服务过的患者");

        listView = (ListView) findViewById(R.id.list_server_gone);
        no_data = (LinearLayout) findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) findViewById(R.id.tv_nodata_title);
        tv_nodata_title.setText("暂无服务过的患者");
        img_nodata = (ImageView) findViewById(R.id.img_nodate);
        img_nodata.setBackgroundResource(R.drawable.log_nodata);
//        ((TextView) findViewById(R.id.tv_title)).setText("服务过的患者");
        if (NetworkUtil.isNetWorkConnected()) {
            PatientFriend.getData(Patient_ServerGoneActiviy.this, "2", hander, 100);
        } else {
            no_data.setVisibility(View.VISIBLE);
            ToastUtils.shortToast("网络连接失败");
            tv_nodata_title.setText("网络连接失败");
        }
    }

    @Override
    protected void initData() {

    }
}
