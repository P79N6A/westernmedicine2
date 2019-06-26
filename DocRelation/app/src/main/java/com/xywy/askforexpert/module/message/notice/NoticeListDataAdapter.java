package com.xywy.askforexpert.module.message.notice;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.better.YMRVSingleTypeBaseAdapter;
import com.xywy.askforexpert.appcommon.utils.DateUtils;
import com.xywy.askforexpert.model.notice.Notice;
import com.zhy.adapter.recyclerview.base.ViewHolder;


/**
 *  Created by xugan on 2018/5/28.
 */
public class NoticeListDataAdapter extends YMRVSingleTypeBaseAdapter<Notice> {
    public NoticeListDataAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_notice_list_adapter;
    }

    @Override
    protected void convert(ViewHolder holder, Notice item, int position) {
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_content = holder.getView(R.id.tv_content);
        ImageView iv_notice_dot = holder.getView(R.id.iv_notice_dot);
        tv_title.setText(item.title);
        tv_time.setText(DateUtils.translateDate(Long.parseLong(item.intime)*1000));
        tv_content.setText(item.content);
        iv_notice_dot.setVisibility((item.hs_read == 0)? View.VISIBLE:View.GONE);
    }

}
