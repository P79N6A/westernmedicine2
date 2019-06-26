package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.content.Context;

import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicalCategoryEntity;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;


/**
 * Created by bobby on 17/3/21.
 */

public class PharmacySecondAdapter extends XYWYRVMultiTypeBaseAdapter<MedicalCategoryEntity> {
    public PharmacySecondAdapter(Context context) {
        super(context);
        addItemViewDelegate(new PharmacySecondDelegate());
    }
}
