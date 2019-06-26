package com.xywy.askforexpert.module.discovery.adapter.discovermain;

import android.content.Context;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.better.YMRVSingleTypeBaseAdapter;
import com.xywy.askforexpert.module.discovery.adapter.discovermain.bean.DiscoverServiceInnerItem;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by bailiangjin on 2017/3/24.
 */

public class DiscoverServiceInnerAdapter extends YMRVSingleTypeBaseAdapter<DiscoverServiceInnerItem> {

    public DiscoverServiceInnerAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_discover_service_new;
    }

    @Override
    public void convert(ViewHolder holder, final DiscoverServiceInnerItem innerItem, int position) {
        holder.setText(R.id.tv_name, innerItem.getName());
        holder.setImageResource(R.id.iv_icon, innerItem.getIconResId());
    }
}
