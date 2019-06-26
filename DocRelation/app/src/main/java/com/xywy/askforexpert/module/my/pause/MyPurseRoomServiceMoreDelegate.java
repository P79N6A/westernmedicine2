package com.xywy.askforexpert.module.my.pause;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.model.MyPurse.BillMonthInfo;
import com.xywy.askforexpert.model.MyPurse.MyPurseItemBean;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 *  Created by xgxg on 2018/6/20.
 */

public class MyPurseRoomServiceMoreDelegate implements ItemViewDelegate<MyPurseItemBean> {
    Context context;
    private View ll_bind_bnakcard;

    public MyPurseRoomServiceMoreDelegate(Context context){
        this.context = context;
        YmRxBus.registerFinshBandCardInfo(new EventSubscriber() {
            @Override
            public void onNext(Object o) {
                if(null!=ll_bind_bnakcard){
                    ll_bind_bnakcard.setVisibility(View.INVISIBLE);
                }
            }
        }, this);
    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_my_purse_recent_tow_month_bill;
    }

    @Override
    public boolean isForViewType(MyPurseItemBean item, int position) {
        return MyPurseItemBean.TYPE_MORE == item.getType();
    }

    @Override
    public void convert(ViewHolder holder, MyPurseItemBean myPurseItemBean, int position) {
        if(null != myPurseItemBean){
            ll_bind_bnakcard = holder.getView(R.id.ll_bind_bnakcard);
            ll_bind_bnakcard.setVisibility((myPurseItemBean.card ==0)?View.VISIBLE:View.INVISIBLE);
            ll_bind_bnakcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StatisticalTools.eventCount(context, "bindbankcardZD");
                    context.startActivity(new Intent(context,BankCardActivity.class));

                }
            });
        }
        List<BillMonthInfo> monthInfoDatas = myPurseItemBean.getMonthInfoDatas();
        if(null != monthInfoDatas){
            if(monthInfoDatas.size() == 0){
                ViewStub viewStub = holder.getView(R.id.viewstub);
                viewStub.inflate();
                holder.getView(R.id.rl_month_lastest).setVisibility(View.GONE);
                holder.getView(R.id.line).setVisibility(View.GONE);
                holder.getView(R.id.rl_month_last).setVisibility(View.GONE);
                return;
            }
            if(monthInfoDatas.size() == 1){
                TextView tv_month = holder.getView(R.id.tv_month);
                tv_month.setText("近1个月账单");
                holder.getView(R.id.line).setVisibility(View.GONE);
                holder.getView(R.id.rl_month_last).setVisibility(View.GONE);
            }
            BillMonthInfo billMonthInfo =null;
            for (int i = 0; i < monthInfoDatas.size(); i++) {
                if(i==0){
                    billMonthInfo = monthInfoDatas.get(i);
                    if(null != billMonthInfo){
                        TextView tv_month_lastest = holder.getView(R.id.tv_month_lastest);
                        TextView tv_month_lastest_state = holder.getView(R.id.tv_month_lastest_state);
                        TextView tv_month_lastest_income = holder.getView(R.id.tv_month_lastest_income);
                        setDataForView(tv_month_lastest,tv_month_lastest_state,tv_month_lastest_income,billMonthInfo);
                        final BillMonthInfo finalBillMonthInfo = billMonthInfo;
                        holder.getView(R.id.tv_month_lastest_detail).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                StatisticalTools.eventCount(context, "Checktheincomelist");
                                Intent intent = new Intent(context, BillSummaryDetailActivity.class);
                                intent.putExtra(Constants.INTENT_KEY_MONTH, finalBillMonthInfo.month);
                                context.startActivity(intent);
                            }
                        });
                    }
                }else if(i==1){
                    billMonthInfo = monthInfoDatas.get(i);
                    if(null != billMonthInfo){
                        TextView tv_month_last = holder.getView(R.id.tv_month_last);
                        TextView tv_month_last_state = holder.getView(R.id.tv_month_last_state);
                        TextView tv_month_last_income = holder.getView(R.id.tv_month_last_income);
                        setDataForView(tv_month_last,tv_month_last_state,tv_month_last_income,billMonthInfo);
                        final BillMonthInfo finalBillMonthInfo1 = billMonthInfo;
                        holder.getView(R.id.tv_month_last_detail).setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View view) {
                                StatisticalTools.eventCount(context, "Checktheincomelist");
                                Intent intent = new Intent(context, BillSummaryDetailActivity.class);
                                intent.putExtra(Constants.INTENT_KEY_MONTH, finalBillMonthInfo1.month);
                                context.startActivity(intent);
                            }
                        });
                    }

                }
            }
        }
    }

    private void setDataForView(TextView tv_month_last, TextView tv_month_last_state,
                                TextView tv_month_last_income,BillMonthInfo billMonthInfo) {
        tv_month_last.setText(billMonthInfo.month+"月份账单");
        tv_month_last_state.setText("("+billMonthInfo.faf+")");
        tv_month_last_income.setText(billMonthInfo.jixiao+"(元)");
    }
}
