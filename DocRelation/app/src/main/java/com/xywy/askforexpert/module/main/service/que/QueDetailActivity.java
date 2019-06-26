package com.xywy.askforexpert.module.main.service.que;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.activity.WebViewActivity;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.CornerDialog;
import com.xywy.askforexpert.appcommon.utils.DateUtils;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.SPUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.QuestionSquareMsgItem;
import com.xywy.askforexpert.model.questionsquare.QuestionDetailPageBean;
import com.xywy.askforexpert.model.questionsquare.SendMedicineResultBean;
import com.xywy.askforexpert.model.questionsquare.ZhuiWenItem;
import com.xywy.askforexpert.module.consult.activity.SumUpActivity;
import com.xywy.askforexpert.module.doctorcircle.FastReplyActivity;
import com.xywy.askforexpert.module.main.service.HistoryReplyActivity;
import com.xywy.askforexpert.module.main.service.que.adapter.QueDetailAdapter;
import com.xywy.askforexpert.module.main.service.que.adapter.QuestionDetailBtnAdapter;
import com.xywy.askforexpert.module.main.service.que.adapter.QuestionDetailBtnItem;
import com.xywy.askforexpert.module.main.service.que.fragment.RecognizeMedicineGuideDialogFragment;
import com.xywy.askforexpert.module.main.service.que.model.MedicineBean;
import com.xywy.askforexpert.module.main.service.que.utils.QuestionSquareCacheUtils;
import com.xywy.askforexpert.widget.PasteEditText;
import com.xywy.askforexpert.widget.view.MyPopupWindow;
import com.xywy.base.view.ProgressDialog;
import com.xywy.datarequestlibrary.model.CommonResponse;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.uilibrary.recyclerview.adapter.HSItemDecoration;
import com.xywy.util.LogUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.xywy.askforexpert.module.main.service.que.adapter.QuestionDetailBtnItem.Type.CORRECT_OFFICE;
import static com.xywy.askforexpert.module.main.service.que.adapter.QuestionDetailBtnItem.Type.RAPID_REPLY;
import static com.xywy.askforexpert.module.main.service.que.adapter.QuestionDetailBtnItem.Type.RECOMMEND_MEDICINE;

/**
 * 问题详情页 stone
 *
 * @author shihao 2015-5-17
 */
public class QueDetailActivity extends YMBaseActivity {

    protected static final String TAG = "QueDeatilActivity";
    private static final String INTENT_KEY_STARTTIME = "startTime";
    private static final long LIMITTIME = 600000;//10分钟

    private InputMethodManager manager;

    private PasteEditText mEditTextContent, mEditTextContent1;

    private LinearLayout editBottom;

    private MyPopupWindow mPopupWindow;

    private ProgressDialog loadingDialog;

    private ProgressDialog sendDialog;

    private ProgressDialog skipDialog;

    private ProgressDialog zhuiwenDialog;

    private ProgressDialog jubaoDialog;

    private RelativeLayout correntReply, closeQue;

    @Bind(R.id.rv_btn_list)
    RecyclerView rv_btn_list;
    @Bind(R.id.rl_tips)
    RelativeLayout mRlTips;
    @Bind(R.id.ll_text1)
    LinearLayout mLlText1;
    @Bind(R.id.text1)
    TextView mText1;
    @Bind(R.id.ll_text2)
    LinearLayout mLlText2;
    @Bind(R.id.text2)
    TextView mText2;
    @Bind(R.id.ll_reply)
    LinearLayout mLl_reply;
    @Bind(R.id.iv)
    ImageView mIv;
    @Bind(R.id.ll_bottom)
    LinearLayout ll_bottom;

    private static final int REQUESTCODE = 2019;
    private static final int RESULTCODE_ZHUIWEN = 2020;
    private static final String ANALYSIS = "analysis";
    private static final String SUGGETIION = "suggestion";
    private static final String REPLY = "reply";
    private static final String INTENT_VALUE_TRANSFER_TREATMENT = "transfer_treatment";//转诊标记
    private static final String INTENT_KEY_TRANSFER_TREATMENT = "from";//转诊标记
    private static final int RESULTCODE_TRANSFER_TREATMENT = 2021;
    private TextView tvCorrentRlply;

    private static final String QUES = "ques";//未回答过的问题
    private static final String ZHUIWEN = "zhuiwen";//追问的问题

    /**
     * 刷新的listview
     */
    private ListView detailView;

    private QueDetailAdapter queDetailAdapter;

    private QuestionDetailBtnAdapter btnAdapter;

    private String msg = "";
    private String msg1 = "";
    /**
     * id 帖子id type 当前是哪一个
     */
    private String id, type, tag;
    private String mQ_Type;
    private static final String INTENT_KEY_Q_TYPE = "q_type";//1 指定付费 2 悬赏 3绩效
    private static final String Q_TYPE_1 = "1";//1 指定付费
    private static final String Q_TYPE_2 = "2";//2 悬赏
    private static final String Q_TYPE_3 = "3";//3绩效
    private static final String INTENT_KEY_ISFROM = "isFrom";
    private static final String WAITREPLY = "waitreply";
    private static final String RUNLIST = "runlist";
    private static final String JPUSH = "JPush";
    private static final String MYREPLY = "myreply";
    private static final int TYPE_QUES = 2;  //当前是问题广场的非追问回复界面
    private static final int TYPE_ZHUIWEN = 3;  //当前是问题广场的追问回复界面
    private String mIsFrom;
    private String mRid;
    private SharedPreferences mLoginSp;
    private final static String ISFISTSHOWZHUANZHEN = "isFistShowZhuanZhen";
    private final static String ISFISTSHOWCHULIWANCHENG = "isFistShowChuLiWanCheng";
    private final static String CORRECT_SUBJET = "纠正科室";
    private final static String TRANSFER_TREATMENT = "转诊";
    private static final String INTENT_KEY_RID = "rid";
    private boolean mIsFistShowZhuanZhen, mIsFistShowChuLiWanCheng;

    private List<QuestionDetailBtnItem> btnItemList = new ArrayList<>();

