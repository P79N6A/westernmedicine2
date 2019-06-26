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
 * 我的诊所 预约转诊 stone
 *
 * @author 王鹏
 * @2015-5-14上午8:39:42
 */
public class AppointActivity extends Activity {
    private android.support.v7.app.AlertDialog.Builder dialog;

    private SharedPreferences sp;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.order_add_number);
        userid = YMApplication.getLoginInfo().getData().getPid();
        sp = getSharedPreferences("save_user", MODE_PRIVATE);
        dialog = new android.support.v7.app.AlertDialog.Builder(
                AppointActivity.this);

        ((TextView) findViewById(R.id.tv_title)).setText("预约转诊");
        ((TextView) findViewById(R.id.tv_text))
                .setText("预约转诊是寻医问药网的一项免费预约转诊服务，通过协助专家筛选患者，把真正需要专家的重大疾病患者推荐给专家，把更多的宝贵时间留给关注的患者，提升门诊质量，保证专家可以更有效率的安排门诊工作。");

    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn_appay_open:
                StatisticalTools.eventCount(AppointActivity.this, "applyadd");
                if (YMApplication.isDoctorApprove()) {
                    //old stone
//                    // if ("5".equals(YMApplication.getLoginInfo().getData().getXiaozhan().getYuyue()))
//                    // {
//                    if ("-2".equals(YMApplication.getLoginInfo().getData().getXiaozhan().getYuyue())) {
//                        Dialog(AppointActivity.this,
//                                getResources().getString(
//                                        R.string.myclick_errinfo_addnum));
//                    } else if ("-1".equals(YMApplication.getLoginInfo().getData().getXiaozhan().getYuyue())) {
////					Dialog(AppointActivity.this,
////							getResources().getString(
////									R.string.myclick_errinfo_addnum));
//
//                        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(
//                                this);
//                        dialog.setTitle("您还没有进行专业认证");
//
//                        dialog.setMessage("开通预约转诊，需先通过专业认证");
//
//                        dialog.setPositiveButton("去认证", new OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (sp.getBoolean(userid + "expertapp", false)) {
//                                    Intent intent = new Intent(
//                                            AppointActivity.this,
//                                            CheckStateActivity.class);
//                                    intent.putExtra("type", "checking");
//                                    intent.putExtra("title", "审核中");
//                                    startActivity(intent);
//                                    dialog.dismiss();
//                                } else {
//                                    Intent intent = new Intent(
//                                            AppointActivity.this,
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
//                    } else if ("3".equals(YMApplication.getLoginInfo().getData().getXiaozhan().getYuyue())) {
//                        Dialog(AppointActivity.this,
//                                getResources().getString(R.string.err_add_num));
//                    } else {
//                        Intent intent = new Intent(AppointActivity.this,
//                                AddnumberSettingActivity.class);
//                        startActivityForResult(intent, 29);
//                    }

                    //new stone
                    Intent intent = new Intent(AppointActivity.this,
                            AddnumberSettingActivity.class);
                    startActivityForResult(intent, 29);

                } else

                {
                    // dialog = new DialogWindow(AppointActivity.this, "您还没有进行专业认证",
                    // "开通预约转诊，需先通过专业认证！", "取消", "去认证");
                    // dialog.createDialogView(itemsOnClick);

                    dialog.setTitle("您还没有进行专业认证");

                    dialog.setMessage("预约转诊，需先通过专业认证");

                    dialog.setPositiveButton("去认证", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (YMApplication.DoctorApproveType() == -1) {
                                CheckStateActivity.startActivity(AppointActivity.this, "checking", "审核中");
                                dialog.dismiss();
                            } else {
                                Intent intent = new Intent(AppointActivity.this,
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK & requestCode == 29) {
            finish();
        }
    }

    public void Dialog(Context context, String str) {
        // LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
        // .inflate(R.layout.myclic_dialog, null);
        // final AlertDialog dialog = new AlertDialog.Builder(context).create();
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
                AppointActivity.this);
        dialogs.setTitle("提示");

        dialogs.setMessage(str);

//		dialogs.setPositiveButton("确定", new OnClickListener()
//		{
//
//			@Override
//			public void onClick(DialogInterface dialog, int which)
//			{
////				((DialogInterface) dialogs).dismiss();
//			}
//		});
        dialogs.setNegativeButton("确定", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        dialogs.create().show();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
