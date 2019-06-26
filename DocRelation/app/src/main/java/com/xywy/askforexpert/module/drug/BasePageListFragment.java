package com.xywy.askforexpert.module.drug;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.view.XYWYLoadMoreWrapper;
import com.xywy.retrofit.net.BaseData;
import com.xywy.uilibrary.fragment.XywySuperBaseFragment;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 分页的基类 stone
 */
public abstract class BasePageListFragment<T> extends XywySuperBaseFragment {

    //stone 分页相关
    protected Activity mActivity;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected EmptyWrapper mEmptyWrapper;
    protected XYWYLoadMoreWrapper mLoadMoreWrapper;
    protected int mPage = 1;

    protected XYWYRVMultiTypeBaseAdapter<T> adapter;

    private boolean isFirstLoad = true;//首次加载

    protected List mList = new ArrayList<>();

    protected int mEmptyLayoutId;
    private boolean isMoreSizi = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.common_recyclerview_list;
    }


    @Override
    protected void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void unBindView() {
        ButterKnife.unbind(this);
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isMoreSizi = false;
                mPage = 1;
                loadData(State.ONREFRESH.getFlag());
            }
        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST);
        itemDecoration.setDividerDrawble(getResources().getDrawable(R.drawable.item_divider_f2f2f2_8dp));
        mRecyclerView.addItemDecoration(itemDecoration);

        adapter = getAdapter();
        adapter.setData(mList);
        mEmptyWrapper = new EmptyWrapper(adapter);

        if (getEmptyLayoutId() != 0) {
            mEmptyLayoutId = getLayoutResId();
        } else {
            mEmptyLayoutId = R.layout.item_no_data_consult_answered;
        }

        mLoadMoreWrapper = new XYWYLoadMoreWrapper(mEmptyWrapper, mRecyclerView);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (isMoreSizi) {
                    mLoadMoreWrapper.setLoadingState(isMoreSizi);
                }else{
                    mPage++;
                    loadData(State.LOADMORE.getFlag());
                }

            }
        });
        mRecyclerView.setAdapter(mLoadMoreWrapper);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (isFirstLoad) {
            isFirstLoad = false;
            loadData(State.ONREFRESH.getFlag());
        }
    }

    protected void handleList(int state, BaseData<List<T>> entry) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (entry != null && entry.getData() != null) {
            if(entry.getData() instanceof ArrayList){
                List<T> data = entry.getData();
                int size = data.size();

                if (state == State.LOADMORE.getFlag()) {
                    if (size == 0 || size<10) {
                        isMoreSizi = true;
                        mLoadMoreWrapper.setLoadingState(isMoreSizi);
                        return;
                    }
                    for (int i = 0; i < size; i++) {
                        mList.add(data.get(i));
                    }
                    adapter.setData(mList);
                    mLoadMoreWrapper.notifyDataSetChanged();
                } else {
                    if (size == 0 || size<10) {
                        isMoreSizi = true;
                        mLoadMoreWrapper.setLoadingState(isMoreSizi);
                        mEmptyWrapper.setEmptyView(R.layout.item_pharmacy_record_list_empty);
                    }
                    mList = data;
                    adapter.setData(mList);
                    mLoadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
    }

    public enum State {
        ONREFRESH(1), LOADMORE(2);
        private int flag;

        private State(int flag) {
            this.flag = flag;
        }

        public int getFlag() {
            return this.flag;
        }
    }

    @Override
    protected void initListener() {

    }

    public abstract void loadData(int state);

    protected abstract int getEmptyLayoutId();

    protected abstract XYWYRVMultiTypeBaseAdapter<T> getAdapter();

}
