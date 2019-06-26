package com.xywy.askforexpert.module.main.diagnose;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.PatientGroupInfo;
import com.xywy.askforexpert.module.main.diagnose.adapter.BasePatientGroupListAdapter;
import com.xywy.askforexpert.module.main.patient.activity.AddPatientGroupEditActvity;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.HashMap;
import java.util.Map;

import static com.xywy.askforexpert.module.discovery.medicine.common.MyRxBus.notifyLatestPatientInfoChanged;
import static com.xywy.askforexpert.module.discovery.medicine.common.MyRxBus.notifyPatientInfoChanged;

/**
 * 分组管理 stone
 *
 * @author 王鹏
 * @2015-5-21上午10:19:33
 */
public class Patient_Group_ManagerActivity extends YMBaseActivity {

    private SharedPreferences sp;
    private ListView list_group;
    private PatientGroupInfo pginfo;
    private BasePatientGroupListAdapter adapter;
    private Map<String, String> map = new HashMap<String, String>();
    private String type;
    private String lastgid;
    private String uid;
    private String hx_userid;
    private String newid;
    private String mNewGroupName;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (pginfo.getCode().equals("0")) {
                        adapter = new BasePatientGroupListAdapter(
                                Patient_Group_ManagerActivity.this);
                        adapter.setData(pginfo.getData());
                        list_group.setAdapter(adapter);

                        if (type.equals("set_group")) {
                            for (int i = 0; i < pginfo.getData().size(); i++) {
                                if (!TextUtils.isEmpty(lastgid)) {
                                    if (lastgid.equals(pginfo.getData().get(i).getId())) {
                                        adapter.selectionMap.put(i, true);
                                    }
                                }

                            }
                        }
                    }
                    break;
                case 200:
                    if (map.get("code").equals("0")) {
                        // Intent intent =new
                        // Intent(Patient_Group_ManagerActivity.this,PatientPersonInfoActiviy.class);
                        // intent.putExtra("uid", uid);
                        // intent.putExtra("hx_userid", hx_userid);
                        // startActivity(intent);
                        sp.edit().putString(hx_userid, newid).commit();
//					YMApplication.Trace("新id" + newid + "..."
//							+ YMApplication.lastGid);
                        //通知
                        notifyPatientInfoChanged(mNewGroupName);
                        //通知最近咨询患者接口更新数据
                        notifyLatestPatientInfoChanged();
                        finish();

                    }
                    break;
                default:
                    break;
            }

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        getData();

    }

    @Override
    protected void initView() {

        sp = getSharedPreferences("save_gid", MODE_PRIVATE);


        list_group = (ListView) findViewById(R.id.list_group);
        type = getIntent().getStringExtra("type");
        if (type.equals("gruoup")) {
            titleBarBuilder.setTitleText("分组管理").addItem("编辑", new ItemClickListener() {
                @Override
                public void onClick() {
                    if (pginfo != null) {
                        boolean isdelete = adapter.isDelete;
                        adapter.isDelete = !isdelete;
                        adapter.notifyDataSetChanged();
                    }
                }
            }).build();

            View view = LayoutInflater.from(Patient_Group_ManagerActivity.this)
                    .inflate(R.layout.list_foot_view, null);
            Button tv_name = (Button) view.findViewById(R.id.next_btn);
            tv_name.setText("添加分组");
            list_group.addFooterView(view);
        } else if (type.equals("set_group")) {
            titleBarBuilder.setTitleText("分组管理");

            lastgid = getIntent().getStringExtra("lastgid");
            uid = getIntent().getStringExtra("uid");
            hx_userid = getIntent().getStringExtra("hx_userid");
//            findViewById(R.id.btn2).setVisibility(View.GONE);
            list_group.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    adapter.init();
                    adapter.selectionMap.put(arg2, true);
                    adapter.notifyDataSetChanged();
                    newid = pginfo.getData().get(arg2).getId();
                    mNewGroupName = pginfo.getData().get(arg2).getName();
                    LogUtils.i("newid=" + newid + "---groupName=" + mNewGroupName);
//					YMApplication.Trace("新id222" + newid + "..."
//							+ YMApplication.lastGid+"lastgid"+lastgid);
                    sendData(newid, uid, lastgid);

                }
            });

        }
    }

    @Override
    protected void initData() {

    }

    public void sendData(String id, String uid, String lastgid) {
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did + uid + lastgid + id;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "groupUserEdit");
        params.put("did", did);//医生id
        params.put("uid", uid);//患者id
        params.put("newgid", id);//新的分组id
        params.put("lastgid", lastgid);//老的分组id
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        map = ResolveJson.R_Action(t.toString());
                        handler.sendEmptyMessage(200);
                        super.onSuccess(t);
                    }
                });

    }

    public void getData() {
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "groupList");
        params.put("did", did);
//		 params.put("uid", uid);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url,
                params, new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        pginfo = ResolveJson.R_PatientGroupList(t.toString());
                        handler.sendEmptyMessage(100);
                        super.onSuccess(t);
                    }
                });

    }

    public void onClick_back(View view) {
        switch (view.getId()) {
//            case R.id.btn1:
//
//                finish();
//                break;
//            case R.id.btn2:
//                // T.showNoRepeatShort(Patient_Group_ManagerActivity.this, "修改");
//                if (pginfo != null) {
//                    boolean isdelete = adapter.isDelete;
//                    adapter.isDelete = !isdelete;
//                    adapter.notifyDataSetChanged();
//                }

//                break;
            case R.id.next_btn:
                Intent intent = new Intent(Patient_Group_ManagerActivity.this,
                        AddPatientGroupEditActvity.class);
                intent.putExtra("type", "groupAdd");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.patient_group_list;
    }
}
