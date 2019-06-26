package com.xywy.askforexpert.module.drug.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.better.YMRVSingleTypeBaseAdapter;
import com.xywy.askforexpert.module.discovery.medicine.common.ViewCallBack;
import com.xywy.askforexpert.module.drug.bean.DrugBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by xugan on 2018/7/9.
 */
public class PrescriptionDetailAdapter extends YMRVSingleTypeBaseAdapter<DrugBean> {

    private ViewCallBack mMyCallBack;
    private final StringBuilder sb;

    public void setViewCallBack(ViewCallBack viewCallBack) {
        mMyCallBack = viewCallBack;
    }

    public PrescriptionDetailAdapter(Context context) {
        super(context);
        sb = new StringBuilder();
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.prescription_detail_item;
    }

    @Override
    protected void convert(ViewHolder holder, final DrugBean entity, final int position) {
        if (entity != null) {
            ((TextView) holder.getView(R.id.tv_drug_name)).setText(entity.gname);
            ((TextView) holder.getView(R.id.tv_drug_small_spec)).setText(entity.specification);
            sb.setLength(0);
            sb.append(entity.take_rate+"，");
            sb.append("每次"+entity.take_num+entity.take_unit+"，");
            sb.append(TextUtils.isEmpty(entity.take_time)?"":entity.take_time+"，");
            sb.append(entity.take_method);
            ((TextView) holder.getView(R.id.tv_usage)).setText("用法:"+sb.toString());
            ((TextView) holder.getView(R.id.tv_drug_big_spec)).setText(entity.num+entity.drug_unit);
        }
    }
}
