package com.xywy.askforexpert.module.main.service.que;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.interfaces.TextWatcherImpl;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.uilibrary.titlebar.ItemClickListener;

/**
 * Created by xugan on 2017/12/21.
 * 回复页面
 */

public class ReplyActivity extends YMBaseActivity {
    private static final int RESULTCODE = 2019;
    private static final int RESULTCODE_ZHUIWEN = 2020;
    private static final String ANALYSIS = "analysis";
    private static final String SUGGETIION = "suggestion";
    private static final String REPLY = "reply";
    private EditText mEtAnalysis,mEtSuggestion,mEtReply;
    private TextView mTvAnalysisNum,mTvSuggestionNum,mTvReplyNum;
    private TextView mTvAnalysis,mTvSuggestion,mTvReply;
    private FrameLayout mFlAnalysis,mFlSuggestion,mFlReply;
    private String mType;

    @Override
    protected void initView() {
        mType = getIntent().getStringExtra("type");
        initTitle();
        //问题分析
        mTvAnalysis = (TextView) findViewById(R.id.tv_analysis);
        mFlAnalysis = (FrameLayout) findViewById(R.id.fl_analysis);
        mEtAnalysis = (EditText) findViewById(R.id.et_analysis);
        mTvAnalysisNum = (TextView) findViewById(R.id.tv_analysis_num);
        mEtAnalysis.addTextChangedListener(new TextWatcherImpl() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if(s.length()==0){
                    mTvAnalysisNum.setText("20-200");
                }else {
                    mTvAnalysisNum.setText(""+s.length());
                }
            }
        });
        //指导建议
        mTvSuggestion = (TextView) findViewById(R.id.tv_suggestion);
        mFlSuggestion = (FrameLayout) findViewById(R.id.fl_suggestion);
        mEtSuggestion = (EditText) findViewById(R.id.et_suggestion);
        mTvSuggestionNum = (TextView) findViewById(R.id.tv_suggestion_num);
        mEtSuggestion.addTextChangedListener(new TextWatcherImpl() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if(s.length()==0){
                    mTvSuggestionNum.setText("20-200");
                }else {
                    mTvSuggestionNum.setText(""+s.length());
                }
            }
        });
        //我的回复
        mTvReply = (TextView) findViewById(R.id.tv_reply);
        mFlReply = (FrameLayout) findViewById(R.id.fl_reply);
        mEtReply = (EditText) findViewById(R.id.et_reply);
        mTvReplyNum = (TextView) findViewById(R.id.tv_reply_num);
        mEtReply.addTextChangedListener(new TextWatcherImpl() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if(s.length()==0){
                    mTvReplyNum.setText("10-200");
                }else {
                    mTvReplyNum.setText(""+s.length());
                }
            }
        });

        if("zhuiwen".equals(mType)){//追问
            mTvAnalysis.setVisibility(View.GONE);
            mFlAnalysis.setVisibility(View.GONE);
            mTvSuggestion.setVisibility(View.GONE);
            mFlSuggestion.setVisibility(View.GONE);
            mTvReply.setVisibility(View.VISIBLE);
            mFlReply.setVisibility(View.VISIBLE);
        }else {
            //如果不是追问,
            mTvReply.setVisibility(View.GONE);
            mFlReply.setVisibility(View.GONE);
        }

    }

    private void initTitle() {
        titleBarBuilder.setTitleText("问题广场");
        titleBarBuilder.addItem("提交", new ItemClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent();
                if("zhuiwen".equals(mType)){//追问
                    String reply = mEtReply.getText().toString().trim();
                    if(check(reply,10,200,"请填写回复信息","回复信息字数要求10-200字")){
                        intent.putExtra(REPLY,reply);
                        setResult(RESULTCODE_ZHUIWEN,intent);
                        finish();
                    }
                }else {
                    //如果不是追问,
                    String analysis = mEtAnalysis.getText().toString().trim();
                    String suggestion = mEtSuggestion.getText().toString().trim();
                    if(check(analysis,20,200,"请填写问题分析","问题分析字数要求20-200字")
                            && check(suggestion,20,200,"请填写指导建议","指导建议字数要求20-200字")){
                        intent.putExtra(ANALYSIS,analysis);
                        intent.putExtra(SUGGETIION,suggestion);
                        setResult(RESULTCODE,intent);
                        finish();
                    }
                }

            }
        }).build();
    }

    private boolean check(String str,int minLength,int maxLength,String emptyMsg,String inValidMsg) {
        if(TextUtils.isEmpty(str)){
            ToastUtils.shortToast(emptyMsg);
            return false;
        }else {
            if(str.length()<minLength || str.length()>maxLength){
                ToastUtils.shortToast(inValidMsg);
                return false;
            }
        }
        return true;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_reply;
    }
}
