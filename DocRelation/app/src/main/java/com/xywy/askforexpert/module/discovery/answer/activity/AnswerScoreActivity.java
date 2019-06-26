package com.xywy.askforexpert.module.discovery.answer.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.answer.api.ScoreBean;
import com.xywy.askforexpert.module.discovery.answer.constants.AnswerIntentKey;

import butterknife.Bind;

/**
 * 获取得分 排名 Activity
 * Created by bailiangjin on 16/4/21.
 */
public class AnswerScoreActivity extends YMBaseActivity {


    @Bind(R.id.rl_answer_score_bg)
    RelativeLayout rl_answer_score_bg;
    @Bind(R.id.rl_answer_score_icon)
    RelativeLayout rl_answer_score_icon;
    @Bind(R.id.tv_answer_time_content)
    TextView tv_answer_time_content;
    @Bind(R.id.tv_score)
    TextView tv_score;
    @Bind(R.id.tv_score_rank_content)
    TextView tv_score_rank_content;


    /**
     * 是否通过
     */
    private boolean isSuccess;
    /**
     * 分数
     */
    private int score;
    /**
     * 排名
     */
    private int rank = -1;


    private String paperId;
    private String paperName;
    /**
     * 答题时间
     */
    private String answerTime;


    public static void start(Activity activity, ScoreBean scoreBean) {

        if (null == scoreBean) {
            ToastUtils.shortToast("传入答题结果参数为空");
            return;
        }
        Intent intent = new Intent(activity, AnswerScoreActivity.class);
        intent.putExtra(AnswerIntentKey.SCORE_KEY, scoreBean.getScore());
        intent.putExtra(AnswerIntentKey.RANK_KEY, scoreBean.getUserRank());
        intent.putExtra(AnswerIntentKey.IS_SUCCESS_KEY, scoreBean.ispass());
        intent.putExtra(AnswerIntentKey.ANSWER_TIME_KEY, scoreBean.getAnswerTime());
        activity.startActivity(intent);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_answer_score;
    }

    @Override
    protected void beforeViewBind() {
        paperId = getIntent().getStringExtra(AnswerIntentKey.PAPER_ID_KEY);
        paperName = getIntent().getStringExtra(AnswerIntentKey.PAPER_NAME_KEY);


        score = getIntent().getIntExtra(AnswerIntentKey.SCORE_KEY, 0);
        rank = getIntent().getIntExtra(AnswerIntentKey.RANK_KEY, 0);
        isSuccess = getIntent().getBooleanExtra(AnswerIntentKey.IS_SUCCESS_KEY, false);
        answerTime = getIntent().getStringExtra(AnswerIntentKey.ANSWER_TIME_KEY);
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void initData() {

        titleBarBuilder.setTitleText(TextUtils.isEmpty(paperName) ? "试卷标题" : paperName);

        updateData(score, isSuccess, rank, answerTime);

    }

    /**
     * 更新页面数据
     *
     * @param score      得分
     * @param isSuccess  是否通过
     * @param rank       排名
     * @param answerTime 答题时间
     */
    private void updateData(int score, boolean isSuccess, int rank, String answerTime) {
        //根据结果设置不同主题
        tv_score_rank_content.setEnabled(isSuccess);
        rl_answer_score_bg.setEnabled(isSuccess);
        rl_answer_score_icon.setEnabled(isSuccess);

        //填充数据
        tv_score.setText("" + score);
        tv_answer_time_content.setText(answerTime);
        tv_score_rank_content.setText("" + rank);
    }


}
