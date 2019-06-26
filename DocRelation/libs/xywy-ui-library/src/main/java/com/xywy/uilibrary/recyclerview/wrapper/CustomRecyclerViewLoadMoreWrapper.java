package com.xywy.uilibrary.recyclerview.wrapper;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.xywy.uilibrary.R;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

/**
 * 自定义的LoadMoreWrapper 目前用于即时问答模块中的问题列表:咨询中/问题库/历史回复
 * stone
 * 2017/12/22 下午4:38
 */
public class CustomRecyclerViewLoadMoreWrapper<T> extends LoadMoreWrapper<T> {

    private int status = LoadingMoreViewStatus.SHOWLOADING.getFlag();//默认加载中
    private RecyclerView recyclerView;
    protected UpdateLoadMoreStatusCallback mUpdateLoadMoreStatusCallback;

    private View loading_more;
    private View no_more_data;
    private View loadMoreViewRoot;

    public CustomRecyclerViewLoadMoreWrapper(RecyclerView.Adapter adapter, RecyclerView recyclerView) {
        super(adapter);
        if (null == recyclerView || !(recyclerView.getLayoutManager() instanceof LinearLayoutManager)) {
            throw new RuntimeException("recyclerView is null or the layoutManager of recyclerview is not instance of LinearLayoutManager");
        }
        this.recyclerView = recyclerView;
        setDefaultLoadMoreView(recyclerView.getContext());
    }

    protected void setDefaultLoadMoreView(Context context) {
        loadMoreViewRoot = LayoutInflater.from(context).inflate(R.layout.loading_more, recyclerView, false);
        loading_more = loadMoreViewRoot.findViewById(R.id.loading_more);
        no_more_data = loadMoreViewRoot.findViewById(R.id.no_more_data);
        mUpdateLoadMoreStatusCallback = new UpdateLoadMoreStatusCallback() {

            @Override
            public void onShowLoadMore() {
                setLoadingState(LoadingMoreViewStatus.SHOWLOADING.getFlag());
            }

            @Override
            public void onShowLoadFailed() {
                setLoadingState(LoadingMoreViewStatus.HIDE.getFlag());
            }

            @Override
            public void onShowNoMoreData() {
                setLoadingState(LoadingMoreViewStatus.NODATA.getFlag());
            }

        };
        setLoadMoreView(loadMoreViewRoot, mUpdateLoadMoreStatusCallback);
    }

    public void showLoadMore() {
        mUpdateLoadMoreStatusCallback.onShowLoadMore();
    }

    public void showNoMoreData() {
        mUpdateLoadMoreStatusCallback.onShowNoMoreData();
    }

    public void loadDataFailed() {
        mUpdateLoadMoreStatusCallback.onShowLoadFailed();
    }

    protected CustomRecyclerViewLoadMoreWrapper setLoadMoreView(View loadMoreView, UpdateLoadMoreStatusCallback loadMoreAndNoMoreDataCallback) {
        this.mUpdateLoadMoreStatusCallback = loadMoreAndNoMoreDataCallback;
        return (CustomRecyclerViewLoadMoreWrapper) setLoadMoreView(loadMoreView);
    }

    //0 隐藏 1 加载中 2 没有数据 (要求没有数据的布局的背景颜色是f2f2f2,直接融为一体)
    private void setLoadingState(int status) {
        this.status = status;
        if (status == LoadingMoreViewStatus.HIDE.getFlag()) {
            setLoadMoreView(null,mUpdateLoadMoreStatusCallback);
//            mLoadMoreView.setVisibility(View.VISIBLE);
//            mLoadMoreView.setBackgroundResource(R.color.color_f2f2f2);
//            loading_more.setVisibility(View.GONE);
//            no_more_data.setVisibility(View.GONE);
        } else {
            setLoadMoreView(loadMoreViewRoot,mUpdateLoadMoreStatusCallback);
//            mLoadMoreView.setVisibility(View.VISIBLE);
//            mLoadMoreView.setBackgroundResource(R.color.color_f1f1f1);
            loading_more.setVisibility(status == LoadingMoreViewStatus.SHOWLOADING.getFlag() ? View.VISIBLE : View.GONE);
            no_more_data.setVisibility(status == LoadingMoreViewStatus.NODATA.getFlag() ? View.VISIBLE : View.GONE);
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public enum LoadingMoreViewStatus {
        HIDE(0), SHOWLOADING(1), NODATA(2);
        private int flag;

        private LoadingMoreViewStatus(int flag) {
            this.flag = flag;
        }

        public int getFlag() {
            return this.flag;
        }
    }

}
