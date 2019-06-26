package com.xywy.askforexpert.appcommon.base.fragment;

import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.better.YMRVMultiTypeBaseAdapter;
import com.xywy.askforexpert.appcommon.base.interfaze.FragmentInterface;
import com.xywy.askforexpert.appcommon.base.interfaze.StatisticsInterface;
import com.xywy.uilibrary.fragment.listfragment.XywyListFragment;

import butterknife.ButterKnife;

/**
 * Created by bailiangjin on 2017/3/22.
 */

public abstract class YMListFragment extends XywyListFragment<YMRVMultiTypeBaseAdapter> implements StatisticsInterface,FragmentInterface {

    @Override
    protected void beforeViewBind() {

    }

    @Override
    public void bindView(View view) {
        titleBarBuilder.setBackGround(R.drawable.toolbar_bg_no_alpha_new);
        ButterKnife.bind(this, view);
    }

    @Override
    public void unBindView() {
        //ButterKnife.unbind(this);
    }

    @Override
    public void refreshFragment() {
        onRefresh();
    }

    @Override
    public void onRefresh() {

    }

}
