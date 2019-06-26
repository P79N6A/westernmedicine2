package com.xywy.askforexpert.module.my.clinic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.my.clinic.adapter.BaseFamShowTimeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 家庭医生 时间 显示
 *
 * @author 王鹏
 * @2015-6-1上午10:32:06
 */
public class FamDoctorTimeShowActivity extends Activity {
    private ListView listView;
    private BaseFamShowTimeAdapter adapter;
    private String type;
    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.num_stop_time_list);

        type = getIntent().getStringExtra("type");
        if (type.equals("fam")) {
            ((TextView) findViewById(R.id.tv_title)).setText("接电话时间");
            list = YMApplication.famdocinfo.getList();
        } else if (type.equals("phone")) {
            ((TextView) findViewById(R.id.tv_title)).setText("服务时间");
            list = YMApplication.phosetinfo.getList_times();
        }

        listView = (ListView) findViewById(R.id.num_list);

        adapter = new BaseFamShowTimeAdapter(FamDoctorTimeShowActivity.this);
        adapter.setData(list);
        listView.setAdapter(adapter);
    }

    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
        }
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(FamDoctorTimeShowActivity.this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        StatisticalTools.onPause(FamDoctorTimeShowActivity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }
}