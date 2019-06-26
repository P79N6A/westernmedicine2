package com.xywy.uilibrary.recyclerview.wrapper;

/**
 * 刷新加载更多view的状态
 * stone
 * 2017/12/22 下午4:48
 */
public interface UpdateLoadMoreStatusCallback {

    void onShowLoadMore();

    void onShowLoadFailed();

    void onShowNoMoreData();

}
