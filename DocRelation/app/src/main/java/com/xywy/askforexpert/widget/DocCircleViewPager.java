package com.xywy.askforexpert.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/6/29 9:36
 */

/**
 * ViewPager ，可设置禁止滑动
 */
public class DocCircleViewPager extends ViewPager {
    private static final String TAG = "DocCircleViewPager";

    private boolean isPageAllowScroll = true;
    private boolean isDialogShow = false;

    private float downX;
    private float downY;

    public DocCircleViewPager(Context context) {
        super(context);
    }

    public DocCircleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isPageAllowScroll() {
        return isPageAllowScroll;
    }

    public void setPageAllowScroll(boolean pageAllowScroll) {
        isPageAllowScroll = pageAllowScroll;
    }

    public boolean isDialogShow() {
        return isDialogShow;
    }

    public void setDialogShow(boolean dialogShow) {
        isDialogShow = dialogShow;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return this.isPageAllowScroll && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;

            case MotionEvent.ACTION_UP:
                float upX = ev.getX();
                float upY = ev.getY();
                float deltaX = upX - downX;
                float deltaY = upY - downY;
                if (deltaX < 0) {
                    deltaX = -deltaX;
                }
                if (deltaY < 0) {
                    deltaY = -deltaY;
                }
                if (deltaX > deltaY) { // 左右滑动
                    if (!isPageAllowScroll && isDialogShow) {
                        DialogUtil.LoginDialog(new YMOtherUtils(getContext()).context);
                    }
                }
                break;
        }

        return this.isPageAllowScroll && super.onInterceptTouchEvent(ev);
    }
}
