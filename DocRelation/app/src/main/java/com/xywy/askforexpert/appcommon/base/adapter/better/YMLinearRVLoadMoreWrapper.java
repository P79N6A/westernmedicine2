package com.xywy.askforexpert.appcommon.base.adapter.better;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.uilibrary.recyclerview.wrapper.LoadMoreAndNoMoreDataCallback;
import com.xywy.uilibrary.recyclerview.wrapper.XYWYLinearRVLoadMoreWrapper;

/**
 * list 布局的 RecyclerView 加载更多 包装器
 * Created by bailiangjin on 2017/1/17.
 */
public class YMLinearRVLoadMoreWrapper<T> extends XYWYLinearRVLoadMoreWrapper<T> {


    public YMLinearRVLoadMoreWrapper(RecyclerView.Adapter adapter, RecyclerView recyclerView) {
        super(adapter, recyclerView);
    }

    @Override
    public void setDefaultLoadMoreView(Context context) {
        View loadMoreView = LayoutInflater.from(context).inflate(R.layout.loading_more, recyclerView, false);
        setLoadMoreView(loadMoreView, new LoadMoreAndNoMoreDataCallback() {

            @Override
            public void onShowLoadMore(View mLoadMoreView) {
                setLoadingState(mLoadMoreView, true);

            }

            @Override
            public void onShowLoadFailed(View mLoadMoreView) {

            }

            @Override
            public void onShowNoMoreData(View mLoadMoreView) {
                setLoadingState(mLoadMoreView, false);
            }

        });
    }

    private void setLoadingState(View mLoadMoreView, boolean isNoMoreData) {
        View loading_more = mLoadMoreView.findViewById(R.id.loading_more);
        View no_more_data = mLoadMoreView.findViewById(R.id.no_more_data);
        loading_more.setVisibility(isNoMoreData ? View.VISIBLE : View.GONE);
        no_more_data.setVisibility(isNoMoreData ? View.GONE : View.VISIBLE);
    }


}
