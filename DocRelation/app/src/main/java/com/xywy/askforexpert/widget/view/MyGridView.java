package com.xywy.askforexpert.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自动义GridView 用于活动详情 和精品筛选 scrovview 和GridView 避免冲突
 *
 * @author Administrator
 */
public class MyGridView extends GridView {
    public MyGridView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }

}
