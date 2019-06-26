package com.xywy.askforexpert.module.drug.adapter;

import android.content.Context;

import com.xywy.askforexpert.module.discovery.medicine.module.medical.RecipeDetailDelegate;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineCartEntity;
import com.xywy.askforexpert.module.drug.bean.RrescriptionBean;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;

/**
 * Created by jason on 2018/7/11.
 */

public class DrugRecipeDetailAdapter extends XYWYRVMultiTypeBaseAdapter<RrescriptionBean> {
        public DrugRecipeDetailAdapter(Context context) {
            super(context);
            addItemViewDelegate(new DrugRecipeDetailDelegate(context));
        }
}
