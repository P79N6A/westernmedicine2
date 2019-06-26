package com.xywy.askforexpert.module.consult.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.model.consultentity.ConsultPagerTabBean;
import com.xywy.askforexpert.model.consultentity.ConsultPagerTabItemBean;
import com.xywy.askforexpert.model.consultentity.QuestionAnsweredLIstRspEntity;
import com.xywy.askforexpert.model.consultentity.QuestionInHandleRspEntity;
import com.xywy.askforexpert.model.consultentity.QuestionsListEntity;
import com.xywy.askforexpert.model.consultentity.QuestionsPoolRspEntity;
import com.xywy.askforexpert.model.websocket.msg.chatmsg.ChatMsg;
import com.xywy.askforexpert.module.consult.ConsultConstants;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.consult.activity.ConsultChatActivity;
import com.xywy.askforexpert.module.consult.adapter.ConsultListAdapter;
import com.xywy.askforexpert.module.websocket.WebSocketRxBus;
import com.xywy.easeWrapper.utils.ImFileCacheUtil;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.uilibrary.fragment.XywySuperBaseFragment;
import com.xywy.uilibrary.recyclerview.wrapper.CustomRecyclerViewLoadMoreWrapper;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * 咨询中/问题库/历史回复 stone 即时问答模块
 */
public class ConsultItemFragment extends XywySuperBaseFragment {

    public static final String STR_PARAM = "STR_PARAM";
    public static final String SUMUP_PARAM = "SUMUP_PARAM";
    public static final String FROM_WTK_OR_ANSWER = "FROM_WTK_OR_ANSWER";
    //stone 分页相关
    private Activity mActivity;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mPharmacyRecordRecyclerView;
    private EmptyWrapper mEmptyWrapper;
    private CustomRecyclerViewLoadMoreWrapper mLoadMoreWrapper;
    private int mPage = 1;

    private ConsultListAdapter adapter;
    private ConsultPagerTabBean tabBean;

    private IModifyMsgTip modifyMsgTip;

    //stone 来自问题库或者历史回复问题
    private boolean isFromWTKOrHistory;
    //stone 是否来自历史回复
    private boolean isFromMyAnswer;
    private boolean isFirstLoad = true;//首次加载

    private List<QuestionsListEntity> mList = new ArrayList<>();

    private String doctorId;
    private boolean mIsLoadingMore;
    private int mEmptyLayoutId;



    private int sumUp; //为1为未总结 2为全部

    private ImFileCacheUtil imFileCacheUtil = ImFileCacheUtil.getInstance();
    private static int TIME = 0;
    private static String imageUrl = "";
    //false表示来自问题库不更新ConsultOnlineActivity的滚动条
    public boolean isQuestionList = true;




    //isFromWTKOrHistory 为true时 聊天页面不展示头部的 跳过和转诊 stone  sumUp 为1为未总结 2为全部
    public static ConsultItemFragment newInstance(ConsultPagerTabBean bean, IModifyMsgTip modifyMsgTip, boolean isFromWTKOrHistory, int sumUp) {
        ConsultItemFragment fragment = new ConsultItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SUMUP_PARAM, sumUp);
        bundle.putSerializable(STR_PARAM, bean);
        bundle.putBoolean(FROM_WTK_OR_ANSWER, isFromWTKOrHistory);
        fragment.setArguments(bundle);
        fragment.setModifyMsgTip(modifyMsgTip);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.question_list_fragment;
    }

    @Override
    protected void beforeViewBind() {
        if (getArguments() != null) {
            sumUp = getArguments().getInt(SUMUP_PARAM);
            tabBean = (ConsultPagerTabBean) getArguments().getSerializable(STR_PARAM);
            isFromWTKOrHistory = getArguments().getBoolean(FROM_WTK_OR_ANSWER);

        }
        doctorId = YMApplication.getUUid();
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
    protected void initListener() {

    }

    @Override
    public void onResume() {
        //stone 不是历史回复(咨询中,问题库刷新) 并且不是首次加载都刷新
//        if (!isFromMyAnswer && !isFirstLoad) {
//            mPage = 1;
//            loadData(State.ONREFRESH.getFlag());
//        }
        if (!isFirstLoad) {
            mPage = 1;
            loadData(State.ONREFRESH.getFlag());
        }
        super.onResume();
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //stone 断开自动重连 2018年01月11日13:19:59
                YMOtherUtils.autoConnectWebSocketIfNeed();

                mPage = 1;
                if (tabBean.getData().getType() == ConsultPagerTabItemBean.TYPE_MY_CONSULT||
                        tabBean.getData().getType() == ConsultPagerTabItemBean.TYPE_UNANSWERED){
                    modifyMsgTip.loadProcessingData();
                }else{
                    loadData(State.ONREFRESH.getFlag());
                }
            }
        });
        mPharmacyRecordRecyclerView = (RecyclerView) rootView.findViewById(R.id.pharmacy_record);
        mPharmacyRecordRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
