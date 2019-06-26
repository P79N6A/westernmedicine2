package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.content.Context;

import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.PharmacyRecordEntity;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;


/**
 * Created by bobby on 17/3/21.
 */

public class PharmacyRecordAdapter extends XYWYRVMultiTypeBaseAdapter<PharmacyRecordEntity> {
    public PharmacyRecordAdapter(Context context) {
        super(context);
        addItemViewDelegate(new PharmacyRecordDelegate());
    }
}
