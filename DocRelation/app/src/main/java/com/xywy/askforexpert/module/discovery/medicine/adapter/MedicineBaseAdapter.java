package com.xywy.askforexpert.module.discovery.medicine.adapter;

import android.content.Context;

import com.xywy.askforexpert.appcommon.base.adapter.better.YMRVMultiTypeBaseAdapter;
import com.xywy.askforexpert.module.discovery.medicine.adapter.entity.MedicineItemBean;

/**
 * Created by xgxg on 2017/10/23.
 * 药品助手首页的adapter
 */

public class MedicineBaseAdapter extends YMRVMultiTypeBaseAdapter<MedicineItemBean> {
    public MedicineBaseAdapter(Context context) {
        super(context);
        addItemViewDelegate(new LastestPatientListDelegate(context));
        addItemViewDelegate(new LastestMedicineRecordDelegate());
    }
}
