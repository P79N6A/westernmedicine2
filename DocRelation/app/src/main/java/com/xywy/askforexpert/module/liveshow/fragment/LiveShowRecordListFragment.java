package com.xywy.askforexpert.module.liveshow.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseFragment;
import com.xywy.askforexpert.appcommon.base.adapter.better.YMLinearRVLoadMoreWrapper;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.rxretrofitoktools.service.WWSXYWYService;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.liveshow.LiveShowHostInfo;
import com.xywy.askforexpert.module.liveshow.adapter.LiveShowRecordAdapter;
import com.xywy.askforexpert.module.liveshow.utils.LiveShowUtils;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static android.R.attr.id;

/**
 * 我的直播记录 分页请求数据
 * Created by bailiangjin on 2017/2/23.
 */

public class LiveShowRecordListFragment extends YMBaseFragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.tv_no_data)
    TextView tv_no_data;

    LiveShowRecordAdapter liveShowRecordAdapter;
    private YMLinearRVLoadMoreWrapper loadMoreWrapper;

    private List<LiveShowHostInfo.DataBean.ListBean> liveShowRecordList = new ArrayList<>();

    //分页相关

    private static final int DEFAULT_PAGE = 1;
    private boolean isHasMoreData = true;
    private int pageNumber = DEFAULT_PAGE;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_live_show_record_list;
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        liveShowRecordAdapter = new LiveShowRecordAdapter(getActivity());
        liveShowRecordAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                LiveShowHostInfo.DataBean.ListBean dataBean = liveShowRecordList.get(position);
                if (null == dataBean) {
                    shortToast("条目数据为空");
                    return;
                }
                try {
                    LogUtils.e("跳转直播详情页:" + GsonUtils.toJson(dataBean));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int type = dataBean.getState();
                String liveShowId = dataBean.getId();
                String chatRoomId = dataBean.getChatroomsid();
                String rtmp = dataBean.getRtmp();
                List<String> vodList = dataBean.getVod_list();

                if(1==type){
                    LiveShowUtils.watchLive(getActivity(),liveShowId, chatRoomId, rtmp);
                }else {
                    LiveShowUtils.watchLiveRecord(getActivity(),liveShowId, chatRoomId, vodList);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        loadMoreWrapper = new YMLinearRVLoadMoreWrapper(liveShowRecordAdapter, recyclerView);
        loadMoreWrapper.setOnLoadMoreListener(new YMLinearRVLoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (isHasMoreData) {
                    pageNumber++;
                    LogUtils.d("加载更多" + id);
                    //shortToast("加载更多" + id);
                    requestData();
                } else {
                    //shortToast("没有更多数据");
                    LogUtils.d("没有更多数据");
                    loadMoreWrapper.showNoMoreData();
                }
            }
        });
        recyclerView.setAdapter(loadMoreWrapper);
    }



    @Override
    protected void initData(Bundle savedInstanceState) {
        requestData();
    }

    private void requestData() {

        WWSXYWYService.getLiveShowHostInfo(YMUserService.getCurUserId(), YMUserService.getCurUserId(), pageNumber, new CommonResponse<LiveShowHostInfo>() {
            //WWSXYWYService.getLiveShowHostInfo(YMUserService.getCurUserId(), 1, new CommonResponse<LiveShowHostInfo>() {
            @Override
            public void onNext(LiveShowHostInfo liveShowHostInfo) {

                if (null == liveShowHostInfo) {
                    LogUtils.e("服务端返回数据为空");
                    if (DEFAULT_PAGE == pageNumber) {
                        showNoDataView();
                    }
                    pageNumber--;
                    return;
                }
                if (null == liveShowHostInfo.getData().getList() || liveShowHostInfo.getData().getList().isEmpty()) {
                    LogUtils.e("没有更多数据");
                    if (DEFAULT_PAGE == pageNumber) {
                        showNoDataView();
                    }
                    isHasMoreData = false;
                    pageNumber--;
                    return;
                }
                //更新数据
                List<LiveShowHostInfo.DataBean.ListBean> videoShowBeanList = liveShowHostInfo.getData().getList();

                updateAdapter(videoShowBeanList);

            }
        });

    }

    private void updateAdapter(List<LiveShowHostInfo.DataBean.ListBean> videoShowBeanList) {
        if (DEFAULT_PAGE == pageNumber) {
            liveShowRecordList.clear();
        }
        hideNoDataView();
        for(LiveShowHostInfo.DataBean.ListBean item:videoShowBeanList){
            if(-1!=item.getState()){
                //state为-1 为无效item
                liveShowRecordList.add(item);
            }
        }
        liveShowRecordAdapter.setData(liveShowRecordList);
        loadMoreWrapper.notifyDataSetChanged();
    }

    private void showNoDataView() {
        recyclerView.setVisibility(View.GONE);
        tv_no_data.setVisibility(View.VISIBLE);
    }

    private void hideNoDataView() {
        recyclerView.setVisibility(View.VISIBLE);
        tv_no_data.setVisibility(View.GONE);
    }

    @Override
    public String getStatisticalPageName() {
        return null;
    }
}
