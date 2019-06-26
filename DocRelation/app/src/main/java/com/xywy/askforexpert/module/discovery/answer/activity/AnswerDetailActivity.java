package com.xywy.askforexpert.module.discovery.answer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.utils.L;
import com.umeng.socialize.UMShareAPI;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.uilibrary.dialog.pndialog.XywyPNDialog;
import com.xywy.uilibrary.dialog.pndialog.listener.PNDialogListener;
import com.xywy.uilibrary.titlebar.ItemClickListener;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.tv.TVUtils;
import com.xywy.askforexpert.appcommon.utils.tv.bean.ColorText;
import com.xywy.uilibrary.dialog.pndialog.listener.PositiveDialogListener;
import com.xywy.askforexpert.model.answer.api.ScoreBean;
import com.xywy.askforexpert.model.answer.api.deletewrong.DeleteWrongQuestionResultBean;
import com.xywy.askforexpert.model.answer.api.paper.Question;
import com.xywy.askforexpert.model.answer.api.wrongforsubmit.WrongQuestionList;
import com.xywy.askforexpert.model.answer.api.wrongquestion.WrongPaperPageBean;
import com.xywy.askforexpert.model.answer.local.ExamPaper;
import com.xywy.askforexpert.model.answer.local.ItemSelectedMsg;
import com.xywy.askforexpert.model.answer.show.PaperItem;
import com.xywy.askforexpert.model.api.BaseResultBean;
import com.xywy.askforexpert.module.discovery.answer.AnswerService;
import com.xywy.askforexpert.module.discovery.answer.adapter.PaperPagerAdapter;
import com.xywy.askforexpert.module.discovery.answer.constants.AnswerIntentKey;
import com.xywy.askforexpert.module.discovery.answer.constants.AnswerShareConstants;
import com.xywy.askforexpert.module.discovery.answer.service.AnswerDataService;
import com.xywy.askforexpert.module.discovery.answer.service.CacheUtils;
import com.xywy.askforexpert.module.discovery.answer.utils.AnswerDataUtils;
import com.xywy.askforexpert.module.discovery.answer.utils.AnswerNumberUtils;
import com.xywy.askforexpert.module.message.share.ShareUtil;
import com.xywy.askforexpert.widget.module.answer.AnswerQuestionViewPager;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 试题详情页 答题页 or 错题集展示页 根据showType 决定按试题样式展示还是按错题样式展示
 * Created by bailiangjin on 16/4/18.
 */
public class AnswerDetailActivity extends YMBaseActivity {

    /**
     * 展示类型常量 答题
     */
    public static final int SHOW_TYPE_PAPER = 0;
    /**
     * 展示类型常量 错题
     */
    public static final int SHOW_TYPE_WONG_PAPER = 1;

    /**
     * 点击选项 handler 事件 Tag
     */
    public static final int HANDLER_TAG_ITEM_CLICK = 101;
    /**
     * 试题展示类型
     */
    public int showType = 0;//0:答题 1:错题
    @Bind(R.id.sv_root)
    ScrollView sv_root;
    @Bind(R.id.ll_answer_root)
    LinearLayout ll_answer_root;
    /**
     * 题型说明部分布局 试题显示 错题 不显示
     */
    @Bind(R.id.ll_question_type_desc)
    LinearLayout ll_question_type_desc;
    @Bind(R.id.tv_question_class)
    TextView tv_question_class;
    @Bind(R.id.tv_question_type_desc)
    TextView tv_question_type_desc;
    @Bind(R.id.tv_statement)
    TextView tv_statement;
    @Bind(R.id.tv_attach_statement)
    TextView tv_attach_statement;
    //TODO:追加标题的处理逻辑添加
    @Bind(R.id.vp_question)
    AnswerQuestionViewPager vp_question;
    @Bind(R.id.btn_submit_answer)
    Button btn_submit_answer;
    private List<Question> questionList;
    private PaperPagerAdapter paperPagerAdapter;
    private ExamPaper curExamPaper = new ExamPaper();
    private String paperId;
    private String paperName;
    private String paperVersion;
    /**
     * 试题总分
     */
    //public int totalScore;
    /**
     * 当前问题
     */
    private Question curQuestion;

    /**
     * 当前位置
     */
    private int curPosition;

    /**
     * 目标位置
     */
    private int targetPosition;


    /**
     * 错题集
     */
    private WrongQuestionList wrongQuestionList;


    private boolean isSubmited;

