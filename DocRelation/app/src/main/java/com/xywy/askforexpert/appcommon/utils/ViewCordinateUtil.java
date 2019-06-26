package com.xywy.askforexpert.appcommon.utils;

import android.graphics.Rect;
import android.view.View;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/12/14 11:01
 */

public class ViewCordinateUtil {
    /**
     * 获取view在整个屏幕上的坐标
     * @param view
     * @return
     */
    public static int getGalobalY(View view) {
        final int[] loc = new int[2];
        view.getLocationOnScreen(loc);
        return loc[1];
    }
    public static int getGalobalX(View view) {
        final int[] loc = new int[2];
        view.getLocationOnScreen(loc);
        return loc[0];
    }

    /**
     * 获取view中心点在整个屏幕上的坐标
     * @param view
     * @return
     */
    public static int getCenterGalobalY(View view) {
        return getGalobalY(view)+view.getPaddingTop()+view.getHeight()/2;
    }
    public static int getCenterGalobalX(View view) {
       return getGalobalX(view)+view.getPaddingLeft()+view.getWidth()/2;
    }

    public static Rect getGlobalVisible(View view) {
        Rect r=new Rect();
        view.getGlobalVisibleRect(r);
        return r;
    }
}
