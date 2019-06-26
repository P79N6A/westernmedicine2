//package com.xywy.askforexpert.module.main.service;
//
//import android.content.Intent;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.xywy.askforexpert.R;
//import com.xywy.askforexpert.YMApplication;
//import com.xywy.askforexpert.appcommon.YMUserService;
//import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
//import com.xywy.askforexpert.appcommon.base.adapter.RecyclerOnScrollListener;
//import com.xywy.askforexpert.appcommon.net.CommonUrl;
//import com.xywy.askforexpert.appcommon.old.Constants;
//import com.xywy.askforexpert.appcommon.utils.GsonUtils;
//import com.xywy.askforexpert.appcommon.utils.ToastUtils;
//import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
//import com.xywy.askforexpert.appcommon.utils.others.DLog;
//import com.xywy.askforexpert.model.main.HistoryListPageBean;
//import com.xywy.askforexpert.module.main.service.que.QueDetailActivity;
//import com.xywy.askforexpert.module.main.service.que.adapter.QueItemAdapter;
//import com.xywy.askforexpert.module.main.service.que.model.QuestionNote;
//import com.xywy.askforexpert.widget.ActionItem;
//import com.xywy.askforexpert.widget.TitlePopup;
//import com.xywy.base.view.ProgressDialog;
//import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;
//
//import net.tsz.afinal.FinalHttp;
//import net.tsz.afinal.http.AjaxCallBack;
//import net.tsz.afinal.http.AjaxParams;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by xugan on 2017/12/22. stone
// * 历史回复 问题广场
// */
//
//public class CopyHistoryReplyActivity extends YMBaseActivity implements TitlePopup.OnItemOnClickListener, View.OnClickListener {
//    /**
//     * 刷新控件
//     */
//    private SwipeRefreshLayout mSwipeRefreshLayout;
//
//    private RecyclerView mRecyclerView;
//
//    private LinearLayoutManager mLinearLayoutManager;
//    /**
//     * 是否开始
//     */
//    private boolean mIsStart;
//    /**
//     * 是否有更多数据
//     */
//    private boolean isMore;
//    /**
//     * 是否在刷新
//     */
//    private boolean mIsRefreshing = false;
//    /**
//     * 是否隐藏dialogue
//     */
//    private boolean mHideDialogue = false;
//
//    private String mAct;//最近帖子 1：一周 2：一个月 3：两个月
//
//    private RelativeLayout mNoDataLayout;
//    private TextView mNoDataTv;
//
//    private int page = 1;
//
//    private int pageSize = 10;
//
//    private QueItemAdapter mQueItemAdapter;
//    private List<QuestionNote> myReplyDatas = new ArrayList<QuestionNote>();
//    private static final String INTENT_KEY_ISFROM = "isFrom";
//    private static final String MYREPLY = "myreply";//历史回复
//    private static final String INTENT_KEY_Q_TYPE = "q_type";//1 指定付费 2 悬赏 3绩效
//    private TitlePopup menuPopup;
//    private static final String WEEK = "近一周";
//    private static final String MONTH = "近一个月";
//    private static final String TWO_MONTH = "近两个月";
//    private ProgressDialog mDialog;
//    private static final String TYPEURL = "";
//    private Handler handler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 1) {
//
//                if (mIsStart) {
//                    mQueItemAdapter.bindItemData(myReplyDatas);
//                    mRecyclerView.setAdapter(mQueItemAdapter);
//                } else {
//                    mQueItemAdapter.bindItemData(myReplyDatas);
//                }
//
//                mQueItemAdapter.setOnItemClickListener(new QueItemAdapter.OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(int position, Object object) {
//                        if (TYPEURL.equals("del")) {
//                            ToastUtils.shortToast("该问题已删除");
//                        } else {
//                            Intent intent = new Intent(CopyHistoryReplyActivity.this,
//                                    QueDetailActivity.class);
//                            QuestionNote data = (QuestionNote) object;
//                            intent.putExtra("tag", "myreply");
//                            intent.putExtra("type", data.type);
//                            intent.putExtra("index", 0);
//                            intent.putExtra(INTENT_KEY_ISFROM, MYREPLY);//从历史回复的帖子列表跳转
//                            intent.putExtra("id", data.qid + "");
//                            startActivity(intent);
//
//
//                            intent.putExtra(INTENT_KEY_Q_TYPE, data.q_type);
//                        }
//                    }
//                });
//            }
//        }
//    };
//
//    @Override
//    protected void initView() {
//        hideCommonBaseTitle();
//        mDialog = new ProgressDialog(this, "正在加载中，请稍后...");
//        mDialog.showProgersssDialog();
//        mNoDataLayout = (RelativeLayout) findViewById(R.id.que_no_data_layout);
//        mNoDataTv = (TextView) findViewById(R.id.no_data_tv);
//        ImageView back = (ImageView) findViewById(R.id.back);
//        back.setOnClickListener(this);
//        ImageButton date = (ImageButton) findViewById(R.id.date);
//        date.setOnClickListener(this);
//        menuPopup = new TitlePopup(this, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        menuPopup.setItemOnClickListener(this);
//
//        menuPopup.cleanAction();
//        menuPopup.addAction(new ActionItem(this, WEEK));
//        menuPopup.addAction(new ActionItem(this, MONTH));
//        menuPopup.addAction(new ActionItem(this, TWO_MONTH));
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
////        mSwipeRefreshLayout.setClipChildren(true);
//
////        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_scheme_2_1,
////                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
////                R.color.color_scheme_2_4);
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return mIsRefreshing;
//            }
//        });
//        mLinearLayoutManager = new LinearLayoutManager(this);
//        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
//        itemDecoration.setDividerDrawble(getResources().getDrawable(R.drawable.item_divider_10dp));
//        mRecyclerView.addItemDecoration(itemDecoration);
//        mRecyclerView.setLayoutManager(mLinearLayoutManager);
//        mQueItemAdapter = new QueItemAdapter(this);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//                mIsStart = true;
//                isMore = true;
//                mIsRefreshing = true;
//                mHideDialogue = true;
//                requestData(mAct);
//            }
//        });
//
//        mRecyclerView.setOnScrollListener(new RecyclerOnScrollListener(
//                mLinearLayoutManager) {
//
//            @Override
//            public void onLoadMore() {
//                mHideDialogue = true;
//                if (isMore) {
//                    mQueItemAdapter.notifyDataSetChanged();
//                }
//                mQueItemAdapter.setCanLoadMore(true);
//                new Handler().postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        mIsStart = false;
//                        requestData(mAct);
//                    }
//                }, 1000);
//            }
//
//            @Override
//            public void onScrolling() {
//
//            }
//        });
//    }
//
//    @Override
//    protected void initData() {
//        mIsStart = true;
//        isMore = true;
//        mIsRefreshing = true;
//        // TODO: 2018/4/12 测试stone 正常是1   测试用3
//        mAct = "3";
//        requestData(mAct);
//    }
//
//    /**
//     * 初始化数据
//     */
//    private void requestData(String act) {
//        if (mIsStart) {
//            page = 1;
//            getHistoryReply(page, act, mIsStart);
//        } else {
//            page++;
//            if (isMore) {
//                getHistoryReply(page, act, mIsStart);
//                mQueItemAdapter.setCanLoadMore(true);
//            } else {
//                mQueItemAdapter.setCanLoadMore(false);
//                mQueItemAdapter.notifyDataSetChanged();
//            }
//        }
//    }
//
//    private void getHistoryReply(int page, String act, boolean mIsStart) {
//        AjaxParams params = new AjaxParams();
//        FinalHttp fh = new FinalHttp();
//        params.put("userid", YMUserService.getCurUserId());
//        params.put("act", act);
//        params.put("page", page + "");
//        params.put("pagesize", pageSize + "");
//        params.put("sign", MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid() + Constants.MD5_KEY));
//        DLog.d("my reply", "my reply url = " + (CommonUrl.QUE_MY_REPLY + "?" + params.toString()));
//        fh.post(CommonUrl.QUE_MY_REPLY, params, new AjaxCallBack() {
//
//            @Override
//            public void onStart() {
//                super.onStart();
//                if (null != mDialog && !mHideDialogue) {
//                    mDialog.show();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t, int errorNo, String strMsg) {
//                super.onFailure(t, errorNo, strMsg);
//                mSwipeRefreshLayout.setRefreshing(false);
//                if (null != mDialog && mDialog.isShowing() && !mHideDialogue) {
//                    mDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onSuccess(String t) {
//                super.onSuccess(t);
//                if (null != mDialog && mDialog.isShowing() && !mHideDialogue) {
//                    mDialog.dismiss();
//                }
//                DLog.d("my reply", "my reply = " + t);
//                parseJsonMyReplyData(t);
//            }
//        });
//    }
//
//    /**
//     * 我的回复
//     *
//     * @param json
//     */
//    private void parseJsonMyReplyData(String json) {
//
//
//        if (TextUtils.isEmpty(json)) {
//            return;
//        }
//        HistoryListPageBean bean = GsonUtils.toObj(json, HistoryListPageBean.class);
//        if (bean != null && bean.data != null) {
//            if (bean.code == 0) {
//                if (mIsStart) {
//                    myReplyDatas.clear();
//                    mSwipeRefreshLayout.setRefreshing(false);
//                }
//                isMore = bean.data.more == 1;
//                if (bean.data.data != null && bean.data.data.size() > 0) {
//                    myReplyDatas.addAll(bean.data.data);
//                }
//                if (mIsStart) {
//                    if (myReplyDatas.size() >= 10) {
//                        mQueItemAdapter.setUseFooter(true);
//                    } else {
//                        mQueItemAdapter.setUseFooter(false);
//                    }
//                }
//                if (myReplyDatas.size() > 0) {
//                    mNoDataLayout.setVisibility(View.GONE);
//                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
//                    Message msgReply = Message.obtain();
//                    msgReply.what = 1;
//                    handler.sendMessage(msgReply);
//
//                } else {
//                    mNoDataLayout.setVisibility(View.VISIBLE);
//                    String date = null;
//                    if ("1".equals(mAct)) {
//                        date = WEEK;
//                    } else if ("2".equals(mAct)) {
//                        date = MONTH;
//                    } else if ("3".equals(mAct)) {
//                        date = TWO_MONTH;
//                    }
//                    mNoDataTv.setText(date + "历史回复暂时没有数据哦！");
//                    mSwipeRefreshLayout.setVisibility(View.GONE);
//                }
//                mIsRefreshing = false;
//            } else {
//                ToastUtils.shortToast(bean.msg);
//            }
//        }
//
//        //stone 老版本解析 old
////        JSONObject jsonObject;
////        try {
////            jsonObject = new JSONObject(json);
////            int code = jsonObject.getInt("code");
////            String msg = jsonObject.getString("msg");
////            if (code == 0) {
////                if (mIsStart) {
////                    myReplyDatas.clear();
////                    mSwipeRefreshLayout.setRefreshing(false);
////                }
////                JSONObject jsonElement = jsonObject.getJSONObject("data");
////                int hasData = jsonElement.getInt("more");
////                isMore = hasData == 1;
////                JSONArray array = jsonElement.getJSONArray("data");
////                for (int i = 0; i < array.length(); i++) {
////                    JSONObject jsonChild = array.getJSONObject(i);
////                    QuestionNote questionNote = new QuestionNote();
////                    questionNote.grade = jsonChild.get("qid") + "";
////                    questionNote.qid = jsonChild.getInt("qid") + "";
////                    questionNote.title = jsonChild.getString("title");
////                    questionNote.nickname = jsonChild.getString("nickname");
////                    questionNote.sex = jsonChild.getString("sex");
////                    questionNote.age = jsonChild.getString("age");
////                    questionNote.time = jsonChild.getString("createtime");
////                    questionNote.subject = jsonChild.getString("subjectname");
////                    questionNote.q_type = jsonChild.getString("q_type");
////                    questionNote.money = jsonChild.getString("givepoint");
////                    questionNote.con = jsonChild.getString("con");
////                    myReplyDatas.add(questionNote);
////                }
////                if (mIsStart) {
////                    if (myReplyDatas.size() >= 10) {
////                        mQueItemAdapter.setUseFooter(true);
////                    } else {
////                        mQueItemAdapter.setUseFooter(false);
////                    }
////                }
////                if (myReplyDatas.size() > 0) {
////                    mNoDataLayout.setVisibility(View.GONE);
////                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
////                    Message msgReply = Message.obtain();
////                    msgReply.what = 1;
////                    handler.sendMessage(msgReply);
////
////                } else {
////                    mNoDataLayout.setVisibility(View.VISIBLE);
////                    String date = null;
////                    if ("1".equals(mAct)) {
////                        date = WEEK;
////                    } else if ("2".equals(mAct)) {
////                        date = MONTH;
////                    } else if ("3".equals(mAct)) {
////                        date = TWO_MONTH;
////                    }
////                    mNoDataTv.setText(date + "历史回复暂时没有数据哦！");
////                    mSwipeRefreshLayout.setVisibility(View.GONE);
////                }
////                mIsRefreshing = false;
////            } else {
////                ToastUtils.shortToast(msg);
////            }
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
//
//    }
//
////    private void test(int count) {
////        myReplyDatas.clear();
////        for (int i = 0; i < count; i++) {
////            QuestionNote questionNote = new QuestionNote();
////            questionNote.qid = i+"";
////            questionNote.title = "感冒了应该吃什么药？能快点好吗？能吃橘子吗？感冒了应该吃什么药？能快点好吗？能吃橘子吗？感冒了应该吃什么药？能快点好吗？能吃橘子吗？";
////            questionNote.nickname = "一只飞鱼";
////            questionNote.sex = "女";
////            questionNote.age = i+20+"";
////            questionNote.time = "09:20";
////            questionNote.subject = "心血管内科";
////            questionNote.q_type = "1";
////            questionNote.money = "25.00";
////            myReplyDatas.add(questionNote);
////        }
////    }
//
//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_history_reply;
//    }
//
//    @Override
//    public void onItemClick(ActionItem item, int position) {
//        mHideDialogue = false;
//        String title = (String) item.mTitle;
//        mIsStart = true;
//        isMore = true;
//        mIsRefreshing = true;
//        if (WEEK.equals(title)) {
//            mAct = "1";
//        } else if (MONTH.equals(title)) {
//            mAct = "2";
//        } else if (TWO_MONTH.equals(title)) {
//            mAct = "3";
//        }
//        requestData(mAct);
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.back:
//                finish();
//                break;
//            case R.id.date:
//                menuPopup.show_new(view);
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (mDialog != null) {
//            mDialog.closeProgersssDialog();
//        }
//        super.onDestroy();
//    }
//}
