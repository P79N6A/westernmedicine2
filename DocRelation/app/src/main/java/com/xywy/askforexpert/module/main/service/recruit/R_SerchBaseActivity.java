package com.xywy.askforexpert.module.main.service.recruit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.main.service.recruit.adapter.BaseR_SerchAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 招聘搜索
 *
 * @author 王鹏
 * @2015-6-16下午4:19:36
 */
public class R_SerchBaseActivity extends Activity {
    private String type;
    String[] key;
    private List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private ListView list_job;
    private BaseR_SerchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.my_job_type);

        list_job = (ListView) findViewById(R.id.list_job);
        type = getIntent().getStringExtra("type");
        if (type.equals("money")) {
            key = new String[]{"salarymin", "salarymax", "description"};
            list = ResolveJson.R_r_serch(getRowJson(R.raw.r_money), key);
            ((TextView) findViewById(R.id.tv_title)).setText("薪资范围");

        } else if (type.equals("xueli")) {
            key = new String[]{"study", "description"};
            list = ResolveJson.R_r_serch(getRowJson(R.raw.r_xueli), key);
            ((TextView) findViewById(R.id.tv_title)).setText("学历要求");

        } else if (type.equals("workyear")) {
            key = new String[]{"workyear", "description"};
            list = ResolveJson.R_r_serch(getRowJson(R.raw.r_workyear), key);
            ((TextView) findViewById(R.id.tv_title)).setText("工作年限");

        } else if (type.equals("worktype")) {
            key = new String[]{"worktype", "description"};
            list = ResolveJson.R_r_serch(getRowJson(R.raw.r_worktype), key);
            ((TextView) findViewById(R.id.tv_title)).setText("工作性质");

        } else if (type.equals("workstat")) {
            key = new String[]{"workstate", "description"};
            list = ResolveJson.R_r_serch(getRowJson(R.raw.r_workstate), key);
            ((TextView) findViewById(R.id.tv_title)).setText("企业性质");

        }
        adapter = new BaseR_SerchAdapter(R_SerchBaseActivity.this, list, type);
        if (list != null) {
            adapter.init();
            list_job.setAdapter(adapter);
        }

        list_job.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                adapter.init();
                adapter.selectionMap.put(arg2, true);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public int getCheck() {
        int position = -1;
        for (int i = 0; i < list.size(); i++) {
            if (adapter.selectionMap.get(i)) {
                position = i;
                break;
            }
        }
        return position;
    }

    public void onClick_back(View view) {
        int position = getCheck();
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:

                if (type.equals("money")) {
                    if (position != -1 & list != null) {
                        RecruitCenterMainActivity.serchinfo.setMoney(list.get(
                                position).get("description"));
                        RecruitCenterMainActivity.serchinfo.setMoney_low(list.get(
                                position).get("salarymin"));
                        RecruitCenterMainActivity.serchinfo.setMoney_top(list.get(
                                position).get("salarymax"));
                    } else {
                        RecruitCenterMainActivity.serchinfo.setMoney("");
                        RecruitCenterMainActivity.serchinfo.setMoney_low("");
                        RecruitCenterMainActivity.serchinfo.setMoney_top("");
                    }
                } else if (type.equals("xueli")) {
                    if (position != -1 & list != null) {
                        RecruitCenterMainActivity.serchinfo.setXueli(list.get(
                                position).get("description"));
                        RecruitCenterMainActivity.serchinfo.setXueli_id(list.get(
                                position).get("study"));
                    } else {
                        RecruitCenterMainActivity.serchinfo.setXueli("");
                        RecruitCenterMainActivity.serchinfo.setXueli_id("");
                    }
                } else if (type.equals("workyear")) {
                    if (position != -1 & list != null) {
                        RecruitCenterMainActivity.serchinfo.setWork_year(list.get(
                                position).get("description"));
                        RecruitCenterMainActivity.serchinfo.setWork_year_id(list.get(
                                position).get("workyear"));
                    } else {
                        RecruitCenterMainActivity.serchinfo.setWork_year("");
                        RecruitCenterMainActivity.serchinfo.setWork_year_id("");
                    }
                } else if (type.equals("worktype")) {
                    if (position != -1 & list != null) {
                        RecruitCenterMainActivity.serchinfo.setWork_type(list.get(
                                position).get("description"));
                        RecruitCenterMainActivity.serchinfo.setWork_type_id(list.get(
                                position).get("worktype"));
                    } else {
                        RecruitCenterMainActivity.serchinfo.setWork_type("");
                        RecruitCenterMainActivity.serchinfo.setWork_type_id("");
                    }
                } else if (type.equals("workstat")) {
                    if (position != -1 & list != null) {
                        RecruitCenterMainActivity.serchinfo.setCom_type(list.get(
                                position).get("description"));
                        RecruitCenterMainActivity.serchinfo.setCom_type_id(list.get(
                                position).get("workstate"));
                    } else {
                        RecruitCenterMainActivity.serchinfo.setCom_type("");
                    }
                }
                finish();
            default:
                break;
        }
    }

    /**
     * 获取 文件里面的json
     *
     * @param id
     * @return
     */
    public String getRowJson(int id) {
        String json = null;
        InputStream is = this.getResources().openRawResource(id);
        byte[] buffer;
        try {
            buffer = new byte[is.available()];
            is.read(buffer);
            json = new String(buffer, "utf-8");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(R_SerchBaseActivity.this);


    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.onPause(R_SerchBaseActivity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
