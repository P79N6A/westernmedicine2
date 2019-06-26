package com.xywy.askforexpert.module.main.service.recruit;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.my.userinfo.adapter.BaseAreaNameAdapter;
import com.xywy.askforexpert.module.my.userinfo.adapter.BaseCityAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地区 选择
 *
 * @author 王鹏
 * @2015-5-8上午10:15:59
 */
@Deprecated
public class R_AreaListActivity extends Activity {

    private String area_province = null;
    private String arey_cityname = null;
    private List<HashMap<String, String>> list_areacityname = new ArrayList<HashMap<String, String>>();
    private List<HashMap<String, String>> list_province;
    private ListView lv_province;
    private ListView lv_city;

    private BaseAreaNameAdapter area_city_adpter;
    // private BaseSchoolNameAdapter area_province_adpter;
    private BaseCityAdapter area_province_adpter;

    private String city;
    private String province;

    private String city_id;
    private String province_id;
    private Map<String, String> map;

    private String type;
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            map = (HashMap<String, String>) msg.obj;
            switch (msg.what) {
                case 100:
                    // // sc_name_adpter.notifyDataSetChanged();
                    // area_province_adpter.notifyDataSetChanged();
                    // area_city_adpter = new BaseAreaNameAdapter(
                    // R_AreaListActivity.this, list_areacityname);
                    // lv_city.setAdapter(area_city_adpter);
                    if (map.get("code").equals("0")) {
                        YMUserService.getPerInfo().getData().setCity(city_id);
                        YMUserService.getPerInfo().getData().setProvince(province_id);
                        YMUserService.getPerInfo().getData().setCityName(city);
                        YMUserService.getPerInfo().getData().setProvinceName(province);

                        finish();
                    }
                    ToastUtils.shortToast(map.get("msg"));
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.select_school);


        Init();

        type = getIntent().getStringExtra("type");
        if (type.equals("per_hostpital")) {
            ((TextView) findViewById(R.id.tv_title)).setText("选择医院地区");
            findViewById(R.id.btn2).setVisibility(View.GONE);
        } else if (type.equals("stu_per_hostpital")) {
            ((TextView) findViewById(R.id.tv_title)).setText("选择实习医院地区");
            findViewById(R.id.btn2).setVisibility(View.GONE);
        } else if (type.equals("area")) {
            ((TextView) findViewById(R.id.tv_title)).setText("地区");
        } else if (type.equals("serch_area")) {
            ((TextView) findViewById(R.id.tv_title)).setText("选择地点");
            // findViewById(R.id.btn2).setVisibility(View.GONE);
        }
    }

    /**
     * 初始化
     */
    private void Init() {
        list_province = new ArrayList<HashMap<String, String>>();
        if (area_province == null) {
            area_province = getRowJson(R.raw.area_1);
            list_province = ResolveJson.R_List(area_province);
        }
        if (arey_cityname == null) {
            arey_cityname = getRowJson(R.raw.area_2);
            // list_areacityname = getSchoolNmae(arey_cityname, "1");
        }
        lv_province = (ListView) findViewById(R.id.list_schoolcity);
        lv_city = (ListView) findViewById(R.id.list_schoolname);
        area_city_adpter = new BaseAreaNameAdapter(R_AreaListActivity.this);
        area_city_adpter.setData(list_areacityname);
        area_province_adpter = new BaseCityAdapter(R_AreaListActivity.this,
                list_province);
        area_province_adpter.init();
        lv_province.setAdapter(area_province_adpter);
        lv_city.setAdapter(area_city_adpter);
        lv_province.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                area_province_adpter.init();
                area_province_adpter.selectionMap.put(arg2, true);
                String id = list_province.get(arg2).get("id");

                list_areacityname.clear();

                list_areacityname = getSchoolNmae(arey_cityname, id);

                area_city_adpter.setData(list_areacityname);
                area_city_adpter.notifyDataSetChanged();
                area_province_adpter.notifyDataSetChanged();
            }
        });

        lv_city.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                area_city_adpter.init();
                area_city_adpter.selectionMap.put(arg2, true);
                area_city_adpter.notifyDataSetChanged();


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

    public String city_id() {
        String str = "";
        for (int i = 0; i < list_province.size(); i++) {
            if (area_province_adpter.selectionMap.get(i)) {
                str = list_province.get(i).get("id");
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
     * 根据 id 返回地区学校
     *
     * @param json
     * @param id
     * @return
     */
    public List<HashMap<String, String>> getSchoolNmae(String json, String id) {
        return ResolveJson.R_List_id2(json, id);
    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
                province = getList1();
                province_id = city_id();
                city = getList2("name");
                city_id = getList2("id");

                if (type.equals("serch_area")) {
                    if (TextUtils.isEmpty(province) & TextUtils.isEmpty(city) & TextUtils.isEmpty(province_id) & TextUtils.isEmpty(city_id)) {

                    } else {
                        RecruitCenterMainActivity.serchinfo.setArea_1(province);
                        RecruitCenterMainActivity.serchinfo.setArea_2(city);
                        RecruitCenterMainActivity.serchinfo.setArea_1_id(province_id);
                        RecruitCenterMainActivity.serchinfo.setArea_2_id(city_id);
                    }


                    finish();
                } else {
                    if (city == null || city.equals("")) {
                        ToastUtils.shortToast("请选择城市");
                    } else if (province == null || province.equals("")) {
                        ToastUtils.shortToast("请选择省份");
                    } else {

                        Update("province", province_id, "city", city_id);
                        finish();
                    }
                }
            default:
                break;
        }
    }

    public void Update(String key, String value, String key2, String value2) {
        AjaxParams params = new AjaxParams();
        String status = "edit";
        String userid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(userid + status + Constants.MD5_KEY);
        params.put("status", status);
        params.put("command", "info");
        params.put("userid", userid);
        params.put("sign", sign);
        params.put(key, value);
        params.put(key2, value2);
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.DP_COMMON, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {

                Message meg = new Message();
                Map<String, String> map = ResolveJson.R_Action(t.toString());
                meg.obj = map;
                meg.what = 100;
                handler.sendMessage(meg);
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(R_AreaListActivity.this);


    }

    @Override
    protected void onPause() {
        StatisticalTools.onPause(R_AreaListActivity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
