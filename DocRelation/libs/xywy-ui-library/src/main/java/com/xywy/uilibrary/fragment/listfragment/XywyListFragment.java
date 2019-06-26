package com.xywy.uilibrary.fragment.listfragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.xywy.uilibrary.R;
import com.xywy.uilibrary.fragment.XywySuperBaseFragment;
import com.xywy.uilibrary.recyclerview.adapter.HSItemDecoration;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;

import java.util.List;


/**
 * Created by bailiangjin on 2017/3/22.
 */

public abstract class XywyListFragment<T extends XYWYRVMultiTypeBaseAdapter> extends XywySuperBaseFragment {


    private FrameLayout fl_no_data_view_container;

    protected RecyclerView recyclerView;

    protected T listRvAdapter;

    protected RecyclerView.Adapter realAdapter;

    protected boolean isDecorationAdded = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_with_rv_list;
    }

    protected int getNoDataLayoutResId() {
        return 0;
    }


    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        fl_no_data_view_container = (FrameLayout) rootView.findViewById(R.id.fl_no_data_view_container);

        if (getNoDataLayoutResId() > 0) {
            View noDataView = layoutInflater.inflate(getNoDataLayoutResId(), null);
            fl_no_data_view_container.addView(noDataView);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (!isDecorationAdded) {
            recyclerView.addItemDecoration(getItemDecoration());
            isDecorationAdded = true;
        }
        listRvAdapter = getListRvAdapter();
        realAdapter = listRvAdapter;
        initRefresh();
    }


    /**
     * 获取分割线样式
     *
     * @return
     */
    private HSItemDecoration getItemDecoration() {

        if (getDividerLineWidth() >= 0 && getDividerColorResId() > 0) {
            return new HSItemDecoration(getActivity(), getDividerColorResId(), getDividerLineWidth());
        } else if (getDividerLineWidth() >= 0) {
            return new HSItemDecoration(getActivity(), getDividerLineWidth());
        } else if (getDividerColorResId() > 0) {
            return new HSItemDecoration(getActivity(), getDividerColorResId());
        } else {
            return new HSItemDecoration(getActivity());
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        recyclerView.setAdapter(realAdapter);
        initLoadMore();
    }

    protected void notifyAdapterDataChanged() {
        if (null != realAdapter) {
            realAdapter.notifyDataSetChanged();
            notifyNoDataViewState();
        }

    }

    /**
     * 更新无数据view状态
     */
    private void notifyNoDataViewState() {
        XYWYRVMultiTypeBaseAdapter curAdapter = realAdapter instanceof XYWYRVMultiTypeBaseAdapter ? (XYWYRVMultiTypeBaseAdapter) realAdapter : listRvAdapter;
        List dataList = curAdapter.getDatas();
        if (null == dataList || dataList.isEmpty()) {
            showNoDataView();
        } else {
            hideNoDataView();
        }
    }

    /**
     * 无数据View当前是否显示
     *
     * @return
     */
    protected boolean isNoDataViewVisiable() {
        return View.VISIBLE == fl_no_data_view_container.getVisibility();
    }

    /**
     * 显示无数据View
     */
    protected void showNoDataView() {
        if (getNoDataLayoutResId() <= 0) {
            return;
        }

        if (!isNoDataViewVisiable()) {
            fl_no_data_view_container.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 隐藏无数据View
     */
    protected void hideNoDataView() {
        if (isNoDataViewVisiable()) {
            fl_no_data_view_container.setVisibility(View.GONE);
        }
    }


    protected void setAdapter(T adapter) {
        listRvAdapter = adapter;
    }


    protected void initRefresh() {

    }


    protected void initLoadMore() {
    }

    protected abstract T getListRvAdapter();


    /**
     * 自定义分割线宽度
     *
     * @return
     */
    protected float getDividerLineWidth() {
        return -1;
    }

    /**
     * 自定义分隔线颜色 返回color的resId
     *
     * @return
     */
    protected int getDividerColorResId() {
        return 0;
    }


}