    /**
     * 启动方法（供答题卡页调用）
     *
     * @param activity  context
     * @param examPaper 试卷对象
     * @param position  目标题目position 传question.getOrderInAll()即可
     */
    public static void start(Activity activity, ExamPaper examPaper, int position) {
        if (null == examPaper) {
            LogUtils.e("试卷信息为空：examPaper=null");
            return;
        }
        if (position < 0 || position > examPaper.getFlatQuestionList().size()) {
            ToastUtils.shortToast("跳转位置为无效位置：position:" + position);
            LogUtils.e("跳转位置为无效位置：position:" + position);
            return;
        }
        PaperItem paperItem = new PaperItem(examPaper.getName());
        paperItem.setPaper_id(examPaper.getId());
        paperItem.setVersion(examPaper.getVersion());
        start(activity, paperItem, SHOW_TYPE_PAPER, position - 1);

    }

    /**
     * 普通调用
     *
     * @param activity
     * @param paperItem
     * @param showType
     */
    public static void start(Activity activity, PaperItem paperItem, int showType) {
        start(activity, paperItem, showType, -1);
    }

    /**
     * 带跳转试题位置的调用
     *
     * @param activity
     * @param id       试卷id examper.getId()
     * @param name     试卷名称 examper.getName()
     * @param version  试卷版本 examper.getVersion()
     * @param position 跳转位置 Question.getOrderInAll()
     */
    public static void start(Activity activity, String id, String name, String version, int position) {
        start(activity, id, name, version, SHOW_TYPE_PAPER, position);
    }

    /**
     * 中间方法
     *
     * @param activity
     * @param paperItem
     * @param showType
     * @param position
     */
    private static void start(Activity activity, PaperItem paperItem, int showType, int position) {
        if (null == paperItem || showType == -1) {
            ToastUtils.shortToast("试题页传入参数不完整");
            return;
        }
        start(activity, paperItem.getPaper_id(), paperItem.getName(), paperItem.getVersion(), showType, position);
    }

    /**
     * 基础启动方法
     *
     * @param activity
     * @param id
     * @param name
     * @param version
     * @param showType
     * @param position
     */
    private static void start(Activity activity, String id, String name, String version, int showType, int position) {

        Intent intent = new Intent(activity, AnswerDetailActivity.class);
        //跳转到 试题详情页 类型为展示
        intent.putExtra(AnswerIntentKey.PAPER_ID_KEY, id);
        intent.putExtra(AnswerIntentKey.PAPER_NAME_KEY, name);
        intent.putExtra(AnswerIntentKey.PAPER_VERSION_KEY, version);
        intent.putExtra(AnswerIntentKey.SHOW_TYPE_KEY, showType);
        intent.putExtra(AnswerIntentKey.TO_POSITION_KEY, position);
        activity.startActivity(intent);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.finish();
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_answer;
    }


    @Override
    protected void beforeViewBind() {
        paperId = getIntent().getStringExtra(AnswerIntentKey.PAPER_ID_KEY);
        paperName = getIntent().getStringExtra(AnswerIntentKey.PAPER_NAME_KEY);
        paperVersion = getIntent().getStringExtra(AnswerIntentKey.PAPER_VERSION_KEY);
        //totalScore = getIntent().getIntExtra(AnswerIntentKey.TOTAL_SCORE_KEY, 0);
        //数据展示类型
        showType = getIntent().getIntExtra(AnswerIntentKey.SHOW_TYPE_KEY, SHOW_TYPE_PAPER);
        targetPosition = getIntent().getIntExtra(AnswerIntentKey.TO_POSITION_KEY, -1);
    }

    @Override
    protected void initView() {
        if (TextUtils.isEmpty(paperId)) {
            shortToast("传入的试卷id为空");
            return;
        }
        initCommonView();

        //设置标题栏文字宽度
        titleBarBuilder.setTitleTvWidth(198);
        if (showType == SHOW_TYPE_PAPER) {
            initNormalQuestionView();

        } else if (showType == SHOW_TYPE_WONG_PAPER) {
            initWrongQuestionView();

        }


    }

