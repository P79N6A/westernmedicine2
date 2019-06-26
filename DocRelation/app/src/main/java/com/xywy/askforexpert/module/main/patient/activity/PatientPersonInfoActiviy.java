package com.xywy.askforexpert.module.main.patient.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.PatientPerInfo;
import com.xywy.askforexpert.module.main.diagnose.DiagnoseLogListActivity;
import com.xywy.askforexpert.module.main.diagnose.HealthTypeActivity;
import com.xywy.askforexpert.module.main.diagnose.Patient_Group_ManagerActivity;
import com.xywy.askforexpert.module.message.msgchat.ChatMainActivity;
import com.xywy.askforexpert.widget.view.DeletePopupWindow;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.Map;

/**
 * 患者个人信息
 *  医脉的患者个人信息
 * @author 王鹏
 * @2015-5-21下午4:39:54
 */
public class PatientPersonInfoActiviy extends YMBaseActivity {

    FinalBitmap fb;
    private TextView tv_realname;
    private TextView tv_sex;
    private TextView tv_age;
    private TextView tv_patient_info;
    private ImageView img_head;
    private String uid;
    private String hx_userid;
    private PatientPerInfo ppinfo;
    private TextView tv_title;
    private TextView gname;
    private DeletePopupWindow menuWindow;

    private View btn_send_message;
    private LinearLayout main;

