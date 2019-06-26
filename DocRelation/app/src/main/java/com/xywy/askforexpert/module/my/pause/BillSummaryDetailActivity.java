package com.xywy.askforexpert.module.my.pause;

import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.MyPurse.MyPurseBean;
import com.xywy.retrofit.net.BaseData;
import com.xywy.util.DensityUtil;

/**
 * Created by xugan on 2018/6/21.
 */

public class BillSummaryDetailActivity extends YMBaseActivity implements View.OnClickListener,IBillSummaryDetailView {

    private int month;
    private IBillSummaryDetailPresenter iBillSummaryDetailPresenter;
    private TextView tv_total_income;
    private TextView tv_question_department_income;
    private TextView tv_imwd_income;
    private TextView tv_home_doctor_income;
    private TextView tv_call_doctor_income;
    private TextView tv_other_income;
    private TextView tv_online_other_income;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bill_summary_detail;
    }

    @Override
    protected void initView() {
        month = getIntent().getIntExtra(Constants.INTENT_KEY_MONTH, -1);
        titleBarBuilder.setTitleText(month +"月账单明细");
        TextView tv_total = (TextView) findViewById(R.id.tv_total);
        tv_total.setText(month+"月总收入");
        findViewById(R.id.rl_question_department).setOnClickListener(this);
        findViewById(R.id.rl_imwd).setOnClickListener(this);
        findViewById(R.id.rl_home_doctor).setOnClickListener(this);
        findViewById(R.id.rl_call_doctor).setOnClickListener(this);
        findViewById(R.id.rl_other).setOnClickListener(this);
        findViewById(R.id.online_other).setOnClickListener(this);

        tv_total_income = (TextView) findViewById(R.id.tv_total_income);
        tv_question_department_income = (TextView) findViewById(R.id.tv_question_department_income);
        tv_imwd_income = (TextView) findViewById(R.id.tv_imwd_income);
        tv_home_doctor_income = (TextView) findViewById(R.id.tv_home_doctor_income);
        tv_call_doctor_income = (TextView) findViewById(R.id.tv_call_doctor_income);
        tv_online_other_income = (TextView) findViewById(R.id.tv_online_other_income);
        tv_other_income = (TextView) findViewById(R.id.tv_other_income);

        iBillSummaryDetailPresenter = new BillSummaryDetailPresenterImpl(this);

    }

    private void setData(TextView tv,String income,AbsoluteSizeSpan absoluteSizeSpan_large,AbsoluteSizeSpan absoluteSizeSpan_small) {
        if(!TextUtils.isEmpty(income)){
            income = income + " 元";
        }
        SpannableString ss = new SpannableString(income);
        // 设置字体大小
        ss.setSpan(absoluteSizeSpan_large,0 , income.indexOf("元"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(absoluteSizeSpan_small, income.indexOf("元"), income.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ss);
    }

    @Override
    protected void initData() {
        iBillSummaryDetailPresenter.getBillSummaryDetail(YMUserService.getCurUserId(),month);
    }

    @Override
    public void onClick(View view) {
        //服务类型 1-即时问答 2-问题广场 3-家庭医生 4-电话医生 5-其他
        switch (view.getId()){
            case R.id.rl_question_department:
                StatisticalTools.eventCount(BillSummaryDetailActivity.this, "incomeWTGC");
                goToBillDetailPage("问题广场",2);
                break;
            case R.id.rl_imwd:
                StatisticalTools.eventCount(BillSummaryDetailActivity.this, "incomeJSWD");
                goToBillDetailPage("即时问答",1);
                break;
            case R.id.rl_home_doctor:
                StatisticalTools.eventCount(BillSummaryDetailActivity.this, "incomeJTYS");
                goToBillDetailPage("家庭医生",3);
                break;
            case R.id.rl_call_doctor:
                StatisticalTools.eventCount(BillSummaryDetailActivity.this, "incomeDHYS");
                goToBillDetailPage("电话医生",4);
                break;
            case R.id.online_other:
                if (!TextUtils.isEmpty(YMApplication.getLoginInfo().getData().getZxzhsh())&&
                        "1".equals(YMApplication.getLoginInfo().getData().getZxzhsh())) {
                    goToBillDetailPage("其他", 6);
                }else{
                    ToastUtils.longToast("您暂未开通问诊用药");
                }
                break;
            case R.id.rl_other:
                StatisticalTools.eventCount(BillSummaryDetailActivity.this, "incomeQT");
                goToBillDetailPage("其他",5);
                break;
            default:
                break;
        }
    }

    private void goToBillDetailPage(String title,int type) {
        Intent intent = new Intent(BillSummaryDetailActivity.this,BillDetailActivity.class);
        intent.putExtra(Constants.INTENT_KEY_TITLE,title);
        intent.putExtra(Constants.INTENT_KEY_MONTH,month);
        intent.putExtra(Constants.INTENT_KEY_TYPE,type);
        startActivity(intent);
    }

    @Override
    public void onSuccessResultView(Object o, String flag) {
        BaseData<MyPurseBean> baseData = (BaseData<MyPurseBean>) o;
        MyPurseBean data = baseData.getData();
        if(null != data){
            AbsoluteSizeSpan absoluteSizeSpan_large = new AbsoluteSizeSpan(DensityUtil.sp2px(this, 16));
            AbsoluteSizeSpan absoluteSizeSpan_small = new AbsoluteSizeSpan(DensityUtil.sp2px(this, 9));
            setData(tv_total_income,data.month_income_total,absoluteSizeSpan_large,absoluteSizeSpan_small);
            absoluteSizeSpan_large = new AbsoluteSizeSpan(DensityUtil.sp2px(this, 14));
            setData(tv_question_department_income,data.club,absoluteSizeSpan_large,absoluteSizeSpan_small);
            setData(tv_imwd_income,data.immediate,absoluteSizeSpan_large,absoluteSizeSpan_small);
            setData(tv_home_doctor_income,data.familydoc,absoluteSizeSpan_large,absoluteSizeSpan_small);
            setData(tv_call_doctor_income,data.dhysdoc,absoluteSizeSpan_large,absoluteSizeSpan_small);
            setData(tv_online_other_income,data.onlinedrug,absoluteSizeSpan_large,absoluteSizeSpan_small);
            setData(tv_other_income,data.other,absoluteSizeSpan_large,absoluteSizeSpan_small);
        }
    }

    @Override
    public void onErrorResultView(Object o, String flag, Throwable e) {

    }

    @Override
    public void showProgressBar() {
        showProgressDialog("");
    }

    @Override
    public void hideProgressBar() {
        hideProgressDialog();
    }
}
