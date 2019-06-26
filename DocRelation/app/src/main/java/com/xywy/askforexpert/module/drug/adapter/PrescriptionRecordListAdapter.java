package com.xywy.askforexpert.module.drug.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.common.ViewCallBack;
import com.xywy.askforexpert.module.drug.bean.DocQues;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处方记录列表的Adapter stone
 */
public class PrescriptionRecordListAdapter extends CommonAdapter<DocQues> {

    private ViewCallBack mMyCallBack;
    private Map payStates = new HashMap();

    public void setViewCallBack(ViewCallBack viewCallBack) {
        mMyCallBack = viewCallBack;
    }

    public PrescriptionRecordListAdapter(Context context, List<DocQues> list) {
        super(context, R.layout.item_onlineroom_record, list);
        payStates.put("0","未购买");
        payStates.put("1","已购买");
        payStates.put("2","已退款");
    }

    @Override
    protected void convert(ViewHolder holder, final DocQues entity, final int position) {
        if (entity != null) {
            ((TextView) holder.getView(R.id.num)).setText("NO." + entity.pbn);
            ((TextView) holder.getView(R.id.name)).setText(entity.uname);
            ((TextView) holder.getView(R.id.sex)).setText(entity.usersex);
            ((TextView) holder.getView(R.id.age)).setText(entity.age);
//            ((TextView) holder.getView(R.id.status)).setText(entity.statusText+"("+payStates.get(entity.paystate)+")");
            ((TextView) holder.getView(R.id.status)).setText(entity.statusText);
            ((TextView) holder.getView(R.id.result)).setText("初步诊断结果: "+entity.diagnosis);
            ((TextView) holder.getView(R.id.time)).setText(entity.time);

            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entity != null) {
                        mMyCallBack.onClick(0, position, entity,false);
                    }
                }
            });
        }
    }
}
