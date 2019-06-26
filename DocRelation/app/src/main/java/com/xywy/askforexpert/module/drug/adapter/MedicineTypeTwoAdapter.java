package com.xywy.askforexpert.module.drug.adapter;

import android.content.Context;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicalCategoryEntity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


/**
 * 药品二级列表的Adapter stone
 */
public class MedicineTypeTwoAdapter extends CommonAdapter<MedicalCategoryEntity> {

    private TwoItemClick mTwoItemClick;

    public interface TwoItemClick {
        void twoItemClick(int drugType, MedicalCategoryEntity typeTwo, int position, boolean isLast);
    }

    public void setTwoItemClick(TwoItemClick itemTypeTwo) {
        this.mTwoItemClick = itemTypeTwo;
    }


    //展示的药品类型 明星 常用 普通
    private int drugType;

    public int getDrugType() {
        return drugType;
    }

    public void setDrugType(int drugType) {
        this.drugType = drugType;
    }

    public MedicineTypeTwoAdapter(Context context, List<MedicalCategoryEntity> list) {
        super(context, R.layout.my_pharmacy_item_two, list);
    }


    @Override
    protected void convert(ViewHolder holder, final MedicalCategoryEntity typeTwo, final int position) {
        holder.setText(R.id.tv_my_pharmacy_item_two, typeTwo.getName());

        holder.getView(R.id.tv_my_pharmacy_item_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoItemClick != null) {
                    mTwoItemClick.twoItemClick(drugType, typeTwo, position, position == mDatas.size() - 1 ? true : false);
                }
            }
        });
    }


    /**
     * 下拉刷新
     *
     * @param list 新数据
     */
    public void refresh(List<MedicalCategoryEntity> list) {
        if (list != null) {
            mDatas.clear();
            mDatas.addAll(list);
            notifyDataSetChanged();
        }
    }


    /**
     * 显示无数据页面
     */
    public void showNoDataPage() {
        mDatas.clear();
        notifyDataSetChanged();
    }
}