    private void initCommonView() {
        titleBarBuilder.setTitleText(TextUtils.isEmpty(paperName) ? "" : paperName);
        vp_question.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * viewpager position 改变回调
             *
             * @param position
             */
            @Override
            public void onPageSelected(int position) {
                //更新页面相关数据
                refreshByPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initNormalQuestionView() {

        titleBarBuilder.addItem("交卷", new ItemClickListener() {
            @Override
            public void onClick() {
                StatisticalTools.eventCount(AnswerDetailActivity.this, "TestQuestionsShare");
                if (null != curExamPaper) {
                    checkAndSubmit();
                } else {
                    shortToast("数据未初始化");
                }
                return;
            }
        }).addShareMenuItem(new ItemClickListener() {
            @Override
            public void onClick() {
                StatisticalTools.eventCount(AnswerDetailActivity.this, "TestQuestionsShare");
                if (null == curExamPaper) {
                    return;
                }

                String shareName = curExamPaper.getName();
                String version = curExamPaper.getVersion();

                new ShareUtil.Builder().setTitle(AnswerShareConstants.shareTitle)
                        .setText(curExamPaper.getShare_text())
                        .setTargetUrl(curExamPaper.getShare_url())
                        .setImageUrl(curExamPaper.getShare_img())
                        .setShareId(AnswerShareConstants.shareId)
                        .setShareSource(AnswerShareConstants.shareSource)
                        .setAnswerData(shareName, paperId, AnswerShareConstants.shareSourceType_Detail, version).build(AnswerDetailActivity.this).innerShare();

                return;
            }
        }).build();


    }

    /**
     * 初始化 错题 特有View
     */
    private void initWrongQuestionView() {

        titleBarBuilder.addItem("删除", new ItemClickListener() {
            @Override
            public void onClick() {

                deleteCurQuestion();
                return;
            }
        }).build();

    }

    /**
     * 错题状态下删除当前试题
     */
    private void deleteCurQuestion() {
        if (null == questionList || questionList.isEmpty()) {
            shortToast("无错题可删除");
            return;
        }
        new XywyPNDialog.Builder()
                .setContent("知识点已经掌握，不再需要查看")
                .setPositiveStr("删除")
                .setNegativeStr("保留")
                .create(AnswerDetailActivity.this, new PNDialogListener() {
                    @Override
                    public void onNegative() {

                    }

                    @Override
                    public void onPositive() {
                        AnswerService.deleteWrongQuestion(curExamPaper.getId(), curQuestion.getId(), new CommonResponse<DeleteWrongQuestionResultBean>(YMApplication.getAppContext()) {
                            @Override
                            public void onNext(DeleteWrongQuestionResultBean deleteWrongQuestionResultBean) {
                                if (null != deleteWrongQuestionResultBean && deleteWrongQuestionResultBean.isResult()) {
                                    shortToast("删除错题成功");
                                    questionList.remove(curQuestion);
                                    paperPagerAdapter.setDataList(questionList);
                                    paperPagerAdapter.notifyDataSetChanged();

                                    //删除试题后细节逻辑处理

                                    if (questionList.isEmpty()) {
                                        curQuestion = null;
                                    } else if (questionList.size() > 1 && curPosition == questionList.size()) {
                                        curQuestion = questionList.get(curPosition - 1);
                                    } else {
                                        curQuestion = questionList.get(curPosition);
                                    }
                                    refreshCurQuestion(curQuestion);
                                } else {
                                    shortToast(deleteWrongQuestionResultBean.getMsg());
                                }
                            }
                        });
                    }
                });

        return;
    }

    @Override
    protected void initData() {
        // 初始化paper 基础值
        //设置试卷id id为缓存键值
        curExamPaper.setId(paperId);
        curExamPaper.setName(paperName);
        curExamPaper.setVersion(paperVersion);

        initMyData();
    }


    @OnClick({R.id.tv_question_type_desc, R.id.btn_submit_answer})
    public void onClick(View v) {
        //点击事件 事件分发处理
        switch (v.getId()) {

            case R.id.tv_question_type_desc:
                if (null != curQuestion) {
                    new XywyPNDialog.Builder()
                            .setPositiveStr("知道了 继续答题")
                            .setContent(curQuestion.getNode_desc())
                            .setNoTitle(true)
                            .setNoNegativeBtn(true)
                            .create(AnswerDetailActivity.this, new PositiveDialogListener() {
                                @Override
                                public void onPositive() {
                                    // confirm
                                }
                            });
                }
                break;
            case R.id.btn_submit_answer:
                if (!curQuestion.isAnswered()) {
                    shortToast("未选择答案 请选择后再提交");
                    return;
                }
                curQuestion.setComplete(true);
                paperPagerAdapter.setDataList(questionList);
                paperPagerAdapter.notifyDataSetChanged();
                //多选提交逻辑添加
                break;

            default:
                break;
        }

    }


    /**
     * handler 回调
     *
     * @param msg
     */
    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            //点击选项答题事件处理
            case HANDLER_TAG_ITEM_CLICK:
                ItemSelectedMsg itemSelectedMsg = (ItemSelectedMsg) msg.obj;
                onBranchSelected(itemSelectedMsg);
                scrollToBottom(sv_root, ll_answer_root);
                break;
            default:
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** 使用SSO授权必须添加如下代码 */
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            finish();
        }

    }


