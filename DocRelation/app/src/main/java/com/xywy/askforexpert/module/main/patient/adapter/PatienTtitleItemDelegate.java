package com.xywy.askforexpert.module.main.patient.adapter;

import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.PatienTtitle;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by jason on 2018/10/31.
 */

public class PatienTtitleItemDelegate implements ItemViewDelegate<PatienTtitle> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.patient_title_item;
    }

    @Override
    public boolean isForViewType(PatienTtitle item, int position) {
        return item.getItemFlag() == 1;
    }

    @Override
    public void convert(ViewHolder holder, PatienTtitle item, int position) {
        TextView patient_time_title = holder.getView(R.id.patient_time_title);
        patient_time_title.setText(item.getTime());

    }
}
