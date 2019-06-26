package com.xywy.livevideo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xywy.livevideolib.R;
import com.xywy.uilibrary.recyclerview.wrapper.LoadMoreAndNoMoreDataCallback;
import com.xywy.uilibrary.recyclerview.wrapper.XYWYLinearRVLoadMoreWrapper;

/**
 * Created by zhangzheng on 2017/3/10.
 */
public class VideoListLoadMoreAdapter extends XYWYLinearRVLoadMoreWrapper {

    private View mLoadMoreView;

    public VideoListLoadMoreAdapter(RecyclerView.Adapter adapter, RecyclerView recyclerView) {
        super(adapter, recyclerView);

    }


    @Override
    protected void setDefaultLoadMoreView(Context context) {
        mLoadMoreView = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.live_loadmore_layout, recyclerView, false);
        setLoadMoreView(mLoadMoreView, new LoadMoreAndNoMoreDataCallback() {
            @Override
            public void onShowLoadMore(View mLoadMoreView) {
                if (mLoadMoreView == null) {
                    return;
                } else {
                    setLoadingState(mLoadMoreView, true);
                }
            }

            @Override
            public void onShowLoadFailed(View mLoadMoreView) {

            }

            @Override
            public void onShowNoMoreData(View mLoadMoreView) {
                if (mLoadMoreView == null) {
                    return;
                } else {
                    setLoadingState(mLoadMoreView, false);
                }
            }
        });
    }

    public void setLoadingState(boolean isLoading) {
        setLoadingState(mLoadMoreView, isLoading);
    }

    private void setLoadingState(View loadingView, boolean isLoading) {
        TextView tvLoadingMore = (TextView) loadingView.findViewById(R.id.tv_loading_more);
        ProgressBar progressBar = (ProgressBar) loadingView.findViewById(R.id.pb_loading_more);
        if (tvLoadingMore != null) {
            tvLoadingMore.setText(isLoading ? "正在加载..." : "没有更多了");
        }
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }


}
