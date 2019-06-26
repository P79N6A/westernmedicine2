package com.xywy.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by yuwentao on 16/3/16.
 */
public class CommonUtils {

    public static boolean checkGpsOpen(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 打开gps设置页面
     */
    public static void openGpsSetting(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);

        } catch (ActivityNotFoundException ex) {

            // The Android SDK doc says that the location settings activity
            // may not be found. In that case show the general settings.

            // General settings activity
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                context.startActivity(intent);
            } catch (Exception e) {
            }
        }
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 判断是否在主线程
     *
     * @return
     */
    public static boolean isInMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    /**
     * Assets 资源文件转换成 string
     * Assets里可放josn数据
     *
     * @param fileName
     * @param context
     * @return
     */
    public static String getFromAssets(String fileName, Context context) {
        StringBuffer json = new StringBuffer();
        //.getClass().getClassLoader().getResourceAsStream("assets/" + "query.json");
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (is == null) {
            Log.e("fileName err", "fileName 路径有问题");
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                // sb.append(line + "/n");
                sb.append(line);
            }
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
