package com.xywy.askforexpert.module.discovery.medicine.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.uilibrary.recyclerview.wrapper.LoadMoreAndNoMoreDataCallback;
import com.xywy.uilibrary.recyclerview.wrapper.XYWYLinearRVLoadMoreWrapper;

/**
 * Created by DongJr on 2017/2/15.
 */

public class XYWYLoadMoreWrapper<T> extends XYWYLinearRVLoadMoreWrapper<T> {

    private View mLoadMoreView;

    public XYWYLoadMoreWrapper(RecyclerView.Adapter adapter, RecyclerView recyclerView) {
        super(adapter, recyclerView);
    }

    @Override
    protected void setDefaultLoadMoreView(Context context) {
        mLoadMoreView = LayoutInflater.from(context).inflate(R.layout.loading_more, recyclerView, false);

        setLoadMoreView(mLoadMoreView, new LoadMoreAndNoMoreDataCallback() {

            @Override
            public void onShowLoadMore(View mLoadMoreView) {
                setLoadingState(mLoadMoreView, false);

            }

            @Override
            public void onShowLoadFailed(View mLoadMoreView) {

            }

            @Override
            public void onShowNoMoreData(View mLoadMoreView) {
                setLoadingState(mLoadMoreView, true);
            }

        });
    }

    public void setLoadingState(boolean isNoMoreData) {
        setLoadingState(mLoadMoreView, isNoMoreData);
    }

    private void setLoadingState(View mLoadMoreView, boolean isNoMoreData) {
        View loadingmore = mLoadMoreView.findViewById(R.id.loading_more);
        View noMore = mLoadMoreView.findViewById(R.id.no_more_data);

        if(isNoMoreData) {
            loadingmore.setVisibility(View.GONE);
            noMore.setVisibility(View.VISIBLE);
        } else {
            loadingmore.setVisibility(View.VISIBLE);
            noMore.setVisibility(View.GONE);
        }
    }


}
