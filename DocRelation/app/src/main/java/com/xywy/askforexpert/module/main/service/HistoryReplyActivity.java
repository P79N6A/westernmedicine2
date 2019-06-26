package com.xywy.askforexpert.module.main.service;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.main.HistoryListPageBean;
import com.xywy.askforexpert.module.main.service.que.QueDetailActivity;
import com.xywy.askforexpert.module.main.service.que.adapter.QueItemAdapter;
import com.xywy.askforexpert.module.main.service.que.model.QuestionNote;
import com.xywy.askforexpert.widget.ActionItem;
import com.xywy.askforexpert.widget.TitlePopup;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;
import com.xywy.uilibrary.recyclerview.wrapper.CustomRecyclerViewLoadMoreWrapper;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史回复 问题广场 stone
 */
public class HistoryReplyActivity extends YMBaseActivity implements TitlePopup.OnItemOnClickListener, View.OnClickListener {

    private EmptyWrapper mEmptyWrapper;
    private CustomRecyclerViewLoadMoreWrapper mLoadMoreWrapper;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private TitlePopup menuPopup;

    private QueItemAdapter adapter;
    private List<QuestionNote> mList = new ArrayList<QuestionNote>();

    private String mAct = "1"; //最近帖子 1：一周 2：一个月 3：两个月
    private int mPage = 1;
    private int pageSize = 10;
    private int mEmptyLayoutId;
    private boolean mIsLoadingMore;

