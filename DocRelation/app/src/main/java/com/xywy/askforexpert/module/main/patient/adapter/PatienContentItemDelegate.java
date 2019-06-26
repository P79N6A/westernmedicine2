package com.xywy.askforexpert.module.main.patient.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.PatienTtitle;
import com.xywy.askforexpert.module.main.patient.activity.PatientDetailActivity;
import com.xywy.askforexpert.widget.CircleImageView;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by jason on 2018/10/31.
 */

public class PatienContentItemDelegate implements ItemViewDelegate<PatienTtitle> {

    private Context mContext;
    private boolean selectedFlag = false;
    public PatienContentItemDelegate(Context mContext, boolean selectedFlag) {
        this.mContext = mContext;
        this.selectedFlag = selectedFlag;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.patien_content_item;
    }

    @Override
    public boolean isForViewType(PatienTtitle item, int position) {
        return item.getItemFlag() == 2;
    }

    @Override
    public void convert(ViewHolder holder, final PatienTtitle item, int position) {
        CircleImageView iv_head_icon = holder.getView(R.id.iv_head_icon);
        Glide.with(mContext).load(item.getPhoto()).into(iv_head_icon);
        TextView user_name = holder.getView(R.id.user_name);
        user_name.setText(item.getUsername());
        TextView age_sex_tv = holder.getView(R.id.age_sex_tv);
        age_sex_tv.setText(item.getSex()+" "+item.getAge()+"岁");
        TextView advice_tv = holder.getView(R.id.advice_tv);
        advice_tv.setText(item.getAdvice());
        final ImageView select_img = holder.getView(R.id.select_img);

        if (selectedFlag){
            select_img.setVisibility(View.VISIBLE);
            holder.getView(R.id.root_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.isSelected()){
                        select_img.setImageResource(R.drawable.un_selected_icon);
                        item.setSelected(false);
                    }else{
                        select_img.setImageResource(R.drawable.selected_icon);
                        item.setSelected(true);
                    }
                }
            });
        }else{
            select_img.setVisibility(View.GONE);
            holder.getView(R.id.root_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext,PatientDetailActivity.class).putExtra("patienData",item));
                }
            });

        }

    }
}