    private ImageView img_swith;
    private SharedPreferences mPreferences;
    private SharedPreferences sp;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (ppinfo != null) {
                        if (ppinfo.getCode().equals("0")) {
                            initview();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };
    // 性别 窗口实现监听类
    private OnClickListener itemsOnClick = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            switch (arg0.getId()) {
                case R.id.item_popupwindows_Photo:
                    menuWindow.dismiss();
                    final android.support.v7.app.AlertDialog.Builder dialogs = new android.support.v7.app.AlertDialog.Builder(
                            PatientPersonInfoActiviy.this);
                    dialogs.setTitle("是否确定删除患者");
                    dialogs.setMessage("删除该患者后将不会再接收到TA消息和不能给TA发送消息！");
                    dialogs.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deletePatient();
                        }
                    });
                    dialogs.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialogs.create().show();
                    break;
                default:
                    break;
            }
        }

    };

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.patientperinfo);
//        sp = getSharedPreferences("save_gid", MODE_PRIVATE);
//        mPreferences = getSharedPreferences("msg_manager", MODE_PRIVATE);
//        btn_send_message =  findViewById(R.id.btn_send_message);
//        main = (LinearLayout) findViewById(R.id.main);
//        tv_realname = (TextView) findViewById(R.id.tv_realname);
//        tv_sex = (TextView) findViewById(R.id.tv_sex);
//        tv_age = (TextView) findViewById(R.id.tv_age);
//        tv_patient_info = (TextView) findViewById(R.id.tv_patient_info);
//        img_head = (ImageView) findViewById(R.id.img_head);
//        uid = getIntent().getStringExtra("uid");
//        hx_userid = getIntent().getStringExtra("hx_userid");
//        fb = FinalBitmap.create(PatientPersonInfoActiviy.this, false);
//        tv_title = (TextView) findViewById(R.id.tv_title);
//        img_swith = (ImageView) findViewById(R.id.img_swith);
//        gname = (TextView) findViewById(R.id.tv_groupname);
//
//        if (mPreferences.getBoolean(hx_userid, false)) {
//            img_swith.setBackgroundResource(R.drawable.radio_off);
//        } else {
//            img_swith.setBackgroundResource(R.drawable.radio_on);
//
//        }
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.patientperinfo;
    }

    @Override
    protected void initView() {
        sp = getSharedPreferences("save_gid", MODE_PRIVATE);
        mPreferences = getSharedPreferences("msg_manager", MODE_PRIVATE);
        btn_send_message =  findViewById(R.id.btn_send_message);
        main = (LinearLayout) findViewById(R.id.main);
        tv_realname = (TextView) findViewById(R.id.tv_realname);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_patient_info = (TextView) findViewById(R.id.tv_patient_info);
        img_head = (ImageView) findViewById(R.id.img_head);
        uid = getIntent().getStringExtra("uid");
        hx_userid = getIntent().getStringExtra("hx_userid");
        fb = FinalBitmap.create(PatientPersonInfoActiviy.this, false);
        tv_title = (TextView) findViewById(R.id.tv_title);
        img_swith = (ImageView) findViewById(R.id.img_swith);
        gname = (TextView) findViewById(R.id.tv_groupname);
        if (mPreferences.getBoolean(hx_userid, false)) {
            img_swith.setBackgroundResource(R.drawable.radio_off);
        } else {
            img_swith.setBackgroundResource(R.drawable.radio_on);
        }
    }

    @Override
    protected void initData() {

    }

    public void initview() {
        tv_realname.setText(ppinfo.getData().getRealname());
        String sex;
        if (ppinfo.getData().getSex().equals("0")) {
            sex = "男";
        } else {
            sex = "女";
        }
        tv_sex.setText(sex);
        tv_age.setText(ppinfo.getData().getAge() + "岁");
        fb.display(img_head, ppinfo.getData().getPhoto());
        fb.configLoadfailImage(R.drawable.icon_photo_def);
//        tv_title.setText(ppinfo.getData().getRealname());
        titleBarBuilder.setTitleText(ppinfo.getData().getRealname());
        titleBarBuilder.addItem("", R.drawable.service_topque_right_btn, new ItemClickListener() {
            @Override
            public void onClick() {
                menuWindow = new DeletePopupWindow(PatientPersonInfoActiviy.this,
                        itemsOnClick, "删除患者", -1);
                // 显示窗口
                if ("R7Plus".equals(Build.MODEL)) {
                    menuWindow.showAtLocation(main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, DensityUtils.dp2px(36));
                } else {
                    menuWindow.showAtLocation(main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        }).build();
        gname.setText(ppinfo.getData().getGname());
    }

    public void getData(String uid) {
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did + uid;
        Long st = System.currentTimeMillis();
        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "patientHome");
        params.put("did", did);
        params.put("uid", uid);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        ppinfo = ResolveJson.R_PatientPer(t.toString());
                        handler.sendEmptyMessage(100);
                        super.onSuccess(t);
                    }
                });

    }

    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {
//            case R.id.btn1:
//                finish();
//                break;
            case R.id.btn_send_message:
                if (ppinfo != null) {
                    intent = new Intent(PatientPersonInfoActiviy.this,
                            ChatMainActivity.class);
                    intent.putExtra("userId", hx_userid);
                    intent.putExtra("username", ppinfo.getData().getRealname());
                    intent.putExtra("toHeadImge", ppinfo.getData().getPhoto());
                    intent.putExtra("uid", uid);
                    intent.putExtra("lastgid", ppinfo.getData().getGid());
                    sp.edit().putString(hx_userid, ppinfo.getData().getGid().toString()).commit();
                    startActivity(intent);
                }
                break;
            case R.id.re_diagnose_log:
                intent = new Intent(PatientPersonInfoActiviy.this,
                        DiagnoseLogListActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                break;
            case R.id.re_group_name:
                if (ppinfo != null) {
                    intent = new Intent(PatientPersonInfoActiviy.this,
                            Patient_Group_ManagerActivity.class);
                    intent.putExtra("type", "set_group");
                    intent.putExtra("uid", uid);
                    intent.putExtra("lastgid", ppinfo.getData().getGid());
                    intent.putExtra("hx_userid", hx_userid);
                    startActivity(intent);
                }
                break;
//            case R.id.btn2:
//                menuWindow = new DeletePopupWindow(PatientPersonInfoActiviy.this,
//                        itemsOnClick, "删除患者", -1);
//                // 显示窗口
//                if ("R7Plus".equals(Build.MODEL)) {
//                    menuWindow.showAtLocation(main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, DensityUtils.dp2px(36));
//                } else {
//                    menuWindow.showAtLocation(main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//                }
//                break;
            case R.id.img_swith:

                boolean ischeck = mPreferences.getBoolean(hx_userid, false);
                if (ischeck) {
                    img_swith.setBackgroundResource(R.drawable.radio_on);

                } else {
                    img_swith.setBackgroundResource(R.drawable.radio_off);
                }
                mPreferences.edit().putBoolean(hx_userid, !ischeck).commit();
                break;

            case R.id.re_health_type:
                intent = new Intent(PatientPersonInfoActiviy.this,
                        HealthTypeActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void deletePatient() {
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did + uid;
        Long st = System.currentTimeMillis();
        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "patientdelete");
        params.put("did", did);
        params.put("uid", uid);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        Map<String, String> map = ResolveJson.R_Action(t
                                .toString());
                        if (map.get("code").equals("0")) {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("isdelete", "true");
                            intent.putExtras(bundle);
                            PatientPersonInfoActiviy.this.setResult(RESULT_OK, intent);
                            finish();
                            YMApplication.isrefresh = true;
                            ToastUtils.shortToast(
                                    "删除成功");
                        }
                        handler.sendEmptyMessage(100);
                        super.onSuccess(t);
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkUtil.isNetWorkConnected()) {
            getData(uid);
        } else {
            ToastUtils.shortToast("网络连接失败");
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);

        super.onDestroy();
    }
}