    private List<QuestionSquareMsgItem> msgList;

    QuestionDetailPageBean detailPageBean;
    QuestionDetailPageBean.DataBean dataBean;

    private boolean hasAnswered = false;

    /**
     * 开始时间
     */
    private long startTime;

    private int index;
    private String level; // level 的值如果是"8",表示医生的回复需要修改

    private Handler queHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            if (msg.what == 1) {
                if (null != msgList) {
                    queDetailAdapter.bindData(msgList);
                    detailView.setAdapter(queDetailAdapter);
                    //滑动到底部
                    if (msgList.size() > 0) {
//                    detailView.smoothScrollToPosition(msgList.size() - 1);
                        detailView.setSelection(msgList.size() - 1);
                    }
                }
//                detailView.setOnItemClickListener(new OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view,
//                                            int position, long id) {
//                        hideKeyboard();
//
//                    }
//                });

                tvCorrentRlply.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(QueDetailActivity.this,
//                                EditMyReplyActivity.class);
//                        intent.putExtra("analyse", dataBean.getAnalyse());
//                        intent.putExtra("suggest", dataBean.getSuggest());
//                        intent.putExtra("type", type);
//                        intent.putExtra("id", id);
//                        intent.putExtra("rid", dataBean.getRid());
//                        intent.putExtra("box_num", dataBean.getBox_num());
//                        startActivityForResult(intent, 2016);

                        SumUpActivity.startSumUpActivityForResult(QueDetailActivity.this, TYPE_QUES, YMUserService.getCurUserId(), id, REQUESTCODE);
                    }
                });
            }

        }
    };
    private CornerDialog mDialog;

    @Override
    protected void onDestroy() {

        if (YMApplication.sWTGCQuestionId != null && YMApplication.sWTGCQuestionId.size() > 0) {
            YMApplication.sWTGCQuestionId.remove(0);
        }

        if (loadingDialog != null) {
            loadingDialog.closeProgersssDialog();
        }
        if (sendDialog != null) {
            sendDialog.closeProgersssDialog();
        }
        if (skipDialog != null) {
            skipDialog.closeProgersssDialog();
        }
        if (zhuiwenDialog != null) {
            zhuiwenDialog.closeProgersssDialog();
        }
        if (jubaoDialog != null) {
            jubaoDialog.closeProgersssDialog();
        }

        super.onDestroy();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_que_deatil;
    }

    @Override
    protected void beforeViewBind() {
        //注册抢题的回复超时监听
        YmRxBus.registerTimeOut(new EventSubscriber() {
            @Override
            public void onNext(Object o) {
                finish();
            }
        }, this);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        mQ_Type = getIntent().getStringExtra(INTENT_KEY_Q_TYPE);

        index = getIntent().getIntExtra("index", 0);
        level = getIntent().getStringExtra(Constants.LEVEL);

        tag = getIntent().getStringExtra("tag");
        mIsFrom = getIntent().getStringExtra(INTENT_KEY_ISFROM);
        mRid = getIntent().getStringExtra(INTENT_KEY_RID);

        if (YMApplication.sWTGCQuestionId != null) {
            YMApplication.sWTGCQuestionId.add(0, id);
        }

        if ("mobileDetail".equals(type)) {
            startTime = System.currentTimeMillis();
            ToastUtils.longToast("5分钟后问题将关闭");
        }

        if (RUNLIST.equals(mIsFrom)) {
            startTime = System.currentTimeMillis();
            ToastUtils.longToast("10分钟后问题将关闭");
        }
        mLoginSp = getSharedPreferences("login", Context.MODE_PRIVATE);
        mIsFistShowZhuanZhen = mLoginSp.getBoolean(ISFISTSHOWZHUANZHEN, true);
        mIsFistShowChuLiWanCheng = mLoginSp.getBoolean(ISFISTSHOWCHULIWANCHENG, true);
    }


    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("问题详情");
        editBottom = (LinearLayout) findViewById(R.id.ll_qie_bottom);
        correntReply = (RelativeLayout) findViewById(R.id.rl_corrent_reply);
        closeQue = (RelativeLayout) findViewById(R.id.rl_close_que);
        tvCorrentRlply = (TextView) findViewById(R.id.tv_corrent_reply);

        mEditTextContent = (PasteEditText) findViewById(R.id.et_enter_detail);
        mEditTextContent1 = (PasteEditText) findViewById(R.id.et_enter_detail2);
        detailView = (ListView) findViewById(R.id.lv_detail);


        queDetailAdapter = new QueDetailAdapter(this);

        mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                } else {
                }

            }
        });
        mEditTextContent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                rv_btn_list.setVisibility(View.GONE);
            }
        });
        mEditTextContent1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                rv_btn_list.setVisibility(View.GONE);
            }
        });
        // 监听文字框
        mEditTextContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        if (tag.equals("myreply")) {
//            editBottom.setVisibility(View.GONE);
//        } else {
//            editBottom.setVisibility(View.VISIBLE);
//        }

        rv_btn_list.setLayoutManager(new GridLayoutManager(this, 4));
        rv_btn_list.addItemDecoration(new HSItemDecoration(this, 0F));
        btnAdapter = new QuestionDetailBtnAdapter(this);
        btnAdapter.setData(btnItemList);
        rv_btn_list.setAdapter(btnAdapter);
        btnAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position > btnItemList.size() - 1) {
                    LogUtils.e("数组越位");
                    return;
                }
                QuestionDetailBtnItem btnItem = btnItemList.get(position);
                if (!btnItem.isEnabled()) {
                    LogUtils.e("按钮点击事件被禁止：" + btnItem.getName());
                    return;
                }

                switch (btnItem.getType()) {
                    case CORRECT_OFFICE:
                        // 传递类型
                        // type 、 userid 、 id、
                        Intent intent = new Intent();
                        intent.setClass(QueDetailActivity.this, QueSwitchDpart.class);
                        intent.putExtra("onedpart", dataBean.getOneDpart());
                        intent.putExtra("twodpart", dataBean.getSubjectName());
                        intent.putExtra("id", id);
                        startActivityForResult(intent, 2018);
                        break;

                    case RAPID_REPLY:
                        Intent intentFast = new Intent();
                        intentFast
                                .setClass(QueDetailActivity.this, FastReplyActivity.class);
                        startActivityForResult(intentFast, 2015);
                        break;

                    case REPORT:
                        // 显示popupWindow
                        showPopupWindow();
                        break;

                    case SKIP:
                        if (JPUSH.equals(mIsFrom)) {
                            queSkip(mRid);
                        } else {
                            queSkip(dataBean.getRid());
                        }

                        break;

                    case RECOMMEND_MEDICINE:
                        //shortToast("推荐用药");
                        if (null != dataBean) {
                            StatisticalTools.eventCount(QueDetailActivity.this, "yaorecommended");
                            String keyword = TextUtils.isEmpty(dataBean.getTag()) ? dataBean.getSubjectName() : dataBean.getTag();
                            String webUrl = BuildConfig.YAO_XYWY_BASE_URL + "search.html?from_app=app_ym&keyword=" + keyword;
                            WebViewActivity.start(QueDetailActivity.this, "推荐用药", webUrl);
                        } else {
                            shortToast("未获取到页面数据，无法推荐药品");
                        }
                        break;
                    default:
                        break;
                }

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        btnItemList.add(new QuestionDetailBtnItem(QuestionDetailBtnItem.Type.RECOMMEND_MEDICINE, "推荐用药", R.drawable.recongnize_medicine_selector, false));
        btnItemList.add(new QuestionDetailBtnItem(QuestionDetailBtnItem.Type.RAPID_REPLY, "快捷回复", R.drawable.fast_reply_selector, false));
        btnItemList.add(new QuestionDetailBtnItem(QuestionDetailBtnItem.Type.CORRECT_OFFICE, "纠正科室", R.drawable.correct_department_selector, false));
        btnItemList.add(new QuestionDetailBtnItem(QuestionDetailBtnItem.Type.REPORT, "举报", R.drawable.report_selector, false));
        btnItemList.add(new QuestionDetailBtnItem(QuestionDetailBtnItem.Type.SKIP, "跳过", R.drawable.skip_selector, false));
        initBtnData();

        initBottomView();

    }

    private void initBottomView() {
        if (WAITREPLY.equals(mIsFrom) || RUNLIST.equals(mIsFrom) || JPUSH.equals(mIsFrom)) { //非历史回复，显示底部栏
            if (ZHUIWEN.equals(type)) { //追问的问题
                mIsFistShowChuLiWanCheng = mLoginSp.getBoolean(ISFISTSHOWCHULIWANCHENG, true);
                if (mIsFistShowChuLiWanCheng) {
                    //第一次进入
                    mRlTips.setVisibility(View.VISIBLE);
                    mIv.setImageResource(R.drawable.tiaoguotishi);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    mIv.setLayoutParams(params);
                    mLoginSp.edit().putBoolean(ISFISTSHOWCHULIWANCHENG, false).commit();
                } else {
                    //不是第一次进入
                    mRlTips.setVisibility(View.GONE);
                }
                mLlText1.setVisibility(View.VISIBLE);
                mLlText2.setVisibility(View.VISIBLE);
                mText1.setText("推荐用药");
                mText2.setText("跳过");
                if (!hasAnswered) {   //未回复
                    mLl_reply.setBackgroundResource(R.color.color_0dc3ce);
                    mLl_reply.setEnabled(true);
                } else {     //已回复
                    mLl_reply.setBackgroundResource(R.color.color_d3d3d3);
                    mLl_reply.setEnabled(false);
                }
            } else {  //新问题
                mLlText1.setVisibility(View.VISIBLE);
                mLlText2.setVisibility(View.GONE);
                if (Q_TYPE_1.equals(mQ_Type)) {  //1 指定付费, 2 悬赏,  3绩效
                    mIsFistShowZhuanZhen = mLoginSp.getBoolean(ISFISTSHOWZHUANZHEN, true);
                    if (mIsFistShowZhuanZhen) {  //第一次进入
                        mRlTips.setVisibility(View.VISIBLE);
                        mIv.setImageResource(R.drawable.zhuanzhentishi);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        mIv.setLayoutParams(params);
                        mLoginSp.edit().putBoolean(ISFISTSHOWZHUANZHEN, false).commit();
                    } else {  //不是第一次进入
                        mRlTips.setVisibility(View.GONE);
                    }
                    mText1.setText("转诊");
                } else { //非指定付费(悬赏 和 绩效)
                    mRlTips.setVisibility(View.GONE);
                    mText1.setText("纠正科室");
                }
                if (!hasAnswered) {   //未回复
                    mLl_reply.setBackgroundResource(R.color.color_00c8aa);
                    mLl_reply.setEnabled(true);
                } else {     //已回复
                    mLlText1.setVisibility(View.VISIBLE);
                    mText1.setText("推荐用药");
                    mLl_reply.setBackgroundResource(R.color.color_d3d3d3);
                    mLl_reply.setEnabled(false);
                }
            }
        } else { //历史回复,隐藏底部栏
            mRlTips.setVisibility(View.GONE);
            ll_bottom.setVisibility(View.GONE);
            if(Constants.EIGHT_STR.equals(level)){
                correntReply.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showRecognizeMedicineGuide() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (View.VISIBLE == rv_btn_list.getVisibility() && rv_btn_list.isShown() && QuestionSquareCacheUtils.isRecognizeMedicineGuideShouldShow()) {
                    Bundle bundle = new Bundle();
                    boolean isDoubleLine = btnItemList.size() > 4;
                    bundle.putBoolean("isDoubleLine", isDoubleLine);
                    RecognizeMedicineGuideDialogFragment recognizeMedicineGuideDialogFragment = new RecognizeMedicineGuideDialogFragment();
                    recognizeMedicineGuideDialogFragment.setArguments(bundle);
                    recognizeMedicineGuideDialogFragment.show(getSupportFragmentManager(), "RecognizeMedicineGuideDialogFragment");
                    QuestionSquareCacheUtils.setRecognizeMedicineGuideShouldShow(false);
                }
            }
        });
    }

    @Override
    protected void initData() {
        requestHttpData(type);

        YmRxBus.registerSelectMedicineListener(new EventSubscriber<MedicineBean>() {
            @Override
            public void onNext(Event<MedicineBean> medicineBeanEvent) {
                //最顶端的问题详情才能处理这个 stone
                if (YMApplication.sWTGCQuestionId != null && YMApplication.sWTGCQuestionId.size() > 0 && YMApplication.sWTGCQuestionId.get(0).equals(id)) {


                    StatisticalTools.eventCount(QueDetailActivity.this, "yaorecommendedforpatient");
                    // 调用 发送药品给患者接口
                    final MedicineBean medicineBean = medicineBeanEvent.getData();
                    LogUtils.e("发送药品:" + GsonUtils.toJson(medicineBean));
                    //shortToast("发送药品:" + GsonUtils.toJson(medicineBean));
                    QuestionService.sendMedicineToUser(YMUserService.getCurUserId(), dataBean.getBlood_id(), medicineBean.getId(), medicineBean.getName(), medicineBean.getImageUrl(), new CommonResponse<SendMedicineResultBean>() {
                        @Override
                        public void onNext(SendMedicineResultBean baseResultBean) {
                            //发送药品成功
                            LogUtils.d("发送药品成功:" + GsonUtils.toJson(baseResultBean));
                            //shortToast(GsonUtils.toJson(baseResultBean));
//                            msgList.add(new QuestionSquareMsgItem(medicineBean));
                            QuestionSquareMsgItem questionSquareMsgItem = new QuestionSquareMsgItem(medicineBean);
                            questionSquareMsgItem.setCreateTime(DateUtils.getRecordTime("" + System.currentTimeMillis()));
                            msgList.add(questionSquareMsgItem);

                            updateListAdapter();
                            hasAnswered = false;//产品规定 推荐用药不算真正的回复，需要"去回复"按钮仍然能够使用
                            Intent intent = new Intent("com.refresh.list");
                            intent.putExtra("index", index);
                            sendBroadcast(intent);
                            initBottomView();

                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            e.printStackTrace();
                            shortToast("发送药品异常:" + e.getMessage());
                            LogUtils.e("发送药品异常:" + e.getMessage());
                        }
                    });
                }

            }
        }, this);
    }

    private void updateListAdapter() {
        if (null != queDetailAdapter) {
            queDetailAdapter.bindData(msgList);
            queDetailAdapter.notifyDataSetChanged();
            //滑动到底部
//            detailView.smoothScrollToPosition(queDetailAdapter.getCount() - 1);
            detailView.setSelection(queDetailAdapter.getCount() - 1);
        }
    }


