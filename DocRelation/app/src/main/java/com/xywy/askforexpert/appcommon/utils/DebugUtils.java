package com.xywy.askforexpert.appcommon.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.xywy.askforexpert.BuildConfig;

import java.lang.reflect.Field;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/23 16:56
 */
public class DebugUtils {
    public static int getViewId(View view) {
        if (view != null) {
            if (view.getId() != View.NO_ID)
                return view.getId();
            else
                return getViewId((ViewGroup) view.getParent());
        }
        return View.NO_ID;
    }

    /**
     * 调试时用于获取view id的字符串形式
     *
     * @param context
     * @return
     */
    public static String getViewStringId(Context context, View view) {
        if (!BuildConfig.DEBUG)
            return "";
        int viewId = getViewId(view);
        String packageName = context.getPackageName();
        try {
            Class[] classes = Class.forName(packageName + ".R").getClasses(); //返回类定义的公共的内部类,以及从父类、父接口那里继承来的内部类
            Class desireClass = null;
            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals("id")) {
                    desireClass = classes[i];
                    break;
                }
            }
            if (desireClass == null)
                return "";
            Field[] ids = desireClass.getDeclaredFields();
            for (Field id : ids) {
                Object value = id.get(null);
                if (value != null && value.equals(viewId)) {
                    return id.getName();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }
}

