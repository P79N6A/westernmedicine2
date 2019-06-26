package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.content.Context;

import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineCartEntity;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;

/**
 * Created by bobby on 17/3/21.
 */

public class RecipeDetailAdapter extends XYWYRVMultiTypeBaseAdapter<MedicineCartEntity> {
    public RecipeDetailAdapter(Context context) {
        super(context);
        addItemViewDelegate(new RecipeDetailDelegate(context));
    }
}
