package com.xywy.askforexpert.module.my.userinfo;

import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.LoginInfo;
import com.xywy.askforexpert.model.PersonInfo;
import com.xywy.askforexpert.module.my.userinfo.adapter.BaseCityAdapter;
import com.xywy.askforexpert.module.my.userinfo.adapter.BaseSchoolNameAdapter;
import com.xywy.askforexpert.module.my.userinfo.service.UpdatePersonInfo;

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
 * 选择 毕业院校
 *
 * @author 王鹏
 * @2015-5-7下午2:33:48
 */
public class SchoolListActivity extends YMBaseActivity {

    private static final String TAG = "SchoolListActivity";
    private String School_cityjson = null;
    private String School_namejson = null;
    private List<HashMap<String, String>> list_schoolcity = new ArrayList<HashMap<String, String>>();
    private List<String> list_schoolname;
    private ListView lv_schoolcity;
    private ListView lv_schoolname;
    private Map<String, String> map;

    private BaseCityAdapter sc_city_adpter;
    private BaseSchoolNameAdapter sc_name_adpter;
    BaseSchoolNameAdapter sc_name_adpter1;
    String title;
    String Str_school = null;
    String type;
    List<Integer> list = new ArrayList<Integer>();


    @Override
    protected int getLayoutResId() {
        return R.layout.select_school;
    }

    @Override
    protected void beforeViewBind() {
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        list = getIntent().getIntegerArrayListExtra("list");

    }

    @Override
    protected void initView() {
        hideCommonBaseTitle();
        init();

        ((TextView) findViewById(R.id.tv_title)).setText(title);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);

        map = (HashMap<String, String>) msg.obj;
        switch (msg.what) {
            case 100:
                if (map.get("code").equals("0")) {
                    PersonInfo data = YMUserService.getPerInfo().getData();
                    LoginInfo.UserData data1 = YMApplication.getLoginInfo().getData();
                    if (data != null && data1 != null) {
                        if (type.equals("school")) {
                            data.setSchool(Str_school);
                            data1.setSchool(Str_school);
                        } else if (type.equals("profession")) {
                            data.setProfession(Str_school);
                            data1.setProfession(Str_school);
                        }
                    }


                    finish();
                }
                ToastUtils.shortToast(map.get("msg"));
                break;

            default:
                break;
        }
    }

    /**
     * 初始化
     */
    private void init() {
        list_schoolname = new ArrayList<String>();
        if (School_cityjson == null) {
            School_cityjson = getRowJson(list.get(0));
            list_schoolcity = ResolveJson.R_List(School_cityjson);
        }
        if (School_namejson == null) {
            School_namejson = getRowJson(list.get(1));
        }
        lv_schoolcity = (ListView) findViewById(R.id.list_schoolcity);
        lv_schoolname = (ListView) findViewById(R.id.list_schoolname);
        sc_city_adpter = new BaseCityAdapter(SchoolListActivity.this,
                list_schoolcity);
        sc_city_adpter.init();
        sc_name_adpter1 = new BaseSchoolNameAdapter(SchoolListActivity.this);
        sc_name_adpter1.setData(list_schoolname);
        lv_schoolcity.setAdapter(sc_city_adpter);
        lv_schoolname.setAdapter(sc_name_adpter1);
        lv_schoolcity.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                sc_city_adpter.init();
                sc_city_adpter.selectionMap.put(arg2, true);
                String id = list_schoolcity.get(arg2).get("id");
                list_schoolname.clear();
                list_schoolname = getSchoolNmae(School_namejson, id);
                sc_name_adpter1.setData(list_schoolname);
                sc_name_adpter1.notifyDataSetChanged();
                sc_city_adpter.notifyDataSetChanged();
            }
        });
        lv_schoolname.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                sc_name_adpter1.init();
                sc_name_adpter1.selectionMap.put(arg2, true);
                sc_name_adpter1.notifyDataSetChanged();
            }
        });

    }

    public String getScholl() {
        String str = "";
        for (int i = 0; i < list_schoolname.size(); i++) {
            if (sc_name_adpter1.selectionMap.get(i)) {
                str = list_schoolname.get(i);
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
    public List<String> getSchoolNmae(String json, String id) {
        return ResolveJson.R_shoolname(json, id);
    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
                Str_school = getScholl();
                if (Str_school != null && !Str_school.equals("")) {

                    UpdatePersonInfo.update(type, Str_school, 100, uiHandler);
                } else {
                    ToastUtils.shortToast("请选择" + title);
                }
            default:
                break;
        }
    }

    public void sendData(AjaxParams params) {
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.DP_COMMON, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                DLog.i(TAG, "修改信息。。" + t.toString());

                map = ResolveJson.R_Action(t.toString());
                uiHandler.sendEmptyMessage(100);
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }


}
