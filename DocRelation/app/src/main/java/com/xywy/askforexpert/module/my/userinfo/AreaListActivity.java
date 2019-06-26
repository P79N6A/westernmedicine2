package com.xywy.askforexpert.module.my.userinfo;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.model.IdNameBean;
import com.xywy.askforexpert.module.my.userinfo.adapter.CityAdapter;
import com.xywy.askforexpert.module.my.userinfo.adapter.ProvinceAdapter;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 地区选择-认证模块 stone
 */
public class AreaListActivity extends YMBaseActivity {

    private IdNameBean mCity, mProvince;

    public static final int HOSPITAL = 1;
    public static final int OTHER = 2;//带标题按钮确认的

    private String area_province = null;
    private String arey_cityname = null;
    private List<IdNameBean> list_areacityname;
    private List<IdNameBean> list_province;
    private ListView lv_first_level;
    private ListView lv_second_level;

    private CityAdapter area_city_adpter;
    private ProvinceAdapter area_province_adpter;

    private String city;
    private String province;

    private String city_id;
    private String province_id;

    private JSONObject mJsonObjectCity;
    private int type;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_common_two_level_select;
    }

    @Override
    protected void beforeViewBind() {
        type = getIntent().getIntExtra(Constants.KEY_TYPE, HOSPITAL);
        mCity = (IdNameBean) getIntent().getSerializableExtra(Constants.KEY_CITY);
        mProvince = (IdNameBean) getIntent().getSerializableExtra(Constants.KEY_PROVINCE);
    }

    @Override
    protected void initView() {
        if (type == HOSPITAL) {
            titleBarBuilder.setTitleText("地区");
        } else if (type == OTHER) {
            titleBarBuilder.setTitleText("选择地点");
            addSureBtn();
        }
        lv_first_level = (ListView) findViewById(R.id.lv_first_level);
        lv_second_level = (ListView) findViewById(R.id.lv_second_level);
    }


    private void addSureBtn() {
        titleBarBuilder.addItem("确认", new ItemClickListener() {
            @Override
            public void onClick() {
                onSureClick();
            }
        }).build();
    }


    /**
     * 初始化
     */
    @Override
    protected void initData() {

        area_province = getRowJson(R.raw.province);
        arey_cityname = getRowJson(R.raw.city);

        list_areacityname = new ArrayList<>();
        list_province = GsonUtils.parsJsonArrayStr2List(area_province, IdNameBean.class);

        try {
            mJsonObjectCity = new JSONObject(arey_cityname);
        } catch (JSONException e) {
            e.printStackTrace();
            mJsonObjectCity = null;
        }

        //之前选中过默认选中上次选中的 否则默认第一个选中
        if (list_province != null && list_province.size() > 0 && mJsonObjectCity != null) {

            int pos = 0;
            if (mProvince != null) {
                province = mProvince.name;
                province_id = mProvince.id;
                int len = list_province.size();
                for (int i = 0; i < len; i++) {
                    if (province.equals(list_province.get(i).name)) {
                        pos = i;
                        break;
                    }
                }
            } else {
                province = list_province.get(0).name;
                province_id = list_province.get(0).id;
            }

            area_province_adpter = new ProvinceAdapter(AreaListActivity.this, list_province);
            area_province_adpter.selectionMap.put(pos, true);
            lv_first_level.setAdapter(area_province_adpter);

            initAreaList(province_id);

            area_city_adpter = new CityAdapter(AreaListActivity.this);
            area_city_adpter.setData(list_areacityname);
            if (mCity != null) {
                city_id = mCity.id;
                city = mCity.name;
                int len = list_areacityname.size();
                for (int i = 0; i < len; i++) {
                    if (city.equals(list_areacityname.get(i).name)) {
                        area_city_adpter.selectionMap.put(i, true);
                        break;
                    }
                }
            }
            lv_second_level.setAdapter(area_city_adpter);



            lv_first_level.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    area_province_adpter.init();
                    area_province_adpter.selectionMap.put(position, true);

                    province_id = list_province.get(position).id;
                    province = list_province.get(position).name;

                    initAreaList(province_id);

                    area_city_adpter.setData(list_areacityname);
                    area_city_adpter.notifyDataSetChanged();

                    area_province_adpter.notifyDataSetChanged();
                }
            });

            lv_second_level.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    area_city_adpter.init();
                    area_city_adpter.selectionMap.put(position, true);
                    area_city_adpter.notifyDataSetChanged();

                    city_id = list_areacityname.get(position).id;
                    city = list_areacityname.get(position).name;

                    if (type == HOSPITAL) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.KEY_PROVINCE, new IdNameBean(province_id, province));
                        bundle.putSerializable(Constants.KEY_CITY, new IdNameBean(city_id, city));
                        YMOtherUtils.backForResult(AreaListActivity.this, bundle);
                    }
                }
            });
        } else {
            finish();
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
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 根据 id 返回城市
     *
     * @param id
     * @return
     */
    public void initAreaList(String id) {
        list_areacityname.clear();
        try {
            if (mJsonObjectCity.has(id)) {
                JSONArray json1 = mJsonObjectCity.getJSONArray(id);
                int len = json1.length();
                for (int j = 0; j < len; j++) {
                    list_areacityname.add(new IdNameBean(json1.getJSONObject(j).getString("id")
                            , json1.getJSONObject(j).getString("name")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onSureClick() {
        //待定

    }


}
