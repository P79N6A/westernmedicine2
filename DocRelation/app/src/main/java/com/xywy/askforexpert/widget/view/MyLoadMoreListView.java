package com.xywy.askforexpert.widget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.base.view.CircleProgressBar;

/**
 * ListView 加载更多 stone
 *
 * @author 王鹏
 * @2015-8-10上午9:57:21
 */
public class MyLoadMoreListView extends ListView implements OnScrollListener {
    private static final String TAG = "MyLoadMoreListView";
    private View footer;

    private int totalItem;
    private int lastItem;

    private boolean isLoading;

    private OnLoadMore onLoadMore;

    private LayoutInflater inflater;

    private TextView tv_loading;

    private OnIsOnScrolling isScrolling;
    private CircleProgressBar pb_more;

    public MyLoadMoreListView(Context context) {
        super(context);
        init(context);
    }

    public MyLoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyLoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public OnIsOnScrolling getIsScrolling() {
        return isScrolling;
    }

    public void setIsScrolling(OnIsOnScrolling isScrolling) {
        this.isScrolling = isScrolling;
    }

    @SuppressLint("InflateParams")
    private void init(Context context) {
        inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.refresh_footerview, null, false);
        this.addFooterView(footer);
        tv_loading = (TextView) footer.findViewById(R.id.footerTitle);
        pb_more = (CircleProgressBar) footer.findViewById(R.id.pro);
        footer.setVisibility(View.GONE);
        // pb_more.setVisibility(View.GONE);
        this.setOnScrollListener(this);

//		this.setDivider(new ColorDrawable(R.color.my_line));
//		this.setDividerHeight(DensityUtils.dp2px(context, 1));
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        this.lastItem = firstVisibleItem + visibleItemCount;
        this.totalItem = totalItemCount;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (this.totalItem == lastItem && scrollState == SCROLL_STATE_IDLE) {
            DLog.i(TAG, "isLoading 。。。  yes");
            if (!isLoading) {
                tv_loading.setText("正在加载中...");
                pb_more.setVisibility(View.VISIBLE);
                isLoading = true;
                footer.setVisibility(View.VISIBLE);
                onLoadMore.loadMore();
            }
        }
//		else if (scrollState == SCROLL_STATE_FLING)
//		{
//			isScrolling.getIscroll(scrollState);
//		}
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

    public interface OnIsOnScrolling {
        void getIscroll(int scrollState);
    }

    public interface OnLoadMore {
        void loadMore();
    }

}