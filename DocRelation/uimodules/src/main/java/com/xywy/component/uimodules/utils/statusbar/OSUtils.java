package com.xywy.component.uimodules.utils.statusbar;

import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by stone on 2018/4/27.
 */

public class OSUtils {
    private static String manufacturer = Build.MANUFACTURER;

    public static boolean isMIUI() {
        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是魅族操作系统
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/18,9:43
     * <h3>UpdateTime</h3> 2016/6/18,9:43
     * <h3>CreateAuthor</h3> vera
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @return true 为魅族系统 否则不是
     */
    public static boolean isMeizuFlymeOS() {
        /* 获取魅族系统操作版本标识*/
        String meizuFlymeOSFlag = getSystemProperty("ro.build.display.id", "");
        if (TextUtils.isEmpty(meizuFlymeOSFlag)) {
            return false;
        } else if (meizuFlymeOSFlag.contains("flyme") || meizuFlymeOSFlag.toLowerCase().contains("flyme")) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取系统属性
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/18,9:35
     * <h3>UpdateTime</h3> 2016/6/18,9:35
     * <h3>CreateAuthor</h3> vera
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param key          ro.build.display.id
     * @param defaultValue 默认值
     * @return 系统操作版本标识
     */
    private static String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String) get.invoke(clz, key, defaultValue);
        } catch (ClassNotFoundException e) {
//            L.d("SystemUtil=================>" + e.getMessage());
            return null;
        } catch (NoSuchMethodException e) {
//            L.d("SystemUtil=================>" + e.getMessage());
            return null;
        } catch (IllegalAccessException e) {
//            L.d("SystemUtil=================>" + e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
//            L.d("SystemUtil=================>" + e.getMessage());
            return null;
        } catch (InvocationTargetException e) {
//            L.d("SystemUtil=================>" + e.getMessage());
            return null;
        }
    }

}
