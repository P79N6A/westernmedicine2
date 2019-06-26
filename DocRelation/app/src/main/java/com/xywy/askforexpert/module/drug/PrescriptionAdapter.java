package com.xywy.askforexpert.module.drug;

import android.content.Context;

import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineCartEntity;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;

/**
 * Created by xugan on 2018/7/9.
 */
public class PrescriptionAdapter extends XYWYRVMultiTypeBaseAdapter<MedicineCartEntity> {
    public PrescriptionAdapter(Context context) {
        super(context);
        addItemViewDelegate(new PrescriptionDelegate(context));
    }
}
