package com.xywy.askforexpert.module.discovery.medicine.module.patient;

import android.content.Context;
import android.widget.SectionIndexer;

import com.xywy.askforexpert.module.discovery.medicine.module.patient.entity.Patient;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;

import java.util.List;

/**
 * Created by xgxg on 2017/4/13.
 */

public class NewPatientAdapter extends XYWYRVMultiTypeBaseAdapter<Patient> implements SectionIndexer {
    private List<Patient> mDatas;
    public NewPatientAdapter(Context context, List<Patient> datas) {
        super(context);
        this.mDatas = datas;
        addItemViewDelegate(new NewPatientDelegate(datas));
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }
}
