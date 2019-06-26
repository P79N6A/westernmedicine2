package com.xywy.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SharedPreferencesHelper {

    private SharedPreferences prefs;
    private final static String DEFAULT = "default";

    private static HashMap<String,SharedPreferencesHelper>  map=new HashMap<>();

    private SharedPreferencesHelper(Context context, String fileName) {
        if (context == null) {
            return;
        }
        prefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     * 使用用户共享的单例sp文件，注销删除
     *
     * @return
     */
    public static SharedPreferencesHelper getUser(Context context,String userId) {
        return getInstance(context,MD5Util.MD5("user"+userId));
    }
    public static SharedPreferencesHelper getSystem(Context context) {
        return getInstance(context,"system");
    }
    /**
     * 创建新的sp文件
     *
     * @return
     */
    private static SharedPreferencesHelper getInstance(Context context, String fileName) {
        if (map.get(fileName) == null) {
            SharedPreferencesHelper  sp = new SharedPreferencesHelper(context,fileName);
            map.put(fileName,sp);
        }
        return map.get(fileName);
    }
    public static SharedPreferencesHelper getIns(Context context) {
        return getInstance(context, DEFAULT);
    }


    public boolean putString(String key, String value) {
         prefs.edit().putString(key, value).apply();
        return true;
    }


    public String getString(String key) {
        return getString(key, null);
    }
    public String getString(String key, String value) {
        return prefs.getString(key, value);
    }

    public boolean putInt(String key, int value) {
         prefs.edit().putInt(key, value).apply();
        return true;
    }

    public boolean putFloat(String key, float value) {
        prefs.edit().putFloat(key, value).apply();
        return true;
    }

    public float getFloat(String key, float defaultValue) {
        return prefs.getFloat(key, defaultValue);

    }


    public boolean putDouble(String key, double value) {
        prefs.edit().putLong(key, Double.doubleToRawLongBits(value)).apply();
        return true;
    }


    public double getDouble(final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }


    public int getInt(String key) {
        return prefs.getInt(key, -1);
    }


    public boolean putBoolean(String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
        return true;
    }


    public boolean getBoolean(String key) {
        return getBoolean(key, true);
    }
    public boolean getBoolean(String key,boolean def) {
        return prefs.getBoolean(key, def);
    }
    /**
     * 清空数据
     *
     * @return
     */
    public boolean clear() {
         prefs.edit().clear().apply();
        return true;
    }

    /**
     * 关闭当前对象
     *
     * @return
     */
    public void close() {
        prefs = null;
    }


}