    //-------------------以下是 具体实现代码---------------------------------------------------------

    private void initOrUpdateAdapter(List<Question> questionList) {
        if (null == paperPagerAdapter) {
            paperPagerAdapter = new PaperPagerAdapter(AnswerDetailActivity.this, questionList, uiHandler);
            vp_question.setAdapter(paperPagerAdapter);
            refreshByPosition(0);
        } else {
            paperPagerAdapter.setDataList(questionList);
            paperPagerAdapter.notifyDataSetChanged();
        }
    }

    private void initMyData() {
        showLoadDataDialog();
        if (SHOW_TYPE_PAPER == showType) {
            //读取内存
            ExamPaper cachedExamPaper = AnswerDataService.INSTANCE.getExamPaper(paperId);
            if (null == cachedExamPaper) {
                //读取本地缓存
                cachedExamPaper = CacheUtils.getExamPaper(paperId);
            }
            //读取缓存
            if (null != cachedExamPaper && null != cachedExamPaper.getVersion() && cachedExamPaper.getVersion().equals(paperVersion)) {
                hideProgressDialog();
                curExamPaper = cachedExamPaper;
                AnswerDataService.INSTANCE.addExamPaper(curExamPaper);

                //有答题记录 并且试题版本未更新
                //读取缓存数据 包括已答试题
                questionList = curExamPaper.getFlatQuestionList();
                if (null != questionList && !questionList.isEmpty()) {
                    LogUtils.e("读取缓存");

                    initOrUpdateAdapter(questionList);

                    if (targetPosition >= 0) {
                        toTargetPosition();
                    } else {
                        //跳转到之前位置
                        toPrePosition();
                    }

                } else {
                    //试题列表为空
                    LogUtils.e("试题列表数据为空");
                }

            } else {
                //从服务端请求普通试题数据
                requestNormalPaperFromServer();
            }

        } else if (SHOW_TYPE_WONG_PAPER == showType) {
            //从服务端请求错题数据
            requestWrongPaperFromServer();
        }
    }

    /**
     * 滚动到之前退出时位置
     */
    private void toPrePosition() {
        boolean isAnswered = AnswerDataUtils.isAnswered(questionList);
        if (isAnswered) {
            int prePosition = curExamPaper.getPrePosition();
            toPosition(prePosition);
        }
    }

    /**
     * 跳转到位置
     *
     * @param position
     */
    private void toPosition(int position) {
        if (position > 0 && position <= questionList.size() - 1) {
            vp_question.setCurrentItem(position);
        }
    }

    /**
     * 从服务端请求错题数据
     */
    private void requestWrongPaperFromServer() {


        AnswerService.getPaperWrongQuestionList(paperId, new CommonResponse<WrongPaperPageBean>(YMApplication.getAppContext()) {
            @Override
            public void onNext(WrongPaperPageBean wrongPaperBean) {
                hideProgressDialog();
                if (null == wrongPaperBean) {
                    shortToast("暂无数据");
                    return;
                }

                int code = wrongPaperBean.getCode();
                switch (code) {
                    case ScoreBean.CODE_SUCCESS:
                        questionList = wrongPaperBean.getFlatQuestionList();
                        if (null != questionList && !questionList.isEmpty()) {
                            initOrUpdateAdapter(questionList);
                        } else {
                            shortToast("暂无数据");
                        }
                        break;
                    case ScoreBean.CODE_PARAM_ERROR:
                        LogUtils.e("请求单数异常:错误码:" + code);
                        shortToast("请求失败 请求单数异常");
                        break;
                    default:
                        LogUtils.e("服务的未知异常:错误码:" + code);
                        shortToast("服务的未知异常:错误码:" + code);
                        break;

                }
            }
        });
    }

