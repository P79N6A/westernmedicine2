package com.xywy.askforexpert.module.main.service.recruit;

import android.app.Activity;
import android.content.Intent;
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
import com.xywy.askforexpert.module.my.userinfo.adapter.BaseAreaNameAdapter;
import com.xywy.askforexpert.module.my.userinfo.adapter.BaseJobtype1Adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 招聘搜索 职位类别
 *
 * @author 王鹏
 * @2015-6-18上午10:47:33
 */
public class R_SerchBaseJobTypeActivity extends Activity {

    private String area_province = null;
    private String arey_cityname = null;
    private List<HashMap<String, String>> list_areacityname = new ArrayList<HashMap<String, String>>();
    private List<HashMap<String, String>> list_province;
    private List<HashMap<String, String>> list_stree;
    private ListView lv_province;
    private ListView lv_city;

    private BaseAreaNameAdapter area_city_adpter;
    // private BaseSchoolNameAdapter area_province_adpter;
    private BaseJobtype1Adapter area_province_adpter;

    private String city;
    private String province;

    private String city_id;
    private String province_id;
    private String str_stree;
    private Map<String, String> map;

    private boolean type = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.select_school);

        Init();
    }

    public void Init() {
        ((TextView) findViewById(R.id.tv_title)).setText("职位类别");
        list_province = new ArrayList<HashMap<String, String>>();
        if (area_province == null) {
            area_province = getRowJson(R.raw.position_one);
            list_province = ResolveJson.R_List(area_province);
        }
        if (arey_cityname == null) {
            arey_cityname = getRowJson(R.raw.position_two);
            // list_areacityname = getSchoolNmae(arey_cityname, "1");
        }
        if (str_stree == null) {
            str_stree = getRowJson(R.raw.position_three);
        }
        lv_province = (ListView) findViewById(R.id.list_schoolcity);
        lv_city = (ListView) findViewById(R.id.list_schoolname);
        area_city_adpter = new BaseAreaNameAdapter(
                R_SerchBaseJobTypeActivity.this);
        area_city_adpter.setData(list_areacityname);
        area_province_adpter = new BaseJobtype1Adapter(
                R_SerchBaseJobTypeActivity.this, list_province);
        area_province_adpter.init();
        lv_province.setAdapter(area_province_adpter);
        lv_city.setAdapter(area_city_adpter);

        lv_province.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                area_province_adpter.init();
                area_province_adpter.selectionMap.put(arg2, true);
                String id = list_province.get(arg2).get("id") + "";

                list_areacityname.clear();

                list_areacityname = getSchoolNmae(arey_cityname, id);

                area_city_adpter.setData(list_areacityname);
                area_city_adpter.notifyDataSetChanged();
                area_province_adpter.notifyDataSetChanged();
                // RecruitCenterMainActivity.serchinfo.setArea_1(list_province
                // .get(arg2));
                // handler.sendEmptyMessage(100);
            }
        });

        lv_city.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                area_city_adpter.init();
                area_city_adpter.selectionMap.put(arg2, true);
                area_city_adpter.notifyDataSetChanged();
                String id = list_areacityname.get(arg2).get("id");
                list_stree = getSchoolNmae(str_stree, id);
                if (list_stree != null & list_stree.size() > 0) {
                    RecruitCenterMainActivity.serchinfo.setJobtype(list_areacityname.get(arg2).get("name"));
                    Intent intent = new Intent(R_SerchBaseJobTypeActivity.this,
                            R_SerchBaseJobtype2Activity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    finish();
                }
                // }
            }
        });

    }

    public String getList1() {
        String str = "";
        for (int i = 0; i < list_province.size(); i++) {
            if (area_province_adpter.selectionMap.get(i)) {
                str = list_province.get(i).get("name");
                break;
            }
        }
        return str;
    }

    public String city_id(String id) {
        String str = "";
        for (int i = 0; i < list_province.size(); i++) {
            if (area_province_adpter.selectionMap.get(i)) {
                str = list_province.get(i).get(id);
                break;
            }
        }
        return str;
    }

    public String getList2(String id) {
        String str = "";
        for (int i = 0; i < list_areacityname.size(); i++) {
            if (area_city_adpter.selectionMap.get(i)) {
                str = list_areacityname.get(i).get(id);
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
                province = getList1();

                city = getList2("name");
                city_id = getList2("");

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
                } else {
                    if (province != null & !"".equals(province)) {
                        RecruitCenterMainActivity.serchinfo.setJobtype(province);
                    } else {
                        RecruitCenterMainActivity.serchinfo.setJobtype("");
                    }

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
        StatisticalTools.onResume(R_SerchBaseJobTypeActivity.this);


    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.onPause(R_SerchBaseJobTypeActivity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
