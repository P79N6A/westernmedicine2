package com.xywy.askforexpert.module.consult.fragment;

import com.xywy.askforexpert.appcommon.base.fragment.YMPullToRefreshAndLoadMoreFragment;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;

import rx.Subscriber;

/**
 * Created by zhangzheng on 2017/5/9.
 */

public class ConsultAnsweredListFragment extends YMPullToRefreshAndLoadMoreFragment {
    @Override
    public String getStatisticsId() {
        return null;
    }

    @Override
    protected XYWYRVMultiTypeBaseAdapter getListRvAdapter() {
        return null;
    }

    @Override
    public void initOrRefreshData(Subscriber<Boolean> subscriber) {

    }

    @Override
    protected void loadMoreData(Subscriber<Boolean> subscriber) {

    }
}
