package com.xywy.askforexpert.module.main.service.que.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.better.YMRVSingleTypeBaseAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by bailiangjin on 2017/4/28.
 */

public class QuestionDetailBtnAdapter extends YMRVSingleTypeBaseAdapter<QuestionDetailBtnItem>{

    public QuestionDetailBtnAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_question_detail_btn;
    }

    @Override
    protected void convert(ViewHolder holder, QuestionDetailBtnItem item, int position) {
        holder.setText(R.id.tv_name,item.getName());
        holder.setImageResource(R.id.iv_icon,item.getImageResId());
        ImageView iv_icon=holder.getView(R.id.iv_icon);
        iv_icon.setEnabled(item.isEnabled());

    }
}
