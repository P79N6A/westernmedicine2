package com.xywy.uilibrary.fragment.listfragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.xywy.uilibrary.R;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;
import com.xywy.uilibrary.recyclerview.wrapper.XYWYLinearRVLoadMoreWrapper;
import com.xywy.uilibrary.rx.CommonSubscribe;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import rx.Subscriber;


/**
 * Created by bailiangjin on 2017/3/22.
 */

public abstract class XywyPullToRefreshAndLoadMoreFragment extends XywyListFragment {

    SwipeRefreshLayout swipeRefreshLayout;


    XYWYLinearRVLoadMoreWrapper loadMoreWrapper;

    private static final int DEFAULT_PAGE = 1;

//    private int curPage = DEFAULT_PAGE;
    protected int curPage = DEFAULT_PAGE;
    private boolean hasMoreData = true;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_with_refresh_load_more_rv_list;
    }


    @Override
    protected void beforeViewBind() {

    }

    @Override
    protected void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        super.initView();
    }

    @Override
    protected void initRefresh() {
        super.initRefresh();
        swipeRefreshLayout.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage = DEFAULT_PAGE;
                //下拉刷新
                initOrRefreshData(new CommonSubscribe<Boolean>() {
                    @Override
                    public void onNext(Boolean isSuccess) {
                        hideRefreshProgressBar();
                        if (isSuccess) {
                            notifyAdapterDataChanged();
                        } else {
                            onLoadFailed();
                        }

                    }
                });

            }
        });
    }


    @Override
    protected void initLoadMore() {
        super.initLoadMore();
        hideNoDataView();
        loadMoreWrapper = new XYWYLinearRVLoadMoreWrapper(listRvAdapter, recyclerView);
        loadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Log.e("onLoadMoreRequested","onLoadMoreRequested");
                if (hasMoreData) {
                    //加载更多
                    loadMoreData(new CommonSubscribe<Boolean>() {

                        @Override
                        public void onNext(Boolean isSuccess) {
                            if (isSuccess) {
                                notifyAdapterDataChanged();
                            } else {
                                hasMoreData = false;
                                loadMoreWrapper.showNoMoreData();
                            }
                        }
                    });
                }else {
                    loadMoreWrapper.showNoMoreData();
                }

            }
        });
        realAdapter = loadMoreWrapper;
        recyclerView.setAdapter(realAdapter);
    }

    @Override
    protected void setAdapter(XYWYRVMultiTypeBaseAdapter adapter) {
        super.setAdapter(adapter);
        initLoadMore();
    }

    @Override
    protected void notifyAdapterDataChanged() {
        super.notifyAdapterDataChanged();
        if (null == listRvAdapter.getDatas() || listRvAdapter.getDatas().isEmpty()) {
            onLoadFailed();
        }else {
                loadMoreWrapper.showNoMoreData();
        }
    }

    /**
     * 加载数据失败时调用
     */
    protected void onLoadFailed() {
        loadMoreWrapper.loadDataFailed();
        swipeRefreshLayout.setRefreshing(false);
        showNoDataView();
    }

    protected void hideRefreshProgressBar() {
        swipeRefreshLayout.setRefreshing(false);
    }

    protected void disableRefresh() {
        swipeRefreshLayout.setEnabled(false);
    }

    protected void enableRefresh() {
        swipeRefreshLayout.setEnabled(true);
    }


    public abstract void initOrRefreshData(Subscriber<Boolean> subscriber);

    protected abstract void loadMoreData(Subscriber<Boolean> subscriber);


}
