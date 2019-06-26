package com.xywy.uilibrary.recyclerview.wrapper;

import android.view.View;

/**
 * Created by bailiangjin on 2017/1/17.
 */

public interface LoadMoreAndNoMoreDataCallback {


    void onShowLoadMore(View mLoadMoreView);

    void onShowLoadFailed(View mLoadMoreView);

    void onShowNoMoreData(View mLoadMoreView);
}
