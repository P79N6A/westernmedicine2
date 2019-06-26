package com.xywy.askforexpert.module.discovery.medicine.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.adapter.entity.MedicineItemBean;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.PharmacyRecordEntity;
import com.xywy.uilibrary.recyclerview.adapter.HSItemDecoration;
import com.xywy.util.LogUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by xgxg on 2017/10/23.
 */

public class LastestMedicineRecordDelegate implements ItemViewDelegate<MedicineItemBean> {
    LastestMedicineRecordInnerAdapter innerGridItemAdapter;
    private boolean isAddedDecoration = true;
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_lastest_medicine_record;
    }

    @Override
    public boolean isForViewType(MedicineItemBean item, int position) {
        return item.getType()==MedicineItemBean.TYPE_MEDICINE_RECORD;
    }

    @Override
    public void convert(ViewHolder holder, MedicineItemBean medicineItemBean, int position) {
        final RecyclerView recyclerView = holder.getView(R.id.recyclerView);
        LinearLayout pharmacy_record_empty = holder.getView(R.id.pharmacy_record_empty);
        Context context = recyclerView.getContext();
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (isAddedDecoration) {
            recyclerView.addItemDecoration(new HSItemDecoration(recyclerView.getContext(), R.color.color_eaeaea));
            isAddedDecoration=false;
        }
        final List<PharmacyRecordEntity> innerItemList = medicineItemBean.getLastestMedicineRecordItem().getInnerItemList();
        LogUtils.i("innerItemList.size()="+innerItemList.size());
        if(innerItemList.size()==0){
            pharmacy_record_empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            pharmacy_record_empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            innerGridItemAdapter = new LastestMedicineRecordInnerAdapter(context);
            innerGridItemAdapter.setData(innerItemList);
            recyclerView.setAdapter(innerGridItemAdapter);
        }

    }
}
