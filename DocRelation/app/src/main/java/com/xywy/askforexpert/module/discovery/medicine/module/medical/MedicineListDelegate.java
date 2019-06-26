package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.config.OffLineService;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineEntity;
import com.xywy.util.ImageLoaderUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by bobby on 17/3/21.
 */

public class MedicineListDelegate implements ItemViewDelegate<MedicineEntity> {
    private String mIsFrom;
    public MedicineListDelegate(String isFrom) {
        mIsFrom = isFrom;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_medicine_item;
    }

    @Override
    public boolean isForViewType(MedicineEntity item, int position) {
        return true;
    }

    @Override
    public void convert(final ViewHolder holder, final MedicineEntity entity, int position) {
        if(entity!=null) {
            holder.getConvertView().setTag(entity);
            ((TextView) holder.getView(R.id.medicine_title)).setText(((entity.getName()==null)?"":entity.getName())+" "+((entity.getSpecification()==null)?"":entity.getSpecification()));
            ((TextView) holder.getView(R.id.medicine_company)).setText(entity.getCompany());
            ((TextView) holder.getView(R.id.medicine_price)).setText("￥"+entity.getWksmj());
            ImageLoaderUtils.getInstance().displayImage(entity.getImage(), (ImageView) holder.getView(R.id.img));

            ((TextView) holder.getView(R.id.medicine_index)).setText(String.valueOf(entity.getWeight()));
            if(OffLineService.isOffLine()){
                holder.getView(R.id.star).setVisibility(View.VISIBLE);
                holder.getView(R.id.ll_index).setVisibility(View.INVISIBLE);
            }else {
                holder.getView(R.id.star).setVisibility(View.INVISIBLE);
                holder.getView(R.id.ll_index).setVisibility(View.VISIBLE);
            }
            ((TextView) holder.getView(R.id.stock)).setText("剩余" + entity.getStock() + "件");
            final Button btn = holder.getView(R.id.add_to_cart);
            if(!TextUtils.isEmpty(mIsFrom)){
                btn.setVisibility(View.GONE);
            }else {
                btn.setVisibility(View.VISIBLE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(entity!=null) {
                            MedicineSettingActivity.startActivity(holder.getConvertView().getContext(), entity);
                        }
                    }
                });
                if(MedicineCartCenter.getInstance().isAdded(entity.getProductId())) {
                    btn.setEnabled(false);
                    btn.setText("已添加");
                } else {
                    btn.setEnabled(true);
                    btn.setText("加入处方笺");
                }
            }

        }
    }
}
