package com.xywy.askforexpert.widget.homeWidget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author Xavier-S
 * @date 2015.11.16 15:16
 */
public class ScrollableViewPager extends ViewPager {

    boolean mScrollable = true;

    public ScrollableViewPager(Context context) {
        super(context);
    }

    public ScrollableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isScrollable() {
        return mScrollable;
    }

    public void setScrollable(boolean scrollable) {
        this.mScrollable = scrollable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mScrollable) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

}
