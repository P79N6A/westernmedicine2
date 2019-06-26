package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.content.Context;

import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineEntity;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;

/**
 * Created by bobby on 17/3/21.
 */

public class MedicineListAdapter extends XYWYRVMultiTypeBaseAdapter<MedicineEntity> {
    public MedicineListAdapter(Context context,String isFrom) {
        super(context);
        addItemViewDelegate(new MedicineListDelegate(isFrom));
    }
}
