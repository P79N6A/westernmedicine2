package com.xywy.askforexpert.module.liveshow.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.better.YMRVSingleTypeBaseAdapter;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.model.liveshow.LiveShowHostInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 我的直播 Adapter
 * Created by bailiangjin on 2017/2/24.
 */

public class LiveShowRecordAdapter extends YMRVSingleTypeBaseAdapter<LiveShowHostInfo.DataBean.ListBean> {
    public LiveShowRecordAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_liveshow_record;
    }

    @Override
    protected void convert(ViewHolder holder, LiveShowHostInfo.DataBean.ListBean item, int position) {

        ImageView iv_icon= holder.getView(R.id.iv_icon);

        ImageLoadUtils.INSTANCE.loadImageView(iv_icon, item.getCover(),R.drawable.live_show_item_default_bg);

        holder.setText(R.id.tv_name, item.getName());
        holder.setText(R.id.tv_view_number, ""+item.getAmount());
        holder.setText(R.id.tv_gift_number, ""+item.getAmount());
        holder.setText(R.id.tv_time, "直播时间："+item.getCreatetime());

        TextView tv_invalid = holder.getView(R.id.tv_invalid);
        tv_invalid.setVisibility(item.getState()==0 ? View.GONE : View.VISIBLE);


    }


}
