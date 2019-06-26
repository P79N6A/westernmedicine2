package com.xywy.askforexpert.appcommon.utils.update;

import android.content.Context;
import android.text.TextUtils;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.appcommon.utils.FileUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;


/**
 * Created by bailiangjin on 16/9/19.
 */
public class UpdateAppUtils {

   private   static final String APP_UPDATE_PKG_URL_PREFIX="http://appdl.xywy.com";

    /**
     * checkApk
     *
     * @param context
     * @param path
     */
    public static boolean checkApk(Context context, final String path, String md5) {

        //// TODO: 2018/3/28 测试stone 测试不校验这个
        if (!SafetyUtils.checkFile(path, md5)) {
            return false;
        }

        if (!SafetyUtils.checkPkgName(context, path)) {
            ToastUtils.shortToast("升级包被恶意软件篡改 请重新升级下载安装");
            FileUtils.deleteFile(path);
            return false;
        }

        switch (SafetyUtils.checkPkgSign(path)) {

            case SafetyUtils.SUCCESS:
                return true;
            case SafetyUtils.SIGNATURES_INVALIDATE:
                ToastUtils.shortToast("升级包安全校验失败 请重新升级");
                return false;
            case SafetyUtils.VERIFY_SIGNATURES_FAIL:
                ToastUtils.shortToast("升级包为盗版应用 请重新升级");
                return false;
            default:
                return false;
        }

    }


    /**
     * 验证Apk下载链接是否安全
     *
     * @param apkDownLoadUrl
     * @return
     */
    public static boolean isApkDownloadUrlSafe(String apkDownLoadUrl) {
        return !TextUtils.isEmpty(apkDownLoadUrl) && (apkDownLoadUrl.startsWith(BuildConfig.APP_BASE_URL_YM) || apkDownLoadUrl.startsWith(APP_UPDATE_PKG_URL_PREFIX));
    }
}
