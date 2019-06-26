package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.PharmacyEntity;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by bobby on 17/3/21.
 */

public class PharmacyDelegate implements ItemViewDelegate<PharmacyEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_pharmacy_main_category_item;
    }

    @Override
    public boolean isForViewType(PharmacyEntity item, int position) {
        return true;
    }

    @Override
    public void convert(ViewHolder holder, PharmacyEntity pharmacyEntity, int position) {
        if(pharmacyEntity!=null) {
            ((TextView) holder.getView(R.id.pharmacy_main_title)).setText(pharmacyEntity.getName());
            if(pharmacyEntity.isSelected()) {
                holder.getView(R.id.pharmacy_main_title_container).setBackgroundColor(holder.getConvertView().getResources().getColor(R.color.codex_item_bg));
                holder.getView(R.id.pharmacy_main_title_indicator).setVisibility(View.VISIBLE);
            } else {
                holder.getView(R.id.pharmacy_main_title_container).setBackgroundColor(holder.getConvertView().getResources().getColor(R.color.white));
                holder.getView(R.id.pharmacy_main_title_indicator).setVisibility(View.GONE);
            }
        }
    }
}
