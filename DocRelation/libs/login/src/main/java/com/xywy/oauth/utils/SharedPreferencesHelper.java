package com.xywy.oauth.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.xywy.oauth.LoginManager;


public class SharedPreferencesHelper {

    private final static String TAG = "SharedPreferencesHelperChat";
    private static SharedPreferencesHelper inst;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    private SharedPreferencesHelper(Context context) {
        if (context == null) {
            return;
        }
        prefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static SharedPreferencesHelper getIns() {
        if (inst == null) {
            inst = new SharedPreferencesHelper(LoginManager.getContext());
        }
        return inst;
    }


    public boolean putString(String key, String value) {
        editor.putString(key, value);
        return editor.commit();
    }


    public String getString(String key) {
        return prefs.getString(key, null);
    }


    public boolean putInt(String key, int value) {
        editor.putInt(key, value);
        return editor.commit();
    }

    public boolean putFloat(String key, float value) {
        editor.putFloat(key, value);
        return editor.commit();
    }

    public float getFloat(String key, float defaultValue) {
        return prefs.getFloat(key, defaultValue);

    }


    public boolean putDouble(String key, double value) {
        editor.putLong(key, Double.doubleToRawLongBits(value));
        return editor.commit();
    }


    public double getDouble(final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }


    public int getInt(String key) {
        return prefs.getInt(key, -1);
    }


    public boolean putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        return editor.commit();
    }


    public boolean getBoolean(String key) {
        return prefs.getBoolean(key, true);
    }

    /**
     * 清空数据
     *
     * @return
     */
    public boolean clear() {
        editor.clear();
        return editor.commit();
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