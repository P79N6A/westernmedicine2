package com.xywy.askforexpert.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 类描述:
 * 创建人: shihao on 16/1/5 15:20.
 */

public class OtherGridView extends GridView {

    public OtherGridView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;
        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            // The two leftmost bits in the height measure spec have
            // a special meaning, hence we can't use them to describe height.
            heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        } else {
            // Any other height should be respected as is.
            heightSpec = heightMeasureSpec;
        }
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}

