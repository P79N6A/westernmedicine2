package com.xywy.askforexpert.appcommon.net.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 网络判断工具类
 *
 * @author 王鹏
 */
public class NetworkUtil {

    private static String macAddress;

    private NetworkUtil() {
        throw new UnsupportedOperationException(
                "NetworkUtil cannot be instantiated");
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetWorkConnected() {
     return isConnect();
    }

    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }


    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName componentName = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(componentName);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 使用SSL不信任的证书
     */
    public static void useUntrustedCertificate() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }
        }};
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static Context getContext(){
        return AppUtils.getAppContext();
    }

    public static boolean isConnect() {
        return isConnect(AppUtils.getAppContext());
    }

    /**
     * 当前手机是否有可用网络 (所有网络类型)
     *
     * @param context
     * @return
     */
    private static boolean isConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {

                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {

                        if (info.isAvailable()) {

                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e("error" + e.toString());
        }
        return false;
    }

    public static boolean isWifiConnect() {
        return isWifiConnect(AppUtils.getAppContext());
    }

    /**
     * 当前 环境是否wifi已连接
     *
     * @param context
     * @return
     */
    private static boolean isWifiConnect(Context context) {
        // 获取手机所有连接管理对象
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
                // 如果当前手机没有任何网络连接时,networkInfo == null
                if (null != networkInfo) {
                    return "wifi".equalsIgnoreCase(networkInfo.getTypeName());
                }
            }
        } catch (Exception e) {
            LogUtils.e("error" + e.toString());
        }
        return false;
    }

    /**
     * 获取 wifi 信号强度
     *
     * @return
     */
    public static int getWifiRssi() {
        WifiManager mWifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        int wifi = wifiInfo.getRssi(); // 取 wifi 信号强度
        return wifi;
    }

    public static String int2ip(long ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    // 在wifi未开启状态下，仍然可以获取MAC地址，但是IP地址必须在已连接状态下否则为0
    public static String getMacAddress() {
        if (macAddress.equals("")) {
            String macAddress = null, ip = null;
            WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiManager ? null : wifiManager.getConnectionInfo());
            if (null != info) {
                macAddress = info.getMacAddress();
                ip = int2ip(info.getIpAddress());
            }
            NetworkUtil.macAddress = macAddress;
            return macAddress;
        } else {
            return macAddress;
        }
    }

}