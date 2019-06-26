package com.xywy.askforexpert.appcommon.base.fragment;

import android.view.View;

import com.xywy.askforexpert.appcommon.base.interfaze.FragmentInterface;
import com.xywy.askforexpert.appcommon.base.interfaze.StatisticsInterface;
import com.xywy.uilibrary.fragment.listfragment.XywyPullToRefreshAndLoadMoreFragment;

import butterknife.ButterKnife;

/**
 * Created by bailiangjin on 2017/3/22.
 */

public abstract class YMPullToRefreshAndLoadMoreFragment extends XywyPullToRefreshAndLoadMoreFragment implements StatisticsInterface,FragmentInterface {

    @Override
    protected void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void unBindView() {

    }

    @Override
    public void refreshFragment() {
        onRefresh();
    }

    @Override
    public void onRefresh() {

    }
}
