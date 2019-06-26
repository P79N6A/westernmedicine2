package com.xywy.askforexpert.module.liveshow.adapter.liveshowlist;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.model.liveshow.LiveShowListPageBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by bailiangjin on 2017/3/4.
 */

public class BigItemDelegate implements ItemViewDelegate<LiveShowItem> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_live_show_list_big;
    }

    @Override
    public boolean isForViewType(LiveShowItem item, int position) {
        return LiveShowItem.Type.SINGLE == item.getType();
    }

    @Override
    public void convert(ViewHolder holder, LiveShowItem item, int position) {

        LiveShowListPageBean.DataBean dataBean = item.getItem();

        holder.setText(R.id.tv_title, dataBean.getName());
        holder.setText(R.id.tv_user_name, dataBean.getUser().getName());
        holder.setText(R.id.tv_view_number, "" + dataBean.getAmount());


        TextView tv_live_show = holder.getView(R.id.tv_live_show);
        TextView tv_record = holder.getView(R.id.tv_record);

        tv_live_show.setVisibility(1 == dataBean.getState() ? View.VISIBLE : View.GONE);
        tv_record.setVisibility(0 == dataBean.getState() ? View.VISIBLE : View.GONE);

        ImageView iv_bg = holder.getView(R.id.iv_bg);
        ImageView iv_user_head = holder.getView(R.id.iv_user_head);


        ImageLoadUtils.INSTANCE.loadImageView(iv_bg, dataBean.getCover(), R.drawable.live_show_item_default_bg);
        ImageLoadUtils.INSTANCE.loadCircleImageView(iv_user_head, dataBean.getUser().getPortrait());

    }
}
