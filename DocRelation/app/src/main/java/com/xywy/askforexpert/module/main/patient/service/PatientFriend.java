package com.xywy.askforexpert.module.main.patient.service;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.PatientListInfo;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * 解析患者管理数据
 *
 * @author 王鹏
 * @2015-5-30上午10:28:39
 */
public class PatientFriend {

    public static void getData(final Context context, String cat, final Handler hander, final int tag) {
        final ProgressDialog dialog = new ProgressDialog(context, "正在加载中...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did + cat;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "patientList");
        params.put("cat", cat);
        params.put("did", did);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        Message msg = new Message();
                        try {
                            PatientListInfo pListInfo = ResolveJson.R_Friend(t
                                    .toString());
                            msg.obj = pListInfo;
                            msg.what = tag;
                            hander.sendMessage(msg);
                            super.onSuccess(t);
                        }catch (Exception e){
//                            ToastUtils.shortToast(e.getMessage());
                            ((Activity)context).finish();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        Message msg = new Message();
                        msg.what = 500;
                        hander.sendMessage(msg);
                        super.onFailure(t, errorNo, strMsg);
                    }
                });

    }

}
