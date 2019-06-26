package com.xywy.askforexpert.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Compiler: Android Studio
 * Project: D_Platform
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/12/31 17:26
 */
public class DiscussScrollView extends ScrollView {
    private OnScrollListener onScrollLister;

    public DiscussScrollView(Context context) {
        super(context);
    }

    public DiscussScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DiscussScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnScrollLister(OnScrollListener onScrollLister) {
        this.onScrollLister = onScrollLister;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (onScrollLister != null) {
            onScrollLister.onScroll(l, t, oldl, oldt);
        }
    }

    public interface OnScrollListener {
        void onScroll(int l, int t, int oldl, int oldt);
    }
}
