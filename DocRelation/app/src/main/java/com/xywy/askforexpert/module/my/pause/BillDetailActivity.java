package com.xywy.askforexpert.module.my.pause;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.MyPurse.BillDayInfo;
import com.xywy.askforexpert.model.MyPurse.BillInfo;
import com.xywy.askforexpert.widget.PinnedHeaderListView;
import com.xywy.base.view.CircleProgressBar;
import com.xywy.retrofit.net.BaseData;
import com.xywy.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xugan on 2018/6/21.
 */

public class BillDetailActivity extends YMBaseActivity implements IBillDetailView{
    private PinnedHeaderListView pListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page;
    private int pageSize = 10;

    private boolean mIsStart = true;
    /**
     * 是否有更多数据
     */
    private boolean isMore = true;

    private RelativeLayout noDataLayout;

    // 全局集合存放每个整体元素
    private List<BillDayInfo> billDayInfos = null;

    private BillAdapter sectionedAdapter;

    private boolean isBottom;

    private View footView;
    private CircleProgressBar pb;
    private TextView tv_more2;
    public boolean isloading = true;
    private int type;
    private int month;
    private IBillDetailPresenter iBillDetailPresenter;
    private AbsoluteSizeSpan absoluteSizeSpan_large;
    private AbsoluteSizeSpan absoluteSizeSpan_small;
    private TextView tv_total_income,tv_total_str;
    private TextView tv_heart_income,tv_heart_str;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bill_detail;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String title = intent.getStringExtra(Constants.INTENT_KEY_TITLE);
        title = title + Constants.BILL_DETAIL;
        titleBarBuilder.setTitleText(title);
        type = intent.getIntExtra(Constants.INTENT_KEY_TYPE, -1);
        month = intent.getIntExtra(Constants.INTENT_KEY_MONTH, -1);
        TextView tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(month +"月");
        tv_total_str = (TextView) findViewById(R.id.tv_total_str);
        tv_total_income = (TextView) findViewById(R.id.tv_total_income);
        tv_heart_str = (TextView) findViewById(R.id.tv_heart_str);
        tv_heart_income = (TextView) findViewById(R.id.tv_heart_income);
        if(1!=type){
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    DensityUtil.sp2px(this, 48));
            rl.setLayoutParams(lp);
            tv_heart_str.setVisibility(View.GONE);
            tv_heart_income.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            tv_total_income.setLayoutParams(layoutParams);
            layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            layoutParams.addRule(RelativeLayout.LEFT_OF,R.id.tv_total_income);
            layoutParams.rightMargin = DensityUtil.sp2px(this, 10);
            tv_total_str.setText("收入:");
            tv_total_str.setLayoutParams(layoutParams);
        }
        absoluteSizeSpan_large = new AbsoluteSizeSpan(DensityUtil.sp2px(this, 16));
        absoluteSizeSpan_small = new AbsoluteSizeSpan(DensityUtil.sp2px(this, 9));
        noDataLayout = (RelativeLayout) findViewById(R.id.rl_no_data);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setClipChildren(true);

        swipeRefreshLayout.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        pListView = (PinnedHeaderListView) findViewById(R.id.pinnerHeaderListView);
        footView = LayoutInflater.from(BillDetailActivity.this).inflate(
                R.layout.refresh_footerview, null);
        pb = (CircleProgressBar) footView.findViewById(R.id.pro);
        tv_more2 = (TextView) footView.findViewById(R.id.footerTitle);
        pListView.addFooterView(footView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                isloading = true;
                if (isloading) {
                    isloading = false;
                    if (!NetworkUtil.isNetWorkConnected()) {
                        swipeRefreshLayout.setRefreshing(false);
                        ToastUtils.shortToast( "网络异常，请检查网络连接");
                        return;
                    }
                    mIsStart = true;
                    if (mIsStart && billDayInfos.size() < 6) {
                        footView.setVisibility(View.GONE);
                        tv_more2.setVisibility(View.GONE);
                        pb.setVisibility(View.GONE);
                    } else {
                        footView.setVisibility(View.VISIBLE);
                        tv_more2.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.VISIBLE);
                    }
                    initData();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });

        pListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isBottom
                        && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (isloading) {
                        isloading = false;
                        if (!NetworkUtil
                                .isNetWorkConnected()) {
                            footView.setVisibility(View.GONE);
                            ToastUtils.shortToast( "网络异常，请检查网络连接");
                            return;
                        }
                        tv_more2.setText("正在加载中...");
                        pb.setVisibility(View.VISIBLE);
                        mIsStart = false;
                        if (billDayInfos != null) {
                            if (isMore) {
                                    page++;
                                    if (isMore) {
                                        iBillDetailPresenter.getBillDetail(YMUserService.getCurUserId(),month,type,page,pageSize);
                                    } else {
                                        isloading = false;
                                        if (billDayInfos.size() > 8) {
                                            pb.setVisibility(View.GONE);
                                            tv_more2.setText("没有数据啦");
                                        }
                                    }
                            } else {
                                footView.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        if (isBottom && billDayInfos.size() >= 10) {
                            pb.setVisibility(View.GONE);
                        } else {
                            footView.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                isBottom = (firstVisibleItem + visibleItemCount == totalItemCount);
            }
        });
    }



    @Override
    protected void initData() {
        if(null == iBillDetailPresenter){
            iBillDetailPresenter = new BillDetailPresenterImpl(this);
        }
        page = 1;
        iBillDetailPresenter.getBillDetail(YMUserService.getCurUserId(),month,type,page,pageSize);
    }

    private void setData(TextView tv, String income, AbsoluteSizeSpan absoluteSizeSpan_large, AbsoluteSizeSpan absoluteSizeSpan_small) {
        if(TextUtils.isEmpty(income)){
            income = "元";
        }else{
            income += "元";
        }
        SpannableString ss = new SpannableString(income);
        // 设置字体大小
        ss.setSpan(absoluteSizeSpan_large,0 , income.indexOf("元"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(absoluteSizeSpan_small, income.indexOf("元"), income.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ss);
    }

    @Override
    public void onSuccessResultView(Object o, String flag) {
        isloading = true;
        swipeRefreshLayout.setRefreshing(false);
        pb.setVisibility(View.GONE);
        BaseData baseData = (BaseData) o;
        if(null == billDayInfos){
            billDayInfos = new ArrayList<>();
        }

        BillInfo billInfo = (BillInfo) baseData.getData();
        if(null !=billInfo){
            if(mIsStart){
                setData(tv_total_income,billInfo.z_jixiao, absoluteSizeSpan_large, absoluteSizeSpan_small);
                setData(tv_heart_income,billInfo.sxy_jixiao, absoluteSizeSpan_large, absoluteSizeSpan_small);
                billDayInfos.clear();
            }

            List<BillDayInfo> billDayInfoList = billInfo.list;
            if(null != billDayInfoList){
                if(billDayInfoList.size() == 0){
                    footView.setVisibility(View.VISIBLE);
                    tv_more2.setVisibility(View.VISIBLE);
                    tv_more2.setText("没有数据啦");
                    if(mIsStart){
                        noDataLayout.setVisibility(View.VISIBLE);
                    }else {
                        noDataLayout.setVisibility(View.GONE);
                    }
                    return;
                }
                if (mIsStart && billDayInfoList.size() < 6) {
                    footView.setVisibility(View.GONE);
                    tv_more2.setVisibility(View.GONE);
                    pb.setVisibility(View.GONE);
                }else {
                    footView.setVisibility(View.VISIBLE);
                    tv_more2.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.VISIBLE);
                }
                billDayInfos.addAll(billDayInfoList);
                if(null == sectionedAdapter){
                    sectionedAdapter = new BillAdapter(BillDetailActivity.this, billDayInfos);
                    pListView.setAdapter(sectionedAdapter);
                }else {
                    sectionedAdapter.bindData(billDayInfos);
                    sectionedAdapter.notifyDataSetChanged();
                }
            }else {
                if(mIsStart){
                    noDataLayout.setVisibility(View.VISIBLE);
                }else {
                    noDataLayout.setVisibility(View.GONE);
                }
            }
        }else {
            if(mIsStart){
                noDataLayout.setVisibility(View.VISIBLE);
            }else {
                noDataLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onErrorResultView(Object o, String flag, Throwable e) {
        if(page>1){
            page--;
        }
        pb.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
    }

    @Override
    public void hideProgressBar() {
    }
}
