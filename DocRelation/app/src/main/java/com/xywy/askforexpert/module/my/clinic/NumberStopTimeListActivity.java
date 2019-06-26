package com.xywy.askforexpert.module.my.clinic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.NumStopTimeListInfo;
import com.xywy.askforexpert.module.my.clinic.adapter.BaseNumStopTimeAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * 停诊时间列表
 *
 * @author 王鹏
 * @2015-5-28下午8:48:10
 */
public class NumberStopTimeListActivity extends Activity {

    private static final String TAG = "NumberStopTimeListActivity";
    private NumStopTimeListInfo info;
    private BaseNumStopTimeAdapter adapter;

    private ListView listView;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    adapter = new BaseNumStopTimeAdapter(
                            NumberStopTimeListActivity.this);
                    adapter.setData(info.getData().getData_list());
                    listView.setAdapter(adapter);
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.num_stop_time_list);
        ((TextView) findViewById(R.id.tv_title)).setText("停诊时间");
        listView = (ListView) findViewById(R.id.num_list);
        View view = LayoutInflater.from(
                NumberStopTimeListActivity.this).inflate(
                R.layout.list_foot_view, null);
        Button tv_name = (Button) view.findViewById(R.id.next_btn);
        tv_name.setText("继续添加停诊服务");
        listView.addFooterView(view);
    }

    public void getData() {
        String uid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(uid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("command", "stoplist");
        params.put("userid", uid);
        params.put("pagesize", 5 + "");
        params.put("page", 1 + "");
        params.put("sign", sign);
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.MyClinic_State_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        DLog.i(TAG, "停诊服务列表" + t.toString());
                        // TODO Auto-generated method stub
                        info = ResolveJson.R_num_stop_time_list(t.toString());
                        handler.sendEmptyMessage(100);
                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
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

            case R.id.next_btn:
                intent = new Intent(NumberStopTimeListActivity.this,
                        NumberStopTimeActiviy.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        StatisticalTools.onResume(NumberStopTimeListActivity.this);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        StatisticalTools.onPause(NumberStopTimeListActivity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
