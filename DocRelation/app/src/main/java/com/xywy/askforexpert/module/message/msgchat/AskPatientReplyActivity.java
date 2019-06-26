package com.xywy.askforexpert.module.message.msgchat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.AskPatientReplyInfo;
import com.xywy.askforexpert.module.doctorcircle.adapter.SwipListViewAdapter;
import com.xywy.askforexpert.module.doctorcircle.adapter.SwipListViewAdapter.OnItemDeleteClickListener;
import com.xywy.askforexpert.module.main.patient.activity.AddPatientGroupEditActvity;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.HashMap;
import java.util.Map;

/**
 * 医患 聊天 快捷回复
 *
 * @author 王鹏
 * @2015-6-4上午11:05:55
 */
public class AskPatientReplyActivity extends Activity {
    private static final String TAG = "AskPatientReplyActivity";
    private ListView slvListView;
    private SwipListViewAdapter adapter;
    private int mRightWidth = 200;
    private AskPatientReplyInfo askinfo = new AskPatientReplyInfo();
    private Map<String, String> map = new HashMap<String, String>();

    private android.support.v7.app.AlertDialog.Builder dialog;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (askinfo.getCode().equals("0")) {
                        adapter = new SwipListViewAdapter(askinfo.getData(),
                                AskPatientReplyActivity.this, mRightWidth, listener);
                        slvListView.setAdapter(adapter);
                    }

                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ask_patient_reply);
        dialog = new android.support.v7.app.AlertDialog.Builder(
                AskPatientReplyActivity.this);
        // findViewById(R.id.btn2).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.tv_title)).setText("快捷回复");
        // getData();
        slvListView = (ListView) findViewById(R.id.slv_list_view);
        View view = LayoutInflater.from(AskPatientReplyActivity.this).inflate(
                R.layout.list_foot_view, null);
        Button tv_name = (Button) view.findViewById(R.id.next_btn);
        tv_name.setText("添加");
        slvListView.addFooterView(view);

        slvListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("type", askinfo.getData().get(arg2).getWord());
                intent.putExtras(bundle);
                AskPatientReplyActivity.this.setResult(RESULT_OK, intent);
                finish();
            }
        });
        slvListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int position, long arg3) {

                // dialog = new DialogWindow(AskPatientReplyActivity.this,
                // "删除快捷回复", "是否确定删除？", "取消", "确定");
                dialog.setTitle("删除快捷回复");

                dialog.setMessage("是否确定删除？");
                dialog.setPositiveButton("确定", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        delete(askinfo.getData().get(position).getId(),
                                position);
                        adapter.initView(GlobalContent.viewMap.get(askinfo
                                .getData().get(position).getWord()));
                        // list.remove(contentString);
                        // adapter.setList(list);
                        // adapter.notifyDataSetChanged();

                        askinfo.getData().remove(position);
                        adapter.setList(askinfo.getData());
                        adapter.notifyDataSetChanged();

                    }
                });
                dialog.setNegativeButton("取消", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
                dialog.create().show();
                return true;
            }
        });
    }

    private OnItemDeleteClickListener listener = new OnItemDeleteClickListener() {

        @Override
        public void onRightClick(View v, int position) {
            // String contentString = (String) adapter.getItem(position);

            delete(askinfo.getData().get(position).getId(), position);
            adapter.initView(GlobalContent.viewMap.get(askinfo.getData()
                    .get(position).getWord()));
            // list.remove(contentString);
            // adapter.setList(list);
            // adapter.notifyDataSetChanged();

            askinfo.getData().remove(position);
            adapter.setList(askinfo.getData());
            adapter.notifyDataSetChanged();
        }
    };

    public void delete(String id, final int position) {
        final ProgressDialog dialog = new ProgressDialog(this, "正在加载中...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did + id;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "quickreplydel");
        params.put("id", id);
        params.put("did", did);
        params.put("sign", sign);
        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        dialog.dismiss();
                        DLog.i(TAG, "医患交流快捷回复" + t.toString());
                        Gson gson = new Gson();
                        // TODO Auto-generated method stub
                        map = ResolveJson.R_Action(t.toString());
                        if (map.get("code").equals("0")) {

                        } else {
                            ToastUtils.shortToast(
                                    map.get("msg"));
                        }
                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        dialog.dismiss();
                        // TODO Auto-generated method stub
                        super.onFailure(t, errorNo, strMsg);
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
        params.put("m", "quickreplylist");
        params.put("did", did);
        params.put("sign", sign);
        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        DLog.i(TAG, "医患交流快捷回复" + t.toString());
                        Gson gson = new Gson();
                        // TODO Auto-generated method stub
                        askinfo = gson.fromJson(t.toString(),
                                AskPatientReplyInfo.class);
                        handler.sendEmptyMessage(100);
                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        super.onFailure(t, errorNo, strMsg);
                    }
                });

    }

    public void addNewReply() {
        getData();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getData();

    }

    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.next_btn:
                intent = new Intent(AskPatientReplyActivity.this,
                        AddPatientGroupEditActvity.class);
                intent.putExtra("type", "quickreplyadd");
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
}
