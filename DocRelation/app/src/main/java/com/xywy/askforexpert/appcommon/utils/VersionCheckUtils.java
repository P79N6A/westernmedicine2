package com.xywy.askforexpert.appcommon.utils;

import android.os.Build;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/6/8 9:08
 */

/**
 * 检查Android系统版本
 */
public class VersionCheckUtils {

    /**
     * Android 4.0 API 14
     */
    public static boolean isIceCreamSandwich() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * Android 4.0.3 API 15
     */
    public static boolean isIceCreamSandWickMR1() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
    }

    /**
     * Android 4.1 API 16
     */
    public static boolean isJellyBean() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * Android 4.2 API 17
     */
    public static boolean isJellyBeanMR1() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    /**
     * Android 4.3 API 18
     */
    public static boolean isJellyBeanMR2() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    /**
     * Android 4.4 API 19
     */
    public static boolean isKitkat() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT;
    }

    /**
     * Android 5.0 API 21
     */
    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Android 5.0 API 22
     * Lollipop with an extra sugar coating on the outside!
     */
    public static boolean isLollipopMR1() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    /**
     * Android 6.0 API 23
     */
    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.M;
    }

    /**
     * Android 4.4-
     */
    public static boolean isBeforeKitkat() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT;
    }

    /**
     * Android 5.0-
     */
    public static boolean isBeforeLollipop() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Android 6.0+
     */
    public static boolean isAfterMarshmallow() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.M;
    }

    /**
     * Android M or higher
     */
    public static boolean isMarshmallowOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}
