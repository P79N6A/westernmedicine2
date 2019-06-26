package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicalCategoryEntity;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by bobby on 17/3/21.
 */

public class PharmacySecondDelegate implements ItemViewDelegate<MedicalCategoryEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_pharmacy_second_category_item;
    }

    @Override
    public boolean isForViewType(MedicalCategoryEntity item, int position) {
        return true;
    }

    @Override
    public void convert(ViewHolder holder, MedicalCategoryEntity pharmacyEntity, int position) {
        if(pharmacyEntity!=null) {
            holder.getConvertView().setTag(pharmacyEntity);
            ((TextView) holder.getView(R.id.pharmacy_second_title)).setText(pharmacyEntity.getName());
        }
    }
}
