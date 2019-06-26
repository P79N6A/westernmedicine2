package com.xywy.askforexpert.widget.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.base.view.CircleProgressBar;


/**
 * ListView 加载更多
 *
 * @author 王鹏
 * @2015-8-10上午9:57:21
 */
public class MyLoadMoreExpandListView extends ExpandableListView implements OnScrollListener {
    private View footer;

    private int totalItem;
    private int lastItem;

    private boolean isLoading;

    private OnLoadMore onLoadMore;

    private LayoutInflater inflater;

    private TextView tv_loading;
    private CircleProgressBar pb_more;

    public MyLoadMoreExpandListView(Context context) {
        super(context);
        init(context);
    }

    public MyLoadMoreExpandListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyLoadMoreExpandListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @SuppressLint("InflateParams")
    private void init(Context context) {
        inflater = LayoutInflater.from(context);
//		footer = inflater.inflate(R.layout.loadingmore,null ,false);
//		this.addFooterView(footer);
//		tv_loading = (TextView)footer.findViewById(R.id.tv_more);
//		pb_more = (ProgressBar) footer.findViewById(R.id.pb_more);
//		footer.setVisibility(View.GONE);

        inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.refresh_footerview, null, false);
        this.addFooterView(footer);
        tv_loading = (TextView) footer.findViewById(R.id.footerTitle);
        pb_more = (CircleProgressBar) footer.findViewById(R.id.pro);
        footer.setVisibility(View.GONE);
//		pb_more.setVisibility(View.GONE);
        this.setOnScrollListener(this);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.lastItem = firstVisibleItem + visibleItemCount;
        this.totalItem = totalItemCount;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (this.totalItem == lastItem && scrollState == SCROLL_STATE_IDLE) {
            if (!isLoading) {
                tv_loading.setText("正在加载中...");
                pb_more.setVisibility(View.VISIBLE);
                isLoading = true;
                footer.setVisibility(View.VISIBLE);
                onLoadMore.loadMore();
            }
        }
    }

    public void setLoadMoreListen(OnLoadMore onLoadMore) {
        this.onLoadMore = onLoadMore;
    }

    /**
     * 加载完成调用此方法
     */
    public void onLoadComplete() {
        footer.setVisibility(View.GONE);
        isLoading = false;

    }

    /**
     * 关闭 footview
     */
    public void noMoreLayout() {
        this.removeFooterView(footer);
    }

    /**  */
    public void LoadingMoreText(String str) {
        footer.setVisibility(View.VISIBLE);
        pb_more.setVisibility(View.GONE);
        tv_loading.setText(str);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public interface OnLoadMore {
        void loadMore();
    }

}