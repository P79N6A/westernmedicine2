package com.xywy.askforexpert.module.my.userinfo.service;

import android.os.Handler;
import android.os.Message;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 修改 个人信息
 *
 * @author 王鹏
 * @2015-5-14下午6:58:38
 */
public class UpdatePersonInfo {

    public static void updateHospital(String province, String city,String hospitalKey, String hospitalValue,final int msg_waht,
                                      final Handler handler) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("province", province);
        paramMap.put("city", city);
        paramMap.put(hospitalKey, hospitalValue);
        update(paramMap, msg_waht, handler);
    }

    public static void update(String key, String value, final int msg_waht,
                              final Handler handler) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(key, value);
        update(paramMap, msg_waht, handler);

    }

    public static void update(Map<String, String> paramMap, final int msg_waht,
                              final Handler handler) {
        if (NetworkUtil.isNetWorkConnected()) {

            AjaxParams params = new AjaxParams();
            String status = "edit";
            String userid = YMApplication.getLoginInfo().getData().getPid();
            String sign = MD5Util.MD5(userid + status + Constants.MD5_KEY);
            params.put("status", status);
            params.put("command", "info");
            params.put("userid", userid);
            params.put("sign", sign);
            Iterator iterator = paramMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                params.put(key, value);
            }
            FinalHttp fh = new FinalHttp();
            fh.post(CommonUrl.DP_COMMON, params, new AjaxCallBack() {
                @Override
                public void onSuccess(String t) {
                    DLog.d("info_follow", t.toString());
                    Message meg = new Message();
                    Map<String, String> map = ResolveJson.R_Action(t
                            .toString());
                    meg.obj = map;
                    meg.what = msg_waht;
                    handler.sendMessage(meg);
                    super.onSuccess(t);
                }

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    ToastUtils.shortToast(
                            "网络繁忙，请稍后重试");
                    super.onFailure(t, errorNo, strMsg);
                }
            });
        } else {
            ToastUtils.shortToast("网络连接失败");

        }

    }

}
