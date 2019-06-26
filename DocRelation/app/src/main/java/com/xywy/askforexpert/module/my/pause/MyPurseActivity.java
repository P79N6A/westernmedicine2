package com.xywy.askforexpert.module.my.pause;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.MyPurse.BillMonthInfo;
import com.xywy.askforexpert.model.MyPurse.MyPurseBean;
import com.xywy.askforexpert.model.MyPurse.MyPurseGVItemBean;
import com.xywy.askforexpert.model.MyPurse.MyPurseItemBean;
import com.xywy.askforexpert.module.discovery.service.ServiceOpenStateUtils;
import com.xywy.retrofit.net.BaseData;
import com.xywy.uilibrary.recyclerview.adapter.HSItemDecoration;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的钱包
 *
 * @author SHR
 * @2015-4-27下午4:02:39
 */
public class MyPurseActivity extends Activity implements IBankCardStateView,IBillSummaryDetailView{

    private static final String TAG = "MyPurseActivity";

    private RelativeLayout noDataLayout;

    private MyPurseDataAdapter mAdapter;

    public boolean isloading = true;
    private TextView tv_bank_card;//银行卡
    TextView tv_income_month;    //本月收入
    TextView tv_ranking;  //收入排名
    TextView tv_total_income;    //总收入
    TextView tv_imcome_yesterday;  //昨日收入
    private LinearLayout ll_income_bottom;
    private String income,balance,jixiao;
    private RecyclerView recyclerView;
    protected boolean isDecorationAdded = false;
    private ArrayList<MyPurseGVItemBean> innerItemGrid = new ArrayList<>();
    private ArrayList<MyPurseItemBean> itemList;
    private IBillSummaryDetailPresenter iBillSummaryDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.activity_mypurse);
        initView();
    }

    private void initView() {
        tv_bank_card = (TextView) findViewById(R.id.tv_bank_card);
        tv_bank_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_bank_card.setEnabled(false);
                StatisticalTools.eventCount(MyPurseActivity.this,"Bankcard");
                new BankCardStatePresenterImpl(MyPurseActivity.this).getBankCardState(YMUserService.getCurUserId());
            }
        });
        tv_income_month = (TextView) findViewById(R.id.tv_income_month);
        tv_ranking = (TextView) findViewById(R.id.tv_ranking);
        tv_ranking.setVisibility(View.GONE);
        ll_income_bottom = (LinearLayout) findViewById(R.id.ll_income_bottom);
        ll_income_bottom.setVisibility(View.INVISIBLE);
        tv_total_income = (TextView) findViewById(R.id.tv_total_income);
        tv_imcome_yesterday = (TextView) findViewById(R.id.tv_imcome_yesterday);

        fillHeadData(income);
        noDataLayout = (RelativeLayout) findViewById(R.id.rl_no_data);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyPurseActivity.this));
        if (!isDecorationAdded) {
            recyclerView.addItemDecoration(getItemDecoration());
            isDecorationAdded = true;
        }
        mAdapter = new MyPurseDataAdapter(MyPurseActivity.this);
        itemList = new ArrayList<>();

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                MyPurseItemBean item = mAdapter.getItem(position);
                switch (item.getType()) {
                    case MyPurseItemBean.TYPE_LIST:
                        LogUtils.i(""+position);
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
        initData();
    }

    private void initHalfYearIncomeData(float club_percent, float immediate_percent, float familydoc_percent,
                                        float dhysdoc_percent, float other_percent,
                                        List<BillMonthInfo> atrend) {
        MyPurseItemBean homeItemBean = new MyPurseItemBean(club_percent,immediate_percent,familydoc_percent,dhysdoc_percent,
                other_percent,atrend);
        itemList.add(homeItemBean);
    }

    private void initGVData(String onlineDrugIncome,String questionSquareIncome,String imwdIncome,String homeDoctorIncome,String callDoctorIncome,
                            String transferTreatmentIncome,String otherIncome) {
        boolean isImwdOpened = Constants.OPEN.equals(ServiceOpenStateUtils.getImwdOpenState());
        boolean isQuestionSquareOpened = Constants.OPEN.equals(ServiceOpenStateUtils.getQuestionSquareOpenState());
        boolean isHomeDoctorOpened = Constants.OPEN.equals(ServiceOpenStateUtils.getHomeDoctorOpenState());
        boolean isCallDoctorOpened = Constants.OPEN.equals(ServiceOpenStateUtils.getCallDoctorOpenState());
//        boolean isTransferTreatmentOpened = false;
        boolean isOnlineRoomOpened = !TextUtils.isEmpty(YMApplication.getLoginInfo().getData().getZxzhsh())&&
                "1".equals(YMApplication.getLoginInfo().getData().getZxzhsh())?true:false;
        boolean isOtherOpened = true;
        int imwdIconResId = R.drawable.imwd_income_normal;
        int questionSquareIconResId = R.drawable.wtgc_income_normal;
        int homeDoctorIconResId = R.drawable.jtys_income_normal;
        int callDoctorIconResId = R.drawable.dhys_income_normal;
//        int transferTreatmentIconResId = isTransferTreatmentOpened ? R.drawable.yyzz_open : R.drawable.yyzz_normal;
        int otherIconResId = R.drawable.other_income_normal;
        int onlineRoomIconResId = !TextUtils.isEmpty(YMApplication.getLoginInfo().getData().getZxzhsh())&&
                "1".equals(YMApplication.getLoginInfo().getData().getZxzhsh())?R.drawable.online_income_open:R.drawable.online_income_close;


        MyPurseGVItemBean imwdItemBean = new MyPurseGVItemBean(Constants.IMWD_CN,imwdIncome , imwdIconResId, isImwdOpened, 0);
        MyPurseGVItemBean questionSquareItemBean = new MyPurseGVItemBean(Constants.CLUB_CN, questionSquareIncome, questionSquareIconResId, isQuestionSquareOpened, 0);
        MyPurseGVItemBean homeDoctorItemBean = new MyPurseGVItemBean(Constants.FAMILYDOCTOR_CN,homeDoctorIncome, homeDoctorIconResId, isHomeDoctorOpened, 0);
        MyPurseGVItemBean callDoctorItemBean = new MyPurseGVItemBean(Constants.DHYS_CN, callDoctorIncome, callDoctorIconResId, isCallDoctorOpened, 0);
//        MyPurseGVItemBean transferTreatmentItemBean = new MyPurseGVItemBean("", transferTreatmentIncome, transferTreatmentIconResId, isTransferTreatmentOpened, 0);
        MyPurseGVItemBean otherItemBean = new MyPurseGVItemBean(Constants.OTHER, otherIncome, otherIconResId, isOtherOpened, 0);
        MyPurseGVItemBean onlineRommItemBean = new MyPurseGVItemBean(Constants.CONSULTING_ROOM_ONLINE, onlineDrugIncome, onlineRoomIconResId, isOnlineRoomOpened, 0);
        innerItemGrid.clear();
        innerItemGrid.add(imwdItemBean);
        innerItemGrid.add(questionSquareItemBean);
        innerItemGrid.add(homeDoctorItemBean);
        innerItemGrid.add(callDoctorItemBean);
        innerItemGrid.add(otherItemBean);
        innerItemGrid.add(onlineRommItemBean);

    }


    private void fillHeadData(String income) {
        tv_income_month.setText(income);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        iBillSummaryDetailPresenter = new BillSummaryDetailPresenterImpl(this);
        iBillSummaryDetailPresenter.getBillSummaryDetail(YMUserService.getCurUserId(),0);
    }

    public void onPurseButtonListener(View v) {
        switch (v.getId()) {
            case R.id.ib_purse_back:
                finish();
                break;
        }
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    public void onPause() {
        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    public void onSuccessResultView(Object o, String flag) {
        if("BillSummaryDetailPresenterImpl".equals(flag)){
            //处理钱包详情接口的数据
            BaseData<MyPurseBean> baseData = (BaseData<MyPurseBean>) o;
            MyPurseBean data = baseData.getData();
            if(null != data){
                String month_total = TextUtils.isEmpty(data.month_income_total)?"":data.month_income_total;
                String onlinedrug = TextUtils.isEmpty(data.onlinedrug)?"":data.onlinedrug;
                fillHeadData(month_total);
                String questionSquareIncome = TextUtils.isEmpty(data.club)?"":data.club;
                String imwdIncome = TextUtils.isEmpty(data.immediate)?"":data.immediate;
                String homeDoctorIncome = TextUtils.isEmpty(data.familydoc)?"":data.familydoc;
                String callDoctorIncome = TextUtils.isEmpty(data.dhysdoc)?"":data.dhysdoc;
                String transferTreatmentIncome = "";
                String otherIncome = TextUtils.isEmpty(data.other)?"":data.other;
                initGVData(onlinedrug,questionSquareIncome,imwdIncome,homeDoctorIncome,callDoctorIncome,transferTreatmentIncome,otherIncome);
                itemList.add(0,new MyPurseItemBean(innerItemGrid,data.current_month,MyPurseItemBean.TYPE_GRID));
                itemList.add(1,new MyPurseItemBean(data.card,data.months));
                initHalfYearIncomeData(data.club_percent,data.immediate_percent,data.familydoc_percent,data.dhysdoc_percent,data.other_percent,data.atrend);
                mAdapter.setData(itemList);
                recyclerView.setAdapter(mAdapter);
            }
        }else {
            tv_bank_card.setEnabled(true);
            BaseData baseData = (BaseData) o;
            if(Constants.COMMIT_NEVER.equals(baseData.getMsg())){
                startActivity(new Intent(MyPurseActivity.this,BankCardActivity.class));
            }else {
                startActivity(new Intent(MyPurseActivity.this,BankCardStateActivity.class));
            }
        }
    }

    @Override
    public void onErrorResultView(Object o, String flag, Throwable e) {
        tv_bank_card.setEnabled(true);
    }

    @Override
    public void showProgressBar() {
    }

    @Override
    public void hideProgressBar() {
    }

    @Override
    public void showToast(String str) {
        ToastUtils.shortToast(str);
    }

    /**
     * 获取分割线样式
     *
     * @return
     */
    private HSItemDecoration getItemDecoration() {

        if (getDividerLineWidth() >= 0 && getDividerColorResId() > 0) {
            return new HSItemDecoration(MyPurseActivity.this, getDividerColorResId(), getDividerLineWidth());
        } else if (getDividerLineWidth() >= 0) {
            return new HSItemDecoration(MyPurseActivity.this, getDividerLineWidth());
        } else if (getDividerColorResId() > 0) {
            return new HSItemDecoration(MyPurseActivity.this, getDividerColorResId());
        } else {
            return new HSItemDecoration(MyPurseActivity.this);
        }
    }

    /**
     * 自定义分割线宽度
     *
     * @return
     */
    protected float getDividerLineWidth() {
        return -1;
    }

    /**
     * 自定义分隔线颜色 返回color的resId
     *
     * @return
     */
    protected int getDividerColorResId() {
        return 0;
    }
}
