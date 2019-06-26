package com.xywy.askforexpert.appcommon.utils.update;

/**
 * Created by bailiangjin on 16/9/19.
 */

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * 安全校验
 * Created by LIUYONGKUI on 2016-04-21.
 */
public class SafetyUtils {

    /**
     * install sucess
     */
    protected static final int SUCCESS = 0;
    /**
     * SIGNATURES_INVALIDATE
     */
    protected static final int SIGNATURES_INVALIDATE = 3;
    /**
     * SIGNATURES_NOT_SAME
     */
    protected static final int VERIFY_SIGNATURES_FAIL = 4;
    /**
     * is needcheck
     */
    private static final boolean NEED_VERIFY_CERT = true;

    /**
     * checkPkgSign.
     */
    public static boolean isMD5Right(String filePath, String md5FromServer) {
        LogUtils.d("filePath:" + filePath);

        if (TextUtils.isEmpty(md5FromServer)) {
            LogUtils.e("服务端md5文件值为空或文件路径为空");
            return false;
        }
        if (TextUtils.isEmpty(filePath)) {

            LogUtils.e("服务端md5文件值为空或文件路径为空");
            return false;
        }

        File file = new File(filePath);

        if (!file.exists()) {
            LogUtils.e("升级apk文件不存在filePath:" + filePath);
            return false;
        }
        String apkFileMd5 = getFileMD5(file);

        LogUtils.e("updateApkMd5:" + apkFileMd5);

        return md5FromServer.equals(apkFileMd5);
    }

    /**
     * checkPkgSign.
     */
    public static int checkPkgSign(String srcPluginFile) {

        Context context = YMApplication.getAppContext();

//        PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(srcPluginFile, 0);
//        //Signature[] pluginSignatures = PackageInfo.signatures;
        Signature[] pluginSignatures = PackageVerifyer.collectCertificates(srcPluginFile, false);
        boolean isDebugable = (0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        if (pluginSignatures == null) {
            LogUtils.e("签名验证失败+" + srcPluginFile);
            new File(srcPluginFile).delete();
            return SIGNATURES_INVALIDATE;
        } else if (NEED_VERIFY_CERT && !isDebugable) {
            //可选步骤，验证APK证书是否和现在程序证书相同。
            Signature[] mainSignatures = null;
            try {
                PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), PackageManager.GET_SIGNATURES);
                mainSignatures = pkgInfo.signatures;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (!PackageVerifyer.isSignaturesSame(mainSignatures, pluginSignatures)) {
                LogUtils.e("升级包证书和旧版本证书不一致:" + srcPluginFile);
                new File(srcPluginFile).delete();
                return VERIFY_SIGNATURES_FAIL;
            }
        }
        return SUCCESS;
    }

    /**
     * checkPkgName
     *
     * @param context
     * @param srcNewFile
     * @return
     */
    public static boolean checkPkgName(Context context, String srcNewFile) {
        PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(srcNewFile, PackageManager.GET_ACTIVITIES);

        if (packageInfo != null) {

            return TextUtils.equals(context.getPackageName(), packageInfo.packageName);
        }

        return false;
    }

    /**
     * checkFile
     *
     * @param aPath 文件路径
     */
    public static boolean checkFile(String aPath, String md5) {
        File aFile = new File(aPath);
        if (aFile == null || !aFile.exists()) {
            ToastUtils.shortToast("安装包已被恶意软件删除");
            return false;
        }

        // TODO: 2017/1/20 服务端未传 md5 值 年后添加

        if (!isMD5Right(aPath, md5)) {
            ToastUtils.shortToast("安装包MD5异常");
            return false;
        }

        return true;
    }


    // 用md5对文件进行提取特征码，前几年比较流行的杀毒软件的原理就是用到了这个
    public static String getFileMD5(File file) {
        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            StringBuffer buffer = new StringBuffer();
            FileInputStream fis =  new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            while((len=fis.read(bytes))!=-1){
                //用数组对信息摘要器进行更新
                digest.update(bytes, 0, len);
            }
            byte[] result = digest.digest();
            for (byte b : result) {
                int number = b & 0xff;
                String str = Integer.toHexString(number);
                //如果是单位数，那么前面补零
                if (str.length() == 1) {
                    // 在前面加上一个0
                    buffer.append("0");
                }
                buffer.append(str);
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}