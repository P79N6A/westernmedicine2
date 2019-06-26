package com.xywy.askforexpert.module.main.service.que;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.base.adapter.RecyclerOnScrollListener;
import com.xywy.askforexpert.appcommon.interfaces.callback.HttpCallback;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.BasePage;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.QuestionSquareMsgItem;
import com.xywy.askforexpert.model.base.BaseBean;
import com.xywy.askforexpert.module.main.service.que.adapter.QueItemAdapter;
import com.xywy.askforexpert.module.main.service.que.constants.QestionTabType;
import com.xywy.askforexpert.module.main.service.que.model.QuestionNote;
import com.xywy.askforexpert.module.main.service.que.model.QuestionSquareBean;
import com.xywy.askforexpert.module.main.service.que.model.QuestionTieBreak;
import com.xywy.askforexpert.module.main.service.que.request.WaitOrRunListReplyRequest;
import com.xywy.base.view.ProgressDialog;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;
import com.xywy.util.T;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.adapter.rxjava.HttpException;

/**
 * 项目名称：D_Platform 类名称：QueListPage 类描述：问题广场列表页 创建人：shihao 创建时间：2015-8-12
 * 下午2:16:41 修改备注：
 */
public class QueListPage extends BasePage {

    private static final String TAG = "QueListPage";
    public static final String STATICACTION = "openDoctorCircle";
    @Bind(R.id.tv_detail_dpart)
    TextView tvDetailDpart;
    @Bind(R.id.tv_cash_reward)
    TextView tv_cash_reward;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_detail_info)
    TextView tvDetailInfo;
    @Bind(R.id.tv_detail_des)
    TextView tvDetailDes;
    @Bind(R.id.next_patient_rl)
    RelativeLayout nextPatientRl;
    @Bind(R.id.talk_patient_rl)
    RelativeLayout talkPatientRl;
    @Bind(R.id.no_data_tv1)
    TextView noDatTv1;
    @Bind(R.id.go_to_btn)
    Button goToBtn;
    @Bind(R.id.que_no_data_layout)
    RelativeLayout queNoDataLayout;
    @Bind(R.id.que_ask_new_layout)
    RelativeLayout queAskNewLayout;
    /**
     * 类型 如:网站问题，手机问题，对我追问，对我提问
     */
    private String typeUrl;
    /**
     * 两大类 ：问题广场 ， 我的回复
     */
    private String isFrom;
    private static final String INTENT_KEY_ISFROM = "isFrom";
    /**
     * 刷新控件
     */
    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;

    private QueItemAdapter queItemAdapter;

    private RelativeLayout rlRobLayout;

    private ImageView ivRobQuestion;

    private RelativeLayout noDataLayout;

    private int page = 1;

    private int pageSize = 10;

    private List<QuestionSquareMsgItem> questionSquareMsgItemList = new ArrayList<QuestionSquareMsgItem>();

    private List<QuestionSquareMsgItem> myReplyDatas = new ArrayList<QuestionSquareMsgItem>();
    private List<QuestionNote> myWaitReplyDatas = new ArrayList<QuestionNote>();

    /**
     * 是否开始
     */
    private boolean mIsStart;
    /**
     * 是否有更多数据
     */
    private boolean isMore;
    /**
     * 是否在刷新
     */
    private boolean mIsRefreshing = false;

    private int index;

    private SharedPreferences showPreferences;

    private ProgressDialog robDialog;

    public String articleId = "";

    public interface OnJumpCircleIntentListener {
        void goToCircle();
    }

    public OnJumpCircleIntentListener jumpCircleIntentListener;

    public void setOnJumpCircleListener(OnJumpCircleIntentListener jumpCircleIntentListener) {
        this.jumpCircleIntentListener = jumpCircleIntentListener;
    }

    private static final String WAITREPLY = "waitreply";//处理中
    private static final String RUNLIST = "runlist";//问题库
    private static final String INTENT_KEY_Q_TYPE = "q_type";//1 指定付费 2 悬赏 3绩效
    private static final String Q_TYPE_2 = "2";//  悬赏
    private static final String TYPE = "ques";// 提问

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            if (msg.what == 1) {
//
//                if (mIsStart) {
//                    queItemAdapter.bindItemData(myReplyDatas);
//                    recyclerView.setAdapter(queItemAdapter);
//                } else {
//                    queItemAdapter.bindItemData(myReplyDatas);
//                }
//
//                queItemAdapter.setOnItemClickListener(new OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(int position, Object object) {
//                        if (typeUrl.equals("del")) {
//                            ToastUtils.shortToast("该问题已删除");
//                        } else {
//                            Intent intent = new Intent(context,
//                                    QueDetailActivity.class);
//                            intent.putExtra("tag", isFrom);
//                            intent.putExtra("type", typeUrl);
//                            intent.putExtra("index", index);
//                            intent.putExtra("id",
//                                    ((QuestionSquareMsgItem) object).getqId()
//                                            + "");
//                            context.startActivity(intent);
//
//
//                        }
//                    }
//                });
//            }
//            if (msg.what == 2) {
//                if (mIsStart) {
//                    queItemAdapter.bindItemData(questionSquareMsgItemList);
//                    recyclerView.setAdapter(queItemAdapter);
//                } else {
//                    queItemAdapter.bindItemData(questionSquareMsgItemList);
//                }
//
//                queItemAdapter
//                        .setOnItemClickListener(new OnItemClickListener() {
//
//                            @Override
//                            public void onItemClick(int position, Object object) {
//                                DLog.i(TAG, "POSITION" + position + "id==" + ((QuestionSquareMsgItem) object).getId());
//                                requestHttpData(typeUrl, isFrom, ((QuestionSquareMsgItem) object).getId()
//                                        + "", index, position, 2);
//
//                            }
//                        });
//            }
        }
    };
    private SwipeRefreshLayout swipeQueLayout;

    public QueListPage(Context context, String typeUrl, String isFrom, int index) {
        super(context);
        this.typeUrl = typeUrl;
        this.isFrom = isFrom;
        this.index = index;
    }

    @Override
    public View initView(LayoutInflater inflater) {
        DLog.i(TAG, "QueListPage---initView");
        View view = inflater.inflate(R.layout.que_fragment_item, null);
        ButterKnife.bind(this, view);
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            goToBtn.setVisibility(View.GONE);
            noDatTv1.setVisibility(View.GONE);
        }
        showPreferences = context.getSharedPreferences("msg_manager",
                Context.MODE_PRIVATE);

        swipeRefreshLayout = (SwipeRefreshLayout) view
                .findViewById(R.id.swipe_refresh_layout);

        swipeQueLayout = (SwipeRefreshLayout) view
                .findViewById(R.id.swipe_que_layout);

        swipeRefreshLayout.setClipChildren(true);

        swipeRefreshLayout.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mIsRefreshing;
            }
        });
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST);
        itemDecoration.setDividerDrawble(context.getResources().getDrawable(R.drawable.item_divider_10dp));
        recyclerView.addItemDecoration(itemDecoration);
        rlRobLayout = (RelativeLayout) view.findViewById(R.id.rl_rob_question);

        ivRobQuestion = (ImageView) view.findViewById(R.id.iv_rob_question);

        noDataLayout = (RelativeLayout) view.findViewById(R.id.rl_no_data);

        //刷新列表
        YmRxBus.registerPushEnterExitListener(new EventSubscriber() {
            @Override
            public void onNext(Object o) {
                if (index == 0 && !TextUtils.isEmpty(typeUrl) && typeUrl.equals(WAITREPLY)) {
                    mIsStart = true;
                    isMore = true;
                    mIsRefreshing = true;
                    requestData();
                }
            }
        }, context);


        return view;
    }

    @Override
    public void initData() {

        DLog.i(TAG, "QueListPage--INITdata" + "type==" + typeUrl + "index==" + index);
        mIsStart = true;
        isMore = true;
        if (typeUrl.equals("mobileDetail")) {
            swipeRefreshLayout.setVisibility(View.GONE);
            swipeQueLayout.setVisibility(View.GONE);
            rlRobLayout.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
        } else if (QestionTabType.MWARD.equals(typeUrl)) {
            swipeQueLayout.setVisibility(View.VISIBLE);
            queAskNewLayout.setVisibility(View.GONE);
            queNoDataLayout.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.GONE);
            rlRobLayout.setVisibility(View.GONE);

            swipeQueLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mIsStart = true;
                    isMore = true;
                    mIsRefreshing = true;
                    requestData();
                }
            });
        } else if ("unreply".equals(typeUrl)) {
            swipeQueLayout.setVisibility(View.VISIBLE);
            queAskNewLayout.setVisibility(View.GONE);
            queNoDataLayout.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.GONE);
            rlRobLayout.setVisibility(View.GONE);
        } else if (RUNLIST.equals(typeUrl)) {
            swipeRefreshLayout.setVisibility(View.GONE);
            rlRobLayout.setVisibility(View.VISIBLE);
            swipeQueLayout.setVisibility(View.GONE);
        } else {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            rlRobLayout.setVisibility(View.GONE);
            swipeQueLayout.setVisibility(View.GONE);
        }

        tv_cash_reward.setVisibility(QestionTabType.MWARD.equals(typeUrl) ? View.VISIBLE : View.GONE);

        queItemAdapter = new QueItemAdapter(context);
        queItemAdapter.setOnItemClickListener(new QueItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                QuestionNote data = (QuestionNote) object;
                isFrom = WAITREPLY;//从处理中的帖子列表跳转
                forwordToQueDetailActivity(isFrom, data.qid, data.type, data.q_type);
            }
        });

