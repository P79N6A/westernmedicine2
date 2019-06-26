package com.xywy.askforexpert.module.discovery.medicine.module.patient;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.entity.Patient;
import com.xywy.util.ImageLoaderUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by xgxg on 2017/4/13.
 */

public class NewPatientDelegate implements ItemViewDelegate<Patient> {
    private List<Patient> mDatas;
    public NewPatientDelegate(List<Patient> datas){
        this.mDatas = datas;
    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_patient_list_item_new;
    }

    @Override
    public boolean isForViewType(Patient item, int position) {
        return true;
    }

    @Override
    public void convert(ViewHolder holder, Patient patient, int position) {
        if (patient != null) {
            holder.getConvertView().setTag(patient);
//            DisplayImageOptions options = ImageLoaderUtils.getInstance().getDisplayImageOptions(R.drawable.patient_list_default);
//            ImageLoaderUtils.getInstance().displayImage(patient.getPhoto(),((ImageView) holder.getView(R.id.ivAvatar)),options);
//            ((TextView) holder.getView(R.id.tvCity)).setText(patient.getRealName());
            DisplayImageOptions options = ImageLoaderUtils.getInstance().getDisplayImageOptions(R.drawable.patient_list_default);
            ImageLoaderUtils.getInstance().displayImage(patient.getPhoto(),((ImageView) holder.getView(R.id.iv_face)),options);
            ((TextView) holder.getView(R.id.tv_name)).setText(patient.getRealName());
            if("0".equals(patient.getSex())){
                holder.getView(R.id.iv_gender).setVisibility(View.VISIBLE);
                ((ImageView) holder.getView(R.id.iv_gender)).setImageResource(R.drawable.man);
            }else if("1".equals(patient.getSex())){
                holder.getView(R.id.iv_gender).setVisibility(View.VISIBLE);
                ((ImageView) holder.getView(R.id.iv_gender)).setImageResource(R.drawable.women);
            }else {
                holder.getView(R.id.iv_gender).setVisibility(View.GONE);
            }
            if("0".equals(patient.getAge())){
                holder.getView(R.id.tv_age).setVisibility(View.GONE);
            }else {
                holder.getView(R.id.tv_age).setVisibility(View.VISIBLE);
                ((TextView) holder.getView(R.id.tv_age)).setText(patient.getAge()+"岁");
            }





        }
    }

}