//    public void onDetailClickListener(View v) {
//        switch (v.getId()) {
//            case R.id.btn1:
//                if ("edit".equals(type)) {
//                    Intent intent = new Intent("com.refresh.list");
//                    intent.putExtra("index", index);
//                    sendBroadcast(intent);
//                }
//
//                if (YMApplication.DoctorType() == 2 && index == 0 && "expert_reply".equals(type)) {
//                    queLock();
//                }
//                finish();
//                if (RUNLIST.equals(mIsFrom)) {
//                    if (!hasAnswered) { //如果没有回复了
//                        if (System.currentTimeMillis() - startTime > LIMITTIME) {  //如果没有回复了，时间超过10分钟，点击左上角的返回按钮，则直接关闭当前页面
//                            finish();
//                        } else {  //如果没有回复了，时间未超过10分钟，点击左上角的返回按钮，则弹出是否跳过的提示框
//                            showCornerDialog("您确定要跳过该问题吗？");
//                        }
//                    } else { //如果已经回复了，则不需要进行时间的判断，点击左上角的返回按钮，则直接关闭当前页面
//                        finish();
//                    }
//                } else { //如果是从处理中的帖子过来的，则点击左上角的返回按钮，则直接关闭当前页面
//                    finish();
//                }
//                break;
//
//            default:
//                break;
//        }
//    }

    private void showCornerDialog(String msg) {
        View view = View
                .inflate(QueDetailActivity.this, R.layout.que_detail_alert_dialog, null);
        mDialog = new CornerDialog(QueDetailActivity.this, view, R.style.exit_dialog);

        mDialog.setCancelable(false);
        mDialog.show();
        //取消或确定按钮监听事件处理
        TextView tv_msg = (TextView) view
                .findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        TextView btn_cancel = (TextView) view
                .findViewById(R.id.btn_cancel);//取消按钮
        TextView btn_ok = (TextView) view
                .findViewById(R.id.btn_ok);//确定按钮
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!RUNLIST.equals(mIsFrom)) {
                    if (ZHUIWEN.equals(type)) {
                        //处理完成
                        StatisticalTools.eventCount(QueDetailActivity.this, "submitendofreply");
                        queSkip(dataBean.getRid());
                    } else if (QUES.equals(type)) {
                        //转诊
                        StatisticalTools.eventCount(QueDetailActivity.this, "determinechangedepartment");
                        zhuanZhen();
                    }
                } else {
                    //抢题
                    if (System.currentTimeMillis() - startTime > LIMITTIME) {
                        if (null != mDialog) {
                            mDialog.dismiss();
                        }
                        finish();
                        return;
                    }
                    //跳过
                    queSkip(dataBean.getRid());
                }
            }
        });
    }

    /**
     * 发送追问信息
     *
     * @param content
     */
    private void sendZhuiMsg(final String content) {
        zhuiwenDialog = new ProgressDialog(this, "发送中...");
        zhuiwenDialog.showProgersssDialog();
        AjaxParams params = new AjaxParams();
        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put("id", id);
        params.put("rid", dataBean.getRid());
        params.put("content", content);
        params.put("sign", MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid() + id + dataBean.getRid() + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.QUE_SEND_REPLY_ZHUI, params, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {

                super.onFailure(t, errorNo, strMsg);
                zhuiwenDialog.closeProgersssDialog();
            }

            @Override
            public void onLoading(long count, long current) {

                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    zhuiwenDialog.closeProgersssDialog();
                    if (code == 0) {
                        Intent intent = new Intent("com.refresh.list");
                        intent.putExtra("index", index);
                        sendBroadcast(intent);
                        afterSendMsg(content);
                    }
                    ToastUtils.shortToast(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void requestHttpData(String type) {
        loadingDialog = new ProgressDialog(this, "正在加载中，请稍后...");
        loadingDialog.showProgersssDialog();
        AjaxParams params = new AjaxParams();
        String userid = YMApplication.getLoginInfo().getData().getPid();
        params.put("userid", userid);

//        if (tag.equals("myreply")) {
//            params.put("isReply", "1");
//            params.put("id", id);
//        } else {
//            params.put("isReply", "0");
//            params.put("id", id);
//        }

        if (WAITREPLY.equals(mIsFrom)) {
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

        fh.post(CommonUrl.QUE_CONTENT_DETAIL, params,
                new AjaxCallBack() {

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {

                        super.onFailure(t, errorNo, strMsg);
                        loadingDialog.closeProgersssDialog();
                    }

                    @Override
                    public void onLoading(long count, long current) {
                        super.onLoading(count, current);
                    }

                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        Log.i(TAG, t);
                        parseJsonData(t);
                        loadingDialog.closeProgersssDialog();
                        Message msg = Message.obtain();
                        msg.what = 1;
                        queHandler.sendMessage(msg);
                    }
                });
    }

    private void parseJsonData(String json) {
        if (TextUtils.isEmpty(json)) {
            shortToast("服务端返回数据为空");
            return;
        }
        detailPageBean = GsonUtils.toObj(json, QuestionDetailPageBean.class);
        if (null == detailPageBean || null == detailPageBean.getData()) {
            shortToast("服务端返回数据为空");
            return;
        }
        dataBean = detailPageBean.getData();
        if (detailPageBean.getCode() != 0) {
            ToastUtils.shortToast(detailPageBean.getMsg());
            return;
        }
        if (!MYREPLY.equals(mIsFrom)) {
            if (Integer.parseInt(dataBean.getStatus()) > 0) {
                if (0 == dataBean.getHasZw()) { //追问的问题，并且已经回复了，则直接跳转到历史回复
                    startActivity(new Intent(QueDetailActivity.this, HistoryReplyActivity.class));
                    QueDetailActivity.this.finish();
                    return;
                }
            }
        }
        msgList = new ArrayList<QuestionSquareMsgItem>();
        // 解析主体
        QuestionSquareMsgItem data = new QuestionSquareMsgItem();
        data.setCreateTime(dataBean.getCreatetime());
        data.setType(0);
        data.setSubject_pidName(dataBean.getOneDpart());
        data.setSubjectName(dataBean.getSubjectName());
        data.setTitle(dataBean.getTitle());
        data.setSex(dataBean.getSex());
        data.setAge(dataBean.getAge());
        data.setDetail(dataBean.getDetail());
        data.setState(dataBean.getState());
        data.setHelp(dataBean.getHelp());
        data.setPhoto(dataBean.getPhoto());
        data.setAwardMoney(dataBean.getGivepoint());
        data.setQues_from(dataBean.getQues_from());
        msgList.add(data);
        //解析图片
        List<String> pictureList = dataBean.getPicture();
        int uid1 = 0;//默认值
        int type1 = 3;//默认值
        String createTime = dataBean.getCreatetime();
        addPictureList(pictureList, uid1, type1, createTime);
        if (ZHUIWEN.equals(type) || null == type) {  //null == type表示是从历史回复页面点击跳转到的详情页面
            QuestionSquareMsgItem doctor = new QuestionSquareMsgItem();
            doctor.setType(1);
            doctor.setCreateTime(dataBean.getRep_createtime());
            doctor.setuId(dataBean.getRep_uid());
            doctor.setrId(dataBean.getRid());
            String detail = dataBean.getRep_content();
            doctor.setDetail(TextUtils.isEmpty(detail) ? "" : Html.fromHtml(detail).toString());
            doctor.setPhoto(dataBean.getDoc_photo());
            msgList.add(doctor);

            List<ZhuiWenItem> zhuiWenItemList = dataBean.getZhuiwen();
            if (null != zhuiWenItemList && !zhuiWenItemList.isEmpty()) {
                for (ZhuiWenItem zhuiWenItem : zhuiWenItemList) {
                    int uid = zhuiWenItem.getReply_uid();
                    int type = uid == doctor.getuId() ? 2 : 3;

                    if (!TextUtils.isEmpty(zhuiWenItem.getContent())) {
                        addDataZhui(uid, zhuiWenItem.getContent(), zhuiWenItem.getAddtime(), type);
                    }

                    List<String> picUrlList = zhuiWenItem.getPicutres();
                    addPictureList(picUrlList, uid, type, zhuiWenItem.getAddtime());
                }
            }
        }
//        updateButton(hasAnswered);
        // TODO: 2017/5/4 update cover
    }

    private void addPictureList(List<String> pictureList, int uid, int type, String createTime) {
        if (pictureList != null && !pictureList.isEmpty()) {
            for (String pictureUrl : pictureList) {
                addPicture(uid, createTime, pictureUrl, type);
            }
        }
    }

    private void addDataZhui(int uid, String tempContent, String tempTime, int type) {
        QuestionSquareMsgItem dataZhui = new QuestionSquareMsgItem();
        dataZhui.setType(type);
        dataZhui.setuId(uid);
        dataZhui.setDetail(tempContent);
        dataZhui.setCreateTime(tempTime);
        dataZhui.setPhoto(dataBean.getDoc_photo());
        msgList.add(dataZhui);
    }

    private void updateFirstReply(int uid, String tempContent, String tempTime, int type) {
        QuestionSquareMsgItem dataFirstReply = null;
        int index = -1;
        for (int i = 0; i < msgList.size(); i++) {
            dataFirstReply = msgList.get(i);
            if(dataFirstReply.getType() == type){
                index = i;
                dataFirstReply.setType(type);
                dataFirstReply.setuId(uid);
                dataFirstReply.setDetail(tempContent);
                dataFirstReply.setCreateTime(tempTime);
                dataFirstReply.setPhoto(dataBean.getDoc_photo());
                break;
            }
        }
        if(-1 != index){
            msgList.set(index,dataFirstReply);
        }
    }

    private void addPicture(int uid, String createTime, String picUrl, int type) {
        QuestionSquareMsgItem dataPic = new QuestionSquareMsgItem();
        dataPic.setType(type);
        dataPic.setuId(uid);
        dataPic.setDetail("");
        dataPic.setCreateTime(createTime);
        dataPic.setPicture(picUrl);
        msgList.add(dataPic);
    }

    private void updateButton(boolean isAnswered) {
        btnItemList.clear();
        btnItemList.add(new QuestionDetailBtnItem(RECOMMEND_MEDICINE, "推荐用药", R.drawable.recongnize_medicine_selector, isAnswered));
        if (dataBean.getIsQuick() == 1) {
            btnItemList.add(new QuestionDetailBtnItem(RAPID_REPLY, "快捷回复", R.drawable.fast_reply_selector, !isAnswered));
        } else {
        }
        if (dataBean.getBox_num() == 2) {
            mEditTextContent.setHintTextColor(getResources().getColor(
                    R.color.tab_color_nomal));
            mEditTextContent.setHint("病情分析(>20字)");
            mEditTextContent1.setVisibility(View.VISIBLE);
        } else {
            mEditTextContent1.setVisibility(View.GONE);
        }
        if (detailPageBean.getData().getIsEdit() == 1) {
            btnItemList.add(new QuestionDetailBtnItem(CORRECT_OFFICE, "纠正科室", R.drawable.correct_department_selector, !isAnswered));
            if (tag.equals("myreply")) {
//                tvCorrentRlply.setVisibility(View.VISIBLE);
//                correntReply.setVisibility(View.VISIBLE);
                closeQue.setVisibility(View.GONE);
            } else {
                tvCorrentRlply.setVisibility(View.GONE);
                correntReply.setVisibility(View.GONE);
            }

        } else {//问题关闭
            tvCorrentRlply.setVisibility(View.GONE);
            correntReply.setVisibility(View.GONE);
            if (tag.equals("myreply")) {
                closeQue.setVisibility(View.VISIBLE);
            }
        }

        if (dataBean.getIsTou() == 1) {
            btnItemList.add(new QuestionDetailBtnItem(QuestionDetailBtnItem.Type.REPORT, "举报", R.drawable.report_selector, !isAnswered));
        } else {
        }

        if (dataBean.getAllowSkip() == 1) {
            btnItemList.add(new QuestionDetailBtnItem(QuestionDetailBtnItem.Type.SKIP, "跳过", R.drawable.skip_selector, !isAnswered));
        } else {
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initBtnData();
            }
        });
        showRecognizeMedicineGuide();
    }

    private void initBtnData() {
        btnAdapter.setData(btnItemList);
        btnAdapter.notifyDataSetChanged();
    }

    /**
     * 发送消息
     *
     * @param str1 消息内容
     * @param str2 消息内容
     */
    private void sendMsg(final String str1, final String str2) {
        sendDialog = new ProgressDialog(this, "发送中...");
        sendDialog.showProgersssDialog();
        AjaxParams params = new AjaxParams();
        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put("id", id);
        params.put("suggest", str1);
        params.put("analyse", str2);
        if(Constants.EIGHT_STR.equals(level)) {
            params.put("rid", mRid);
            params.put("qedit", "1");
            params.put("tag", "");
            params.put(
                    "sign",
                    MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid() + id + ""
                            + Constants.MD5_KEY));
        }else {
            params.put("tag", type);
            params.put(
                    "sign",
                    MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid() + id + type
                            + Constants.MD5_KEY));
        }

        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.QUE_SEND_REPLY, params, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                sendDialog.closeProgersssDialog();
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    sendDialog.closeProgersssDialog();
                    JSONObject jsonObject = new JSONObject(t);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 0) {
                        Intent intent = new Intent("com.refresh.list");
                        intent.putExtra("index", index);
                        sendBroadcast(intent);
                        LogUtils.e("回复成功");
                        YmRxBus.notifyHistoryReplyUpdateSucess(id);
                        afterSendMsg("问题分析:" + str2 + "\n指导建议:" + str1);
                    } else {
                        ToastUtils.shortToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    private void afterSendMsg() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                hasAnswered = true;
//                addDataZhui(Integer.parseInt(YMUserService.getCurUserId()), mEditTextContent.getText().toString().trim(), DateUtils.getRecordTime("" + System.currentTimeMillis()), 2);
//                updateListAdapter();
//                mEditTextContent.setText("");
//                mEditTextContent.setEnabled(false);
//                rv_btn_list.setVisibility(View.VISIBLE);
//                updateButton(hasAnswered);
//                initBottomView();
//            }
//        });
//    }

    private void afterSendMsg(final String content) {
        SPUtils.getUser().put(Constants.QUESTION_ID, "");
        SPUtils.getUser().put(Constants.SUMUP_DATA1, "");
        SPUtils.getUser().put(Constants.SUMUP_DATA2, "");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hasAnswered = true;
                if(Constants.EIGHT_STR.equals(level)){
                    updateFirstReply(Integer.parseInt(YMUserService.getCurUserId()), content, DateUtils.getRecordTime("" + System.currentTimeMillis()), 1);
                }else {
                    addDataZhui(Integer.parseInt(YMUserService.getCurUserId()), content, DateUtils.getRecordTime("" + System.currentTimeMillis()), 2);
                }
                updateListAdapter();
                initBottomView();
            }
        });
    }

    @OnClick({R.id.btn_more, R.id.btn_send, R.id.rl_tips, R.id.ll_text1, R.id.ll_text2, R.id.ll_reply})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_more:
                hideKeyboard();
                rv_btn_list.setVisibility(View.VISIBLE);

                break;
            case R.id.btn_send:

                if (hasAnswered) {
                    shortToast("您已回复该问题 无法再次回复\n可点推荐药品按钮 向患者推荐药品");
                    return;
                }
                msg = mEditTextContent.getText().toString().trim();
                if (dataBean.getBox_num() == 2) {
                    msg1 = mEditTextContent1.getText().toString().trim();
                    if ("".equals(msg1)) {
                        ToastUtils.shortToast("意见建议不能为空");
                        return;
                    }
                }
                if ("".equals(msg)) {
                    ToastUtils.shortToast("您的输入不能为空");
                    return;
                }
                if ("mobileDetail".equals(type)) {
                    if ((System.currentTimeMillis() - startTime) > 300000) {
                        ToastUtils.longToast("发送失败，因为已经超过答题时间限制");
                    } else {
                        sendMsg(msg, msg1);
                    }
                } else if ("zhuiwen".equals(type)) {
                    sendZhuiMsg(msg);
                } else {
                    sendMsg(msg, msg1);
                }

                break;
            case R.id.rl_tips:
                mRlTips.setVisibility(View.GONE);
                break;
            case R.id.ll_text1:
                //跳转到转诊页面或者纠正科室页面或者推荐用药页面
                if (RUNLIST.equals(mIsFrom)) {
                    if (System.currentTimeMillis() - startTime > LIMITTIME) {
                        shortToast("回复超时,抢到题后请在10分钟内进行回复哦~");
                        return;
                    }
                }
                if (CORRECT_SUBJET.equals(mText1.getText().toString())) {
                    StatisticalTools.eventCount(QueDetailActivity.this, "correctdepartment");
                    Intent intent = new Intent();
                    intent.setClass(QueDetailActivity.this, QueSwitchDpart.class);
                    intent.putExtra("onedpart", dataBean.getOneDpart());
                    intent.putExtra("twodpart", dataBean.getSubjectName());
                    intent.putExtra("id", id);
                    intent.putExtra(INTENT_KEY_STARTTIME, startTime);
                    startActivityForResult(intent, 2018);
                } else if (TRANSFER_TREATMENT.equals(mText1.getText().toString())) {
                    StatisticalTools.eventCount(QueDetailActivity.this, "changedepartment");
                    showCornerDialog("确定要将该指定付费的问题转给其他医生吗？转诊成功后，您无法获取该题目的绩效哦");
                } else {
                    if (null != dataBean) {
                        StatisticalTools.eventCount(QueDetailActivity.this, "yaorecommended");
                        String keyword = TextUtils.isEmpty(dataBean.getTag()) ? dataBean.getSubjectName() : dataBean.getTag();
                        String webUrl = BuildConfig.YAO_XYWY_BASE_URL + "search.html?from_app=app_ym&keyword=" + keyword;
                        WebViewActivity.start(QueDetailActivity.this, "推荐用药", webUrl);
                    } else {
                        shortToast("未获取到页面数据，无法推荐药品");
                    }
                }
                break;
            case R.id.ll_text2:
                //跳过
                StatisticalTools.eventCount(QueDetailActivity.this, "endofreply");
                showCornerDialog("确定不需要回复该条追问吗？跳过后该追问将从待回复移至历史回复列表");
                break;
            case R.id.ll_reply:
                //去回复,跳转到
                StatisticalTools.eventCount(QueDetailActivity.this, "replyquestion");
                if (RUNLIST.equals(mIsFrom)) {
                    if (System.currentTimeMillis() - startTime > LIMITTIME) {
                        shortToast("回复超时,抢到题后请在10分钟内进行回复哦~");
                        queHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 500);
                        return;
                    }
                    SumUpActivity.startSumUpActivityForResult(this, TYPE_QUES, YMUserService.getCurUserId(), id, startTime, REQUESTCODE);
                } else {
                    if (ZHUIWEN.equals(type)) {
                        SumUpActivity.startSumUpActivityForResult(this, TYPE_ZHUIWEN, YMUserService.getCurUserId(), id, REQUESTCODE);
                    } else {
                        SumUpActivity.startSumUpActivityForResult(this, TYPE_QUES, YMUserService.getCurUserId(), id, REQUESTCODE);
                    }
                }

                break;
            default:
                break;

        }
    }

    private void zhuanZhen() {
        String changedid = "1";
        String sign = MD5Util.MD5(YMUserService.getCurUserId() + id + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("id", id);
        params.put("userid", YMUserService.getCurUserId());
        params.put("sign", sign);
        params.put("changedid", changedid);
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.QUE_EDIT_SUB, params, new AjaxCallBack() {

            @Override
            public void onStart() {
                super.onStart();
                jubaoDialog = new ProgressDialog(
                        QueDetailActivity.this, "全力加载中...");
                jubaoDialog.showProgersssDialog();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                if (null != jubaoDialog && jubaoDialog.isShowing()) {
                    jubaoDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 0) {
                        Intent intent = new Intent("com.refresh.list");
                        intent.putExtra("index", index);
                        sendBroadcast(intent);
                        finish();
                    }
                    ToastUtils.shortToast(msg);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
                if (null != jubaoDialog && jubaoDialog.isShowing()) {
                    jubaoDialog.dismiss();
                }
            }
        });
    }

    /**
     * 跳过
     */
    private void queSkip(String rid) {
        skipDialog = new ProgressDialog(this, "正在处理中...");
        skipDialog.showProgersssDialog();
        AjaxParams params = new AjaxParams();
        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        // params.put("userid", 9345330 + "");
        params.put("id", id);
//        if (tag.equals("zhuiwen")) {
//            params.put("rid", dataBean.getRid());
//        } else {
//
//        }

        if (ZHUIWEN.equals(type)) { //追问的问题
            params.put("rid", rid);
        }
        params.put("act", type);

        params.put(
                "sign",
                MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid() + id + type
                        + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.QUE_SKIP, params, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                skipDialog.closeProgersssDialog();
                if (null != mDialog) {
                    mDialog.dismiss();
                }

            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                if (null != mDialog) {
                    mDialog.dismiss();
                }
                skipDialog.closeProgersssDialog();
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 0) {
                        Intent intent = new Intent("com.refresh.list");
                        intent.putExtra("index", index);
                        sendBroadcast(intent);
                        ToastUtils.shortToast(msg);

                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {

            // Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }

    }

    private void showPopupWindow() {

        mPopupWindow = new MyPopupWindow(QueDetailActivity.this, itemsOnClick);

        if ("R7Plus".equals(Build.MODEL)) {
            mPopupWindow.showAtLocation(QueDetailActivity.this.findViewById(R.id.que_detail), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, DensityUtils.dp2px(36));
        } else {
            mPopupWindow.showAtLocation(QueDetailActivity.this.findViewById(R.id.que_detail), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        backgroundAlpha(0.5f);
        // 添加pop窗口关闭事件
        mPopupWindow.setOnDismissListener(new poponDismissListener());
    }


    private OnClickListener itemsOnClick = new OnClickListener() {

        public void onClick(View v) {
            // mPopupWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_chanel:
                    mPopupWindow.dismiss();
                    break;
                case R.id.btn_content1:
                    reqHttp("广告内容");
                    break;
                case R.id.btn_content2:
                    reqHttp("无意义内容");
                    break;
                case R.id.btn_content3:
                    reqHttp("重复提问");
                    break;
                case R.id.btn_content4:
                    reqHttp("非医学类资讯");
                    break;
                case R.id.btn_content5:
                    Intent intent = new Intent(QueDetailActivity.this,
                            OtherReasonActivity.class);
                    intent.putExtra("id", id + "");
                    intent.putExtra("type", type);
                    startActivityForResult(intent, 2017);
                    mPopupWindow.dismiss();
                    break;

            }

        }
    };

    /**
     * 举报
     *
     * @param content
     */
    private void reqHttp(String content) {
        jubaoDialog = new ProgressDialog(
                QueDetailActivity.this, "全力加载中...");
        jubaoDialog.showProgersssDialog();
        AjaxParams params = new AjaxParams();

        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put("id", id);
        params.put("reason", content);
        params.put(
                "sign",
                MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid() + id
                        + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.QUE_OTHER_REASON, params, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                jubaoDialog.closeProgersssDialog();
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    jubaoDialog.closeProgersssDialog();
                    JSONObject jsonObject = new JSONObject(t);
                    String msg = jsonObject.getString("msg");
                    int code = jsonObject.getInt("code");

                    if (code == 0) {
                        Intent intent = new Intent("com.refresh.list");
                        intent.putExtra("index", index);
                        sendBroadcast(intent);
                        ToastUtils.shortToast(msg);
                        finish();
                    } else {
                        ToastUtils.shortToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2015) {
            if (resultCode == 2015) {
                mEditTextContent.setText(data.getStringExtra("content"));
            }
        }
        if (requestCode == 2016) {
            if (resultCode == 2016) {
                requestHttpData(type);
            }
        }
        if (requestCode == 2017) {
            if (resultCode == 2017) {
                finish();
            }
        }
        if (requestCode == 2018) {
            if (resultCode == 2018) {
                finish();
            }
        }

        if (requestCode == REQUESTCODE) {
            if (resultCode == REQUESTCODE) {
                //发送医生的问题分析和指导建议
                String analysis = data.getStringExtra(ANALYSIS);
                String suggestion = data.getStringExtra(SUGGETIION);
                //发送数据
                sendMsg(suggestion, analysis);
            } else if (resultCode == RESULTCODE_ZHUIWEN) {
                //发送医生的回复
                String reply = data.getStringExtra(REPLY);
                //发送数据
                sendZhuiMsg(reply);
            } else if (resultCode == RESULTCODE_TRANSFER_TREATMENT) {
                //转诊成功
                finish();
            }


        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //按下的如果是BACK，同时没有重复
            if (RUNLIST.equals(mIsFrom)) {
                if (!hasAnswered) {
                    if (System.currentTimeMillis() - startTime > LIMITTIME) {
                        return super.onKeyDown(keyCode, event);
                    } else {
                        showCornerDialog("您确定要跳过该问题吗？");
                        return true;
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 锁
     */
    private void queLock() {
        AjaxParams params = new AjaxParams();
        String userId = YMApplication.getLoginInfo().getData().getPid();
        params.put("club_id", userId);
        params.put("conid", id);
        params.put("command", "expunlock");
        params.put(
                "sign",
                MD5Util.MD5(userId + id
                        + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.QUE_OTHER_REASON, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                DLog.i("lock", s);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    //从推送进来的,并且之前位于这个问题,让按钮可以点击,并通知去刷新列表中的问题,防止返回的时候看不到这个题目
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        String atype = getIntent().getStringExtra("type");
//        if (!TextUtils.isEmpty(atype)) {
//            type = atype;
//        }
        type = ZHUIWEN;

        requestHttpData(type);
        initBottomView();
        mLl_reply.setBackgroundResource(R.color.color_00c8aa);
        mLl_reply.setEnabled(true);
        YmRxBus.notifyPushEnterExitSuccess();
    }


    //点击事件
    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                if (RUNLIST.equals(mIsFrom)) {
                    if (!hasAnswered) { //如果没有回复了
                        if (System.currentTimeMillis() - startTime > LIMITTIME) {  //如果没有回复了，时间超过10分钟，点击左上角的返回按钮，则直接关闭当前页面
                            finish();
                        } else {  //如果没有回复了，时间未超过10分钟，点击左上角的返回按钮，则弹出是否跳过的提示框
                            showCornerDialog("您确定要跳过该问题吗？");
                        }
                    } else { //如果已经回复了，则不需要进行时间的判断，点击左上角的返回按钮，则直接关闭当前页面
                        finish();
                    }
                } else { //如果是从处理中的帖子过来的，则点击左上角的返回按钮，则直接关闭当前页面
                    finish();
                }
                break;
        }
    }

}
