package com.xywy.askforexpert.module.drug;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.retrofit.net.BaseData;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;
import com.xywy.uilibrary.recyclerview.wrapper.CustomRecyclerViewLoadMoreWrapper;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页基类 stone
 */
public abstract class BasePageListActivity<T> extends YMBaseActivity {

    protected EmptyWrapper mEmptyWrapper;
    protected CustomRecyclerViewLoadMoreWrapper mLoadMoreWrapper;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager mLinearLayoutManager;

    protected MultiItemTypeAdapter<T> adapter;
    protected List<T> mList = new ArrayList<>();

    //    private String mDoctorId = YMApplication.getPID();
    protected int mPage = 1;
    protected int pageSize = 10;
    protected int mEmptyLayoutId;
    private boolean mIsLoadingMore;



    @Override
    protected int getLayoutResId() {
        return R.layout.common_recyclerview_list;
    }

    @Override
    protected void initView() {

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                loadData(State.ONREFRESH.getFlag());
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        itemDecoration.setDividerDrawble(getResources().getDrawable(R.drawable.item_divider_f2f2f2_8dp));
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        adapter = getAdapter();

        mEmptyWrapper = new EmptyWrapper(adapter);
        mEmptyLayoutId = R.layout.layout_list_no_data_common;

        mLoadMoreWrapper = new CustomRecyclerViewLoadMoreWrapper(mEmptyWrapper, mRecyclerView);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //避免上来就加载
                if (!mIsLoadingMore
                        && mList.size() > 0
                        && mLoadMoreWrapper.getStatus() == CustomRecyclerViewLoadMoreWrapper.LoadingMoreViewStatus.SHOWLOADING.getFlag()) {
                    mPage++;
                    mIsLoadingMore = true;
                    loadData(State.LOADMORE.getFlag());
                }
            }
        });
        mRecyclerView.setAdapter(mLoadMoreWrapper);

//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
//        mSwipeRefreshLayout.setClipChildren(true);

//        adapter.setOnItemClickListener(new QueItemAdapter.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(int position, Object object) {
//                if (TYPEURL.equals("del")) {
//                    ToastUtils.shortToast("该问题已删除");
//                } else {
//                    Intent intent = new Intent(CommonDrugListActivity.this,
//                            QueDetailActivity.class);
//                    QuestionNote data = (QuestionNote) object;
//                    intent.putExtra("tag", "myreply");
//                    intent.putExtra("type", data.type);
//                    intent.putExtra("index", 0);
//                    intent.putExtra(INTENT_KEY_ISFROM, MYREPLY);//从历史回复的帖子列表跳转
//                    intent.putExtra("id", data.qid + "");
//                    startActivity(intent);
//
//
//                    intent.putExtra(INTENT_KEY_Q_TYPE, data.q_type);
//                }
//            }
//        });
    }

    @Override
    protected void initData() {
        mPage = 1;
        loadData(State.ONREFRESH.getFlag());
    }


    private void loadData(int page, String doctorId, final int state) {


    }

    /**
     * 处理列表请求结果
     *
     * @param state
     * @param entry
     */
    protected void handleList(int state, BaseData<List<T>> entry) {
        //失败
        if (entry == null) {
            if (state == State.LOADMORE.getFlag()) {
                mIsLoadingMore = false;
                mPage--;
                if (mPage <= 1) {
                    mPage = 1;
                }
            } else {
                mList.clear();
                adapter.getDatas().clear();
                adapter.getDatas().addAll(mList);
            }
            mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条
            mLoadMoreWrapper.loadDataFailed();
            mLoadMoreWrapper.notifyDataSetChanged();
        } else {
            //成功
            mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条

            if (entry != null
                    && entry.getData() != null) {
                int size = ((ArrayList) entry.getData()).size();
                if (state == State.LOADMORE.getFlag()) {
                    mIsLoadingMore = false;
                    if (size == 0) {
                        //stone 后台一页展示10个数据 数据过于少,就直接不展示没有最多数据的view
                        if (mList.size() > 10) {
                            mLoadMoreWrapper.showNoMoreData();
                        } else {
                            mLoadMoreWrapper.loadDataFailed();
                        }
                    } else {
                        mLoadMoreWrapper.showLoadMore();
                        mList.addAll((ArrayList) (entry.getData()));
                    }
                    adapter.getDatas().clear();
                    adapter.getDatas().addAll(mList);
                    mLoadMoreWrapper.notifyDataSetChanged();
                } else {
                    if (size == 0) {
                        mList.clear();
                        mLoadMoreWrapper.loadDataFailed();
                        mEmptyWrapper.setEmptyView(mEmptyLayoutId);
                    } else {
                        mLoadMoreWrapper.showLoadMore();
                        mList = ((ArrayList) (entry.getData()));
                    }
                    adapter.getDatas().clear();
                    adapter.getDatas().addAll(mList);
                    mLoadMoreWrapper.notifyDataSetChanged();
                }
            } else {
                mLoadMoreWrapper.loadDataFailed();
                if (state == State.LOADMORE.getFlag()) {
                    mIsLoadingMore = false;
                } else {
                    mList.clear();
                    mEmptyWrapper.setEmptyView(mEmptyLayoutId);
                }
                adapter.getDatas().clear();
                adapter.getDatas().addAll(mList);
                mLoadMoreWrapper.notifyDataSetChanged();
            }
        }
    }

    protected enum State {
        ONREFRESH(1), LOADMORE(2);
        private int flag;

        private State(int flag) {
            this.flag = flag;
        }

        public int getFlag() {
            return this.flag;
        }
    }


    public abstract void loadData(int state);

    protected abstract int getEmptyLayoutId();

    protected abstract MultiItemTypeAdapter<T> getAdapter();
}
