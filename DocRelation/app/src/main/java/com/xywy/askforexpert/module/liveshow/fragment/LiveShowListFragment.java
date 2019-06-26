package com.xywy.askforexpert.module.liveshow.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseFragment;
import com.xywy.askforexpert.appcommon.base.adapter.better.YMLinearRVLoadMoreWrapper;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.rxretrofitoktools.service.WWSXYWYService;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.liveshow.LiveShowListPageBean;
import com.xywy.askforexpert.model.liveshow.MyFollowedPageBean;
import com.xywy.askforexpert.model.liveshow.VideoShowBean;
import com.xywy.askforexpert.module.liveshow.LiveShowListActivity;
import com.xywy.askforexpert.module.liveshow.adapter.liveshowlist.LiveShowItem;
import com.xywy.askforexpert.module.liveshow.adapter.liveshowlist.LiveShowListAdapter;
import com.xywy.askforexpert.module.liveshow.utils.LiveShowUtils;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Subscriber;

/**
 * 我的粉丝
 * Created by bailiangjin on 2017/2/23.
 */

public class LiveShowListFragment extends YMBaseFragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    LiveShowListAdapter liveShowListAdapter;

    YMLinearRVLoadMoreWrapper loadMoreWrapper;


    /**
     * 服务端返回的数据list 不用于列表和Adapter
     */
    List<LiveShowListPageBean.DataBean> serverFormatDataList = new ArrayList<LiveShowListPageBean.DataBean>();

    /**
     * 列表显示用的数据list用于列表和Adapter
     */
    List<LiveShowItem> liveShowItemForViewList = new ArrayList<>();

    private String state;
    private String cate_id;

    public static final String STATE_BUNDLE_KEY = "STATE_BUNDLE_KEY";
    public static final String CATE_ID_BUNDLE_KEY = "CATE_ID_BUNDLE_KEY";

    private static final int DEFAULT_PAGE = 1;

    private int curPage = DEFAULT_PAGE;
    private boolean hasMoreData = true;

    public static LiveShowListFragment getInstance(String state, String cate_id) {
        LiveShowListFragment liveShowListFragment = new LiveShowListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(STATE_BUNDLE_KEY, state);
        bundle.putString(CATE_ID_BUNDLE_KEY, cate_id);
        liveShowListFragment.setArguments(bundle);
        return liveShowListFragment;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_live_show_list;
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        swipeRefreshLayout.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage = DEFAULT_PAGE;
                initOrRefreshData();
            }
        });

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        state = getArguments().getString(STATE_BUNDLE_KEY);
        cate_id = getArguments().getString(CATE_ID_BUNDLE_KEY);
        initOrRefreshData();
    }

    private void initOrRefreshData() {

        getDataByPage(curPage, new CommonResponse<PageDataResultBean<LiveShowListPageBean.DataBean>>() {

            @Override
            public void onNext(PageDataResultBean<LiveShowListPageBean.DataBean> dataBeanPageDataResultBean) {
                swipeRefreshLayout.setRefreshing(false);
                if (null != dataBeanPageDataResultBean) {
                    if (dataBeanPageDataResultBean.isHasMoreData()) {
                        hasMoreData = true;
                        showDataInList(dataBeanPageDataResultBean.getDataList());
                        curPage++;
                    } else {
                        hasMoreData = false;
                        if(null!=loadMoreWrapper){
                            loadMoreWrapper.showNoMoreData();
                        }
                        curPage = DEFAULT_PAGE == curPage ? DEFAULT_PAGE : curPage - 1;
                    }
                }
            }
        });
    }

    private void getDataByPage(int curPage, final Subscriber<PageDataResultBean<LiveShowListPageBean.DataBean>> callback) {
        if (LiveShowListActivity.stateArray[2].equals(state)) {
            // 调用我关注的直播 1552接口获取数据
            WWSXYWYService.getMyFollowedLiveShowPageInfo(YMUserService.getCurUserId(), curPage, new CommonResponse<MyFollowedPageBean>() {
                @Override
                public void onNext(MyFollowedPageBean myFollowedPageBean) {

                    if (null == myFollowedPageBean) {
                        LogUtils.e("服务端返回数据为空");
                        //shortToast("服务端返回数据为空");
                        callback.onNext(new PageDataResultBean<LiveShowListPageBean.DataBean>(false, null));
                        return;
                    }

                    List<VideoShowBean> dataList = myFollowedPageBean.getData();
                    if (null == dataList || dataList.isEmpty()) {
                        LogUtils.e("服务端返回数据列表为空");
                        //shortToast("服务端返回数据列表为空");
                        callback.onNext(new PageDataResultBean<LiveShowListPageBean.DataBean>(false, null));
                        return;
                    }
                    //数据类型转换
                    List<LiveShowListPageBean.DataBean> dataListForView = new ArrayList<LiveShowListPageBean.DataBean>();

                    for (VideoShowBean videoShowBean : dataList) {
                        if (null == videoShowBean) {
                            continue;
                        }
                        LiveShowListPageBean.DataBean dataBean = new LiveShowListPageBean.DataBean();
                        dataBean.setId(videoShowBean.getId());
                        dataBean.setAmount(videoShowBean.getAmount());
                        dataBean.setName(videoShowBean.getName());
                        dataBean.setCover(videoShowBean.getCover());
                        dataBean.setCreatetime(videoShowBean.getCreatetime());
                        dataBean.setState(videoShowBean.getState());
                        dataBean.setRtmp(videoShowBean.getRtmp());
                        dataBean.setVod(videoShowBean.getVod());
                        dataBean.setVod_list(videoShowBean.getVod_list());
                        dataBean.setChatroomsid(videoShowBean.getChatroomsid());
                        dataBean.setUserid(videoShowBean.getUserid());
                        VideoShowBean.UserBean userBean = videoShowBean.getUser();
                        LiveShowListPageBean.DataBean.UserBean llUserBean = new LiveShowListPageBean.DataBean.UserBean();
                        llUserBean.setUserid(userBean.getUserid());
                        llUserBean.setName(userBean.getName());
                        llUserBean.setState(userBean.getState());
                        llUserBean.setLever(userBean.getLever());
                        llUserBean.setPortrait(userBean.getPortrait());
                        llUserBean.setSynopsis(userBean.getSynopsis());
                        llUserBean.setSex(userBean.getSex());
                        dataBean.setUser(llUserBean);
                        dataListForView.add(dataBean);

                    }
                    callback.onNext(new PageDataResultBean<LiveShowListPageBean.DataBean>(true, dataListForView));
                }
            });

        } else {
            WWSXYWYService.getLiveShowList(state, cate_id, curPage, 10, new CommonResponse<LiveShowListPageBean>() {
                @Override
                public void onNext(LiveShowListPageBean liveShowListPageBean) {
                    if (null == liveShowListPageBean) {
                        LogUtils.e("服务端返回数据为空");
                        //shortToast("服务端返回数据为空");
                        callback.onNext(new PageDataResultBean<LiveShowListPageBean.DataBean>(false, null));
                        return;
                    }

                    List<LiveShowListPageBean.DataBean> dataList = liveShowListPageBean.getData();
                    if (null == dataList || dataList.isEmpty()) {
                        LogUtils.e("服务端返回数据列表为空");
                        //shortToast("服务端返回数据列表为空");
                        callback.onNext(new PageDataResultBean<LiveShowListPageBean.DataBean>(false, null));
                        return;
                    }
                    callback.onNext(new PageDataResultBean<LiveShowListPageBean.DataBean>(true, dataList));
                }
            });
        }
    }

    private void showDataInList(List<LiveShowListPageBean.DataBean> dataList) {


        if (DEFAULT_PAGE == curPage) {
            serverFormatDataList.clear();
        }

        serverFormatDataList.addAll(dataList);

        liveShowItemForViewList.clear();
        if (LiveShowListActivity.stateArray[0].equals(state)) {
            if (serverFormatDataList.size() <= 5) {
                liveShowItemForViewList.add(new LiveShowItem(serverFormatDataList));
            } else {
                List<LiveShowListPageBean.DataBean> firstFiveList = new ArrayList<LiveShowListPageBean.DataBean>();
                for (int i = 0; i < serverFormatDataList.size(); i++) {
                    if (i < 5) {
                        firstFiveList.add(serverFormatDataList.get(i));
                    }
                    if (i == 5) {
                        liveShowItemForViewList.add(new LiveShowItem(firstFiveList));
                    }
                    if (i >= 5) {
                        liveShowItemForViewList.add(new LiveShowItem(serverFormatDataList.get(i)));
                    }
                }
            }
        } else {
            for (int i = 0; i < serverFormatDataList.size(); i++) {
                liveShowItemForViewList.add(new LiveShowItem(serverFormatDataList.get(i)));
            }
        }

        if (null == liveShowListAdapter) {
            liveShowListAdapter = new LiveShowListAdapter(getActivity());
            liveShowListAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    if (YMUserService.isLoginUser(getActivity())) {
                        LiveShowItem item = liveShowItemForViewList.get(position);
                        if (LiveShowItem.Type.SINGLE == item.getType()) {
                            LiveShowListPageBean.DataBean dataBean = item.getItem();

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
                           } else {
                            //do nothing
                        }
                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            loadMoreWrapper = new YMLinearRVLoadMoreWrapper(liveShowListAdapter, recyclerView);
            loadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {

                    if (hasMoreData) {
                        initOrRefreshData();
                        //loadMoreWrapper.showNoMoreData();
                    } else {
                        loadMoreWrapper.showNoMoreData();
                    }
                }
            });
            recyclerView.setAdapter(loadMoreWrapper);
            liveShowListAdapter.setData(liveShowItemForViewList);
            loadMoreWrapper.notifyDataSetChanged();
        } else {
            liveShowListAdapter.setData(liveShowItemForViewList);
            loadMoreWrapper.notifyDataSetChanged();
        }
    }

    @Override
    public String getStatisticalPageName() {
        return null;
    }
}
