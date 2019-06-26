package com.xywy.askforexpert.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/1/8 13:46
 */
public class ActionItem {
    //定义图片对象
    public Drawable mDrawable;
    //定义文本对象
    public CharSequence mTitle;

    public ActionItem(Drawable drawable, CharSequence title) {
        this.mDrawable = drawable;
        this.mTitle = title;
    }

    public ActionItem(Context context, CharSequence title) {
        this.mTitle = title;
    }

    public ActionItem(Context context, int titleId, int drawableId) {
        this.mTitle = context.getResources().getText(titleId);
        this.mDrawable = context.getResources().getDrawable(drawableId);
    }

    public ActionItem(Context context, CharSequence title, int drawableId) {
        this.mTitle = title;
        this.mDrawable = context.getResources().getDrawable(drawableId);
    }
}
