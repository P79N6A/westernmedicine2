package com.xywy.livevideo.adapter;

import android.content.Context;

import com.xywy.livevideo.entity.RechargeBean;
import com.xywy.livevideolib.R;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/2/24 10:28
 */
public class RechargeAdapter extends XYWYRVMultiTypeBaseAdapter<RechargeBean> {
    public RechargeAdapter(Context context) {
        super(context);
        addItemViewDelegate(new RechargeItemDelegate());
    }
}
class RechargeItemDelegate implements ItemViewDelegate<RechargeBean> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_recharge;
    }

    @Override
    public boolean isForViewType(RechargeBean item, int position) {
        return true;
    }

    @Override
    public void convert(ViewHolder holder, RechargeBean emMessage, int position) {
        holder.setText(R.id.tv_live_money,String.format("%d健康币", emMessage.getLiveMoney()));
        holder.setText(R.id.tv_real_money,String.format("￥%d元", emMessage.getRealMoney()));
    }
}