    private static final String INTENT_KEY_ISFROM = "isFrom";
    private static final String MYREPLY = "myreply";//历史回复
    private static final String INTENT_KEY_Q_TYPE = "q_type";//1 指定付费 2 悬赏 3绩效
    private static final String WEEK = "近一周";
    private static final String MONTH = "近一个月";
    private static final String TWO_MONTH = "近两个月";
    //    private ProgressDialog mDialog;
    private static final String TYPEURL = "";
    private static final String INTENT_KEY_RID = "rid";
    @Override
    protected void initView() {
        //stone
//        hideCommonBaseTitle();
        titleBarBuilder.setTitleText(getString(R.string.question_history_reply));
        YmRxBus.registerHistoryReplyUpdateSucess(new EventSubscriber<String>() {
            @Override
            public void onNext(Event<String> stringEvent) {
                String data = stringEvent.getData();
                int tempIndex = -1;
                QuestionNote questionNote = null;
                if(null != mList){
                    for (int i = 0; i < mList.size(); i++) {
                        if(stringEvent.getData().equals(mList.get(i).qid)){
                            questionNote = mList.get(i);
                            tempIndex = i;
                            break;
                        }
                    }
                    if(-1 != tempIndex && null != questionNote){
                        questionNote.level = "9";
                        mList.set(tempIndex,questionNote);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        },this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                getHistoryReply(mPage, mAct, State.ONREFRESH.getFlag());
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        itemDecoration.setDividerDrawble(getResources().getDrawable(R.drawable.item_divider_f2f2f2_8dp));
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        adapter = new QueItemAdapter(this);
        adapter.setHistory(true);
        adapter.setList(mList);
        mEmptyWrapper = new EmptyWrapper(adapter);
        mEmptyLayoutId = R.layout.item_no_data_history_reply;

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
                    getHistoryReply(mPage, mAct, State.LOADMORE.getFlag());
                }
            }
        });
        mRecyclerView.setAdapter(mLoadMoreWrapper);

//        mDialog = new ProgressDialog(this, "正在加载中，请稍后...");
//        mDialog.showProgersssDialog();
//        mNoDataLayout = (RelativeLayout) findViewById(R.id.que_no_data_layout);
//        mNoDataTv = (TextView) findViewById(R.id.no_data_tv);
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        ImageButton date = (ImageButton) findViewById(R.id.date);
        date.setOnClickListener(this);
        menuPopup = new TitlePopup(this, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        menuPopup.setItemOnClickListener(this);

        menuPopup.cleanAction();
        menuPopup.addAction(new ActionItem(this, WEEK));
        menuPopup.addAction(new ActionItem(this, MONTH));
        menuPopup.addAction(new ActionItem(this, TWO_MONTH));
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
//        mSwipeRefreshLayout.setClipChildren(true);

        adapter.setOnItemClickListener(new QueItemAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position, Object object) {
                if (TYPEURL.equals("del")) {
                    ToastUtils.shortToast("该问题已删除");
                } else {
                    Intent intent = new Intent(HistoryReplyActivity.this,
                            QueDetailActivity.class);
                    QuestionNote data = (QuestionNote) object;
                    intent.putExtra("tag", "myreply");
                    intent.putExtra("type", data.type);
                    intent.putExtra("index", 0);
                    intent.putExtra(Constants.LEVEL, data.level);
                    intent.putExtra(INTENT_KEY_ISFROM, MYREPLY);//从历史回复的帖子列表跳转
                    intent.putExtra(INTENT_KEY_RID, data.id);//从历史回复的帖子列表跳转
                    intent.putExtra("id", data.qid + "");
                    startActivity(intent);


                    intent.putExtra(INTENT_KEY_Q_TYPE, data.q_type);
                }
            }
        });
    }

    @Override
    protected void initData() {
        mPage = 1;
        getHistoryReply(mPage, mAct, State.ONREFRESH.getFlag());
    }


    private void getHistoryReply(int page, String act, final int state) {
        AjaxParams params = new AjaxParams();
        FinalHttp fh = new FinalHttp();
        params.put("userid", YMUserService.getCurUserId());
        params.put("act", act);
        params.put("page", page + "");
        params.put("pagesize", pageSize + "");
        params.put("sign", MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid() + Constants.MD5_KEY));
        DLog.d("my reply", "my reply url = " + (CommonUrl.QUE_MY_REPLY + "?" + params.toString()));
        fh.post(CommonUrl.QUE_MY_REPLY, params, new AjaxCallBack() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                if (state == State.LOADMORE.getFlag()) {
                    mIsLoadingMore = false;
                    mPage--;
                    if (mPage <= 1) {
                        mPage = 1;
                    }
                } else {
                    mList.clear();
                    adapter.setList(mList);
                }
                mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条
                mLoadMoreWrapper.loadDataFailed();
                mLoadMoreWrapper.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);

                if (TextUtils.isEmpty(t)) {
                    return;
                }

                HistoryListPageBean entry = GsonUtils.toObj(t, HistoryListPageBean.class);


                mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条

                if (entry != null
                        && entry.data != null && entry.data.data != null) {
                    int size = entry.data.data.size();
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
                            mList.addAll(entry.data.data);
                        }
                        adapter.setList(mList);
                        mLoadMoreWrapper.notifyDataSetChanged();
                    } else {
                        if (size == 0) {
                            mList.clear();
                            mLoadMoreWrapper.loadDataFailed();
                            mEmptyWrapper.setEmptyView(mEmptyLayoutId);
                        } else {
                            //是否有更多
                            if (entry.data.more == 1) {
                                mLoadMoreWrapper.showLoadMore();
                            } else {
                                mLoadMoreWrapper.loadDataFailed();
                            }
                            mList = entry.data.data;
                        }
                        adapter.setList(mList);
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
                    adapter.setList(mList);
                    mLoadMoreWrapper.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_history_reply;
    }

    @Override
    public void onItemClick(ActionItem item, int position) {
        String title = (String) item.mTitle;
        mPage = 1;
        if (WEEK.equals(title)) {
            mAct = "1";
        } else if (MONTH.equals(title)) {
            mAct = "2";
        } else if (TWO_MONTH.equals(title)) {
            mAct = "3";
        }
        getHistoryReply(mPage, mAct, State.ONREFRESH.getFlag());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
            case R.id.back:
                finish();
                break;
            case R.id.date:
                menuPopup.show_new(view);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private enum State {
        ONREFRESH(1), LOADMORE(2);
        private int flag;

        private State(int flag) {
            this.flag = flag;
        }

        public int getFlag() {
            return this.flag;
        }
    }
}
