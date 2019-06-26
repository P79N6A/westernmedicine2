package com.xywy.askforexpert.module.main.service.que;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.model.QueData;
import com.xywy.askforexpert.module.main.service.que.adapter.QueDpartAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：D_Platform 类名称：QueSwitchDpart 类描述：纠正科室 创建人：shihao 创建时间：2015-6-24
 * 上午10:14:58 修改备注：
 */
public class QueSwitchDpart extends Activity {

    private String str_section_1 = null;
    private String str_section_2 = null;

    private List<QueData> oneQueDatas = new ArrayList<QueData>();
    private List<QueData> twoQueDatas;

    private ListView oneListview, twoListview;

    private QueDpartAdapter one_adpter;
    private QueDpartAdapter two_adpter;

    private String id;

    private int sub1Id, sub2Id;

    private String sub1 = "";
    private String sub2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra("id");
        sub1 = getIntent().getStringExtra("onedpart");
        sub2 = getIntent().getStringExtra("twodpart");

        setContentView(R.layout.select_school);
        init();
        ((TextView) findViewById(R.id.tv_title)).setText("纠正科室");

    }

    private void init() {

        oneListview = (ListView) findViewById(R.id.list_schoolcity);
        twoListview = (ListView) findViewById(R.id.list_schoolname);

        twoQueDatas = new ArrayList<QueData>();
        if (str_section_1 == null) {
            str_section_1 = getRowJson(R.raw.section_1);
            oneQueDatas = parseDpart(str_section_1);
        }

        one_adpter = new QueDpartAdapter(this, 1);
        one_adpter.bindData(oneQueDatas);
        oneListview.setAdapter(one_adpter);

        for (int i = 0; i < oneQueDatas.size(); i++) {
            if (oneQueDatas.get(i).getName().equals(sub1)) {
                one_adpter.isSelector(i);
                sub1Id = oneQueDatas.get(i).getId();
            }
        }

        if (str_section_2 == null) {
            str_section_2 = getRowJson(R.raw.que_two_dpart);
            parseTwoDpart(str_section_2, sub1Id + "");
        }

        two_adpter = new QueDpartAdapter(this, 2);
        two_adpter.bindData(twoQueDatas);
        twoListview.setAdapter(two_adpter);

        for (int i = 0; i < twoQueDatas.size(); i++) {
            if (twoQueDatas.get(i).getName().equals(sub2)) {
                two_adpter.isSelector(i);
                sub2Id = twoQueDatas.get(i).getId();
            }
        }

        oneListview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                one_adpter.isSelector(position);

                int enterId = oneQueDatas.get(position).getId();
                twoQueDatas.clear();
                parseTwoDpart(str_section_2, enterId + "");
                two_adpter.bindData(twoQueDatas);
                two_adpter.isSelector(-1);
                sub2 = "";
                sub1 = enterId + "";
                sub1Id = enterId;
            }
        });

        twoListview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                two_adpter.isSelector(position);
                sub2 = twoQueDatas.get(position).getId() + "";
                sub2Id = twoQueDatas.get(position).getId();
            }
        });

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
     * 解析一级科室
     *
     * @param json
     * @return
     */
    private List<QueData> parseDpart(String json) {

        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonElement = array.getJSONObject(i);
                QueData data = new QueData();
                data.setId(jsonElement.getInt("id"));
                data.setName(jsonElement.getString("name"));

                oneQueDatas.add(data);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return oneQueDatas;
    }

    private List<QueData> parseTwoDpart(String json, String id) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray(id);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonElement = array.getJSONObject(i);
                QueData data = new QueData();
                data.setId(jsonElement.getInt("id"));
                data.setName(jsonElement.getString("name"));
                twoQueDatas.add(data);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return twoQueDatas;
    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:

                if ("".equals(sub1)) {
                    ToastUtils.shortToast( "请选择一级科室");
                } else {
                    if ("".equals(sub2)) {
                        ToastUtils.shortToast( "请选择二级科室");
                    } else {
                        StatisticalTools.eventCount(QueSwitchDpart.this, "submitcorrectdepartment");
                        String userid = YMApplication.getLoginInfo().getData().getPid();
                        String sign = MD5Util.MD5(userid + id + sub1Id + sub2Id
                                + Constants.MD5_KEY);
                        upEditSub(userid, sign, id, sub1Id + "", sub2Id + "");
                    }
                }
        }
    }

    private void upEditSub(String userid, String sign, String id, String sub1,
                           String sub2) {
        AjaxParams params = new AjaxParams();

        params.put("id", id);
        params.put("userid", userid);
        params.put("sign", sign);
        params.put("sub1", sub1);
        params.put("sub2", sub2);
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.QUE_EDIT_SUB, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 0) {
                        ToastUtils.shortToast( msg);
                        SharedPreferences.Editor edit = getSharedPreferences(
                                "isskip", MODE_PRIVATE).edit();
                        edit.putBoolean("skip", true);
                        edit.commit();
                        Intent intent = new Intent(
                                QueSwitchDpart.this,
                                QueDetailActivity.class);
                        setResult(2018, intent);
                        finish();
                    } else {
                        ToastUtils.shortToast( msg);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    public void onPause() {
        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
