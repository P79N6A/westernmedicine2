package com.xywy.askforexpert.module.my.clinic;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.my.userinfo.ApproveInfoActivity;
import com.xywy.askforexpert.module.my.userinfo.CheckStateActivity;

/**
 * 电话医生开通
 *
 * @author 王鹏
 * @2015-5-20下午5:14:15
 */
public class PhoneDoctorOpenActiviy extends Activity {

    private SharedPreferences sp;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        userid = YMApplication.getLoginInfo().getData().getPid();
        sp = getSharedPreferences("save_user", MODE_PRIVATE);

        setContentView(R.layout.order_add_number);
        ((TextView) findViewById(R.id.tv_title)).setText("电话医生");
        ((TextView) findViewById(R.id.tv_text))
                .setText("电话医生是寻医问药网在一站式就医服务平台基础上增设的电话咨询服务，目的是节约医患时间成本、开启医患沟通绿色通道。");
    }

    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn_appay_open:
                if (YMApplication.isDoctorApprove()) {

                    //stone 这两种状态暂时不管 old
//                    if (YMApplication.getLoginInfo().getData().getXiaozhan().getPhone().equals("3")) {
//                        Dialog(PhoneDoctorOpenActiviy.this, getResources()
//                                .getString(R.string.err_add_num));
//                    } else if ("-2"
//                            .equals(YMApplication.getLoginInfo().getData().getXiaozhan().getPhone())) {
//                        Dialog(PhoneDoctorOpenActiviy.this, getResources()
//                                .getString(R.string.myclick_errinfo_phone));
//                    } else
//                    if (Constants.FUWU_AUDIT_STATUS_NO.equals(YMApplication.getLoginInfo().getData().getXiaozhan().getPhone())) {
//
//                        //未开通 去开通 stone
//                        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(
//                                this);
//                        dialog.setTitle("您还没有进行专业认证");
//
//                        dialog.setMessage("开通电话医生，需先通过专业认证");
//
//                        dialog.setPositiveButton("去认证", new OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (sp.getBoolean(userid + "expertapp", false)) {
//                                    CheckStateActivity.startActivity(PhoneDoctorOpenActiviy.this, "checking", "审核中");
//                                    dialog.dismiss();
//                                } else {
//                                    Intent intent = new Intent(
//                                            PhoneDoctorOpenActiviy.this,
//                                            ExpertApproveActivity.class);
//                                    startActivity(intent);
//                                    dialog.dismiss();
//                                }
//
//                            }
//                        });
//                        dialog.setNegativeButton("取消", new OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//
//                            }
//                        });
//                        dialog.create().show();
//
//                    } else {
//                        intent = new Intent(PhoneDoctorOpenActiviy.this,
//                                PhoneDoctorSettingActivity.class);
//                        startActivityForResult(intent, 29);
//                    }


                    //new stone
                    intent = new Intent(PhoneDoctorOpenActiviy.this,
                            PhoneDoctorSettingActivity.class);
                    startActivityForResult(intent, 29);


                } else {

                    android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(
                            PhoneDoctorOpenActiviy.this);
                    dialog.setTitle("您还没有进行专业认证");

                    dialog.setMessage("开通电话医生，需先通过专业认证");

                    dialog.setPositiveButton("去认证", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StatisticalTools.eventCount(PhoneDoctorOpenActiviy.this, "applyteledoc");
                            if (YMApplication.DoctorApproveType() == -1) {
                                CheckStateActivity.startActivity(YMApplication.getAppContext(), "checking", "审核中");
                                dialog.dismiss();
                            } else {
                                Intent intent = new Intent(
                                        PhoneDoctorOpenActiviy.this,
                                        ApproveInfoActivity.class);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.setNegativeButton("取消", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
                    dialog.create().show();
                }
                break;
            default:
                break;
        }
    }

    public void Dialog(Context context, String str) {
        // LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
        // .inflate(R.layout.myclic_dialog, null);
        // final Dialog dialog = new AlertDialog.Builder(context).create();
        // dialog.show();
        // dialog.getWindow().setContentView(layout);
        // TextView content = (TextView) layout.findViewById(R.id.tv_content);
        // content.setText(str);
        // RelativeLayout re_ok = (RelativeLayout)
        // layout.findViewById(R.id.rl_ok);
        // re_ok.setOnClickListener(new OnClickListener()
        // {
        //
        // @Override
        // public void onClick(View arg0)
        // {
        // dialog.dismiss();
        //
        // }
        // });

        final android.support.v7.app.AlertDialog.Builder dialogs = new android.support.v7.app.AlertDialog.Builder(
                context);
        dialogs.setTitle("提示");

        dialogs.setMessage(str);

        // dialogs.setPositiveButton("确定", new OnClickListener()
        // {
        //
        // @Override
        // public void onClick(DialogInterface dialog, int which)
        // {
        // // ((DialogInterface) dialogs).dismiss();
        // }
        // });
        dialogs.setNegativeButton("确定", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        dialogs.create().show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK & requestCode == 29) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(PhoneDoctorOpenActiviy.this);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.onPause(PhoneDoctorOpenActiviy.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
