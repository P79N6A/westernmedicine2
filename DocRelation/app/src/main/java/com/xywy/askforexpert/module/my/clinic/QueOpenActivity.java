package com.xywy.askforexpert.module.my.clinic;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.main.service.que.QueSettingActivity;
import com.xywy.askforexpert.module.my.userinfo.ApproveInfoActivity;
import com.xywy.askforexpert.module.my.userinfo.CheckStateActivity;
import com.xywy.askforexpert.module.my.userinfo.ExpertApproveActivity;

/**
 * 问题广场
 *
 * @author 王鹏
 * @2015-6-15上午9:17:24
 */
public class QueOpenActivity extends Activity {
    private SharedPreferences sp;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.order_add_number);
        ((TextView) findViewById(R.id.tv_title)).setText("问题广场");
        userid = YMApplication.getLoginInfo().getData().getPid();
        sp = getSharedPreferences("save_user", MODE_PRIVATE);
        ((TextView) findViewById(R.id.tv_text))
                .setText("经过专业认证的医生可以通过回复患者的提问来赚取报酬，级别越高所得到的报酬也越多。注：为了保证医生的真实性，让患者受到真实可靠的回复，所以需要医生进行实名认证");
        ((Button) findViewById(R.id.btn_appay_open)).setText("立即设置");
    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn_appay_open:
                StatisticalTools.eventCount(QueOpenActivity.this, "wdset");
                if (YMApplication.isDoctorApprove()) {
                    if ("-1".equals(YMApplication.getLoginInfo().getData().getXiaozhan().getDati())) {
//					Dialog(QueOpenActivity.this, getResources()
//							.getString(R.string.myclick_errinfo_addnum));
                        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(
                                this);
                        dialog.setTitle("您还没有进行专业认证");

                        dialog.setMessage("开通问题广场，需先通过专业认证");

                        dialog.setPositiveButton("去认证", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (sp.getBoolean(userid + "expertapp", false)) {
                                    CheckStateActivity.startActivity(YMApplication.getAppContext(),"checking","审核中");
                                    dialog.dismiss();
                                } else {
                                    Intent intent = new Intent(
                                            QueOpenActivity.this,
                                            ExpertApproveActivity.class);
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

                    } else {
                        Intent intent = new Intent(QueOpenActivity.this,
                                QueSettingActivity.class);
                        startActivity(intent);
                    }

                } else {
//				dialog = new DialogWindow(QueOpenActivity.this, "您还没有进行专业认证",
//						"开通问题广场，需先通过专业认证！", "取消", "去认证");
//				dialog.createDialogView(itemsOnClick);

                    android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(
                            QueOpenActivity.this);
                    dialog.setTitle("您还没有进行专业认证");

                    dialog.setMessage("开通问题广场，需先通过专业认证");

                    dialog.setPositiveButton("去认证", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (YMApplication.DoctorApproveType() == -1) {
                                CheckStateActivity.startActivity(YMApplication.getAppContext(),"checking","审核中");
                                dialog.dismiss();
                            } else {
                                Intent intent = new Intent(QueOpenActivity.this,
                                        ApproveInfoActivity.class);
                                startActivity(intent);
                            }
                            dialog.dismiss();

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
//		LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
//				.inflate(R.layout.myclic_dialog, null);
//		final Dialog dialog = new AlertDialog.Builder(context).create();
//		dialog.show();
//		dialog.getWindow().setContentView(layout);
//		TextView content = (TextView) layout.findViewById(R.id.tv_content);
//		content.setText(str);
//		RelativeLayout re_ok = (RelativeLayout) layout.findViewById(R.id.rl_ok);
//		re_ok.setOnClickListener(new OnClickListener()
//		{
//
//			@Override
//			public void onClick(View arg0)
//			{
//				dialog.dismiss();
//
//			}
//		});

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
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(QueOpenActivity.this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        StatisticalTools.onPause(QueOpenActivity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
