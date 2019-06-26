package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.content.Context;

import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.PharmacyEntity;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;


/**
 * Created by bobby on 17/3/21.
 */

public class PharmacyAdapter extends XYWYRVMultiTypeBaseAdapter<PharmacyEntity> {
    public PharmacyAdapter(Context context) {
        super(context);
        addItemViewDelegate(new PharmacyDelegate());
    }
}
