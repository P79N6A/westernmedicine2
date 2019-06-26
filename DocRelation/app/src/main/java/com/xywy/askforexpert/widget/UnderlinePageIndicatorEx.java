package com.xywy.askforexpert.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.xywy.askforexpert.R;

/**
 * Created by shihao on 16/4/25.
 */
public class UnderlinePageIndicatorEx extends UnderlinePageIndicator {
    public UnderlinePageIndicatorEx(Context context) {
        super(context, null);

    }

    public UnderlinePageIndicatorEx(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.vpiUnderlinePageIndicatorStyle);

    }

    public UnderlinePageIndicatorEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void setViewPager(ViewPager viewPager) {
        if (mViewPager == viewPager) {
            return;
        }

        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = viewPager;
        invalidate();
        post(new Runnable() {
            @Override
            public void run() {
                if (mFades) {
                    post(mFadeRunnable);
                }
            }
        });
    }
}
