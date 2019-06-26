package com.xywy.askforexpert.module.drug.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.module.drug.PrescriptionDetailActivity;
import com.xywy.askforexpert.module.drug.bean.DocQues;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by jason on 2019/1/7.
 */

public class PrescriptionRecordListMainDelegate implements ItemViewDelegate<DocQues> {
    private Context context;
    private int type;
    public PrescriptionRecordListMainDelegate(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_onlineroom_record;
    }

    @Override
    public boolean isForViewType(DocQues item, int position) {
        return true;
    }

    @Override
    public void convert(ViewHolder holder, final DocQues entity, final int position) {
        if (entity != null) {
            ((TextView) holder.getView(R.id.num)).setText("NO." + entity.pbn);
            ((TextView) holder.getView(R.id.name)).setText(entity.uname);
            ((TextView) holder.getView(R.id.sex)).setText(entity.usersex);
            ((TextView) holder.getView(R.id.age)).setText(entity.age);
//            ((TextView) holder.getView(R.id.status)).setText(entity.statusText+"("+payStates.get(entity.paystate)+")");
            ((TextView) holder.getView(R.id.status)).setText(entity.statusText);
            ((TextView) holder.getView(R.id.result)).setText("初步诊断结果: " + entity.diagnosis);
            ((TextView) holder.getView(R.id.time)).setText(entity.time);
            View read_view = holder.getView(R.id.read_view);
            read_view.setVisibility(View.VISIBLE);
            if (type==1){
                read_view.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(entity.is_read)&&entity.is_read.equals("0")){
                    read_view.setBackgroundResource(R.drawable.read_view_bg);
                }else{
                    read_view.setBackgroundResource(R.drawable.answer_select_circle_bg_normal);
                }
            }else{
                read_view.setVisibility(View.GONE);
            }

            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(context, PrescriptionDetailActivity.class);
                        intent.putExtra(Constants.KEY_ID, entity.id);
                        context.startActivity(intent);
                }
            });
        }
    }
}
