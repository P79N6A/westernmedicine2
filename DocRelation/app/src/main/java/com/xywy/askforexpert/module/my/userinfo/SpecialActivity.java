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
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.module.my.userinfo.adapter.BaseCityAdapter;
import com.xywy.askforexpert.module.my.userinfo.adapter.BaseSection2Adapter;
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
 * 擅长疾病
 *
 * @author 王鹏
 * @2015-6-27下午4:47:58
 */


@Deprecated
public class SpecialActivity extends YMBaseActivity {

    private String str_section_1 = null;
    private String str_section_2 = null;
    private List<HashMap<String, String>> list_1 = new ArrayList<HashMap<String, String>>();
    private List<HashMap<String, String>> list_2;

    // private List<String> list_schoolname;
    private ListView lv_schoolcity;
    private ListView lv_schoolname;

    private BaseCityAdapter sc_city_adpter;
    private BaseSection2Adapter sc_name_adpter;

    private String subject;
    private String subject2;
    private String subject_id;
    private String subject2_id;
    private Map<String, String> map;
    String section_id;
    private int count;
    private int maxcount = 10;



    @Override
    protected int getLayoutResId() {
        return R.layout.select_school;
    }

    @Override
    protected void beforeViewBind() {
        section_id = getIntent().getStringExtra("section_id");


    }

    @Override
    protected void initView() {
        hideCommonBaseTitle();
    }

    @Override
    protected void initData() {
        Init();
        ((TextView) findViewById(R.id.tv_title)).setText("擅长疾病");

    }


    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);

        map = (HashMap<String, String>) msg.obj;
        switch (msg.what) {
            case 100:
                // sc_name_adpter.notifyDataSetChanged();
                // sc_city_adpter.notifyDataSetChanged();
                // sc_name_adpter = new
                // BaseSection2Adapter(SecitonActivity.this,
                // list_2);
                // lv_schoolname.setAdapter(sc_name_adpter);
                if (map.get("code").equals("0")) {
                    YMUserService.getPerInfo().getData().setSpecial(subject2);

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
    private void Init() {
        list_2 = new ArrayList<HashMap<String, String>>();
        if (str_section_1 == null) {
            str_section_1 = getRowJson(R.raw.section_info_2);
            list_1 = getBaseNmae(str_section_1, section_id);
        }
        if (str_section_2 == null) {
            str_section_2 = getRowJson(R.raw.illnessadept);
            // list_2 = getBaseNmae(str_section_2, "272");
        }
        lv_schoolcity = (ListView) findViewById(R.id.list_schoolcity);
        lv_schoolname = (ListView) findViewById(R.id.list_schoolname);
        sc_city_adpter = new BaseCityAdapter(SpecialActivity.this, list_1);
        sc_name_adpter = new BaseSection2Adapter(SpecialActivity.this);
        sc_city_adpter.init();
        sc_name_adpter.setData(list_2);
        lv_schoolcity.setAdapter(sc_city_adpter);
        lv_schoolname.setAdapter(sc_name_adpter);
        lv_schoolcity.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                sc_city_adpter.init();
                sc_city_adpter.selectionMap.put(arg2, true);
                String id = list_1.get(arg2).get("id");
                count = 0;
                list_2.clear();
                list_2 = getBaseNmae(str_section_2, id);
                sc_name_adpter.setData(list_2);
                sc_name_adpter.notifyDataSetChanged();
                sc_city_adpter.notifyDataSetChanged();
                // handler.sendEmptyMessage(100);
            }
        });
        lv_schoolname.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // sc_name_adpter.init();
                boolean ischeck = sc_name_adpter.selectionMap.get(arg2);
                if (!ischeck) {

                    if (count >= maxcount) {
                        ToastUtils.shortToast("最多选择10个");
                        return;
                    }

                }

                sc_name_adpter.selectionMap.put(arg2, !ischeck);
                if (ischeck) {
                    --count;
                } else {
                    ++count;
                }
                sc_name_adpter.notifyDataSetChanged();

            }
        });

    }

    public String getList1(String id) {
        String str = "";
        for (int i = 0; i < list_1.size(); i++) {
            if (sc_city_adpter.selectionMap.get(i)) {
                str = list_1.get(i).get(id);
                break;
            }
        }
        return str;
    }

    public String getList2(String id) {
        String str = "";
        StringBuffer sb = new StringBuffer();
        if ("name".equals(id)) {
            for (int i = 0; i < list_2.size(); i++) {
                if (sc_name_adpter.selectionMap.get(i)) {
                    sb.append(list_2.get(i).get("name") + ",");
                }
            }
            if (sb.length() > 0) {
                str = sb.substring(0, sb.lastIndexOf(","));
            }
        } else {

            for (int i = 0; i < list_2.size(); i++) {
                if (sc_name_adpter.selectionMap.get(i)) {
                    str = list_2.get(i).get(id);
                    break;
                }
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
    public List<String> getSchoolNmae(String json, String id) {
//		System.out.println("打印数据..." + id);
        return ResolveJson.R_shoolname(json, id);
    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
                subject_id = getList1("id");
                subject2_id = getList2("id");
                subject = getList1("name");
                subject2 = getList2("name");

                if (subject_id == null || subject_id.equals("")) {
                    ToastUtils.shortToast("请选择科室");
                } else if (subject2_id == null || subject2_id.equals("")) {
                    ToastUtils.shortToast("请选择擅长疾病");
                } else {
                    if (NetworkUtil.isNetWorkConnected()) {
                        UpdatePersonInfo.update("special", subject2, 100, uiHandler);

                    } else {
                        ToastUtils.shortToast("网络连接失败");
                    }
                }
            default:
                break;
        }
    }

    /**
     * 根据 id 返回地区学校
     *
     * @param json
     * @param id
     * @return
     */

    public List<HashMap<String, String>> getBaseNmae(String json, String id)

    {


        return ResolveJson.R_List_id(json, id);

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
                uiHandler.sendMessage(meg);
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });

    }

}
