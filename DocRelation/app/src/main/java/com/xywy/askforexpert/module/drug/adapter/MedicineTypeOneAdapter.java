package com.xywy.askforexpert.module.drug.adapter;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.PharmacyEntity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 药品一级列表的Adapter
 */
public class MedicineTypeOneAdapter extends CommonAdapter<PharmacyEntity> {

    private OneItemClick mOneItemClick;

    //条目点击的接口回调
    public interface OneItemClick {
        void oneItemClick(int position, PharmacyEntity one);
    }

    //条目监听回调
    public void setOneItemClick(OneItemClick msg) {
        this.mOneItemClick = msg;
    }

    public MedicineTypeOneAdapter(Context context, List<PharmacyEntity> list) {
        super(context, R.layout.my_pharmacy_item_one, list);

    }

    /**
     * 一级药品列表的刷新
     */
    public void refreshList(List<PharmacyEntity> oneList) {
        mDatas.clear();

        if (oneList != null) {
            mDatas.addAll(oneList);
        }
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder holder, final PharmacyEntity medicineTypeOneBean, final int position) {
        TextView tv = holder.getView(R.id.tv_my_pharmacy_item_one);
        tv.setText(medicineTypeOneBean.getName());

        if (medicineTypeOneBean.isSelected()) {
            tv.setTextColor(mContext.getResources().getColor(R.color.color_00c8aa));
            tv.setBackgroundColor(mContext.getResources().getColor(R.color.color_f2f2f2));
        } else {
            tv.setTextColor(mContext.getResources().getColor(R.color.color_333333));
            tv.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatus(position);
                if (mOneItemClick != null) {
                    mOneItemClick.oneItemClick(position, medicineTypeOneBean);
                }
                notifyDataSetChanged();
            }
        });
    }

    //改变按钮选中状态
    public void setStatus(int position) {
        for (int i = 0; i < mDatas.size(); i++) {
            PharmacyEntity one = mDatas.get(i);
            one.setSelected(i == position ? true : false);
        }
    }
}