    /**
     * 从服务端请求 普通试题数据
     */
    private void requestNormalPaperFromServer() {
        //请求网络 获取最新试题数据 从头开始答题


        AnswerService.getPaperContent(paperId, new CommonResponse<ExamPaper>(YMApplication.getAppContext()) {
            @Override
            public void onNext(ExamPaper examPaper) {
                hideProgressDialog();
                curExamPaper = examPaper;
                if (null == curExamPaper) {
                    shortToast("服务端返回数据为空");
                    return;
                }
                questionList = curExamPaper.getFlatQuestionList();

                if (null != questionList && !questionList.isEmpty()) {
                    paperId = TextUtils.isEmpty(curExamPaper.getId()) ? paperId : curExamPaper.getId();
                    paperName = TextUtils.isEmpty(curExamPaper.getName()) ? paperName : curExamPaper.getName();
                    paperVersion = TextUtils.isEmpty(curExamPaper.getName()) ? paperVersion : curExamPaper.getVersion();

                    curExamPaper.setId(paperId);
                    curExamPaper.setName(paperName);
                    curExamPaper.setVersion(paperVersion);

                    initOrUpdateAdapter(questionList);
                    AnswerDataService.INSTANCE.addExamPaper(curExamPaper);
                    toTargetPosition();
                } else {
                    shortToast("暂无数据");
                }

            }
        });
    }

    private void toTargetPosition() {
        if (targetPosition >= 0) {
            toPosition(targetPosition);
        }
    }

    /**
     * 校验 提交
     */
    private void checkAndSubmit() {
        boolean unComplete = false;
        StringBuffer sb = new StringBuffer();

        if (null == wrongQuestionList) {
            wrongQuestionList = new WrongQuestionList();
        } else {
            wrongQuestionList.clear();
        }
        for (Question question : questionList) {
            if (!question.isAnswered()) {
                unComplete = true;
                sb = sb.append(question.getNode_order() + "." + question.getOrder() + " ");
            }
            if ((!question.isMulti() && question.isAnswered() || question.isMulti() && question.isComplete()) && !question.isRight()) {
                //错题加入错题集
                wrongQuestionList.addItem(question);
            }
        }

        //计算得分
        //final int score = AnswerDataUtils.countScore(questionList, AnswerDataUtils.COUNT_TYPE_NORMAL);
        final int score = curExamPaper.getUserScore();

        if (unComplete) {
            new XywyPNDialog.Builder().setPositiveStr("是").setNegativeStr("否").setContent("您还有试题未答，确认交卷吗?").create(AnswerDetailActivity.this, new PNDialogListener() {
                @Override
                public void onPositive() {
                    //直接提交 成功则跳转到得分页
                    submit(score, wrongQuestionList.toJson());
                }

                @Override
                public void onNegative() {
                    LogUtils.e("跳转到答题卡页：试卷id：" + curExamPaper.getId());
                    //跳转到答题卡
                    AnswerSheetActivity.startActivity(AnswerDetailActivity.this, curExamPaper.getId());
                }
            });


        } else {
            //直接提交 成功则跳转到得分页
            submit(score, wrongQuestionList.toJson());
        }

    }


    /**
     * 提交
     *
     * @param score
     * @param wrongQuestionArrayStr
     */
    private void submit(int score, String wrongQuestionArrayStr) {
        showLoadDataDialog();

        AnswerService.submitForScore(paperId, score, wrongQuestionArrayStr, new CommonResponse<ScoreBean>(YMApplication.getAppContext()) {
            @Override
            public void onNext(ScoreBean scoreBean) {
                hideProgressDialog();
                if (null == scoreBean) {
                    shortToast("提交失败");
                    return;
                }

                int code = scoreBean.getCode();
                switch (code) {
                    case BaseResultBean.CODE_SUCCESS:
                        isSubmited = true;

                        //试题提交成功后清除缓存数据
                        CacheUtils.deleteExamPaper(paperId);
                        AnswerDataService.INSTANCE.removeExamPaper(paperId);
                        AnswerScoreActivity.start(AnswerDetailActivity.this, scoreBean);
                        finish();
                        break;
                    case BaseResultBean.CODE_PARAM_ERROR:
                        LogUtils.e("请求单数异常:错误码:" + code);
                        shortToast("提交失败 请求单数异常");
                        break;
                    default:
                        LogUtils.e("服务的未知异常:错误码:" + code);
                        shortToast("服务的未知异常:错误码:" + code);
                        break;

                }
            }
        });
    }


    public void scrollToBottom(final View scroll, final View inner) {
        if (scroll == null || inner == null) {
            return;
        }
        uiHandler.post(new Runnable() {
            public void run() {
                int offset = inner.getMeasuredHeight() - scroll.getHeight();
                if (offset < 0) {
                    offset = 0;
                }
                //滑动到底部
                scroll.scrollTo(0, offset);
            }
        });
    }

