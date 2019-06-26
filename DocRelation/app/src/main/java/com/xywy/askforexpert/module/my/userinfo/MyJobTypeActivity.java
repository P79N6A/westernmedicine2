package com.xywy.askforexpert.module.my.userinfo;

import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.PersonInfo;
import com.xywy.askforexpert.module.my.userinfo.adapter.BaseJobAdapter;
import com.xywy.askforexpert.module.my.userinfo.service.UpdatePersonInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;

/**
 * 执业类型
 *
 * @author 王鹏
 * @2015-5-9上午9:46:36
 */
@Deprecated
public class MyJobTypeActivity extends YMBaseActivity {

    private ListView list;
    private String str_json;
    private BaseJobAdapter adapter;
    private List<HashMap<String, String>> list_jobtype = new ArrayList<HashMap<String, String>>();
    private String type;
    private Map<String, String> map;
    private String jobtypid;
    private String profess_job;
    private String title;





    @OnClick({R.id.btn1,R.id.btn2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
                profess_job = getScholl();
                if (profess_job != null && !profess_job.equals("")) {
                    UpdatePersonInfo.update("profess_job", profess_job, 100,
                            uiHandler);
                } else {
                    ToastUtils.shortToast("请选择执业类型");
                }
                break;
        }
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.my_job_type;
    }

    @Override
    protected void beforeViewBind() {
        type = getIntent().getStringExtra("type");
        title = getIntent().getStringExtra("title");

    }

    @Override
    protected void initView() {
        hideCommonBaseTitle();
        list = (ListView) findViewById(R.id.list_job);
        // intent.putExtra("type", "approve");

        ((TextView) findViewById(R.id.tv_title)).setText(title);
        if (str_json == null) {
            str_json = getRowJson(R.raw.job_title_1);
            list_jobtype = ResolveJson.R_List(str_json);
        }
        adapter = new BaseJobAdapter(MyJobTypeActivity.this, list_jobtype);
        adapter.init();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                adapter.init();
                adapter.selectionMap.put(arg2, true);
                adapter.notifyDataSetChanged();

                jobtypid = list_jobtype.get(arg2).get("id");

            }
        });

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
                    if (data != null) {
                        data.setProfess_job(profess_job);
                        data.setProfess_job_id(jobtypid);
                        data.setJob("");
                    }
                    ToastUtils.shortToast(map.get("msg"));
                    UpdatePersonInfo.update("job", "", 200, uiHandler);
                    finish();
                }
                break;

            default:
                break;
        }

    }


    public String getScholl() {
        String str = "";
        for (int i = 0; i < list_jobtype.size(); i++) {
            if (adapter.selectionMap.get(i)) {
                str = list_jobtype.get(i).get("name");
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

}
