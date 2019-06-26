package com.xywy.askforexpert.module.discovery.medicine.module.web.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by bobby on 16/6/20.
 */
public class XYWebview extends WebView  {

    private XYWebviewScrollCallback mScrollCallback;

    public XYWebview(Context context) {
        this(context, null);
    }

    public XYWebview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XYWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public XYWebview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                if (!hasFocus())
                    requestFocus();
                break;
        }

        return super.onTouchEvent(ev);
    }

    public void setXYWebviewScrollCallback(XYWebviewScrollCallback callback) {
        mScrollCallback = callback;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mScrollCallback != null) {
            mScrollCallback.onScroll(l, t, oldl, oldt, getScrollY());
        }
    }

    public static interface XYWebviewScrollCallback {
        public void onScroll(int l, int t, int oldl, int oldt, int scrollY);
    }
}