    /**
     * 根据位置 刷新页面数据
     *
     * @param position viewPager position
     */
    private void refreshByPosition(int position) {
        curPosition = position;
        sv_root.scrollTo(0, 0);
        if (null == questionList || questionList.isEmpty()) {
            L.e("数据未初始化");
            return;
        }

        if (position > questionList.size() - 1 || position < 0) {
            L.e("数组越界");
            return;
        }
        curQuestion = questionList.get(position);
        refreshCurQuestion(curQuestion);
    }


    /***
     * 刷新 问题相关数据
     *
     * @param curQuestion viewPager position
     */
    private void refreshCurQuestion(Question curQuestion) {

        titleBarBuilder.setIconVisibility("删除", null != curQuestion && showType == SHOW_TYPE_WONG_PAPER ? true : false);
        if (SHOW_TYPE_PAPER == showType) {
            ColorText[] texts = {new ColorText(AnswerNumberUtils.getCNNnumber(curQuestion.getNode_order()) + "、" + curQuestion.getNode_name(), R.color.answer_tv_color1), new ColorText("(共", R.color.answer_tv_color3), new ColorText("" + curQuestion.getNode_size(), R.color.answer_tv_color4), new ColorText(String.format("道,第%d题)", curQuestion.getOrderInNode()), R.color.answer_tv_color3)};
            TVUtils.setContentWithColor(tv_question_class, texts);
        }
        //错题服务端未提供Node数据 题型说明部分布局不显示
        ll_question_type_desc.setVisibility(SHOW_TYPE_PAPER == showType ? View.VISIBLE : View.GONE);

        String content = AnswerDataUtils.getQuestionContent(curQuestion, showType);
        if (!TextUtils.isEmpty(content)) {
            String mcontent = content;
        }

        tv_statement.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);

        tv_statement.setText(TextUtils.isEmpty(content) ? "" : Html.fromHtml(content));

        String attachMaterial = null == curQuestion ? "" : curQuestion.getAttachMaterial();

        tv_attach_statement.setVisibility(TextUtils.isEmpty(attachMaterial) ? View.GONE : View.VISIBLE);

        tv_attach_statement.setText(TextUtils.isEmpty(attachMaterial) ? "" : Html.fromHtml(attachMaterial));

        btn_submit_answer.setVisibility(SHOW_TYPE_PAPER == showType && (null != curQuestion && Question.ANSWER_TYPE_MULTI == curQuestion.getAnswerType()) ? View.VISIBLE : View.GONE);

    }


    /**
     * 选项点击事件处理
     *
     * @param itemSelectedMsg
     */
    private void onBranchSelected(ItemSelectedMsg itemSelectedMsg) {

        int position = itemSelectedMsg.getQuestionNumber() - 1;
        if (position < 0 || position > questionList.size() - 1) {
            LogUtils.e("数组越界");
            shortToast("数组越界");
            return;
        }
        curQuestion = updateQuestion(position, Integer.parseInt(itemSelectedMsg.getAnswer()));
        //缓存当前选项数据
        cachePaper(position);

        paperPagerAdapter.setDataList(questionList);
        paperPagerAdapter.notifyDataSetChanged();
    }

    /**
     * 缓存试题
     *
     * @param position
     */
    private void cachePaper(int position) {
        curExamPaper.setPrePosition(position);
        CacheUtils.cacheExamPaper(curExamPaper);
    }

    /**
     * 答题后更新问题状态
     *
     * @param position viewPager position
     * @param answer   答案
     * @return
     */
    @NonNull
    private Question updateQuestion(int position, int answer) {
        Question curQuestion = questionList.get(position);
        //设置答案


        if (curQuestion.isContainsAnswer(answer)) {
            if (curQuestion.isMulti()) {
                curQuestion.removeUserAnswer(answer);
            }
        } else {
            curQuestion.addUserAnswer(answer);
        }

        if (null == curQuestion.getUserAnswerMap() || curQuestion.getUserAnswerMap().size() == 0) {
            //设置问题为已答
            curQuestion.setAnswered(false);
        } else {
            curQuestion.setAnswered(true);
        }

        questionList.set(position, curQuestion);
        return curQuestion;
    }

    @Override
    protected void onDestroy() {
        if (!isSubmited) {
            //缓存试题状态
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //缓存试题状态
                    cachePaper(curPosition);
                }
            }).start();
        }
        super.onDestroy();


    }
}