//        // 关闭按钮
//        if (!showPreferences.getBoolean("close_que", true)) {
//            noDataLayout.setVisibility(View.VISIBLE);
//        } else {
//            requestData();
//            noDataLayout.setVisibility(View.GONE);
//        }

        // 关闭按钮
        if (!showPreferences.getBoolean("close_que", true)) {
            noDataLayout.setVisibility(View.VISIBLE);
        } else if (RUNLIST.equals(typeUrl)) {
            noDataLayout.setVisibility(View.GONE);
        } else {
            requestData();
            noDataLayout.setVisibility(View.GONE);
        }

        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                mIsStart = true;
                isMore = true;
                mIsRefreshing = true;
                if (typeUrl.equals("asktome")) {
                    YMApplication.getInstance().remoNotification(1);
                }
                if (typeUrl.equals("zhuiwen")) {
                    YMApplication.getInstance().remoNotification(2);
                }
                if (typeUrl.equals("edit")) {
                    YMApplication.getInstance().remoNotification(3);
                }

                requestData();
            }
        });

        recyclerView.setOnScrollListener(new RecyclerOnScrollListener(
                linearLayoutManager) {

            @Override
            public void onLoadMore() {
//                if (isMore) {
//                    queItemAdapter.notifyDataSetChanged();
//                }
//                queItemAdapter.setCanLoadMore(true);
//                new Handler().postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        mIsStart = false;
//                        requestData();
//                    }
//                }, 1000);
            }

            @Override
            public void onScrolling() {

            }
        });

        ivRobQuestion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StatisticalTools.eventCount(context, "consultinglistquestionsquare");
