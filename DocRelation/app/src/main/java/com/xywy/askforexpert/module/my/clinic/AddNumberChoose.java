package com.xywy.askforexpert.module.my.clinic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.my.userinfo.adapter.BaseJobAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddNumberChoose extends Activity {
    private String type;
    private ListView list;
    private BaseJobAdapter adapter;
    private List<HashMap<String, String>> list_jobtype = new ArrayList<HashMap<String, String>>();
    private int position;
    private String type_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_job_type);
        CommonUtils.initSystemBar(this);
        ((TextView) findViewById(R.id.tv_title)).setText(getIntent()
                .getStringExtra("title"));
        type = getIntent().getStringExtra("type");
        type_item = getIntent().getStringExtra("istype");

        list = (ListView) findViewById(R.id.list_job);
        list.setDivider(null);
//		 = YMApplication.getLoginInfo().getData().getXiaozhan().getYuyue();
        if (type_item.equals(Constants.FUWU_AUDIT_STATUS_1)) {
            findViewById(R.id.btn2).setVisibility(View.GONE);
        }
        if (type.equals("order")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", "委托给工作人员安排预约转诊");
            HashMap<String, String> map2 = new HashMap<String, String>();
            map2.put("name", "必须本人确认才能生效");
            list_jobtype.add(map);
            list_jobtype.add(map2);
            position = YMApplication.addnuminfo.getIsttrue_position();
        } else if (type.equals("demand")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", "复诊患者于新患者均可预约");
            HashMap<String, String> map2 = new HashMap<String, String>();
            map2.put("name", "仅接收复诊患者预约");
            list_jobtype.add(map);
            list_jobtype.add(map2);
            position = YMApplication.addnuminfo.getTreatlimt_position();
        }

        adapter = new BaseJobAdapter(AddNumberChoose.this, list_jobtype);
        adapter.init();//
        list.setAdapter(adapter);
        adapter.selectionMap.put(position, true);
        if (type_item.equals(Constants.FUWU_AUDIT_STATUS_1)) {

        } else {
            list.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    adapter.init();
                    adapter.selectionMap.put(arg2, true);
                    adapter.notifyDataSetChanged();
                    if (type.equals("order")) {
                        YMApplication.addnuminfo.setIsttrue_position(arg2);
                    } else if (type.equals("demand")) {
                        YMApplication.addnuminfo.setTreatlimt_position(arg2);
                    }
                }
            });

        }
    }

    public String getData() {
        String str = "";
        for (int i = 0; i < list_jobtype.size(); i++) {
            if (adapter.selectionMap.get(i)) {
                str = list_jobtype.get(i).get("name");
                break;
            }
        }
        return str;
    }

    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
                String str = getData();
                if (str == null || str.equals("")) {
                    ToastUtils.shortToast("请编辑一项");
                } else {

                    if (type.equals("order")) {
                        if (str.equals("必须本人确认才能生效")) {
                            YMApplication.addnuminfo.setIstrue("1");
                        } else {

                            YMApplication.addnuminfo.setIstrue("2");
                        }
                    } else if (type.equals("demand")) {
                        if (str.equals("仅接收复诊患者预约")) {
                            YMApplication.addnuminfo.setTreatlimt("1");
                        } else {
                            YMApplication.addnuminfo.setTreatlimt("2");
                        }
                    }
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
