package com.xywy.askforexpert.module.drug.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.ArithUtils;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.DialogueDecreaseActivity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineCartEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineEntity;
import com.xywy.askforexpert.module.discovery.medicine.view.AmountView;
import com.xywy.askforexpert.module.drug.bean.RrescriptionBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by jason on 2018/7/11.
 */

public class DrugRecipeDetailDelegate implements ItemViewDelegate<RrescriptionBean> {
    private Context mContext;
    private String[] medicine_time,take_method;
    DrugRecipeDetailDelegate(Context context){
        mContext = context;
        medicine_time = mContext.getResources().getStringArray(R.array.medicine_time);
        take_method = mContext.getResources().getStringArray(R.array.take_method);
    }
    @Override
    public int getItemViewLayoutId() {

        return R.layout.item_drug_recipe;
//        return R.layout.item_recipe_item;
    }

    @Override
    public boolean isForViewType(RrescriptionBean item, int position) {
        return true;
    }

    @Override
    public void convert(ViewHolder holder, final RrescriptionBean entity, final int position) {
        holder.getConvertView().setTag(entity);
        TextView name_dose_tv =  holder.getView(R.id.name_dose_tv);
        name_dose_tv.setText(entity.getDrugName()+" "+entity.getSpecification());
        TextView format_tv = holder.getView(R.id.format_tv);
        format_tv.setText(entity.getNum()+entity.getDoseUnit());
        TextView method_tv = holder.getView(R.id.method_tv);
        String take_time = TextUtils.isEmpty(entity.getTake_time())?"":","+entity.getTake_time()+",";
        String day = entity.getTake_day().equals("0")?"":","+entity.getTake_day()+"天";
        method_tv.setText("用法："+entity.getTake_rate()+",每次"+entity.getTake_num()+
                entity.getDoseName()+take_time+entity.getMethodName()+day);
    }
}