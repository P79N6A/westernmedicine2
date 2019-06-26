package com.xywy.askforexpert.appcommon.utils;

import android.content.Context;

import com.xywy.askforexpert.YMApplication;

/**
 * Context(上下文)工具类
 * Author:  liangjin.bai
 * Email: bailiangjin@gmail.com
 * Create Time: 2015/9/28 15:01
 */
public class CtxtUtils {

    /**
     * 全局 根据id获取string 字段方法
     *
     * @param resId
     * @return
     */
    public static String getString(int resId) {
        Context context = YMApplication.getAppContext();
        return context.getString(resId);
    }
}