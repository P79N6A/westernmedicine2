package com.xywy.askforexpert.module.discovery.medicine.module.web.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.base.view.CircleProgressBar;

/**
 * 统一的loading和错误页面
 */
public class CommonLoadingView extends FrameLayout {

    public enum LoadingState {
        LoadingStateSuccess,
        LoadingStateLoading,
        LoadingStateFailed,
        noData
    }

    protected LayoutInflater inflater;
    //用于点击事件的监听
    protected CommonLoadingListener listerner;

    protected CircleProgressBar mProgressBar;
    protected LinearLayout mRefresh;
    protected LoadingState mState = LoadingState.LoadingStateSuccess;
    protected LinearLayout mNoData;
    protected TextView mTvNoData;

    public CommonLoadingView(Context context) {
        super(context);
        init(null, 0);
    }

    public CommonLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CommonLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    protected void init(AttributeSet attrs, int defStyle) {
        initView();
        setLoadingState(mState);
    }

    protected void initView() {
        inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.view_common_loading_view, this, true);

        mProgressBar = (CircleProgressBar) findViewById(R.id.loading_progressbar);
        mNoData = (LinearLayout) findViewById(R.id.noData);
        mRefresh = (LinearLayout) findViewById(R.id.refresh_text);
        mRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listerner != null) {
                    listerner.onRefresh();
                }
            }
        });
    }

    public void setListerner(CommonLoadingListener listerner) {
        if (listerner != null) {
            this.listerner = listerner;
        }
    }

    public void setLoadingState(LoadingState state) {
        this.mState = state;
        switch (state) {
            case LoadingStateFailed:
                this.setVisibility(VISIBLE);
                mProgressBar.setVisibility(GONE);
                mRefresh.setVisibility(VISIBLE);
                break;
            case LoadingStateLoading:
                this.setVisibility(VISIBLE);
                mProgressBar.setVisibility(VISIBLE);
                mRefresh.setVisibility(GONE);
                break;
            case LoadingStateSuccess:
                this.setVisibility(GONE);
                break;
            case noData:
                this.setVisibility(VISIBLE);
                mProgressBar.setVisibility(GONE);
                mRefresh.setVisibility(GONE);
                mNoData.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
        if (getParent() != null) {
            if (getVisibility() == View.VISIBLE) {
                getParent().bringChildToFront(this);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //如果不可见，则拦截所touch事件不分发给子view处理。
        if (shouldIntercepet()) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        if (!shouldDispatchToParent())
            return true;
        return result;
    }

    protected boolean shouldIntercepet() {
        if (getVisibility() == View.VISIBLE) {
            return false;
        }
        return true;
    }

    protected boolean shouldDispatchToParent() {
        if (getVisibility() == View.VISIBLE) {
            return false;
        } else {
            return true;
        }
    }
}
