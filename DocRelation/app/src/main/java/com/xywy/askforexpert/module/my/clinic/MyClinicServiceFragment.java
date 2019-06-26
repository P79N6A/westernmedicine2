package com.xywy.askforexpert.module.my.clinic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseFragment;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.ClinicStatInfo;
import com.xywy.askforexpert.module.main.service.que.QueSettingActivity;
import com.xywy.askforexpert.module.my.userinfo.CheckStateActivity;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的诊所
 *
 * @author 王鹏
 * @2015-5-13下午8:34:09
 */
public class MyClinicServiceFragment extends YMBaseFragment {
    private static final String TAG = "MyClinicActivity";
    @Bind(R.id.tv_datil)
    TextView tv_datil;
    @Bind(R.id.tv_yuyue_state)
    TextView tv_yuyue_state;
    @Bind(R.id.tv_phone_state)
    TextView tv_phone_state;
    @Bind(R.id.tv_fam_state)
    TextView tv_fam_state;

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initView();
    }

    @Override
    public String getStatisticalPageName() {
        return null;
    }

    public void getData() {
        final ProgressDialog dialog = new ProgressDialog(getActivity(), "正在获取数据");
        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();
        String uid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(uid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("command", "zhensuo");
        params.put("userid", uid);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.ModleUrl
                        + "club/yuyueApp.interface.php", params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        DLog.i(TAG, "我的诊所返回数据。。" + t.toString());
                        Gson gson = new Gson();
                        YMApplication.cinfo = gson.fromJson(t.toString(),
                                ClinicStatInfo.class);
                        initView();
                        dialog.closeProgersssDialog();
                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        DLog.i(TAG, "我的诊所错误日志" + strMsg);
                        super.onFailure(t, errorNo, strMsg);
                    }
                });

    }

    @OnClick({R.id.rel_que, R.id.re_orderaddnum, R.id.re_phone_doctor, R.id.re_fam_doctor})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rel_que:
                StatisticalTools.eventCount(getActivity(), "questionkt");
                if (YMApplication.getLoginInfo().getData().getXiaozhan() == null) {
                    return;
                }
                if ("1".equals(YMApplication.getLoginInfo().getData().getXiaozhan().getDati())) {
                    intent = new Intent(getActivity(),
                            QueSettingActivity.class);
                    startActivity(intent);
                } else if (YMApplication.getLoginInfo().getData().getXiaozhan().getDati().equals("5") || "-1".equals(YMApplication.getLoginInfo().getData().getXiaozhan().getDati())) {
                    intent = new Intent(getActivity(),
                            QueOpenActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.re_orderaddnum:

                StatisticalTools.eventCount(getActivity(), "ktregistration");
                // if (YMApplication.getLoginInfo().getData().getIsjob().equals("2"))
                // {
                if (YMApplication.getLoginInfo().getData().getXiaozhan() == null) {
                    return;
                }
                if (YMApplication.getLoginInfo().getData().getXiaozhan().getYuyue().equals(Constants.FUWU_AUDIT_STATUS_0)) {
                    CheckStateActivity.startActivity(getActivity(), "checking", "审核中");
                } else if (YMApplication.getLoginInfo().getData().getXiaozhan().getYuyue().equals(Constants.FUWU_AUDIT_STATUS_1)) {
                    intent = new Intent(getActivity(),
                            AddnumberSettingActivity.class);
                    startActivity(intent);
                } else if (Constants.FUWU_AUDIT_STATUS_NO.equals(YMApplication.getLoginInfo().getData().getXiaozhan().getYuyue())
                        || Constants.FUWU_AUDIT_STATUS_2.equals(YMApplication.getLoginInfo().getData().getXiaozhan().getYuyue())
                        || YMApplication.getLoginInfo().getData().getXiaozhan().getYuyue().equals(Constants.FUWU_AUDIT_STATUS_3)){
                    //其余的状态 stone
                    intent = new Intent(getActivity(),
                            AppointActivity.class);
                    startActivity(intent);
                }


                break;
            case R.id.re_fam_doctor:
                // 1 已开通 0 审核中 2审核不通过
                StatisticalTools.eventCount(getActivity(), "ktfamilydoctor");
                if (YMApplication.getLoginInfo().getData().getXiaozhan() == null) {
                    return;
                }
                if (YMApplication.getLoginInfo().getData().getXiaozhan().getFamily().equals("1")) {
                    intent = new Intent(getActivity(),
                            FamDoctorSetingActivity.class);
                    startActivity(intent);
                } else if (YMApplication.getLoginInfo().getData().getXiaozhan().getFamily().equals("0")) {
                    CheckStateActivity.startActivity(getActivity(), "checking", "审核中");
                } else {
                    intent = new Intent(getActivity(),
                            FamDoctorOpenActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.re_phone_doctor:
                StatisticalTools.eventCount(getActivity(), "ktCalldoctor");
                // if (YMApplication.getLoginInfo().getData().getIsjob().equals("2"))
                // {
                if (YMApplication.getLoginInfo().getData().getXiaozhan() == null) {
                    return;
                }
                if (YMApplication.getLoginInfo().getData().getXiaozhan().getPhone().equals(Constants.FUWU_AUDIT_STATUS_0)) {
                    CheckStateActivity.startActivity(getActivity(), "checking", "审核中");
                } else if (YMApplication.getLoginInfo().getData().getXiaozhan().getPhone().equals(Constants.FUWU_AUDIT_STATUS_1)) {
                    intent = new Intent(getActivity(),
                            PhoneDoctorSettingActivity.class);
                    startActivity(intent);
                } else if (Constants.FUWU_AUDIT_STATUS_NO.equals(YMApplication.getLoginInfo().getData().getXiaozhan().getPhone())
                        || Constants.FUWU_AUDIT_STATUS_2.equals(YMApplication.getLoginInfo().getData().getXiaozhan().getPhone())
                        || YMApplication.getLoginInfo().getData().getXiaozhan().getPhone().equals(Constants.FUWU_AUDIT_STATUS_3)) {
                    intent = new Intent(getActivity(),
                            PhoneDoctorOpenActiviy.class);
                    startActivity(intent);
                }

                // } else
                // {
                // Dialog(getActivity(),
                // getResources().getString(R.string.myclick_errinfo));
                //
                // }
                break;
            default:
                break;
        }
    }

//    public void Dialog(Context context, String str) {
//        LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
//                .inflate(R.layout.myclic_dialog, null);
//        final Dialog dialog = new AlertDialog.Builder(context).create();
//        dialog.show();
//        dialog.getWindow().setContentView(layout);
//        TextView content = (TextView) layout.findViewById(R.id.tv_content);
//        content.setText(str);
//        RelativeLayout re_ok = (RelativeLayout) layout.findViewById(R.id.rl_ok);
//        re_ok.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                dialog.dismiss();
//
//            }
//        });
//
//    }


    @Override
    protected int getLayoutResId() {
        return R.layout.myclinic;
    }

    @Override
    protected void initView() {
        if (YMApplication.getLoginInfo().getData().getXiaozhan() != null) {
            tv_fam_state.setText(YMApplication.fam_map().get(
                    YMApplication.getLoginInfo().getData().getXiaozhan().getFamily()));
            if (!YMApplication.isDoctorApprove()) {
                tv_phone_state.setText("未开通");
                tv_yuyue_state.setText("未开通");
            } else {
                tv_phone_state.setText(YMApplication.phone_map().get(
                        YMApplication.getLoginInfo().getData().getXiaozhan().getPhone()));
                tv_yuyue_state.setText(YMApplication.yuyue_map().get(
                        YMApplication.getLoginInfo().getData().getXiaozhan().getYuyue()));

            }

            tv_datil.setText(YMApplication.dati_map().get(
                    YMApplication.getLoginInfo().getData().getXiaozhan().getDati()));
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (YMApplication.getLoginInfo().getData().getXiaozhan() != null) {
            initView();
        } else {
            getData();
        }
    }

}