//                getQueId();
                requestData();
            }

        });
    }

    private void forwordToQueDetailActivity(String isFrom, String qid, String type, String q_type) {
        Intent intent = new Intent(context, QueDetailActivity.class);
        intent.putExtra("tag", "other");
        intent.putExtra(INTENT_KEY_ISFROM, isFrom);
        intent.putExtra("type", type);
        intent.putExtra(INTENT_KEY_Q_TYPE, q_type);
        intent.putExtra("id", qid);
        context.startActivity(intent);
    }

    private void getQueId() {
        robDialog = new ProgressDialog(context, "正在加载中...");
        robDialog.showProgersssDialog();
        AjaxParams params = new AjaxParams();
        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put("tag", "mobile");
        params.put("sign", MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid()
                + "mobile" + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.DP_COMMON + "command=mobileDetail", params,
                new AjaxCallBack() {

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        robDialog.closeProgersssDialog();
                    }

                    @Override
                    public void onLoading(long count, long current) {
                        super.onLoading(count, current);
                    }

                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        robDialog.closeProgersssDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(t);
                            int code = jsonObject.getInt("code");
                            String msg = jsonObject.getString("msg");
                            if (code == 0) {
                                articleId = jsonObject.getString("data");
                                Intent intent = new Intent(context, QueDetailActivity.class);
                                intent.putExtra("tag", "other");
                                intent.putExtra("type", typeUrl);
                                intent.putExtra("id", articleId);
                                context.startActivity(intent);
                            } else {
                                ToastUtils.shortToast(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * 初始化数据
     */
    private void requestData() {
//        if (mIsStart) {
//            page = 1;
//            getHttpData(page, mIsStart, isFrom);
//        } else {
//            page++;//这里逻辑是有问题的?????????
//            if (isMore) {
//                getHttpData(page, mIsStart, isFrom);
//                queItemAdapter.setCanLoadMore(true);
//            } else {
//                queItemAdapter.setCanLoadMore(false);
//                queItemAdapter.notifyDataSetChanged();
//            }
//        }

        if (WAITREPLY.equals(typeUrl)) {
            getWaitReply();
        } else if (RUNLIST.equals(typeUrl)) {
            getRunList();
        }
    }

    private void getRunList() {
        WaitOrRunListReplyRequest.getInstance().getRunList(typeUrl, YMUserService.getCurUserId()).subscribe(new BaseRetrofitResponse<BaseBean<QuestionTieBreak>>() {
            @Override
            public void onStart() {
                super.onStart();
                robDialog = new ProgressDialog(context, "正在加载中...");
                robDialog.showProgersssDialog();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                if (null != robDialog && robDialog.isShowing()) {
                    robDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (null != robDialog && robDialog.isShowing()) {
                    robDialog.dismiss();
                }
                if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
                    Toast.makeText(RetrofitClient.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    T.showShort(RetrofitClient.getContext(), e.getMessage());
                }
//                getRunList();
            }

            @Override
            public void onNext(BaseBean<QuestionTieBreak> questionTieBreakBean) {
                super.onNext(questionTieBreakBean);
                QuestionTieBreak data = questionTieBreakBean.getData();
                if (null != data) {
                    isFrom = RUNLIST;//从处理中的帖子列表跳转
                    forwordToQueDetailActivity(isFrom, data.id, TYPE, Q_TYPE_2);
                }
            }
        });
    }

    private void getWaitReply() {
        WaitOrRunListReplyRequest.getInstance().getWaitReply(typeUrl, "list", YMUserService.getCurUserId()).subscribe(new BaseRetrofitResponse<BaseBean<QuestionSquareBean>>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                swipeRefreshLayout.setRefreshing(false);
                mIsRefreshing = false;
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                swipeRefreshLayout.setRefreshing(false);
                mIsRefreshing = false;
            }

            @Override
            public void onNext(BaseBean<QuestionSquareBean> questionSquareBeanBaseBean) {
                super.onNext(questionSquareBeanBaseBean);
                if (mIsStart) {
                    myWaitReplyDatas.clear();
                    if (null != questionSquareBeanBaseBean) {
                        QuestionSquareBean data = questionSquareBeanBaseBean.getData();
                        if (null != data.list) {

                            if (data.list.size() == 0) {
                                noDataLayout.setVisibility(View.VISIBLE);
                                queItemAdapter.notifyDataSetChanged();
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                myWaitReplyDatas.addAll(data.list);
                                noDataLayout.setVisibility(View.GONE);
                                queItemAdapter.bindItemData(myWaitReplyDatas);
                                recyclerView.setAdapter(queItemAdapter);
                            }
                            mRedNumChangListener.isShowRedPoint(myWaitReplyDatas.size(), typeUrl);
                            YmRxBus.notifyUnReadMsgCount(myWaitReplyDatas.size());
                        }
                    }
                } else {
                    if (null != questionSquareBeanBaseBean) {
                        QuestionSquareBean data = questionSquareBeanBaseBean.getData();
                        if (null != data.list) {
                            myWaitReplyDatas.addAll(data.list);
                            queItemAdapter.notifyDataSetChanged();
                            mRedNumChangListener.isShowRedPoint(myWaitReplyDatas.size(), typeUrl);
                            YmRxBus.notifyUnReadMsgCount(myWaitReplyDatas.size());
                        }
                    }
                }

            }
        });

    }

    private void getHttpData(int page, final boolean mIsStart, String ifFrom) {
        AjaxParams params = new AjaxParams();
        FinalHttp fh = new FinalHttp();
        if (isFrom.equals("myreply")) {
            params.put("userid", YMApplication.getLoginInfo().getData().getPid());
            params.put("act", typeUrl);
            params.put("page", page + "");
            params.put("pagesize", pageSize + "");
            params.put("sign", MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid()
                    + typeUrl + Constants.MD5_KEY));
            DLog.d("my reply", "my reply url = " + (CommonUrl.QUE_MY_REPLY + "?" + params.toString()));
            fh.post(CommonUrl.QUE_MY_REPLY, params, new AjaxCallBack() {

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    DLog.d("my reply", "my reply = " + t);
                    parseJsonMyReplyData(t);
                }
            });
        } else {
            if ("mobileDetail".equals(typeUrl)) {
                return;
            }
            params.put("userid", YMApplication.getLoginInfo().getData().getPid());
            params.put("act", typeUrl);
            params.put("sign", MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid()
                    + typeUrl + Constants.MD5_KEY));
            //标记
            if ("unreply".equals(typeUrl) || QestionTabType.MWARD.equals(typeUrl)) {
                DLog.i(TAG, CommonUrl.QUE_NEW_DETAIL + "?" + params.toString());
                fh.post(CommonUrl.QUE_NEW_DETAIL, params, new AjaxCallBack() {
                    @Override
                    public void onSuccess(String s) {
                        super.onSuccess(s);
                        DLog.i(TAG, "que详情" + s);
                        if (TextUtils.isEmpty(s)) {
                            LogUtils.e("服务端无返回数据");
                            return;
                        }

                        if (mIsStart) {
                            swipeQueLayout.setRefreshing(false);
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                JSONObject jsonElement = jsonObject.getJSONObject("data");
                                queAskNewLayout.setVisibility(View.VISIBLE);
                                queNoDataLayout.setVisibility(View.GONE);
                                QuestionSquareMsgItem data = new QuestionSquareMsgItem();
                                data.setSubjectName(jsonElement.getString("subject_name"));
                                data.setSex(jsonElement.getString("sex"));
                                data.setAge(jsonElement.getString("age"));
                                data.setTitle(jsonElement.getString("title"));
                                data.setDetail(jsonElement.getString("detail_1"));
                                data.setState(jsonElement.getString("detail_2"));
                                data.setHelp(jsonElement.getString("detail_3"));
                                data.setAwardMoney(jsonElement.optString("givepoint"));
                                data.setQues_from(jsonElement.optString("ques_from"));
                                articleId = jsonElement.getString("id");
                                tvDetailDpart.setText(data.getSubjectName() + "");
                                tvDetailInfo.setText(data.getSex() + "   " + data.getAge());
                                tvDetailDes.setText(data.getDetail());
                                String title = TextUtils.isEmpty(data.getTitle()) ? "标    题：" : "标    题：" + data.getTitle();
                                tv_title.setText(title);
                                tv_cash_reward.setText(TextUtils.isEmpty(data.getAwardMoney()) ? "" : "现金悬赏：" + data.getAwardMoney() + "元");
                            } else {
                                queNoDataLayout.setVisibility(View.VISIBLE);
                                queAskNewLayout.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            queNoDataLayout.setVisibility(View.VISIBLE);
                            queAskNewLayout.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        queNoDataLayout.setVisibility(View.VISIBLE);
                        queAskNewLayout.setVisibility(View.GONE);
                        swipeQueLayout.setRefreshing(false);
                    }
                });
            } else {
                params.put("page", page + "");
                params.put("pagesize", pageSize + "");
                DLog.i(TAG, CommonUrl.QUE_CONTENT_LIST + "?" + params.toString());
                fh.post(CommonUrl.QUE_CONTENT_LIST, params,
                        new AjaxCallBack() {
                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                super.onFailure(t, errorNo, strMsg);
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onLoading(long count, long current) {
                                super.onLoading(count, current);
                            }

                            @Override
                            public void onSuccess(String t) {
                                super.onSuccess(t);
                                parseJsonData(t);
                            }
                        });
            }
        }
    }

    /**
     * 我的回复
     *
     * @param json
     */
    private void parseJsonMyReplyData(String json) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            if (code == 0) {
                if (mIsStart) {
                    myReplyDatas.clear();
                    swipeRefreshLayout.setRefreshing(false);
                }
                JSONObject jsonElement = jsonObject.getJSONObject("data");
                int hasData = jsonElement.getInt("more");
                isMore = hasData == 1;
                JSONArray array = jsonElement.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonChild = array.getJSONObject(i);
                    QuestionSquareMsgItem queData = new QuestionSquareMsgItem();
                    queData.setId(jsonChild.getInt("id"));
                    queData.setqId(jsonChild.getInt("qid"));
                    queData.setTitle(jsonChild.getString("title"));
                    queData.setSex(jsonChild.getString("sex"));
                    queData.setAge(jsonChild.getString("age"));
                    queData.setCreateTime(jsonChild.getString("createtime"));
                    queData.setQues_from(jsonChild.optString("ques_from"));
                    queData.setAwardMoney(jsonChild.optString("givepoint"));
                    myReplyDatas.add(queData);
                }
                if (mIsStart) {
                    if (myReplyDatas.size() >= 10) {
                        queItemAdapter.setUseFooter(true);
                    } else {
                        queItemAdapter.setUseFooter(false);
                    }
                }
                if (typeUrl.equals("edit")) {
                    mRedNumChangListener.isShowRedPoint(myReplyDatas.size(), typeUrl);
                }
                if (myReplyDatas.size() > 0) {
                    noDataLayout.setVisibility(View.GONE);
                    Message msgReply = Message.obtain();
                    msgReply.what = 1;
                    handler.sendMessage(msgReply);
                } else {
                    noDataLayout.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                }
                mIsRefreshing = false;
            } else {
                ToastUtils.shortToast(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 问题广场
     *
     * @param json
     */
    private void parseJsonData(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            if (code == 0) {
                if (mIsStart) {
                    questionSquareMsgItemList.clear();
                    swipeRefreshLayout.setRefreshing(false);
                }
                JSONObject jsonElement = jsonObject.getJSONObject("data");
                int hasData = jsonElement.getInt("more");
                isMore = hasData == 1;
                JSONArray array = jsonElement.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonChild = array.getJSONObject(i);
                    QuestionSquareMsgItem queData = new QuestionSquareMsgItem();
                    queData.setId(jsonChild.getInt("id"));
                    queData.setTitle(jsonChild.getString("title"));
                    queData.setSex(jsonChild.getString("sex"));
                    queData.setAge(jsonChild.getString("age"));
                    queData.setQues_from(jsonChild.optString("ques_from"));
                    queData.setAwardMoney(jsonChild.optString("givepoint"));
                    queData.setCreateTime(jsonChild.getString("createtime"));
                    questionSquareMsgItemList.add(queData);
                }
                if (mIsStart) {
                    if (questionSquareMsgItemList.size() > 8) {
                        queItemAdapter.setUseFooter(true);
                    } else {
                        queItemAdapter.setUseFooter(false);
                    }
                }

                if (typeUrl.equals("asktome") || typeUrl.equals("zhuiwen")) {//||type.equals("zhuiwen")
                    Log.i(TAG, "G=RE" + questionSquareMsgItemList.size());
                    mRedNumChangListener.isShowRedPoint(questionSquareMsgItemList.size(), typeUrl);
                }
                if (questionSquareMsgItemList.size() > 0) {
                    noDataLayout.setVisibility(View.GONE);
                    Message msgQue = Message.obtain();
                    msgQue.what = 2;
                    handler.sendMessage(msgQue);
                } else {
                    noDataLayout.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                }
                mIsRefreshing = false;
            } else {
                ToastUtils.shortToast(msg);
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    @OnClick({R.id.next_patient_rl, R.id.talk_patient_rl, R.id.go_to_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_patient_rl:
                queSkip(articleId, typeUrl);
                if (QestionTabType.MWARD.equals(typeUrl)) {
                    StatisticalTools.eventCount(context, "RewardQuestionNextPatient");
                } else {
                    MobclickAgent.onEvent(context, "NextPatient");
                }
                break;
            case R.id.talk_patient_rl:
                if (QestionTabType.MWARD.equals(typeUrl)) {
                    StatisticalTools.eventCount(context, "RewardQuestionCommunicate");
                } else {
                    StatisticalTools.eventCount(context, "CommunicationWithPatients");
                }

                Intent intent = new Intent(context,
                        QueDetailActivity.class);
                intent.putExtra("tag", isFrom);
                intent.putExtra("type", typeUrl);
                intent.putExtra("id", articleId);
                context.startActivity(intent);
                break;
            case R.id.go_to_btn:
                MobclickAgent.onEvent(context, "GoYq");
                ((Activity) context).finish();
                Intent goToCircle = new Intent();
                goToCircle.setAction(STATICACTION);
                goToCircle.putExtra("msg", "接收静态注册广播成功！");
                context.sendBroadcast(goToCircle);
                break;
        }
    }

    public RedNumChangListener mRedNumChangListener;

    public void setmRedNumChangListener(RedNumChangListener mRedNumChangListener) {
        this.mRedNumChangListener = mRedNumChangListener;
    }

    /**
     * 跳过
     */
    private void queSkip(String id, String type) {

        QuestionService.skip(id, type, new HttpCallback() {
            @Override
            public void onSuccess(Object obj) {
                if (null == obj || !(obj instanceof String)) {
                    //返回异常
                    ToastUtils.shortToast("返回值异常");
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject((String) obj);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 0) {
                        getHttpData(page, true, isFrom);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(Throwable throwable, int errorNo, String strMsg) {
                ToastUtils.shortToast("请求异常");
            }
        });
    }

    private void requestHttpData(final String type, String tag, final String id, final int index, final int position, final int msgWhat) {

        AjaxParams params = new AjaxParams();
        String userid = YMApplication.getLoginInfo().getData().getPid();
        params.put("userid", userid);

        if (tag.equals("myreply")) {
            params.put("isReply", "1");
            params.put("id", id);
        } else {
            params.put("isReply", "0");
            params.put("id", id);
        }

        params.put("act", type);

        params.put("sign",
                MD5Util.MD5(userid + id + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();

        DLog.i(TAG, "QUE" + CommonUrl.QUE_CONTENT_DETAIL + "?" + params);
        fh.post(CommonUrl.QUE_CONTENT_DETAIL, params,
                new AjaxCallBack() {
                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                    }

                    @Override
                    public void onLoading(long count, long current) {

                        super.onLoading(count, current);
                    }

                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        Log.i(TAG, t);

                        try {
                            JSONObject jsonObject = new JSONObject(t);
                            int code = jsonObject.getInt("code");
                            String msg = jsonObject.getString("msg");
                            if (code == 0) {
                                if (msgWhat == 1) {
                                    Intent intent = new Intent(context, QueDetailActivity.class);
                                    intent.putExtra("index", index);
                                    intent.putExtra("type", type);
                                    intent.putExtra("tag", isFrom);
                                    intent.putExtra("id", id + "");
                                    context.startActivity(intent);
                                }
                                if (msgWhat == 2) {
                                    Intent intent = new Intent(context,
                                            QueDetailActivity.class);
                                    intent.putExtra("index", index);
                                    if (type.equals("zhuiwen")) {
                                        intent.putExtra("tag", "zhuiwen");
                                    } else if (type.equals("expert_zw")) {
                                        intent.putExtra("tag", "expert_zw");
                                    } else {
                                        intent.putExtra("tag", "other");
                                    }
                                    intent.putExtra("type", type);
                                    intent.putExtra("position", position);
                                    intent.putExtra("id",
                                            id + "");
                                    context.startActivity(intent);
                                }
                            } else {
                                ToastUtils.shortToast(msg);
                            }


                        } catch (IndexOutOfBoundsException e) {
                            Log.d("queitemfragment", e.toString());
                        } catch (Exception e) {

                        }
                    }
                });
    }


}
