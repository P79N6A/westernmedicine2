package com.xywy.askforexpert.appcommon.base.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * 项目名称：D_Platform
 * 类名称：RecyclerOnScrollListener
 * 类描述：刷新滑动到底部加载更多
 * 创建人：shihao
 * 创建时间：2015-8-7 上午10:23:47
 * 修改备注：
 */
@Deprecated
public abstract class RecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = RecyclerOnScrollListener.class.getSimpleName();
    //用来标记是否正在向最后一个滑动，既是否向右滑动或向下滑动
    boolean isSlidingToLast = false;
    private LinearLayoutManager manager;

    public RecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.manager = linearLayoutManager;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        // 当不滚动时
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            //获取最后一个完全显示的ItemPosition
            int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
            int totalItemCount = manager.getItemCount();

            // 判断是否滚动到底部，并且是向右滚动
            if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                //加载更多功能的代码
                onLoadMore();
            }
        } else {
            onScrolling();
        }

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
        //大于0表示，正在向右滚动
//小于等于0 表示停止或向左滚动
        isSlidingToLast = dx > 0 || dy > 0;

    }


    public abstract void onLoadMore();//要在实现类中实现

    public abstract void onScrolling();
}

