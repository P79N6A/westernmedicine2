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
import com.xywy.askforexpert.module.my.userinfo.adapter.BaseAreaNameAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 王鹏
 * @2015-6-18上午11:34:02
 */
public class R_SerchBaseJobtype2Activity extends Activity {
    private List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private ListView list_job;
    private BaseAreaNameAdapter adapter;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_job_type);

        ((TextView) findViewById(R.id.tv_title)).setText("职位类别");
        list_job = (ListView) findViewById(R.id.list_job);
        id = getIntent().getStringExtra("id");
        list = getSchoolNmae(getRowJson(R.raw.position_three), id);
        adapter = new BaseAreaNameAdapter(R_SerchBaseJobtype2Activity.this);
        if (list != null) {
            adapter.setData(list);
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

    public String getList2(String id) {
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            if (adapter.selectionMap.get(i)) {
                str = list.get(i).get(id);
                break;
            }
        }
        return str;
    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
                String city = getList2("name");

//			if (city == null || city.equals(""))
//			{
//				T.showNoRepeatShort(R_SerchBaseJobTypeActivity.this, "请选泽");
//			} else
//				if (province == null || province.equals(""))
//			{
//				T.showNoRepeatShort(R_SerchBaseJobTypeActivity.this, "请选择省份");
//			} else
//			{
//				
//			}

                if (city != null & !"".equals(city)) {
                    RecruitCenterMainActivity.serchinfo.setJobtype(city);
                }

                finish();
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

    /**
     * 根据 id 返回地区学校
     *
     * @param json
     * @param id
     * @return
     */
    public List<HashMap<String, String>> getSchoolNmae(String json, String id) {
        return ResolveJson.R_List_id(json, id);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(R_SerchBaseJobtype2Activity.this);


    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.onPause(R_SerchBaseJobtype2Activity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
