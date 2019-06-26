package com.xywy.askforexpert.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/5/10 9:09
 */
public class NoScrollTextView extends TextView {
    public NoScrollTextView(Context context) {
        super(context);
    }

    public NoScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void scrollTo(int x, int y) {
//        super.scrollTo(x, y);
    }
}
