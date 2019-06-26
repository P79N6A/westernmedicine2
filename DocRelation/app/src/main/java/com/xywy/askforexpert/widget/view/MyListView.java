package com.xywy.askforexpert.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自动义listview 用于精品筛选   scrovview 和listview 避免冲突
 *
 * @author Administrator
 */
public class MyListView extends ListView {

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }


}
