package com.xywy.askforexpert.module.discovery.medicine.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.module.discovery.medicine.adapter.entity.MedicineItemBean;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.PatientDetailsActivity;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.PatientManager;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.entity.Patient;
import com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils;
import com.xywy.util.LogUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by xgxg on 2017/10/23.
 */

public class LastestPatientListDelegate implements ItemViewDelegate<MedicineItemBean> {

    private final Context mContext;
    private final String PATIENT = "patient";
    private final String PATIENT_BUNDLE = "patient_bundle";
    LastestPatientListInnerAdapter innerGridItemAdapter;
//    private boolean isAddedDecoration = false;

    public LastestPatientListDelegate(Context context) {
        this.mContext = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_lastest_patient_list;
    }

    @Override
    public boolean isForViewType(MedicineItemBean item, int position) {
        return item.getType() == MedicineItemBean.TYPE_LIST_PATIENT;
    }

    @Override
    public void convert(ViewHolder holder, MedicineItemBean medicineItemBean, int position) {
        final RecyclerView recyclerView = holder.getView(R.id.recyclerView);
        LinearLayout patient_empty = holder.getView(R.id.patient_empty);
        Context context = recyclerView.getContext();
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //stone 去掉分割线
//        if (!isAddedDecoration) {
//            recyclerView.addItemDecoration(new HSItemDecoration(recyclerView.getContext(), R.color.app_common_divider_color));
//            isAddedDecoration=true;
//        }
        final List<Patient> innerItemList = medicineItemBean.getLastestPatientItem().getInnerItemList();
        LogUtils.i("innerItemList.size()=" + innerItemList.size());
        if (innerItemList.size() == 0) {
            patient_empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            patient_empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            innerGridItemAdapter = new LastestPatientListInnerAdapter(context);
            innerGridItemAdapter.setData(innerItemList);
            recyclerView.setAdapter(innerGridItemAdapter);
            innerGridItemAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Patient item = innerGridItemAdapter.getItem(position);

                    //添加最近咨询患者统计
                    StatisticalTools.eventCount(mContext, "YPRecentconsulting");
                    if (null != item) {
                        PatientManager.getInstance().setPatient(item);
                        Intent intent = new Intent(mContext, PatientDetailsActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable(PATIENT, item);
                        intent.putExtra(PATIENT_BUNDLE, b);
                        mContext.startActivity(intent);
                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        }

    }
}
