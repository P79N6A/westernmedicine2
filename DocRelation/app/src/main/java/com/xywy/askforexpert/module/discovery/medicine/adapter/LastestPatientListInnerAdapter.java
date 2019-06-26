package com.xywy.askforexpert.module.discovery.medicine.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.better.YMRVSingleTypeBaseAdapter;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.entity.Patient;
import com.xywy.util.ImageLoaderUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by xgxg on 2017/10/23.
 */

public class LastestPatientListInnerAdapter extends YMRVSingleTypeBaseAdapter<Patient> {
    public LastestPatientListInnerAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_lastest_patient_list_item;
    }

    @Override
    protected void convert(ViewHolder holder, Patient item, int position) {
        holder.setText(R.id.tv_name, item.getRealName());
        ImageLoaderUtils.getInstance().displayImage(item.getPhoto(), (ImageView) holder.getView(R.id.iv_face));
    }
}
