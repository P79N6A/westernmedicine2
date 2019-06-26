package com.xywy.askforexpert.module.main.patient.adapter;

import android.content.Context;

import com.xywy.askforexpert.model.PatienTtitle;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;

/**
 * Created by jason on 2018/10/30.
 */

    public class PatientManagerAdapter extends XYWYRVMultiTypeBaseAdapter<PatienTtitle> {


    public PatientManagerAdapter(Context context,boolean selectedFlag) {
        super(context);
        addItemViewDelegate(new PatienTtitleItemDelegate());
        addItemViewDelegate(new PatienContentItemDelegate(context,selectedFlag));
    }
}
