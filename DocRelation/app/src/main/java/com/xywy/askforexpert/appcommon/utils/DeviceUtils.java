package com.xywy.askforexpert.appcommon.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.xywy.askforexpert.YMApplication;

/**
 * 设备相关工具类
 *
 * @author bailiangjin
 */
public class DeviceUtils {

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getDevicModel() {
        return Build.MODEL;
    }

    public static String getDevicBrand() {
        return Build.BRAND;
    }


    /**
     * 获取Imei
     *
     * @return
     */
    public static String getIMei() {

        return getIMei(YMApplication.getAppContext());
    }

    /**
     * 获取Imei
     *
     * @param context
     * @return
     */
    private static String getIMei(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            return imei;
        } catch (Exception e) {
            return "0000000000";
        }
    }

    /**
     * 获取手机系统版本
     *
     * @return
     */
    public static String getMobileOSVersion() {
        try {
            String release = Build.VERSION.RELEASE; // android系统版本号
            return release;
        } catch (Exception e) {
            return "未知";
        }
    }

    /**
     * 获取当前手机系统版本int值
     *
     * @return
     */
    public static int getCurrentApiVersion() {
        try {
            int currentApiVersion = Build.VERSION.SDK_INT;

            return currentApiVersion;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 判断是否为4.4版本
     * @return
     */
    public static boolean isKitKat(){
        return getCurrentApiVersion()== Build.VERSION_CODES.KITKAT;
    }

    /**
     * 判断是否为4.4版本
     * @return
     */
    public static boolean isApiVersion(int versionCode){
        return getCurrentApiVersion()== versionCode;
    }

    /**
     * 判断是否为4.4以上版本
     * @return
     */
    public static boolean isitKatOrUperVersion(){
        return isVersionOrAbove(Build.VERSION_CODES.KITKAT);
    }

    /**
     * 判断是否为某个版本或以上版本
     * @return
     */
    public static boolean isVersionOrAbove(int apiVersion){
        int curVersion = getCurrentApiVersion();
        return -1!=curVersion&&curVersion>=apiVersion;
    }

    /**
     * 判断是否为某个版本或以下版本
     * @return
     */
    public static boolean isVersionOrBelow(int apiVersion){
        int curVersion = getCurrentApiVersion();
        return -1!=curVersion&&curVersion<=apiVersion;
    }




}