//        mPharmacyRecordRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));

        adapter = new ConsultListAdapter(mActivity, isFromWTKOrHistory, new ConsultListAdapter.ImDetailCall() {
            @Override
            public void imDetailCall() {
//                imFileCacheUtil.IM_CACHE_TAG = false;
            }
        });
//        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                QuestionsListEntity item = adapter.getItem(position);
//
//            }
//
//            @Override
//            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
//                return false;
//            }
//        });

        adapter.setData(mList);
        mEmptyWrapper = new EmptyWrapper(adapter);

        if (tabBean != null) {
            if (tabBean.getData().getType() == ConsultPagerTabItemBean.TYPE_MY_CONSULT) {
                mEmptyLayoutId = R.layout.item_no_data_consult_in_handle;
            }else if (tabBean.getData().getType() == ConsultPagerTabItemBean.TYPE_UNANSWERED) {
                mEmptyLayoutId = R.layout.item_no_data_consult_in_handle;
            }else if (tabBean.getData().getType() == ConsultPagerTabItemBean.TYPE_QUESTIONS) {
                mEmptyLayoutId = R.layout.item_no_data_consult_pool;
            } else if (tabBean.getData().getType() == ConsultPagerTabItemBean.TYPE_ANSWERED) {
                mEmptyLayoutId = R.layout.item_no_data_consult_answered;
                isFromMyAnswer = true;
            }
        } else {
            mEmptyLayoutId = R.layout.item_no_data_consult_answered;
        }

        mLoadMoreWrapper = new CustomRecyclerViewLoadMoreWrapper(mEmptyWrapper, mPharmacyRecordRecyclerView);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //避免上来就加载 只有历史回复是分页的 加载更多view处于加载状态
                if (!mIsLoadingMore
                        && mList.size() > 0
                        && mLoadMoreWrapper.getStatus() == CustomRecyclerViewLoadMoreWrapper.LoadingMoreViewStatus.SHOWLOADING.getFlag()
                        && tabBean.getData().getType() == ConsultPagerTabItemBean.TYPE_ANSWERED) {
                    mPage++;
                    mIsLoadingMore = true;
                    loadData(State.LOADMORE.getFlag());
                } else if(!mIsLoadingMore
                        && mList.size() > 0
                        && mLoadMoreWrapper.getStatus() == CustomRecyclerViewLoadMoreWrapper.LoadingMoreViewStatus.SHOWLOADING.getFlag()
                        && tabBean.getData().getType() == ConsultPagerTabItemBean.TYPE_QUESTIONS){
                    mPage++;
                    mIsLoadingMore = true;
                    loadData(State.LOADMORE.getFlag());
                }
            }
        });
        mPharmacyRecordRecyclerView.setAdapter(mLoadMoreWrapper);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (isFirstLoad) {
            isFirstLoad = false;
//            loadData(State.ONREFRESH.getFlag());
        }




        WebSocketRxBus.registerWebSocketChatMagListener(new EventSubscriber<ChatMsg>() {
            @Override
            public void onNext(Event<ChatMsg> chatMsgEvent) {
                if (isResumed() && isVisible()) {
                    if (ConsultPagerTabItemBean.TYPE_QUESTIONS == tabBean.getData().getType()
                            ||ConsultPagerTabItemBean.TYPE_UNANSWERED == tabBean.getData().getType()) {
                        //stone
                        if (!isFromMyAnswer) {
                            //刷新问题列表
//                            mPage = 1;
                            modifyMsgTip.loadProcessingData();
//                            loadData(State.ONREFRESH.getFlag());
                        }
                    }
                }
//                if (imFileCacheUtil.IM_CACHE_TAG) {
//                    if (imFileCacheUtil.existsDir(chatMsgEvent.getData().getBody().getQid())) {
//                        ConsultChatEntity entity = null;
//                        if (chatMsgEvent.getData().getBody().getType() == ChatMsg.RECV_MSG_TYPE_NORMAL) {
//                            if (chatMsgEvent.getData().getBody().getContent().getType() == ContentType.text) {
//                                entity = ConsultChatEntity.newInstanceText(chatMsgEvent.getData().getBody().getContent().getText(), ConsultChatEntity.MSG_TYPE_RECV);
//                                entity.setDataBean(new QuestionMsgListRspEntity.DataBean.ListBean());
//                                if (!TextUtils.isEmpty(chatMsgEvent.getData().getBody().getUser_photo())) {
//                                    entity.getDataBean().setUser_photo(chatMsgEvent.getData().getBody().getUser_photo());
//                                }
//                                if (TIME!=chatMsgEvent.getData().getBody().getTime()) {
//                                    TIME = chatMsgEvent.getData().getBody().getTime();
//                                    ImCacheAsyncTask mCacheAsyncTaski = new ImCacheAsyncTask();
//                                    mCacheAsyncTaski.addData(entity, chatMsgEvent.getData().getBody().getQid());
//                                    mCacheAsyncTaski.execute();
//
//                                }
//                            } else if (chatMsgEvent.getData().getBody().getContent().getType() == ContentType.image) {
//                                List<String> imgUrls = new ArrayList<>();
//                                imgUrls.add(chatMsgEvent.getData().getBody().getContent().getFile());
//                                entity = ConsultChatEntity.newInstanceImgs(imgUrls);
//                                entity.setMsg_type(ConsultChatEntity.MSG_TYPE_RECV);
//                                entity.setDataBean(new QuestionMsgListRspEntity.DataBean.ListBean());
//                                if (!TextUtils.isEmpty(chatMsgEvent.getData().getBody().getUser_photo())) {
//                                    entity.getDataBean().setUser_photo(chatMsgEvent.getData().getBody().getUser_photo());
//                                }
//                                if (!imageUrl.equals(chatMsgEvent.getData().getBody().getContent().getFile())) {
//                                    imageUrl = chatMsgEvent.getData().getBody().getContent().getFile();
//                                    ImCacheAsyncTask mCacheAsyncTaski = new ImCacheAsyncTask();
//                                    mCacheAsyncTaski.addData(entity, chatMsgEvent.getData().getBody().getQid());
//                                    mCacheAsyncTaski.execute();
//                                }
//                            }
//                        }else{
//                            switch (chatMsgEvent.getData().getBody().getType()) {
//                                case RECV_MSG_TYPE_NIGHT_OFFLINE:
//                                    entity = ConsultChatEntity.newInstanceText(chatMsgEvent.getData().getBody().getContent().getText(), ConsultChatEntity.MSG_TYPE_SEND);
//                                    entity.setDataBean(new QuestionMsgListRspEntity.DataBean.ListBean());
//                                    if (!TextUtils.isEmpty(YMApplication.getLoginInfo().getData().getPhoto())) {
//                                        entity.getDataBean().setUser_photo(YMApplication.getLoginInfo().getData().getPhoto());
//                                    }
//                                    if (TIME!=chatMsgEvent.getData().getBody().getTime()+1) {
//                                        TIME = chatMsgEvent.getData().getBody().getTime()+1;
//                                        ImCacheAsyncTask mCacheAsyncTaski = new ImCacheAsyncTask();
//                                        mCacheAsyncTaski.addData(entity, chatMsgEvent.getData().getBody().getQid());
//                                        mCacheAsyncTaski.execute();
//                                    }
//                                    break;
//                            }
//                        }
//                    }
//                }
            }
        }, mActivity);

        //stone 认领成功刷新列表
        YmRxBus.registerAdoptQuestionListener(new EventSubscriber() {
            @Override
            public void onNext(Object o) {
                //stone 断开自动重连 2018年01月11日13:19:59
                YMOtherUtils.autoConnectWebSocketIfNeed();

                mPage = 1;
                modifyMsgTip.loadProcessingData();
                loadData(State.ONREFRESH.getFlag());
            }
        },mActivity);
    }

    private void loadData(int state) {

        if (tabBean != null) {
            if (tabBean.getData().getType() == ConsultPagerTabItemBean.TYPE_MY_CONSULT||
                    tabBean.getData().getType() == ConsultPagerTabItemBean.TYPE_UNANSWERED) {
//                loadInHandledData(doctorId, state);
            } else if (tabBean.getData().getType() == ConsultPagerTabItemBean.TYPE_QUESTIONS) {
                loadPoolData(mPage,doctorId, state);
            } else if (tabBean.getData().getType() == ConsultPagerTabItemBean.TYPE_ANSWERED) {
                loadAnsweredData(mPage, doctorId, state, sumUp);
            }
        }
    }

    private void loadInHandledData(String doctorId, final int state) {
        StatisticalTools.eventCount(YMApplication.getAppContext(), ConsultConstants.EVENT_MY_CONSULT);
        ServiceProvider.getQuestionsInHandle(doctorId, new Subscriber<QuestionInHandleRspEntity>() {
            @Override
            public void onCompleted() {
                mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条
            }

            @Override
            public void onError(Throwable e) {
                mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条
                mLoadMoreWrapper.loadDataFailed();

                mList.clear();
                adapter.setData(mList);
                mLoadMoreWrapper.notifyDataSetChanged();
            }

            @Override
            public void onNext(QuestionInHandleRspEntity entry) {
                mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条
                mLoadMoreWrapper.loadDataFailed();

                if (entry != null
                        && entry.getData() != null
                        && entry.getData().getList() != null) {
                    if (modifyMsgTip != null) {
                        modifyMsgTip.updateMsgCount(0, entry.getData().getNo_read_total() + "");
                    }
                    int size = entry.getData().getList().size();
                    if (size == 0) {
                        mList.clear();
                        mEmptyWrapper.setEmptyView(mEmptyLayoutId);
                    } else {
                        mList = QuestionsListEntity.getList(entry);
                    }
                    adapter.setData(mList);
                    mLoadMoreWrapper.notifyDataSetChanged();
                } else {
                    if (modifyMsgTip != null) {
                        modifyMsgTip.updateMsgCount(0, "0");
                    }
                    mEmptyWrapper.setEmptyView(mEmptyLayoutId);
                    mList.clear();
                    adapter.setData(mList);
                    mLoadMoreWrapper.notifyDataSetChanged();
                }
            }
        });
    }

    private void loadPoolData(int page, String doctorId, final int state) {
        StatisticalTools.eventCount(YMApplication.getAppContext(), ConsultConstants.EVENT_QUESTION_POOL);
        ServiceProvider.getQuestionsPool(page,30,doctorId, new Subscriber<QuestionsPoolRspEntity>() {
            @Override
            public void onCompleted() {
                mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条
            }

            @Override
            public void onError(Throwable e) {
                mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条
                mLoadMoreWrapper.loadDataFailed();

                mList.clear();
                adapter.setData(mList);
                mLoadMoreWrapper.notifyDataSetChanged();

            }

            @Override
            public void onNext(QuestionsPoolRspEntity entry) {
                if (null != entry) {
                    mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条
                    mLoadMoreWrapper.loadDataFailed();

                    String unreadMsgCount = "0";
                    if (entry != null
                            && entry.getData() != null
                            && entry.getData().getList() != null) {

                        unreadMsgCount = entry.getData().getTotal();
                        if (modifyMsgTip != null) {
                            modifyMsgTip.updateMsgCount(1, unreadMsgCount);
                        }
                        int size = entry.getData().getList().size();

                        if (size == 0) {
                            mList.clear();
                            mEmptyWrapper.setEmptyView(mEmptyLayoutId);
                        } else {
                            mList = QuestionsListEntity.getList(entry);
                        }
                        adapter.setData(mList);
                        mLoadMoreWrapper.notifyDataSetChanged();
                    } else {
                        if (modifyMsgTip != null) {
                            modifyMsgTip.updateMsgCount(1, "0");
                        }
                        mList.clear();
                        adapter.setData(mList);
                        mEmptyWrapper.setEmptyView(mEmptyLayoutId);
                        mLoadMoreWrapper.notifyDataSetChanged();
                    }
                } else {
//                    Toast.makeText(getActivity(),"数据返回异常",Toast.LENGTH_SHORT).show();
                    Log.e("dataError","数据返回异常");
                }
            }

        });
    }

    public void processingData(QuestionInHandleRspEntity entry,int tabPage){
        if (null==entry){
            mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条
            mLoadMoreWrapper.loadDataFailed();

            mList.clear();
            adapter.setData(mList);
            mLoadMoreWrapper.notifyDataSetChanged();
        }else{
            mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条
            mLoadMoreWrapper.loadDataFailed();

            if (entry != null
                    && entry.getData() != null
                    && entry.getData().getList() != null) {
                if (modifyMsgTip != null) {
                    if (tabPage == 1){
                        modifyMsgTip.updateMsgCount(0, entry.getData().getList().size() + "");
                    } else if(tabPage == 2){
                        modifyMsgTip.updateMsgCount(0, entry.getData().getNo_read_total() + "");
                    }
                }
                int size = entry.getData().getList().size();
                if (size == 0) {
                    mList.clear();
                    mEmptyWrapper.setEmptyView(mEmptyLayoutId);
                } else {
                    mList = QuestionsListEntity.getList(entry);
                }
                adapter.setData(mList);
                mLoadMoreWrapper.notifyDataSetChanged();
            } else {
                if (modifyMsgTip != null) {
                    modifyMsgTip.updateMsgCount(0, "0");
                }
                mEmptyWrapper.setEmptyView(mEmptyLayoutId);
                mList.clear();
                adapter.setData(mList);
                mLoadMoreWrapper.notifyDataSetChanged();
            }
        }
    }

    private void loadAnsweredData(final int page, final String doctorId, final int state, int sumUp) {
        Subscriber<QuestionAnsweredLIstRspEntity> mHistorySubscriber = new Subscriber<QuestionAnsweredLIstRspEntity>() {
            @Override
            public void onCompleted() {
                mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条
            }

            @Override
            public void onError(Throwable e) {
                if (state == State.LOADMORE.getFlag()) {
                    mIsLoadingMore = false;
                    mPage--;
                    if (mPage <= 1) {
                        mPage = 1;
                    }
                } else {
                    mList.clear();
                    adapter.setData(mList);
                }
                mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条
                mLoadMoreWrapper.loadDataFailed();
                mLoadMoreWrapper.notifyDataSetChanged();
            }

            @Override
            public void onNext(QuestionAnsweredLIstRspEntity entry) {
                if (null!=entry) {
                    mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条

                    if (entry != null
                            && entry.getData() != null) {
                        int size = entry.getData().size();
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
                                mList.addAll(QuestionsListEntity.getList(entry));
                            }
                            adapter.setData(mList);
                            mLoadMoreWrapper.notifyDataSetChanged();
                        } else {
                            if (size == 0) {
                                mList.clear();
                                mLoadMoreWrapper.loadDataFailed();
                                mEmptyWrapper.setEmptyView(mEmptyLayoutId);
                            } else {
                                mLoadMoreWrapper.showLoadMore();
                                mList = QuestionsListEntity.getList(entry);
                            }
                            adapter.setData(mList);
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
                        adapter.setData(mList);
                        mLoadMoreWrapper.notifyDataSetChanged();
                    }
                }else{
//                    Toast.makeText(getActivity(),"数据返回异常",Toast.LENGTH_SHORT).show();
                    Log.e("dataError","数据返回异常");
                }
            }
        };
        //请求添加个字段 总结与否
        if (sumUp == 1) {
            ServiceProvider.getNotSumupList(String.valueOf(page), doctorId, mHistorySubscriber);
        } else if (sumUp == 2) {
            ServiceProvider.getAnsweredList(String.valueOf(page), doctorId, mHistorySubscriber);
        }
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


    public void setModifyMsgTip(IModifyMsgTip modifyMsgTip) {
        this.modifyMsgTip = modifyMsgTip;
    }

    public interface IModifyMsgTip {
        void updateMsgCount(int tab, String count);
        void loadProcessingData();
    }




    /*备用
     mSwipeRefreshLayout.setRefreshing(false);//隐藏进度条


     String unreadMsgCount = "0";
     if (entry != null
     && entry.getData() != null
     && entry.getData().getList() != null) {
     int size = entry.getData().getList().size();
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
     mList.addAll(QuestionsListEntity.getList(entry));
     }
     adapter.setData(mList);
     mLoadMoreWrapper.notifyDataSetChanged();
     } else {
     if (size == 0) {
     mList.clear();
     mLoadMoreWrapper.loadDataFailed();
     mEmptyWrapper.setEmptyView(mEmptyLayoutId);
     } else {
     mLoadMoreWrapper.showLoadMore();
     mList = QuestionsListEntity.getList(entry);
     }
     adapter.setData(mList);
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
     adapter.setData(mList);
     mLoadMoreWrapper.notifyDataSetChanged();
     }
     */
}
