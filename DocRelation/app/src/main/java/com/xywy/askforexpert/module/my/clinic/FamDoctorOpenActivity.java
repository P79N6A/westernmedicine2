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
import com.xywy.askforexpert.module.my.userinfo.ExpertApproveActivity;

/**
 * 开通家庭医生
 *
 * @author 王鹏
 * @2015-5-20下午4:15:53
 */
public class FamDoctorOpenActivity extends Activity {

    private SharedPreferences sp;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        sp = getSharedPreferences("save_user", MODE_PRIVATE);

        setContentView(R.layout.order_add_number);
        ((TextView) findViewById(R.id.tv_title)).setText("家庭医生");
        ((TextView) findViewById(R.id.tv_text))
                .setText("家庭医生服务面向公立一级及以上等级医院工作的全科医生，公立二级及以上等级医院的医师、主治医生、副主任医师、主任医师，具有二级公共营养师或国家二级心理咨询师证且有相关工作经验3年以上，在健康顾问团队的协助下，通过寻医问药平台为广大用户提供个性化的健康管理与咨询服务");
        userid = YMApplication.getLoginInfo().getData().getPid();
    }

    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn_appay_open:
                StatisticalTools.eventCount(FamDoctorOpenActivity.this, "applyfamilydoc");

                if (YMApplication.isDoctorApprove()) {
                    // if ("-1".equals(YMApplication.getLoginInfo().getData()
                    // .getXiaozhan().getDati()))
                    // {
                    // // dialog = new DialogWindow(FamDoctorOpenActivity.this,
                    // // "您还没有进行专业认证", "开通家庭医生，需先通过专业认证！", "取消", "去认证");
                    // // dialog.createDialogView(itemsOnClick);
                    // android.support.v7.app.AlertDialog.Builder dialog = new
                    // android.support.v7.app.AlertDialog.Builder(
                    // FamDoctorOpenActivity.this);
                    // dialog.setTitle("您还没有进行专业认证");
                    //
                    // dialog.setMessage("开通家庭医生，需先通过专业认证");
                    //
                    // dialog.setPositiveButton("去认证", new OnClickListener()
                    // {
                    //
                    // @Override
                    // public void onClick(DialogInterface dialog, int which)
                    // {
                    // if (sp.getBoolean(userid + "expertapp", false))
                    // {
                    // Intent intent = new Intent(
                    // FamDoctorOpenActivity.this,
                    // CheckState.class);
                    // intent.putExtra("type", "checking");
                    // intent.putExtra("title", "审核中");
                    // startActivity(intent);
                    // dialog.dismiss();
                    // } else
                    // {
                    // Intent intent = new Intent(
                    // FamDoctorOpenActivity.this,
                    // ExpertApproveActivity.class);
                    // startActivity(intent);
                    // dialog.dismiss();
                    // }
                    //
                    // }
                    // });
                    // dialog.setNegativeButton("取消", new OnClickListener()
                    // {
                    //
                    // @Override
                    // public void onClick(DialogInterface dialog, int which)
                    // {
                    // dialog.dismiss();
                    //
                    // }
                    // });
                    // dialog.create().show();
                    // } else
                    // {

                    if ("-1".equals(YMApplication.getLoginInfo().getData()
                            .getXiaozhan().getFamily())) {
                        // Dialog(FamDoctorOpenActivity.this, getResources()
                        // .getString(R.string.myclick_errinfo_phone));
                        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(
                                FamDoctorOpenActivity.this);
                        dialog.setTitle("您还没有进行专业认证");

                        dialog.setMessage("开通家庭医生，需先通过专业认证");

                        dialog.setPositiveButton("去认证", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (sp.getBoolean(userid + "expertapp", false)) {
                                    CheckStateActivity.startActivity(FamDoctorOpenActivity.this,"checking","审核中");
                                    dialog.dismiss();
                                } else {
                                    Intent intent = new Intent(
                                            FamDoctorOpenActivity.this,
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
                        intent = new Intent(FamDoctorOpenActivity.this,
                                FamDoctorSetingActivity.class);
                        startActivityForResult(intent, 29);
                    }

                    // }
                } else {
                    // dialog = new DialogWindow(FamDoctorOpenActivity.this,
                    // "您还没有进行专业认证", "开通家庭医生，需先通过专业认证！", "取消", "去认证");
                    // dialog.createDialogView(itemsOnClick);
                    android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(
                            FamDoctorOpenActivity.this);
                    dialog.setTitle("您还没有进行专业认证");

                    dialog.setMessage("开通家庭医生，需先通过专业认证");

                    dialog.setPositiveButton("去认证", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (YMApplication.DoctorApproveType() == -1) {
                                CheckStateActivity.startActivity(FamDoctorOpenActivity.this,"checking","审核中");
                                dialog.dismiss();
                            } else {
                                Intent intent = new Intent(
                                        FamDoctorOpenActivity.this,
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
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(FamDoctorOpenActivity.this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        StatisticalTools.onPause(FamDoctorOpenActivity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }
}
