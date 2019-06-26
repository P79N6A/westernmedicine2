package com.xywy.askforexpert.module.my.account.utils;

import android.app.Activity;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.uilibrary.dialog.pndialog.XywyPNDialog;
import com.xywy.uilibrary.dialog.pndialog.listener.PositiveDialogListener;

/**
 * 强制退出提示框工具类
 * Created by bailiangjin on 2017/1/16.
 */

public class ForceLogoutDialogUtils {

    public static void showForceDialogWithLog(int resultCode,String resultMsg){
        LogUtils.e("强制退出code:"+resultCode+"  msg:"+resultMsg);
        showForceLogoutDialog(resultMsg);
    }

    private static void showForceLogoutDialog(String notice){
        Activity activity = YMApplication.getInstance().getTopActivity();
        new XywyPNDialog.Builder()
                .setCancelable(false)
                .setContent(notice)
                .setNoNegativeBtn(true)
                .setPositiveStr("确认")
                .create(activity, new PositiveDialogListener() {
                    @Override
                    public void onPositive() {
                        YMUserService.ymLogout();
                    }
                }).show();

    }
}
