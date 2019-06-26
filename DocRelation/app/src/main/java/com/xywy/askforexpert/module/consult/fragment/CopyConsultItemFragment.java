package com.xywy.askforexpert.module.consult.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.consultentity.ConsultPagerTabBean;
import com.xywy.askforexpert.model.consultentity.ConsultPagerTabItemBean;
import com.xywy.askforexpert.model.consultentity.QuestionAnsweredLIstRspEntity;
import com.xywy.askforexpert.model.consultentity.QuestionInHandleRspEntity;
import com.xywy.askforexpert.model.consultentity.QuestionsListEntity;
import com.xywy.askforexpert.model.consultentity.QuestionsPoolRspEntity;
import com.xywy.askforexpert.model.websocket.msg.chatmsg.ChatMsg;
import com.xywy.askforexpert.module.consult.ConsultConstants;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.consult.adapter.ConsultListAdapter;
import com.xywy.askforexpert.module.websocket.WebSocketRxBus;
import com.xywy.datarequestlibrary.XywyDataRequestApi;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.uilibrary.fragment.listfragment.XywyPullToRefreshAndLoadMoreFragment;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;
import com.xywy.util.LogUtils;

import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * 我的咨询/问题库(问题列表)/我的回复 stone
 * Created by zhangzheng on 2017/4/26.
 */

public class CopyConsultItemFragment extends XywyPullToRefreshAndLoadMoreFragment {

    private ConsultListAdapter adapter;
    public static final String STR_PARAM = "PARAM";
    public static final String FROM_WTK = "FROM_WTK_OR_ANSWER";
    private ConsultPagerTabBean tabBean;

    private IModifyMsgTip modifyMsgTip;

    //stone 来自问题库或者我的回复问题
    private boolean isFromQuestionLabrary;
    //stone 是否来自我的回复
    private boolean isFromMyAnswer;
    private boolean isLoadingMore;//正在加载更多
    private boolean isFirstLoad = true;//首次加载

//    public static ConsultItemFragment newInstance(ConsultPagerTabBean bean, IModifyMsgTip modifyMsgTip) {
//        ConsultItemFragment fragment = new ConsultItemFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(STR_PARAM, bean);
//        fragment.setArguments(bundle);
//        fragment.setModifyMsgTip(modifyMsgTip);
//        return fragment;
//    }

