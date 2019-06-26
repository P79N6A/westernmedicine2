package com.xywy.askforexpert.appcommon.base.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewStub;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseFragment;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.BaseUltimateRecycleAdapter;
import com.xywy.askforexpert.module.docotorcirclenew.interfaze.IViewRefresh;
import com.xywy.askforexpert.module.docotorcirclenew.interfaze.IViewRender;
import com.xywy.askforexpert.module.docotorcirclenew.model.IRecycleViewModel;
import com.xywy.askforexpert.widget.view.ultimaterecycleview.UltimateRecyclerView;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;

import butterknife.Bind;

/**
 * 医圈 带上拉刷新和下拉加载更多公用列表 Fragment
 * Created by bailiangjin on 2016/10/27.
 */

public class CommonListFragment extends YMBaseFragment implements IViewRender,IViewRefresh {

    public UltimateRecyclerView getRecyclerView() {
        return ultimateRecyclerView;
    }

    public interface CallBack {
        void afterInit(UltimateRecyclerView ultimateRecyclerView);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    protected CallBack callBack;
    @Bind(R.id.ultimate_recycler_view)
    protected UltimateRecyclerView ultimateRecyclerView;
    @Bind(R.id.vs_head)
    protected ViewStub  vsHead;


    public void setRecycleViewModel(IRecycleViewModel recycleViewModel) {
        this.recycleViewModel = recycleViewModel;
    }

    private IRecycleViewModel recycleViewModel;

    public void setAdapter(BaseUltimateRecycleAdapter adapter) {
        this.adapter = adapter;
    }

    private BaseUltimateRecycleAdapter adapter;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_doctor_circle_common_list;
    }

    @Override
    protected void initView() {
        ultimateRecyclerView.mRecyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ultimateRecyclerView.getContext());
        ultimateRecyclerView.mRecyclerView.setLayoutManager(linearLayoutManager);
        ultimateRecyclerView.setEmptyView(R.layout.no_data, UltimateRecyclerView.EMPTY_KEEP_HEADER,UltimateRecyclerView.STARTWITH_ONLINE_ITEMS);
        ultimateRecyclerView.enableFootView(true);
        if (itemDecoration==null){
//            itemDecoration = new DividerItemDecoration(ultimateRecyclerView.getContext(), LinearLayoutManager.VERTICAL);
            itemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
            itemDecoration.setDividerDrawble(getResources().getDrawable(R.drawable.item_divider_common));
        }
        ultimateRecyclerView.mRecyclerView.addItemDecoration(itemDecoration);
        if (callBack !=null)
            callBack.afterInit(ultimateRecyclerView);
    }
//    private RecyclerView.ItemDecoration itemDecoration;
//    public void setDivider(RecyclerView.ItemDecoration itemDecoration){
//        this.itemDecoration=itemDecoration;
//    }

    private DividerItemDecoration  itemDecoration;
    public void setDivider(DividerItemDecoration itemDecoration){
        this.itemDecoration=itemDecoration;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
//        ultimateRecyclerView.reenableLoadmore();
        ultimateRecyclerView.setAdapter(adapter);
        refresh();
        adapter.setUiListener(recycleViewModel.getItemClickListener());
        adapter.setData(recycleViewModel.getData());
    }

    @Override
    protected void initListener() {
        super.initListener();
        ultimateRecyclerView.setDefaultOnRefreshListener(recycleViewModel);
        ultimateRecyclerView.setOnLoadMoreListener(recycleViewModel);
        recycleViewModel.registerEvent(getActivity());
    }

    @Override
    public String getStatisticalPageName() {
        return null;
    }


    @Override
    public void refresh() {
        recycleViewModel.onRefresh();
    }



    @Override
    public void handleModelMsg(Message msg) {
        switch (msg.what) {
            case Refreshing:
                ultimateRecyclerView.setRefreshing(true);
                break;
            case StopRefreshing:
                ultimateRecyclerView.setRefreshing(false);
                break;
            case DataChanged:
                adapter.notifyDataSetChanged();
                break;
            case isLoadingData:
                adapter.getState().setLoadingData(true);
                break;
            case stopLoadingData:
                adapter.getState().setLoadingData(false);
                break;
            case HasMore:
                adapter.getState().setHasMore(true);
                break;
            case NoMore:
                adapter.getState().setHasMore(false);
                break;
            case isFirstLoading:
                adapter.getState().setFirstLoading(true);
                break;
            case notFirstLoading:
                adapter.getState().setFirstLoading(false);
                break;
        }
    }
}

