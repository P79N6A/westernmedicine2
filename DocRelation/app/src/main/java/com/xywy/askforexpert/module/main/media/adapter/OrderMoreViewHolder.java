package com.xywy.askforexpert.module.main.media.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.module.main.media.newpart.MediaListActivityNew;
import com.xywy.askforexpert.widget.view.ultimaterecycleview.UltimateRecyclerviewViewHolder;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/1/11 14:52
 */
public class OrderMoreViewHolder<T> extends UltimateRecyclerviewViewHolder<T> {

    public OrderMoreViewHolder(View itemView) {
        super(itemView);
        ViewGroup.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(48));
        itemView.setLayoutParams(layoutParams2);
        itemView.setBackground(null);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaListActivityNew.start(v.getContext());
            }
        });
    }

    public void updateView(final Context context, T item) {
        super.updateView(context, item);
    }
}