    //isFromQuestionLabrary 为true时 聊天页面不展示头部的 跳过和转诊 stone
    public static CopyConsultItemFragment newInstance(ConsultPagerTabBean bean, IModifyMsgTip modifyMsgTip, boolean isFromQuestionLabrary) {
        CopyConsultItemFragment fragment = new CopyConsultItemFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(STR_PARAM, bean);
        bundle.putBoolean(FROM_WTK, isFromQuestionLabrary);
        fragment.setArguments(bundle);
        fragment.setModifyMsgTip(modifyMsgTip);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        //stone
        if (!isFromMyAnswer) {
            loadData(null);
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //stone
        if (isFromMyAnswer && isFirstLoad) {
            isFirstLoad = false;
            loadData(null);
        }
        //loadData(null);
        WebSocketRxBus.registerWebSocketChatMagListener(new EventSubscriber<ChatMsg>() {
            @Override
            public void onNext(Event<ChatMsg> chatMsgEvent) {
                if (isResumed() && isVisible()) {
                    if (ConsultPagerTabItemBean.TYPE_QUESTIONS == tabBean.getData().getType()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //stone
                                if (!isFromMyAnswer) {
                                    //刷新问题列表
                                    loadData(null);
                                }
                            }
                        });

                    }
                }
            }
        }, this);
    }

    @Override
    protected XYWYRVMultiTypeBaseAdapter getListRvAdapter() {
        if (adapter == null) {
            adapter = new ConsultListAdapter(getActivity(), isFromQuestionLabrary);
        }
        return adapter;
    }

    @Override
    protected void beforeViewBind() {
        super.beforeViewBind();
        if (getArguments() != null) {
            tabBean = (ConsultPagerTabBean) getArguments().getSerializable(STR_PARAM);
            isFromQuestionLabrary = getArguments().getBoolean(FROM_WTK);
        }
    }

    @Override
    public void initOrRefreshData(final Subscriber<Boolean> subscriber) {
        LogUtils.e("initOrRefreshData");
        loadData(subscriber);
    }

    @Override
    protected void loadMoreData(final Subscriber<Boolean> subscriber) {
        if (isFromMyAnswer) {
            if (!isLoadingMore) {
                isLoadingMore = true;
                curPage++;
                loadData(subscriber);
            }
        } else {
            subscriber.onNext(false);
        }
    }

    @Override
    protected int getNoDataLayoutResId() {
        int rid = R.layout.item_no_data_consult_in_handle;
        if (tabBean == null || tabBean.getData() == null) {

        } else {
            switch (tabBean.getData().getType()) {
                case ConsultPagerTabItemBean.TYPE_QUESTIONS:
                    rid = R.layout.item_no_data_consult_pool;
                    break;
                case ConsultPagerTabItemBean.TYPE_ANSWERED:
                    rid = R.layout.item_no_data_consult_answered;
                    isFromMyAnswer = true;
                    break;
            }
        }
        return rid;
    }

    @Override
    protected void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void unBindView() {
        ButterKnife.unbind(this);
    }

    private void loadData(final Subscriber<Boolean> subscriber) {
        String doctorId = YMApplication.getUUid();
        if (tabBean == null) {
            tabBean = new ConsultPagerTabBean("", new ConsultPagerTabItemBean(ConsultPagerTabItemBean.TYPE_MY_CONSULT, ""));
        }
        if (tabBean.getData().getType() == ConsultPagerTabItemBean.TYPE_MY_CONSULT) {
            loadInHandledData(doctorId, subscriber);
        } else if (tabBean.getData().getType() == ConsultPagerTabItemBean.TYPE_QUESTIONS) {
            loadPoolData(doctorId, subscriber);
        } else if (tabBean.getData().getType() == ConsultPagerTabItemBean.TYPE_ANSWERED) {
            loadAnsweredData(curPage, doctorId, subscriber);
        }
    }


    public IModifyMsgTip getModifyMsgTip() {
        return modifyMsgTip;
    }

    public void setModifyMsgTip(IModifyMsgTip modifyMsgTip) {
        this.modifyMsgTip = modifyMsgTip;
    }

    public interface IModifyMsgTip {
        void updateMsgCount(int tab, String count);
    }

    private void loadInHandledData(String doctorId, final Subscriber<Boolean> subscriber) {
        StatisticalTools.eventCount(YMApplication.getAppContext(), ConsultConstants.EVENT_MY_CONSULT);
        ServiceProvider.getQuestionsInHandle(doctorId, new Subscriber<QuestionInHandleRspEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e("onError");
                if (subscriber != null) {
                    //stone
                    subscriber.onNext(true);
                }
            }

            @Override
            public void onNext(QuestionInHandleRspEntity questionInHandleRspEntity) {
                if (questionInHandleRspEntity != null) {
                    if (questionInHandleRspEntity.getData() == null ||
                            questionInHandleRspEntity.getData().getList() == null ||
                            questionInHandleRspEntity.getData().getList().isEmpty()
                            || questionInHandleRspEntity.getCode() == ConsultConstants.CODE_EMPTY_DATA) {
                        //空数据
                        adapter.getDatas().clear();
                        modifyMsgTip.updateMsgCount(0, "0");
                    } else if (questionInHandleRspEntity.getCode() == ConsultConstants.CODE_REQUEST_SUCCESS) {
                        adapter.setData(QuestionsListEntity.getList(questionInHandleRspEntity));
                        modifyMsgTip.updateMsgCount(0, questionInHandleRspEntity.getData().getNo_read_total() + "");
                        adapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.shortToast(questionInHandleRspEntity.getMsg());
                    }

                }

                if (subscriber != null) {
                    //stone
                    subscriber.onNext(true);
                } else {
                    notifyAdapterDataChanged();
                }
            }
        });
    }

    private void loadPoolData(String doctorId, final Subscriber<Boolean> subscriber) {
        StatisticalTools.eventCount(YMApplication.getAppContext(), ConsultConstants.EVENT_QUESTION_POOL);
        ServiceProvider.getQuestionsPool(1,30,doctorId, new Subscriber<QuestionsPoolRspEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (subscriber != null) {
                    //stone
                    subscriber.onNext(true);
                }
            }

            @Override
            public void onNext(QuestionsPoolRspEntity questionsPoolRspEntity) {
                if (null != questionsPoolRspEntity) {
                    String unreadMsgCount = "0";
                    if (questionsPoolRspEntity.getData() == null || questionsPoolRspEntity.getData().getList() == null ||
                            questionsPoolRspEntity.getData().getList().isEmpty() || questionsPoolRspEntity.getCode() == ConsultConstants.CODE_EMPTY_DATA) {
                        //空数据
                        adapter.getDatas().clear();
                    } else if (questionsPoolRspEntity.getCode() == ConsultConstants.CODE_REQUEST_SUCCESS) {
                        adapter.setData(QuestionsListEntity.getList(questionsPoolRspEntity));
                        unreadMsgCount = questionsPoolRspEntity.getData().getTotal();
                    } else {
                        ToastUtils.shortToast(questionsPoolRspEntity.getMsg());
                    }

                    modifyMsgTip.updateMsgCount(1, unreadMsgCount);
                } else {
//                    Toast.makeText(getActivity(),"数据返回异常",Toast.LENGTH_SHORT).show();
                    Log.e("dataError","数据返回异常");
                }

                if (subscriber != null) {
                    //stone
                    subscriber.onNext(true);
                } else {
                    notifyAdapterDataChanged();
                }
            }
        });
    }

    private void loadAnsweredData(final int page, final String doctorId, final Subscriber<Boolean> subscriber) {
        ServiceProvider.getAnsweredList(String.valueOf(page), doctorId, new Subscriber<QuestionAnsweredLIstRspEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (subscriber != null) {
                    //stone
                    subscriber.onNext(true);
                }

                if (page != 1) {
                    isLoadingMore = false;
                }
            }

            @Override
            public void onNext(QuestionAnsweredLIstRspEntity questionAnsweredLIstRspEntity) {
                if (null != questionAnsweredLIstRspEntity) {
                    if (questionAnsweredLIstRspEntity.getData() == null || questionAnsweredLIstRspEntity.getData().isEmpty()
                            || questionAnsweredLIstRspEntity.getCode() == ConsultConstants.CODE_EMPTY_DATA) {
                        if (page == 1) {
                            adapter.getDatas().clear();
                        }
                    } else if (questionAnsweredLIstRspEntity.getCode() == ConsultConstants.CODE_REQUEST_SUCCESS) {
                        //stone 正在加载下一页时下拉 网络不好情况导致数据重复
                        if (page == 1) {
                            adapter.setData(QuestionsListEntity.getList(questionAnsweredLIstRspEntity));
                        } else if (page == curPage) {
                            adapter.getDatas().addAll(QuestionsListEntity.getList(questionAnsweredLIstRspEntity));
                        }
//                        else {
//                            curPage = 1;
//                            loadAnsweredData(curPage, doctorId, subscriber);
//                        }
                    } else {
                        ToastUtils.shortToast(questionAnsweredLIstRspEntity.getMsg());
                    }
                }

                if (subscriber != null) {
                    //stone
                    subscriber.onNext(true);
                } else {
                    notifyAdapterDataChanged();
                }

                if (page != 1) {
                    isLoadingMore = false;
                }

            }
        });
    }
}
