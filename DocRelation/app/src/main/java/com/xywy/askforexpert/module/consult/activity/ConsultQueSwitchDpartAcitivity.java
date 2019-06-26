package com.xywy.askforexpert.module.consult.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.model.QueData;
import com.xywy.askforexpert.module.consult.ConsultConstants;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.model.consultentity.CommonRspEntity;
import com.xywy.askforexpert.module.main.service.que.adapter.QueDpartAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by zhangzheng on 2017/5/9.
 */

public class ConsultQueSwitchDpartAcitivity extends Activity {
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


    public static final String PARAM_RESULT = "PARAM_RESULT";

    public static void startActivity(Activity context, String questionId) {
        Intent intent = new Intent(context, ConsultQueSwitchDpartAcitivity.class);
        intent.putExtra("id", questionId);
        context.startActivityForResult(intent, ConsultChatActivity.REQUEST_CODE_CHANGE_DEPARTMENT);
    }


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

        oneListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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

        twoListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
                    ToastUtils.shortToast("请选择一级科室");
                } else {
                    if ("".equals(sub2)) {
                        ToastUtils.shortToast("请选择二级科室");
                    } else {
                        String userid = YMApplication.getUUid();
                        String sign = MD5Util.MD5(userid + id + sub1Id + sub2Id
                                + Constants.MD5_KEY);
                        upEditSub(userid, sign, id, sub1Id + "", sub2Id + "");
                    }
                }
        }
    }

    private void upEditSub(String userid, String sign, String id, String sub1,
                           String sub2) {
        ServiceProvider.changeDepartment(userid, id, sub1, sub2, new Subscriber<CommonRspEntity>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CommonRspEntity commonRspEntity) {
                if (commonRspEntity != null && commonRspEntity.getCode() == ConsultConstants.CODE_REQUEST_SUCCESS) {
                    ToastUtils.shortToast("转诊成功");
                    Intent data = new Intent();
                    data.putExtra(PARAM_RESULT, true);
                    setResult(ConsultChatActivity.REQUEST_CODE_CHANGE_DEPARTMENT, data);
                    onBackPressed();
                } else {
                    ToastUtils.shortToast("转诊失败");
                }
            }
        });
    }

}
