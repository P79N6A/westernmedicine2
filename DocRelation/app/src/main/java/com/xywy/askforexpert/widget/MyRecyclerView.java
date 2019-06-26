package com.xywy.askforexpert.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

import com.xywy.askforexpert.widget.homeWidget.InnerScroller;
import com.xywy.askforexpert.widget.homeWidget.OuterScroller;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/6/24 16:16
 */
public class MyRecyclerView extends RecyclerView implements InnerScroller, AbsListView.OnScrollListener {

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void triggerOuterScroll() {

    }

    @Override
    public void recordScrollPosition(int firstVisibleItem) {

    }

    @Override
    public void syncScroll() {

    }

    @Override
    public void adjustEmptyHeaderHeight() {

    }

    @Override
    public int getInnerScrollY() {
        return 0;
    }

    @Override
    public OuterScroller getOuterScroller() {
        return null;
    }

    @Override
    public void register2Outer(OuterScroller mOuterScroller, int mIndex) {

    }

    @Override
    public View getReceiveView() {
        return null;
    }

    @Override
    public void scrollToTop() {

    }

    @Override
    public boolean isScrolling() {
        return false;
    }

    @Override
    public void scrollToInnerTop() {

    }

    @Override
    public void addHeaderView(View headerView) {

    }

    @Override
    public void onRefresh(boolean isRefreshing) {

    }

    @Override
    public void setCustomEmptyView(View emptyView) {

    }

    @Override
    public void setCustomEmptyViewHeight(int height, int offset) {

    }

    @Override
    public void setContentAutoCompletionColor(int color) {

    }
}
